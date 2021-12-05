package com.scs.voxlib.chunk;

import com.scs.voxlib.GridPoint3;
import com.scs.voxlib.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public final class VoxTransformChunk extends VoxChunk {

	public final int id;
	public int child_node_id;
	public GridPoint3 transform = new GridPoint3();

	public VoxTransformChunk(int id) {
		super(ChunkFactory.nTRN);
		this.id = id;
	}

	public static VoxTransformChunk read(InputStream stream) throws IOException {
		var id = StreamUtils.readIntLE(stream);
		var chunk = new VoxTransformChunk(id);
		HashMap<String, String> dict = StreamUtils.readDictionary(stream);
		/*if (dict.containsKey("_name")) {
			Settings.p("nTrn Name: " + dict.get("_name"));
		}*/
		// todo - check for "_hidden"
		chunk.child_node_id = StreamUtils.readIntLE(stream);
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
				chunk.transform.set(tmp.x, tmp.y, tmp.z);
			}
			if (rot.containsKey("_r")) {
				System.err.println("Warning: _r is being ignored");
			}
		}
		return chunk;
	}

	@Override
	public String toString() {
		return "VoxTransformChunk#" + id + "_" + this.transform;
	}

	@Override
	protected void writeContent(OutputStream stream) throws IOException {
		StreamUtils.writeIntLE(id, stream);
		StreamUtils.writeIntLE(0, stream); // dict
		StreamUtils.writeIntLE(child_node_id, stream);
		StreamUtils.writeIntLE(-1, stream); // neg
		StreamUtils.writeIntLE(0, stream); // layer_id
		if (transform.x != 0 || transform.y != 0 || transform.z != 0) {
			StreamUtils.writeIntLE(1, stream); // frames
			var rot = new HashMap<String, String>();
			rot.put("_t", String.format("%d %d %d", transform.x, transform.y, transform.z));
			StreamUtils.writeDictionary(rot, stream);
		} else {
			StreamUtils.writeIntLE(0, stream); // frames
		}

	}
}


