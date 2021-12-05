package com.scs.voxlib;

public class VoxModelInstance {

	public final int id;
	private static int next_id = 0;
	
	public final VoxModelBlueprint model;
	public final GridPoint3 worldOffset;
	
	public VoxModelInstance(VoxModelBlueprint _model, GridPoint3 _world_offset) {
		id = next_id++;
		model = _model;
		worldOffset = _world_offset;
	}
	
	
	@Override
	public String toString() {
		return "ModelInstance#" + id + "_" + this.worldOffset;
	}

}
