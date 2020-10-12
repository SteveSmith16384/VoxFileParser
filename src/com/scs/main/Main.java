package com.scs.main;

import java.io.FileInputStream;

import com.scs.voxlib.GridPoint3;
import com.scs.voxlib.VoxModelBlueprint;
import com.scs.voxlib.VoxModelInstance;
import com.scs.voxlib.VoxReader;
import com.scs.voxlib.VoxRootChunk;
import com.scs.voxlib.Voxel;

public class Main {

	public static void main(String args[]) {
		try (VoxReader reader = new VoxReader(new FileInputStream("E:\\mycode\\VoxWorldExplorer\\test_release\\vox\\cyber_city\\cyber_city.vox"))) {
			VoxRootChunk voxFile = reader.read();

			for (VoxModelInstance model_instance : voxFile.getModelInstances()) {
				GridPoint3 world_Offset = model_instance.world_offset;
				VoxModelBlueprint model = model_instance.model;
				for (Voxel voxel : model.getVoxels()) {
					int x = world_Offset.x + voxel.getPosition().x;
					int y = world_Offset.y + voxel.getPosition().y;
					int z = world_Offset.z + voxel.getPosition().z;
					
					// Do stuff with the data 
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}


