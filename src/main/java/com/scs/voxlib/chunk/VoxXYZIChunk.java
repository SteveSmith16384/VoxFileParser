package com.scs.voxlib.chunk;

import com.scs.voxlib.StreamUtils;
import com.scs.voxlib.Voxel;

import java.io.IOException;
import java.io.InputStream;

public final class VoxXYZIChunk extends VoxChunk {
	
    private final Voxel[] voxels;

    public VoxXYZIChunk(String type, int voxelCount) {
        super(type);
        voxels = new Voxel[voxelCount];
    }

    public static VoxXYZIChunk read(String type, InputStream stream) throws IOException {
        int voxelCount = StreamUtils.readIntLE(stream);
        var chunk = new VoxXYZIChunk(type, voxelCount);
        //System.out.println(voxelCount + " voxels");

        for (int i = 0; i < voxelCount; i++) {
            var position = StreamUtils.readVector3b(stream);
            var colorIndex = (byte) ((byte)stream.read() & 0xff);
            chunk.voxels[i] = new Voxel(position, colorIndex);
        }
        return chunk;
    }

    public Voxel[] getVoxels() {
        return voxels;
    }
    
}
