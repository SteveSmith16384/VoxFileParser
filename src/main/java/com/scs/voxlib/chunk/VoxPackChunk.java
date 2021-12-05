package com.scs.voxlib.chunk;

import com.scs.voxlib.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class VoxPackChunk extends VoxChunk {
    private final int modelCount;

    public VoxPackChunk(int modelCount) {
        super(ChunkFactory.PACK);
        this.modelCount = modelCount;
    }

    public static VoxPackChunk read(InputStream stream) throws IOException {
        var modelCount = StreamUtils.readIntLE(stream);
        return new VoxPackChunk(modelCount);
    }

    int getModelCount() {
        return modelCount;
    }

    @Override
    protected void writeContent(OutputStream stream) throws IOException {
        StreamUtils.writeIntLE(modelCount, stream);
    }
}
