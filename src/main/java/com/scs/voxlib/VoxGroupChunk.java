package com.scs.voxlib;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoxGroupChunk extends VoxChunk {
	
	public int id;
	public List<Integer> child_ids = new ArrayList<Integer>();

    public VoxGroupChunk(InputStream stream) throws IOException {
        id = StreamUtils.readIntLE(stream);
        HashMap<String, String> dict = StreamUtils.readDictionary(stream);
        /*if (dict.size() > 0) {
    		Settings.p("dict=" + dict);
        }*/
        int num_children = StreamUtils.readIntLE(stream);

        for (int i=0 ; i<num_children ; i++) {
            int child_id = StreamUtils.readIntLE(stream);
            child_ids.add(child_id);
        }
    }


}
