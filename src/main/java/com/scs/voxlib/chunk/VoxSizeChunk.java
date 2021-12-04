package com.scs.voxlib.chunk;

import com.scs.voxlib.GridPoint3;
import com.scs.voxlib.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

final class VoxSizeChunk extends VoxChunk {
	
    private final GridPoint3 size;

    public VoxSizeChunk(String type, GridPoint3 size) {
        super(type);
        this.size = size;
    }

    public static VoxSizeChunk read(String type, InputStream stream) throws IOException {
        var size = StreamUtils.readVector3i(stream);
        //System.out.println("Read size of " + size);
        return new VoxSizeChunk(type, size);
    }

    public GridPoint3 getSize() {
        return size;
    }
}