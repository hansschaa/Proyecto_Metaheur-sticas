/**
 *  JSoko - A Java implementation of the game of Sokoban
 *  Copyright (c) 2014 by Matthias Meger, Germany
 *
 *  This file is part of JSoko.
 *
 *	JSoko is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.sokoban_online.jsoko.leveldata;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import de.sokoban_online.jsoko.leveldata.SelectableLevelCollectionComboBoxModel.SelectableLevelCollection;


/**
 * The list of available collections selectable for playing.
 * The list is displayed in the GUI. The user can see the collection
 * currently being played and select any other in the list for playing.
 * <p>
 * The list contains the last played level collections and the collections
 * that are stored in the database. <br>
 * This list only stores the collection data WITHOUT the level data to ensure a low RAM usage.
 */
@SuppressWarnings("serial")
public class SelectableLevelCollectionComboBoxModel extends DefaultComboBoxModel<SelectableLevelCollection> {

	private ArrayList<SelectableLevelCollection> lastPlayedLevelCollections = new ArrayList<>();

	/** Number of level collections to be saved as "last played". */
	private final int maxLastPlayedLevelCollectionsCount;


	/**
	 * Creates a new model for managing the level collections to be played.
	 *
	 * @param maxLastPlayedLevelCollectionsCount  maximum number of level collections to be
	 * stored as last played
	 */
	public SelectableLevelCollectionComboBoxModel(int maxLastPlayedLevelCollectionsCount) {
		this.maxLastPlayedLevelCollectionsCount = maxLastPlayedLevelCollectionsCount;
	}

	/**
	 * Fills the list of available collections selectable for playing.
	 * The list is displayed in the GUI. The user can see the collection
	 * currently being played and select any other in the list for playing.
	 *
	 * @param database the {@code Database} to read the available collections from
	 */
	public void updateAvailableCollections(Database database) {

		// This list is filled from scratch.
		removeAllElements();

		HashMap<Integer, SelectableLevelCollection> collectionsFromDatabase = new HashMap<>();

		// Add all level collections stored in the database.
		for(LevelCollection levelCollection : database.getCollectionsInfo()) {
			SelectableLevelCollection collection = new SelectableLevelCollection(levelCollection, false);
			collectionsFromDatabase.put(collection.databaseID, collection);
			addElement(collection);
		}

		// Add the last played collections at the beginning of the list.
		ArrayList<SelectableLevelCollection> newLastPlayedLevelCollections = new ArrayList<>();
		for(SelectableLevelCollection levelCollection : lastPlayedLevelCollections) {
			// The user may have changed the title in the database -> take the refreshed
			// data for collections stored in the database.
			if(levelCollection.databaseID != Database.NO_ID) {
				SelectableLevelCollection levelCollectionFromDatabase = collectionsFromDatabase.get(levelCollection.databaseID);
				if(levelCollectionFromDatabase == null) {
					continue;  // Either the ID in the settings file is invalid or the collection has been deleted from the database.
				}
				levelCollection = new SelectableLevelCollection(levelCollectionFromDatabase, true);
			}
			newLastPlayedLevelCollections.add(levelCollection);
			insertElementAt(levelCollection, 0);
		}
		lastPlayedLevelCollections = newLastPlayedLevelCollections;
		setSelectedItem(getElementAt(0));
	}

	/**
	 * Adds the passed collection as "last played collection" to the selectable collections.<br>
	 * <p>
	 * Note: the contained levels of the collection are NOT stored to ensure a low RAM usage.
	 *
	 * @param levelCollection the collection to be added
	 */
	public void newLevelCollectionIsPlayed(LevelCollection levelCollection) {

		if(levelCollection == null) {
			return;
		}

		// However, this list can only contain level collections that can be loaded when selected.
		// Level collections imported from the clipboard or the editor aren't saved, yet.
		// Hence, they aren't added to this list.
		if(!levelCollection.isConnectedWithDatabase() && levelCollection.getFile().isEmpty()) {
			return;
		}

		// For lower RAM usage only relevant data of a level collection are stored in this list.
		SelectableLevelCollection selectableLevelCollection = new SelectableLevelCollection(levelCollection, true);

		// Remove the collection if it is already in the list: it's inserted again at position 0.
		if(lastPlayedLevelCollections.remove(selectableLevelCollection)) {
			removeElement(selectableLevelCollection);
		}
		lastPlayedLevelCollections.add(selectableLevelCollection);
		insertElementAt(selectableLevelCollection, 0);
		if(lastPlayedLevelCollections.size() > maxLastPlayedLevelCollectionsCount) {
			removeElement(lastPlayedLevelCollections.remove(0));
		}

	}

	/**
	 * Sets the passed {@code LevelCollection} as selected item.
	 *
	 * @param levelCollection  the {@code LevelCollection} to select
	 */
	public void setSelectedItem(LevelCollection levelCollection) {
		super.setSelectedItem(new SelectableLevelCollection(levelCollection, false));
	}

	/**
	 * Sets the last played collections.
	 *
	 * @param lastPlayedCollections
	 */
	public void setLastPlayedCollections(List<SelectableLevelCollection> lastPlayedCollections) {
		lastPlayedLevelCollections.clear();
		lastPlayedLevelCollections.addAll(lastPlayedCollections);
	}


	@Override
	public SelectableLevelCollection getSelectedItem() {
		return (SelectableLevelCollection) super.getSelectedItem();
	}


	/**
	 * Returns the index to display a separator dividing the last played collections
	 * from the selectable collections.
	 *
	 * @return the index
	 */
	public int getSeparatorIndex() {
		return lastPlayedLevelCollections.size() - 1;
	}

	/**
	 * Returns the last played level collections.
	 *
	 * @return the last played level collections
	 */
	public List<SelectableLevelCollection> getLastPlayedCollections() {
		return lastPlayedLevelCollections;
	}

	/**
	 * Returns the {@code SelectableLevelCollection} stored in this model having the passed database ID.
	 *
	 * @param databaseID the database ID to search
	 * @return the {@code SelectableLevelCollection} having the passed database ID
	 */
	public SelectableLevelCollection getLevelCollectionByID(final int databaseID) {
		for(int index=0; index < getSize(); index++) {
			SelectableLevelCollection collection = getElementAt(index);
			if(collection != null && collection.databaseID == databaseID) {
				return collection;
			}
		}

		return null;
	}

	/**
	 * This list only contains the relevant data of the level collections for lower RAM usage.
	 * These relevant data are stored in this class.
	 */
	public final static class SelectableLevelCollection {

		/** Title of the collection. */
		public final String title;

		/** File the collection has been loaded from. */
		public final String file;

		/** ID of the collection in the database. */
		public final int databaseID;

		/** Flag, indicating whether this is a "last played" level collection. */
		public final boolean isLastPlayedEntry;


		/**
		 * Creates a new item to be added to the list of selectable level collections.
		 *
		 * @param title the title of the collection
		 * @param file the file the collection is stored in
		 * @param databaseID the ID of the collection in the database
		 * @param isLastPlayedEntry  flag indicating whether this is a "last played" level collection
		 */
		public SelectableLevelCollection(String title, String file, int databaseID, boolean isLastPlayedEntry) {
			this.title = title;
			this.file  = file;
			this.databaseID = databaseID;
			this.isLastPlayedEntry = isLastPlayedEntry;
		}


		/**
		 * Creates a new item to be added to the list of selectable level collections.
		 *
		 * @param levelCollection  the {@code LevelCollection} to extract the data from
		 * @param isLastPlayedEntry  flag indicating whether this is a "last played" level collection
		 */
		public SelectableLevelCollection(LevelCollection levelCollection, boolean isLastPlayedEntry) {
			this(levelCollection.getTitle(), levelCollection.getFile(), levelCollection.getDatabaseID(), isLastPlayedEntry);
		}

		/**
		 * Creates a new item to be added to the list of selectable level collections.
		 *
		 * @param levelCollection  the {@code SelectableLevelCollection} to extract the data from
		 * @param isLastPlayedEntry  flag indicating whether this is a "last played" level collection
		 */
		public SelectableLevelCollection(SelectableLevelCollection levelCollection, boolean isLastPlayedEntry) {
			this(levelCollection.title, levelCollection.file, levelCollection.databaseID, isLastPlayedEntry);
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + databaseID;
			result = prime * result + ((file == null) ? 0 : file.hashCode());
			result = prime * result + (isLastPlayedEntry ? 1231 : 1237);
			result = prime * result + ((title == null) ? 0 : title.hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			SelectableLevelCollection other = (SelectableLevelCollection) obj;
			if (databaseID != other.databaseID) {
				return false;
			}
			if (file == null) {
				if (other.file != null) {
					return false;
				}
			} else if (!file.equals(other.file)) {
				return false;
			}
			if (isLastPlayedEntry != other.isLastPlayedEntry) {
				return false;
			}
			if (title == null) {
				if (other.title != null) {
					return false;
				}
			} else if (!title.equals(other.title)) {
				return false;
			}
			return true;
		}
	}
}