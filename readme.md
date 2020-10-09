# VOX FILE PARSER

Reads in a .vox file and creates a data structure.  Can handle files created with the latest version of Magicavoxel.

Usage:
```Java

		try (VoxReader reader = new VoxReader(new FileInputStream(filename))) {
			VoxRootChunk voxFile = reader.read();
			
			for (VoxModelInstance model_instance : voxFile.getModelInstances()) {
				VoxModelBlueprint model = model_instance.model;
					for (Voxel voxel : model.getVoxels()) {
						int x = voxel.getPosition().x;
						int y = voxel.getPosition().y;
						int z = voxel.getPosition().z;
					}
			}

		}

```

## Notes
* Any "rotation" settings are ignored.
* The following chunk types are ignored: rOBJ, rCAM, NOTE
* Magicavoxel treats the Z axis as up/down.


## Licence
MIT Licence.  See included files.


## Credits
* Programmed by Stephen Carlyle-Smith (stephen.carlylesmith@googlemail.com)

* Code based on JVox https://github.com/Lignum/JVox
