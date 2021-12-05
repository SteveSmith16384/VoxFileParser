package com.scs.voxlib;

import java.util.Objects;

public final class Voxel {
	
    private final GridPoint3 position;
    private final byte colourIndex;

    public Voxel(GridPoint3 position, byte colourIndex) {
        this.position = position;
        this.colourIndex = colourIndex;
    }

    public Voxel(int x, int y, int z, byte colourIndex) {
        this(new GridPoint3(x, y, z), colourIndex);
    }

    public GridPoint3 getPosition() {
        return position;
    }

    public int getColourIndex() {
        return colourIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voxel voxel = (Voxel) o;
        return colourIndex == voxel.colourIndex &&
                Objects.equals(position, voxel.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, colourIndex);
    }

    @Override
    public String toString() {
        return "(" + position.toString() + ", " + Byte.toUnsignedInt(colourIndex) + ")";
    }
}
