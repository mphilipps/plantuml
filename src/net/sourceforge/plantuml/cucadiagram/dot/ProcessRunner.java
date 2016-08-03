/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of PlantUML.
 *
 * Licensed under The MIT License (Massachusetts Institute of Technology License)
 * 
 * See http://opensource.org/licenses/MIT
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *
 * Original Author:  Arnaud Roques
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sourceforge.plantuml.api.MyRunnable;
import net.sourceforge.plantuml.api.TimeoutExecutor;

public class ProcessRunner {
	// http://steveliles.github.io/invoking_processes_from_java.html
	public static long TIMEOUT = 15 * 60 * 1000L;

	private final String[] cmd;

	private String error;
	private String out;

	private volatile ProcessState state = ProcessState.INIT();
	private final Lock changeState = new ReentrantLock();

	public ProcessRunner(String[] cmd) {
		this.cmd = cmd;
	}

	public ProcessState run(byte in[], OutputStream redirection) {
		return run(in, redirection, null);
	}

	public ProcessState run(byte in[], OutputStream redirection, File dir) {
		if (this.state.differs(ProcessState.INIT())) {
			throw new IllegalStateException();
		}
		this.state = ProcessState.RUNNING();
		final MainThread mainThread = new MainThread(cmd, dir, redirection, in);
		try {
			final boolean done = new TimeoutExecutor(TIMEOUT).executeNow(mainThread);
		} finally {
			changeState.lock();
			try {
				if (state.equals(ProcessState.RUNNING())) {
					state = ProcessState.TIMEOUT();
					// mainThread.cancel();
				}
			} finally {
				changeState.unlock();
			}
		}
		if (state.equals(ProcessState.TERMINATED_OK())) {
			assert mainThread != null;
			this.error = mainThread.getError();
			this.out = mainThread.getOut();
		}
		return state;
	}

	class MainThread implements MyRunnable {

		private final String[] cmd;
		private final File dir;
		private final OutputStream redirection;
		private final byte[] in;
		private volatile Process process;
		private volatile ThreadStream errorStream;
		private volatile ThreadStream outStream;

		public MainThread(String[] cmd, File dir, OutputStream redirection, byte[] in) {
			this.cmd = cmd;
			this.dir = dir;
			this.redirection = redirection;
			this.in = in;
		}

		public String getOut() {
			return outStream.getString();
		}

		public String getError() {
			return errorStream.getString();
		}

		public void runJob() throws InterruptedException {
			try {
				startThreads();
				if (state.equals(ProcessState.RUNNING())) {
					final int result = joinInternal();
				}
			} finally {
				changeState.lock();
				try {
					if (state.equals(ProcessState.RUNNING())) {
						state = ProcessState.TERMINATED_OK();
					}
				} finally {
					changeState.unlock();
				}
				if (process != null) {
					process.destroy();
					close(process.getErrorStream());
					close(process.getOutputStream());
					close(process.getInputStream());
				}
			}

		}

		public void cancelJob() {
			// The changeState lock is ok
			// assert changeState.tryLock();
			// assert state == ProcessState.TIMEOUT;
			if (process != null) {
				errorStream.cancel();
				outStream.cancel();
				process.destroy();
				// interrupt();
				close(process.getErrorStream());
				close(process.getOutputStream());
				close(process.getInputStream());
			}
		}

		private void startThreads() {
			try {
				process = Runtime.getRuntime().exec(cmd, null, dir);
			} catch (IOException e) {
				e.printStackTrace();
				changeState.lock();
				try {
					state = ProcessState.IO_EXCEPTION1(e);
				} finally {
					changeState.unlock();
				}
				e.printStackTrace();
				return;
			}
			errorStream = new ThreadStream(process.getErrorStream(), null);
			outStream = new ThreadStream(process.getInputStream(), redirection);
			errorStream.start();
			outStream.start();
			if (in != null) {
				final OutputStream os = process.getOutputStream();
				try {
					try {
						os.write(in);
					} finally {
						os.close();
					}
				} catch (IOException e) {
					changeState.lock();
					try {
						state = ProcessState.IO_EXCEPTION2(e);
					} finally {
						changeState.unlock();
					}
					e.printStackTrace();
				}
			}
		}

		public int joinInternal() throws InterruptedException {
			errorStream.join();
			outStream.join();
			final int result = process.waitFor();
			return result;
		}

	}

	class ThreadStream extends Thread {

		private volatile InputStream streamToRead;
		private volatile OutputStream redirection;
		private volatile StringBuffer sb = new StringBuffer();

		ThreadStream(InputStream streamToRead, OutputStream redirection) {
			this.streamToRead = streamToRead;
			this.redirection = redirection;
		}

		public String getString() {
			if (sb==null) {
				return "";
			}
			return sb.toString();
		}

		public void cancel() {
			assert state.equals(ProcessState.TIMEOUT()) || state.equals(ProcessState.RUNNING()) : state;
			this.interrupt();
			sb = null;
			streamToRead = null;
			redirection = null;
			// Because of this, some NPE may occurs in run() method, but we do not care
		}

		@Override
		public void run() {
			int read = 0;
			try {
				while ((read = streamToRead.read()) != -1) {
					if (state.equals(ProcessState.TIMEOUT())) {
						return;
					}
					if (redirection == null) {
						sb.append((char) read);
					} else {
						redirection.write(read);
					}
				}
			} catch (Throwable e) {
				System.err.println("ProcessRunnerA " + e);
				e.printStackTrace();
				sb.append('\n');
				sb.append(e.toString());
			}
		}
	}

	public final String getError() {
		return error;
	}

	public final String getOut() {
		return out;
	}

	private void close(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void close(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
