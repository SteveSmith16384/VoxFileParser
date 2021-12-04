package com.scs.voxlib;

import java.io.IOException;
import java.io.InputStream;

final class VoxSizeChunk extends VoxChunk {
	
    private final GridPoint3 size;

    public VoxSizeChunk(InputStream stream) throws IOException {
        this.size = StreamUtils.readVector3i(stream);
        //System.out.println("Read size of " + size);
    }

    public GridPoint3 getSize() {
        return size;
    }
}
