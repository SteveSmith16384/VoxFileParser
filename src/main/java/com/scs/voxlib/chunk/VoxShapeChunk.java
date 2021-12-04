package com.scs.voxlib.chunk;

import com.scs.voxlib.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoxShapeChunk extends VoxChunk {
	
	public int id;
	public List<Integer> model_ids = new ArrayList<Integer>();

    public VoxShapeChunk(String type) {
        super(type);
    }

    public static VoxShapeChunk read(String type, InputStream stream) throws IOException {
        var chunk = new VoxShapeChunk(type);
        chunk.id = StreamUtils.readIntLE(stream);
        
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

}
