package com.scs.voxlib;

import java.io.IOException;
import java.io.InputStream;

final class VoxPackChunk extends VoxChunk {
    private final int modelCount;

    public VoxPackChunk(String type, int modelCount) {
        super(type);
        this.modelCount = modelCount;
    }

    public static VoxPackChunk read(String type, InputStream stream) throws IOException {
        var modelCount = StreamUtils.readIntLE(stream);
        return new VoxPackChunk(type, modelCount);
    }

    int getModelCount() {
        return modelCount;
    }
}
