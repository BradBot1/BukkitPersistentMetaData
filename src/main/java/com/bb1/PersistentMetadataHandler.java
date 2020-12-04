package com.bb1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import com.bb1.defaults.DefaultMetaBlock;
import com.bb1.defaults.DefaultMetaFileHandler;
import com.bb1.interfaces.MetaBlock;
import com.bb1.interfaces.MetaSavingHandler;
import com.bb1.interfaces.MetaType;

import lombok.Getter;

public class PersistentMetadataHandler {
	
	private static @Getter PersistentMetadataHandler persistentMetadataHandler;
	
	private static @Getter Plugin instance;
	
	private static final Set<Block> BLOCKSTOSAVE = new HashSet<>();
	
	private final MetaSavingHandler metaSavingHandler;
	
	private final GetMetaBlockCallable metaBlockCallable;
	
	public PersistentMetadataHandler(Plugin plugin, MetaSavingHandler metaSavingHandler, GetMetaBlockCallable callable) {
		if (getPersistentMetadataHandler()!=null) {
			Bukkit.getPluginManager().disablePlugin(plugin);
			throw new RuntimeException("MetaDataHandler cannot be called when already initialised!");
		}
		instance = plugin;
		if (metaSavingHandler==null) {
			this.metaSavingHandler = new DefaultMetaFileHandler(instance);
		} else {
			this.metaSavingHandler = metaSavingHandler;
		}
		if (callable==null) {
			this.metaBlockCallable = new GetMetaBlockCallable() {

				@Override
				public MetaBlock getMetaBlock(Block block, Plugin plugin) {
					return new DefaultMetaBlock(block, plugin);
				}
				
			};
		} else {
			this.metaBlockCallable = callable;
		}
		persistentMetadataHandler = this;
	}
	
	public MetaBlock getMetaBlockOf(Block block) {
		BLOCKSTOSAVE.add(block);
		return this.metaBlockCallable.getMetaBlock(block, instance);
	}
	
	public void save() {
		Map<Location, Set<MetaType>> map = new HashMap<>();
		for (Block b : BLOCKSTOSAVE) {
			if (b==null || b.getLocation()==null) continue;
			map.put(b.getLocation(), getMetaBlockOf(b).getAllMetaTypesOnBlock());
		}
		this.metaSavingHandler.save(map);
	}
	
	public void load() {
		Map<Location, Set<MetaType>> map = this.metaSavingHandler.load();
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {

			@Override
			public void run() {
				map.forEach((l,s) -> {
					MetaBlock metaBlock = getMetaBlockOf(l.getBlock());
					for (MetaType metaType : s) {
						metaBlock.addMetaType(metaType);
					}
				});
			}
			
		}, 1l);
	}
	
}
