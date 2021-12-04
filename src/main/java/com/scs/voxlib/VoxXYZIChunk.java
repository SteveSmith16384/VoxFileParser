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
            var position = StreamUtils.readVector3b(stream);
            var colorIndex = (byte) ((byte)stream.read() & 0xff);
            voxels[i] = new Voxel(position, colorIndex);
        }
    }

    public Voxel[] getVoxels() {
        return voxels;
    }
    
}
