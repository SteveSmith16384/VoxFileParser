package com.scs.voxlib.chunk;

import com.scs.voxlib.GridPoint3;
import com.scs.voxlib.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class VoxLayerChunk extends VoxChunk {

	public int id;
	public int child_node_id;
	public GridPoint3 transform = new GridPoint3();

	public VoxLayerChunk(String type) {
		super(type);
	}

	public static VoxLayerChunk read(String type, InputStream stream)
		throws IOException
	{
		var chunk = new VoxLayerChunk(type);
		//TODO implement VoxLayerChunk
		chunk.id = StreamUtils.readIntLE(stream);
		HashMap<String, String> dict = StreamUtils.readDictionary(stream);
		//Settings.p("dict=" + dict);
		/*if (dict.containsKey("_name")) {
			Settings.p("Layer Name: " + dict.get("_name"));
		}*/
		int reserved = StreamUtils.readIntLE(stream);
		return chunk;
	}

	@Override
	public String toString() {
		return "VoxLayerChunk#" + id + "_" + this.transform;
	}
	
}


