# VOX FILE PARSER

Reads in a .vox file and creates a data structure.  Can handle files created with the latest version of Magicavoxel, i.e. can handle very large multi-model scenes.

Usage:
```Java

		try (VoxReader reader = new VoxReader(new FileInputStream(filename))) {
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

		}

```

## Notes
* Any "rotation" settings are ignored.
* Magicavoxel treats the Z axis as up/down.


## Licence
MIT Licence.  See included LICENCE file.


## Credits
* Programmed by Stephen Carlyle-Smith (stephen.carlylesmith@googlemail.com)

* Code based on JVox https://github.com/Lignum/JVox
