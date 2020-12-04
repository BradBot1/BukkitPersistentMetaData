package com.bb1.metatypes;

import com.bb1.interfaces.MetaType;

import lombok.NonNull;

public class MetaTypeInteger implements MetaType {
	
	private static final long serialVersionUID = -8861314298523049958L;
	
	private String key;
	private Integer value;
	
	public MetaTypeInteger(@NonNull String key, @NonNull Integer value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getValue() {
		return Integer.toString(this.value);
	}

	@Override
	public String getMetaTypeName() {
		return "Integer";
	}

	@Override
	public boolean canBeOverridden() {
		return true;
	}
	
}
