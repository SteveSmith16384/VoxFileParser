package com.scs.voxlib;

public class VoxModelInstance {

	public final int id;
	private static int next_id = 0;
	
	public final VoxModelBlueprint model;
	public final GridPoint3 world_offset;
	
	public VoxModelInstance(VoxModelBlueprint _model, GridPoint3 _world_offset) {
		id = next_id++;
		model = _model;
		world_offset = _world_offset;
	}
	
	
	@Override
	public String toString() {
		return "ModelInstance#" + id + "_" + this.world_offset;
	}

}
