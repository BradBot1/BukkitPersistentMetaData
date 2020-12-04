package com.bb1.interfaces;

import java.io.Serializable;
/**
 * 
 * @author BradBot_1
 * 
 * An interface that MetaData can be stored in
 * 
 */
public interface MetaType extends Serializable {
	/**
	 * The key for the MetaData
	 */
	public String getKey();
	/**
	 * The value of the MetaData
	 */
	public String getValue();
	/**
	 * If the tag can be overridden after being added
	 */
	public boolean canBeOverridden();
	// Stuff every type will inherit
	/**
	 * Get the {@link MetaType}'s name
	 */
	public String getMetaTypeName();	
}
