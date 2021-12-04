package com.scs.voxlib;

import java.io.IOException;
import java.io.InputStream;

public final class VoxXYZIChunk extends VoxChunk {
	
    private final Voxel[] voxels;

    public VoxXYZIChunk(InputStream stream) throws IOException {
        int voxelCount = StreamUtils.readIntLE(stream);
        //System.out.println(voxelCount + " voxels");
        voxels = new Voxel[voxelCount];

        for (int i = 0; i < voxelCount; i++) {
            voxels[i] = new Voxel(StreamUtils.readVector3b(stream), (byte)stream.read());
        }
    }

    public Voxel[] getVoxels() {
        return voxels;
    }
    
}
