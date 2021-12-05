package com.scs.voxlib.chunk;

import com.scs.voxlib.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class VoxGroupChunk extends VoxChunk {
	
	public final int id;
	public List<Integer> child_ids = new ArrayList<Integer>();

    public VoxGroupChunk(int id) {
        super(ChunkFactory.nGRP);
        this.id = id;
    }

    public static VoxGroupChunk read(InputStream stream) throws IOException {
        var id = StreamUtils.readIntLE(stream);
        var chunk = new VoxGroupChunk(id);
        HashMap<String, String> dict = StreamUtils.readDictionary(stream);
        /*if (dict.size() > 0) {
    		Settings.p("dict=" + dict);
        }*/
        int num_children = StreamUtils.readIntLE(stream);

        for (int i=0 ; i<num_children ; i++) {
            int child_id = StreamUtils.readIntLE(stream);
            chunk.child_ids.add(child_id);
        }
        return chunk;
    }

    @Override
    protected void writeContent(OutputStream stream) throws IOException {
        StreamUtils.writeIntLE(id, stream);
        StreamUtils.writeIntLE(0, stream); // dict
        StreamUtils.writeIntLE(child_ids.size(), stream);
        for (var childId : child_ids) StreamUtils.writeIntLE(childId, stream);
    }
}
