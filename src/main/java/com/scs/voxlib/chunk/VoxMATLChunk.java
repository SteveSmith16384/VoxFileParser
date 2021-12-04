package com.scs.voxlib.chunk;

import com.scs.voxlib.StreamUtils;
import com.scs.voxlib.mat.VoxMaterial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public final class VoxMATLChunk extends VoxChunk {
	
    private final VoxMaterial material;

    public VoxMATLChunk(VoxMaterial material) {
        super(ChunkFactory.MATL);
        this.material = material;
    }

    public static VoxMATLChunk read(InputStream stream) throws IOException {
        int id = StreamUtils.readIntLE(stream);
        HashMap<String, String> props = StreamUtils.readDictionary(stream);
        var material = new VoxMaterial(id, props);
        return new VoxMATLChunk(material);
    }

    public VoxMaterial getMaterial() {
        return material;
    }

    @Override
    protected void writeContent(OutputStream stream) throws IOException {
        StreamUtils.writeIntLE(material.getID(), stream);
        StreamUtils.writeDictionary(material.getProps(), stream);
    }
}
