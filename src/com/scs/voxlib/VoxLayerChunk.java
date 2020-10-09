package com.scs.voxlib;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class VoxLayerChunk extends VoxChunk {

	public int id;
	public int child_node_id;
	public GridPoint3 transform = new GridPoint3();

	public VoxLayerChunk(InputStream stream) throws IOException {
		id = StreamUtils.readIntLE(stream);
		HashMap<String, String> dict = StreamUtils.readDictionary(stream);
		//Settings.p("dict=" + dict);
		/*if (dict.containsKey("_name")) {
			Settings.p("Layer Name: " + dict.get("_name"));
		}*/
		int reserved = StreamUtils.readIntLE(stream);
	}
	

	@Override
	public String toString() {
		return "VoxLayerChunk#" + id + "_" + this.transform;
	}
	
}


