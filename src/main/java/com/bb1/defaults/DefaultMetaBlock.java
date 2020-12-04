package com.bb1.defaults;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.metadata.LazyMetadataValue.CacheStrategy;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.bb1.interfaces.MetaBlock;
import com.bb1.interfaces.MetaType;

import lombok.Getter;

public class DefaultMetaBlock implements MetaBlock {
	/**
	 * The key the MetaTypes are saved under
	 */
	private static final String METAKEY = "BB1DATA";
	/**
	 * The Bukkit version of the block
	 */
	private final Block block;
	/**
	 * The map of the blocks data
	 */
	private final @Getter Set<MetaType> data;
	/**
	 * The {@link Plugin} that the MetaData will be saved under
	 */
	private final Plugin plugin;
	
	public DefaultMetaBlock(Block block, Plugin plugin) {
		this.block = block;
		this.plugin = plugin;
		if (hasMetaData()) {
			this.data = getDataFromBlock();
		} else {
			this.data = new HashSet<MetaType>();
			setDataToBlock();
		}
	}
	
	@Override
	public Block getBlock() {
		// Return the local copy of the block
		return this.block;
	}

	@Override
	public void addMetaType(MetaType metaType) {
		// Get the MetaType's key
		String metaTypeKey = metaType.getKey();
		// Loop threw all MetaTypes
		for (MetaType metaType2 : getData()) {
			// If there keys match
			if (metaType2.getKey().equals(metaTypeKey)) {
				// Check if the MetaType can be overridden
				if (metaType2.canBeOverridden()) {
					// If so stop the loop and return as it cannot be changed
					return;
				} else {
					// Elsewise remove the MetaType
					removeMetaType(metaType2);
					// And stop the loop
					break;
				}
			}
		}
		this.data.add(metaType);
	}

	@Override
	public MetaType getMetaType(String metaTypeKey) {
		// Loop threw all MetaTypes
		for (MetaType metaType : getData()) {
			// Check if the keys match
			if (metaType.getKey().equals(metaTypeKey)) {
				// If they do return the MetaType
				return metaType;
			}
		}
		// If no matching key can be found return null
		return null;
	}

	@Override
	public void removeMetaType(String metaTypeKey) {
		// Loop threw all MetaTypes
		for (MetaType metaType : getData()) {
			// If the keys match
			if (metaType.getKey().equals(metaTypeKey)) {
				// Call the remove method
				removeMetaType(metaType);
				// Stop the loop to reduce wasted process time
				break;
			}
		}
	}

	@Override
	public void removeMetaType(MetaType metaType) {
		// Check if the MetaType can be overridden
		if (metaType.canBeOverridden()) {
			// If so remove it
			this.data.remove(metaType);
		}
	}

	@Override
	public void updateFromBlock() {
		// Clear all old MetaData
		clear(true);
		// Take a copy of all the data on the block
		copyDataFromBlock();
	}

	@Override
	public void updateToBlock() {
		// Remove old MetaData
		this.block.removeMetadata(METAKEY, this.plugin);
		// Add the MetaData to the block
		setDataToBlock();
	}

	@Override
	public Set<MetaType> getAllMetaTypesOnBlock() {
		return this.data;
	}

	@Override
	public void clear() {
		// Loop threw all contained MetaTypes
		for (MetaType metaType : this.data) {
			// If they can be overridden
			if (metaType.canBeOverridden()) {
				// If so remove it
				this.data.remove(metaType);
			}
		}
	}

	@Override
	public void clear(boolean force) {
		// Check if the force variable is set to true
		if (force) {
			// If it is clear all data
			this.data.clear();
		} else {
			// If it is not call #clear();
			clear();
		}
	}
	
	// Metadata stuff
	
	private Set<MetaType> getDataFromBlock() {
		// Create new set
		Set<MetaType> set = new HashSet<>();
		List<MetadataValue> data = this.block.getMetadata(METAKEY);
		// Check if the list is empty or null to avoid errors
		if (data==null || data.isEmpty()) {
			return set;
		} else {
			// Loop threw all new MetaData and add it to our copy
			for (MetadataValue value : data) {
				// Get the MetadataValue as a string
				String valueAsString = value.asString();
				// Convert it from a string to a MetaType set
				Set<MetaType> valueAsList = fromSaveableString(valueAsString);
				// Do a null and empty check on the set
				if (valueAsList==null || valueAsList.isEmpty()) continue;
				// If its not empty or null add it to the set
				set.addAll(valueAsList);
			}
		}
		return set;
	}
	
	private void copyDataFromBlock() {
		// Get the MetaData from the block
		List<MetadataValue> data = this.block.getMetadata(METAKEY);
		// Check if the list is empty or null to avoid errors
		if (data==null || data.isEmpty()) {
			return;
		} else {
			// Loop threw all new MetaData and add it to our copy
			for (MetadataValue value : data) {
				// Get the MetadataValue as a string
				String valueAsString = value.asString();
				// Convert it from a string to a MetaType set
				Set<MetaType> valueAsList = fromSaveableString(valueAsString);
				// Do a null and empty check on the set
				if (valueAsList==null || valueAsList.isEmpty()) continue;
				// If its not empty or null add it to the data set
				this.data.addAll(valueAsList);
			}
		}
	}
	
	/**
	 * @deprecated There is never a reason where you would need to make the data on the block unchangeable as this would render this whole system pointless
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void lockDataToBlock() {
		// Create the data to be saved
		String data = toSaveableString(this.data);
		// Create the MetaData
		FixedMetadataValue metaData = new FixedMetadataValue(this.plugin, data);
		// Apply it to the block
		this.block.setMetadata(METAKEY, metaData);
	}
	
	private void setDataToBlock() {
		// Create the MetaData
		LazyMetadataValue metaData = new LazyMetadataValue(this.plugin, CacheStrategy.NEVER_CACHE, getCallable());
		// Apply it to the block
		this.block.setMetadata(METAKEY, metaData);
	}
	
	private Callable<Object> getCallable() {
		// Create a new Callable
		return new Callable<Object>() {
			// Create the final string that our data will be saved under
			public final String callback = toSaveableString(data);
			
			@Override
			public Object call() throws Exception {
				// Return the data
				return this.callback;
			}
			
		};
	}
	
	// Handling and reading metadata
	
	// Required due to json.simple
	@SuppressWarnings("unchecked")
	public static String toSaveableString(Set<MetaType> metaTypes) {
		// Create json array data will be stored in
		JSONArray jsonArray = new JSONArray();
		// Loop threw all of the MetaTypes
		for (MetaType metaType : metaTypes) {
			// Serialise the MetaType and add it to the array
			jsonArray.add(serialize(metaType));
		}
		return jsonArray.toJSONString();
	}
	
	public static Set<MetaType> fromSaveableString(String data) {
		// Create a set to return
		Set<MetaType> set = new HashSet<>();
		// Create a dummy json array and set it to null
		JSONArray jsonArray = null;
		try {
			// Parse the json
			Object object = new JSONParser().parse(data);
			// Check if its a json array
			if (object instanceof JSONArray) {
				// If it is cast it to one and set it to jsonArray
				jsonArray = (JSONArray) object;
			}
		} catch (ParseException ingore) {
			// Ignore this exception
		}
		// Check if the array was set and if not return the empty set
		if (jsonArray==null) return set;
		// Loop threw all values in the json array
		for (Object object : jsonArray) {
			// Check if object is a string
			if (object instanceof String) {
				// If so cast it to one and deserialise it
				Object metaData = deserialize((String) object);
				// Check if metaData is a MetaType
				if (metaData instanceof MetaType) {
					// If so cast it to one and add it to the set
					set.add((MetaType) metaData);
				}
			}
		}
		return set;
	}
	
	// Extra stuff
	
	public boolean exists() {
		try {
			// do a check to see if the block is not air
			return (!this.block.getLocation().getBlock().getType().equals(Material.AIR));
		} catch (Exception e) {
			// If an error occurs its due to the block not existing so return false
			return false;
		}
	}
	
	public boolean hasMetaData() {
		// Simply checks and returns if MetaData exists on the block already
		return this.block.hasMetadata(METAKEY);
	}
	
	// Serialisation stuff
	
	private static final String serialize(Object o) {
	    try {
	        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
	        BukkitObjectOutputStream out = new BukkitObjectOutputStream(bytesOut);
	        out.writeObject(o);
	        out.flush();
	        out.close();
	        return Base64Coder.encodeLines(bytesOut.toByteArray());
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return null;
	    }
	}

	private static final Object deserialize(String base64) {
	    try {
	        byte[] data = Base64Coder.decodeLines(base64);
	        ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
	        BukkitObjectInputStream in = new BukkitObjectInputStream(bytesIn);
	        Object o = in.readObject();
	        in.close();
	        return o;
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return null;
	    }
	}

}
