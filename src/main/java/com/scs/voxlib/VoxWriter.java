package com.scs.voxlib;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class VoxWriter implements Closeable {
    private final OutputStream stream;

    public VoxWriter(OutputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("stream must not be null");
        }

        this.stream = stream;
    }

    public void write(VoxFile file) throws IOException {
        stream.write(VoxReader.MAGIC_BYTES);
        stream.write(file.getVersion());
        VoxChunk.writeChunk(stream, file.getRoot());
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
