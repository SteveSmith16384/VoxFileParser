package com.scs.voxlib;

import java.util.Arrays;
import java.util.Objects;

public final class VoxModelBlueprint {
	
	public final int id;
    private final GridPoint3 size;
    private final Voxel[] voxels;

    public VoxModelBlueprint(int _id, GridPoint3 _size, Voxel[] _voxels) { 
        if (_size == null || _voxels == null) {
            throw new IllegalArgumentException("Both size and voxels must be non-null");
        }

        id = _id;
        this.size = _size;
        this.voxels = _voxels;
    }

    public GridPoint3 getSize() {
        return size;
    }

    public Voxel[] getVoxels() {
        return voxels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoxModelBlueprint voxModel = (VoxModelBlueprint) o;
        return size.equals(voxModel.size) &&
                Arrays.equals(voxels, voxModel.voxels);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(voxels);
        return result;
    }
}
