package com.bb1.interfaces;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
/**
 * 
 * @author BradBot_1
 * 
 * A simple interface to allow for persistent MetaData
 *
 */
public interface MetaBlock {
	/**
	 * Returns the {@link Block} the {@link MetaBlock} is linked to
	 */
	public Block getBlock();
	/**
	 * Adds the {@link MetaType} specified to the copy
	 * 
	 * @param metaType The {@link MetaType} to add
	 */
	public void addMetaType(MetaType metaType);
	/**
	 * Gets and returns the {@link MetaType} based on the key provided, if no key is found null is returned
	 * 
	 * @param metaTypeKey The key of the {@link MetaType}
	 */
	public MetaType getMetaType(String metaTypeKey);
	/**
	 * Simply calls {@link #getMetaType(String)} and if it receives a null returns the provided default value
	 * 
	 * @param metaTypeKey The key of the {@link MetaType}
	 * @param defaultMetaType The default {@link MetaType} to return if no data is found
	 */
	public default MetaType getOrDefaultMetaType(String metaTypeKey, MetaType defaultMetaType) {
		MetaType metaType = getMetaType(metaTypeKey);
		return (metaType==null) ? defaultMetaType : metaType;
	}
	/**
	 * Gets and returns all {@link MetaType}s from {@link #getBlock()}
	 */
	public Set<MetaType> getAllMetaTypesOnBlock();
	/**
	 * Gets a {@link MetaType} based on its key and then removes it via the {@link #removeMetaType(MetaType)} method
	 * 
	 * @param metaTypeKey The key of the {@link MetaType} to remove
	 */
	public void removeMetaType(String metaTypeKey);
	/**
	 * Removes the {@link MetaType} specified
	 * 
	 * @param metaType The {@link MetaType} to remove
	 */
	public void removeMetaType(MetaType metaType);
	/**
	 * Gets all {@link MetaType}s that is saved onto the {@link #getBlock()} and takes a copy
	 * 
	 * @apiNote This usually runs {@link #clear()}
	 */
	public void updateFromBlock();
	/**
	 * Adds all saved {@link MetaType}s to the {@link #getBlock()}
	 * 
	 * @apiNote This will run {@link #clear(boolean)} with a true value!
	 */
	public void updateToBlock();
	/**
	 * Empties the saved {@link MetaType}
	 * 
	 * @apiNote Does not remove {@link MetaType}s that have {@link MetaType#canBeOveridden()} set to false
	 */
	public void clear();
	/**
	 * @param force If the {@link #clear()} should remove {@link MetaType}s with {@link MetaType#canBeOveridden()} set to false
	 */
	public void clear(boolean force);
	/**
	 * Get the {@link Block} from {@link #getBlock()}'s {@link Location}
	 */
	public default Location getLocation() {
		return getBlock().getLocation();
	}
}