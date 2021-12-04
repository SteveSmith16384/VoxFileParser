package com.scs.voxlib.chunk;

import com.scs.voxlib.StreamUtils;
import com.scs.voxlib.mat.VoxMaterial;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

final class VoxMATLChunk extends VoxChunk {
	
    private final VoxMaterial material;

    public VoxMATLChunk(String type, VoxMaterial material) {
        super(type);
        this.material = material;
    }

    public static VoxMATLChunk read(String type, InputStream stream) throws IOException {
        int id = StreamUtils.readIntLE(stream);
        HashMap<String, String> props = StreamUtils.readDictionary(stream);
        var material = new VoxMaterial(id, props);
        return new VoxMATLChunk(type, material);
    }

    public VoxMaterial getMaterial() {
        return material;
    }
}