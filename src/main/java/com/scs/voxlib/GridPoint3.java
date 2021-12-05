package com.scs.voxlib;

import java.util.Objects;

public class GridPoint3 {

	public int x, y, z;
	
	public GridPoint3() {
	}

	public GridPoint3(int _x, int _y, int _z) {
		x = _x;
		y = _y;
		z = _z;
	}
	
	
	public GridPoint3(GridPoint3 point) {
		x = point.x;
		y = point.y;
		z = point.z;
	}

	public void set(int _x, int _y, int _z) {
		x = _x;
		y =_y;
		z = _z;
	}


	public void add(int _x, int _y, int _z) {
		x += _x;
		y +=_y;
		z += _z;
	}


	public void add(GridPoint3 point) {
		x += point.x;
		y += point.y;
		z += point.z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GridPoint3 that = (GridPoint3) o;
		return x == that.x && y == that.y && z == that.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public String toString() {
		return String.format("%d, %d, %d)", x, y, z);
	}

}
