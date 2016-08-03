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
package net.sourceforge.plantuml.hector2.layering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.hector2.MinMax;
import net.sourceforge.plantuml.hector2.mpos.MutationLayer;
import net.sourceforge.plantuml.hector2.mpos.MutationLayerMove;

public class Layer {

	private final int id;
	private final Map<IEntity, Integer> entities = new HashMap<IEntity, Integer>();

	public Layer(int id) {
		this.id = id;
	}

	public Layer duplicate() {
		final Layer result = new Layer(id);
		result.entities.putAll(this.entities);
		return result;
	}

	public List<MutationLayer> getPossibleMutations() {
		final List<MutationLayer> result = new ArrayList<MutationLayer>();
		for (Map.Entry<IEntity, Integer> ent : entities.entrySet()) {
			final IEntity entity = ent.getKey();
			final int longitude = ent.getValue();
			if (isLongitudeFree(longitude + 2)) {
				result.add(new MutationLayerMove(this, entity, longitude + 2));
			}
			if (isLongitudeFree(longitude - 2)) {
				result.add(new MutationLayerMove(this, entity, longitude - 2));
			}
		}
		return Collections.unmodifiableList(result);
	}

	private boolean isLongitudeFree(int longitude) {
		return entities.values().contains(longitude) == false;
	}

	public void put(IEntity ent, int longitude) {
		if (entities.containsKey(ent) == false) {
			throw new IllegalArgumentException();
		}
		this.entities.put(ent, longitude);
	}

	public void add(IEntity ent) {
		final int pos = entities.size() * 2;
		this.entities.put(ent, pos);
	}

	public Collection<IEntity> entities() {
		return Collections.unmodifiableCollection(entities.keySet());
	}

	public int getLongitude(IEntity ent) {
		return entities.get(ent);
	}

	public MinMax getMinMaxLongitudes() {
		return MinMax.from(entities.values());
	}

	@Override
	public String toString() {
		return "layer " + id + " " + entities;
	}

	public final int getId() {
		return id;
	}

}
