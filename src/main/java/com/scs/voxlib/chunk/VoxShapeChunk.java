package com.scs.voxlib.chunk;

import com.scs.voxlib.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class VoxShapeChunk extends VoxChunk {
	
	public final int id;
	public List<Integer> model_ids = new ArrayList<Integer>();

    public VoxShapeChunk(int id) {
        super(ChunkFactory.nSHP);
        this.id = id;
    }

    public static VoxShapeChunk read(InputStream stream) throws IOException {
        var id = StreamUtils.readIntLE(stream);
        var chunk = new VoxShapeChunk(id);

        HashMap<String, String> dict = StreamUtils.readDictionary(stream);
        /*if (dict.size() > 0) {
    		Settings.p("dict=" + dict);
        }*/

        int num_models = StreamUtils.readIntLE(stream);

        for (int i=0 ; i<num_models ; i++) {
            int model_id = StreamUtils.readIntLE(stream);
            HashMap<String, String> model_dict = StreamUtils.readDictionary(stream);
            /*if (model_dict.size() > 0) {
        		Settings.p("model_dict=" + dict);
            }*/

            chunk.model_ids.add(model_id);
        }
        return chunk;
	}

    @Override
    protected void writeContent(OutputStream stream) throws IOException {
        StreamUtils.writeIntLE(id, stream);
        StreamUtils.writeIntLE(0, stream); // dict
        StreamUtils.writeIntLE(model_ids.size(), stream);
        for (var modelId : model_ids) {
            StreamUtils.writeIntLE(modelId, stream);
            StreamUtils.writeIntLE(0, stream); // dict
        }
    }
}
