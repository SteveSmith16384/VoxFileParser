package com.scs.voxlib;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

final class VoxMATLChunk extends VoxChunk {
	
    private final VoxMaterial material;

    public VoxMATLChunk(InputStream stream) throws IOException {
        int id = StreamUtils.readIntLE(stream);
        HashMap<String, String> props = StreamUtils.readDictionary(stream);
        material = new VoxMaterial(id, props);
    }

    public VoxMaterial getMaterial() {
        return material;
    }
}
