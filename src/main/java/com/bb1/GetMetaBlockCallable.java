package com.bb1;

import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import com.bb1.interfaces.MetaBlock;

public abstract class GetMetaBlockCallable {
	
	public abstract MetaBlock getMetaBlock(Block block, Plugin plugin);
	
}
