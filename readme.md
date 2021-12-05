# VOX FILE PARSER

Reads in a .vox file and creates a data structure.
Can handle files created with the latest version of Magicavoxel,
i.e. can handle very large multi-model scenes.

Also implements minimal functionality of writing out the structure back to a
.vox file.

### Reading usage:
```Java
try (VoxReader reader = new VoxReader(new FileInputStream(filename))) {
    VoxFile voxFile = reader.read();
    
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

### Writing usage:
See [`CreateNewFileTest`](/src/test/java/com/scs/CreateNewFileTest.java)

Expected result:
<p>
    <img src="https://raw.githubusercontent.com/Hunternif/VoxFileParser/master/src/test/resources/test_file_output.png"/>
</p>

## Notes
* Any "rotation" settings are ignored.
* Magicavoxel treats the Z axis as up/down.


## Licence
MIT Licence.  See included LICENCE file.


## Credits
* Programmed by Stephen Carlyle-Smith (stephen.carlylesmith@googlemail.com)
* VoxWriter implemented by Hunternif (https://github.com/Hunternif)
* Code based on JVox https://github.com/Lignum/JVox
