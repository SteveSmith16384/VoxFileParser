package com.scs;

import com.scs.voxlib.GridPoint3;
import com.scs.voxlib.VoxFile;
import com.scs.voxlib.VoxWriter;
import com.scs.voxlib.Voxel;
import com.scs.voxlib.chunk.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class CreateNewFileTest {
    /**
     * This test creates a minimal .vox file.
     */
    @Test
    public void testCreateNewFile() throws IOException {
        // Set size of the model
        var size = new VoxSizeChunk(new GridPoint3(3, 3, 3));

        // Color indices that we'll use. Must be between 1..255.
        final byte ID_GREEN = 1;
        final byte ID_YELLOW = 2;
        final byte ID_RED = 3;

        // Set actual ARGB values for our color indices.
        var palette = new VoxRGBAChunk();
        palette.getPalette()[ID_GREEN] = 0xFF33CC33;
        palette.getPalette()[ID_YELLOW] = 0xFFCCCC33;
        palette.getPalette()[ID_RED] = 0xFFCC3333;

        // Set voxels using the color indices.
        var model = new VoxXYZIChunk(3);
        model.getVoxels()[0] = new Voxel(new GridPoint3(1, 1, 0), ID_GREEN);
        model.getVoxels()[1] = new Voxel(new GridPoint3(1, 1, 1), ID_YELLOW);
        model.getVoxels()[2] = new Voxel(new GridPoint3(1, 1, 2), ID_RED);


        // The following chunks are the necessary containers for our model:
        var groupTransform = new VoxTransformChunk(0);
        var group = new VoxGroupChunk(1);
        groupTransform.child_node_id = group.id;

        var shapeTransform = new VoxTransformChunk(2);
        var shape = new VoxShapeChunk(3);
        shape.model_ids.add(0); // id of the 1st model
        shapeTransform.child_node_id = shape.id;
        group.child_ids.add(shapeTransform.id);

        // Assemble all our chunks under the root chunk.
        // The order of chunks is important.
        var root = new VoxRootChunk();
        root.appendChunk(size);
        root.appendChunk(model);
        root.appendChunk(groupTransform);
        root.appendChunk(group);
        root.appendChunk(shapeTransform);
        root.appendChunk(shape);
        root.appendChunk(palette);
        var voxFile = new VoxFile(VoxWriter.VERSION, root);

        // Write out the file.
        var path = Paths.get("./out/test_file.vox");
        try (
            var tempFileOut = Files.newOutputStream(path, CREATE, TRUNCATE_EXISTING);
            var writer = new VoxWriter(tempFileOut)
        ) {
            writer.write(voxFile);
        }
        // Magica Voxel should be able to open 'test_file.vox'
    }
}
