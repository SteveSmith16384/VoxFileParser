package com.scs.voxlib;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class VoxReader implements Closeable {
	
    private static final byte[] MAGIC_BYTES = new byte[] {
        (byte)'V', (byte)'O', (byte)'X', (byte)' '
    };

    private final InputStream stream;

    public VoxReader(InputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("stream must not be null");
        }

        this.stream = stream;
    }

    public VoxRootChunk read() throws IOException {
        byte[] magicBytes = new byte[4];
        if (stream.read(magicBytes) != 4) {
            throw new InvalidVoxException("Could not read magic bytes");
        }

        if (!Arrays.equals(magicBytes, MAGIC_BYTES)) {
            throw new InvalidVoxException("Invalid magic bytes");
        }

        int fileVersion = StreamUtils.readIntLE(stream);

        if (fileVersion < 150) {
            throw new InvalidVoxException("Vox versions older than 150 are not supported");
        }

        VoxChunk chunk = VoxChunk.readChunk(stream);
        
        if (chunk == null) {
            throw new InvalidVoxException("No root chunk present in the file");
        }

        if (!(chunk instanceof VoxRootChunk)) {
            throw new InvalidVoxException("First chunk is not of ID \"MAIN\"");
        }

        return (VoxRootChunk)chunk;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
