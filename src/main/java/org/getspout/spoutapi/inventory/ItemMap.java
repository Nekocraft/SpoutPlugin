/*
 * This file is part of SpoutPlugin.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * SpoutPlugin is licensed under the GNU Lesser General Public License.
 *
 * SpoutPlugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutPlugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spoutapi.inventory;

import org.getspout.spoutapi.io.store.SimpleStore;

/**
 * This is a map which maps custom item ids to Strings and back.
 *
 * This map can be backed by File to ensure persistence.
 *
 * It also provides functionality to convert ids between 2 maps.
 */
public class ItemMap {
	private static ItemMap root;

	private final ItemMap parent;
	private final SimpleStore<Integer> store;

	private final ItemMapRunnable updateTask;

	private final int[] thisToParentMap;
	private final int[] parentToThisMap;

	private int nextId = 1024;

	public ItemMap(ItemMap parent, SimpleStore<Integer> store, ItemMapRunnable updateTask) {
		this.parent = parent;
		this.store = store;
		this.updateTask = updateTask;
		thisToParentMap = new int[65536];
		parentToThisMap = new int[65536];
		for (int i = 0; i < 65536; i++) {
			thisToParentMap[i] = 0;
			parentToThisMap[i] = 0;
		}
	}

	public static void setRootMap(ItemMap root) {
		ItemMap.root = root;
	}

	public static ItemMap getRootMap() {
		return root;
	}

	/**
	 * Converts an id local to this map to a foreign id, local to another map.
	 *
	 * @param other the other map
	 * @return returns the foreign id, or 0 on failure
	 */

	public int convertTo(ItemMap other, int localId) {
		int foreignId = 0;

		// Check cache
		if (other == this) {
			if (store.reverseGet(localId) != null) {
				return localId;
			} else {
				return 0;
			}
		} else if (other == parent) {
			foreignId = thisToParentMap[localId];
		} else if (other.parent == this) {
			foreignId = other.parentToThisMap[localId];
		}

		// Cache hit
		if (foreignId != 0) {
			return foreignId;
		}

		String localKey = store.reverseGet(localId);

		// There is no entry in the local map to perform the translation
		if (localKey == null) {
			return 0;
		}

		Integer integerForeignId = other.store.get(localKey);

		// The other map doesn't have an entry for this key
		if (integerForeignId == null) {
			integerForeignId = other.register(localKey);
		}

		// Add the key/value pair to the cache, if is no problem with the foreign key
		if (integerForeignId != 0) {
			if (other == parent) {
				thisToParentMap[localId] = integerForeignId;
				parentToThisMap[integerForeignId] = localId;
			} else if (other.parent == this) {
				other.thisToParentMap[integerForeignId] = localId;
				other.parentToThisMap[localId] = integerForeignId;
			}
		}

		return integerForeignId;
	}

	/**
	 * Converts a foreign id, local to a foreign map to an id local to this map.
	 *
	 * @param other the other map
	 * @return returns the local id, or 0 on failure
	 */

	public int convertFrom(ItemMap other, int foreignId) {
		return other.convertTo(this, foreignId);
	}

	/**
	 * Registers a key with the map and returns the matching id.
	 *
	 * The id corresponding to a key will be consistent if registered more than once, including over restarts, subject to the persistence of the store.
	 *
	 * @param key the key to be added
	 * @return returns the local id, or 0 on failure
	 */

	public int register(String key) {
		Integer id = store.get(key);

		if (id != null) {
			return id;
		} else {
			id = findFreeId();
			if (id != 0) {
				store.set(key, id);
				if (updateTask != null) {
					updateTask.run(this, key, id);
				}
			}
			return id;
		}
	}

	/**
	 * Saves the map to the persistence system
	 *
	 * @return returns true if the map saves correctly
	 */
	public boolean save() {
		return store.save();
	}

	private int findFreeId() {
		int offset = 0;
		boolean freeFound = false;
		int checkPos = 0;
		while (offset < 65536 && !freeFound) {
			checkPos = (offset + nextId) % 65536;
			if (checkPos >= 1024 && store.reverseGet(checkPos) == null) {
				freeFound = true;
			}
			offset++;
		}
		if (!freeFound) {
			return 0;
		} else {
			nextId = checkPos + 1;
			return checkPos;
		}
	}

	public void rename(String oldKey, String newKey) {
		Integer id = store.get(oldKey);
		if (id != null) {
			store.set(newKey, id);
			store.remove(oldKey);
			save();
		}
	}

	/**
	 * Returns the id associated with the key.
	 * @param key The key to lookup
	 * @return The id associated with the key
	 */
	public Integer get(String key) {
		return store.get(key);
	}
}
