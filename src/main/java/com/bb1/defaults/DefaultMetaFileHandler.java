package com.bb1.defaults;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.bb1.interfaces.MetaSavingHandler;
import com.bb1.interfaces.MetaType;

import lombok.NonNull;

public class DefaultMetaFileHandler implements MetaSavingHandler {
	
	private final Plugin plugin;
	
	public DefaultMetaFileHandler(@NonNull Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public String getFilePath() {
		//return Bukkit.getServer().getWorldContainer().getAbsolutePath()+"/metaData.mdata";
		return this.plugin.getDataFolder().getAbsolutePath().replaceAll(this.plugin.getName(), "")+"metaData.mdata";
	}
	
	@Override
	public void save(Map<Location, Set<MetaType>> map) {
		File f;
		PrintWriter writer;
		try {
			f = new File(getFilePath());
			f.delete();
			f.createNewFile();
			writer = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			writer.print(serialize(map));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Location, Set<MetaType>> load() {
		Map<Location, Set<MetaType>> map;
		File f;
		Scanner reader;
		try {
			f = new File(getFilePath());
			if (f.exists()) {
				reader = new Scanner(f);
			    ArrayList<String> fileContents = new ArrayList<>();
			    while (reader.hasNext()) {
			    	fileContents.add(reader.nextLine());
				}
			    reader.close();
			    map = (Map<Location, Set<MetaType>>) deserialize(String.join("", fileContents));
			    System.out.println("Loaded MetaData!");
			} else {
				map = new HashMap<Location, Set<MetaType>>();
				System.out.println("There is no MetaData to load");
			}
		} catch (Exception e) {
			map = new HashMap<Location, Set<MetaType>>();
			System.out.println("Failed to load MetaData! Creating a backup file of current data");
			try {
				reader = new Scanner(new File(getFilePath()));
				f = new File(getFilePath()+".backup");
				f.delete();
				f.createNewFile();
				PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			    while (reader.hasNext()) {
			    	writer.print(reader.nextLine());
				}
			    reader.close();
				writer.flush();
				writer.close();
				System.err.println("Created backup!");
			} catch (Exception e1) {
				System.err.println("Failed to create backup! Printing stacktraces");
				e.printStackTrace();
				e1.printStackTrace();
			}
		}
		return map;
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