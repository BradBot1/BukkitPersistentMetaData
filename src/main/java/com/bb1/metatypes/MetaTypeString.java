package com.bb1.metatypes;

import com.bb1.interfaces.MetaType;

import lombok.NonNull;

public class MetaTypeString implements MetaType {
	
	private static final long serialVersionUID = 136207988888674625L;
	
	private String key;
	private String value;
	
	public MetaTypeString(@NonNull String key, @NonNull String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String getMetaTypeName() {
		return "String";
	}

	@Override
	public boolean canBeOverridden() {
		return true;
	}
	
}
