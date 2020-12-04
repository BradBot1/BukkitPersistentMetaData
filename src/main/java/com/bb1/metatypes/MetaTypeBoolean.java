package com.bb1.metatypes;

import com.bb1.interfaces.MetaType;

import lombok.NonNull;

public class MetaTypeBoolean implements MetaType {
	
	private static final long serialVersionUID = -8861314298523049958L;
	
	private String key;
	private Boolean value;
	
	public MetaTypeBoolean(@NonNull String key, @NonNull Boolean value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getValue() {
		return Boolean.toString(this.value);
	}

	@Override
	public String getMetaTypeName() {
		return "Boolean";
	}

	@Override
	public boolean canBeOverridden() {
		return true;
	}
	
}
