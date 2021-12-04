package com.scs.voxlib;

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
	public String toString() {
		return String.format("%d, %d, %d)", x, y, z);
	}

}
