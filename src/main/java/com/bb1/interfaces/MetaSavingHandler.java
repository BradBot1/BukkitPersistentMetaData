package com.bb1.interfaces;

import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
/**
 * 
 * @author BradBot_1
 * 
 * An interface used for saving the persistent MetaData
 */
public interface MetaSavingHandler {
	/**
	 * The path to the file that the MetaData is saved in
	 */
	public String getFilePath();
	/**
	 * Saves the {@link Map} provided into a file
	 * 
	 * @param metaTypeSetFromLocationMap The {@link Map} to be saved
	 */
	public void save(Map<Location, Set<MetaType>> metaTypeSetFromLocationMap);
	/**
	 * Converts the files contents back into the original {@link Map} provided
	 */
	public Map<Location, Set<MetaType>> load();
}
