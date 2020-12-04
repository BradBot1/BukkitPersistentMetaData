package com.bb1.metatypes;

import com.bb1.interfaces.MetaType;

import lombok.NonNull;

public class MetaTypeStringArray implements MetaType {
	
	private static final long serialVersionUID = 136207988888674625L;
	
	private final String joinKey;
	
	private String key;
	private String[] value;
	
	public MetaTypeStringArray(@NonNull String key, String joinKey, @NonNull String... value) {
		this.key = key;
		this.value = value;
		this.joinKey = joinKey;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getValue() {
		return String.join(this.joinKey, this.value);
	}

	@Override
	public String getMetaTypeName() {
		return "String[]";
	}

	@Override
	public boolean canBeOverridden() {
		return true;
	}
	
}
