package com.scs.voxlib;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class VoxTransformChunk extends VoxChunk {

	public int id;
	public int child_node_id;
	public GridPoint3 transform = new GridPoint3();

	public VoxTransformChunk(InputStream stream) throws IOException {
		id = StreamUtils.readIntLE(stream);
		HashMap<String, String> dict = StreamUtils.readDictionary(stream);
		/*if (dict.containsKey("_name")) {
			Settings.p("nTrn Name: " + dict.get("_name"));
		}*/
		// todo - check for "_hidden"
		child_node_id = StreamUtils.readIntLE(stream);
		int neg1 = StreamUtils.readIntLE(stream);
		if (neg1 != -1) {
			throw new RuntimeException("neg1 checksum failed");
		}
		int layer_id = StreamUtils.readIntLE(stream);
		int num_frames = StreamUtils.readIntLE(stream);

		// Rotation
		for (int i=0 ; i<num_frames ; i++) {
			HashMap<String, String> rot = StreamUtils.readDictionary(stream);
			if (rot.containsKey("_t")) {
				//Settings.p("Got _t=" + rot.get("_t"));
				String[] tokens = rot.get("_t").split(" ");
				GridPoint3 tmp = new GridPoint3(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
				transform.set(tmp.x, tmp.y, tmp.z);
			}
			if (rot.containsKey("_r")) {
				System.err.println("Warning: _r is being ignored");
			}
		}
	}
	

	@Override
	public String toString() {
		return "VoxTransformChunk#" + id + "_" + this.transform;
	}
	
}


