package com.scs.voxlib.chunk;

import com.scs.voxlib.GridPoint3;
import com.scs.voxlib.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class VoxSizeChunk extends VoxChunk {
	
    private final GridPoint3 size;

    public VoxSizeChunk(GridPoint3 size) {
        super(ChunkFactory.SIZE);
        this.size = size;
    }

    public VoxSizeChunk(int width, int length, int height) {
        super(ChunkFactory.SIZE);
        this.size = new GridPoint3(width, length, height);
    }

    public static VoxSizeChunk read(InputStream stream) throws IOException {
        var size = StreamUtils.readVector3i(stream);
        //System.out.println("Read size of " + size);
        return new VoxSizeChunk(size);
    }

    public GridPoint3 getSize() {
        return size;
    }

    @Override
    protected void writeContent(OutputStream stream) throws IOException {
        StreamUtils.writeVector3i(size, stream);
    }
}
