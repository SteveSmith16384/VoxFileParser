package com.scs;

import com.scs.voxlib.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class ParseTestModels {
    private VoxFile loadVox(String resourcePath) throws IOException {
        var fullPath = getClass().getResource(resourcePath).getPath();
        try (VoxReader reader = new VoxReader(new FileInputStream(fullPath))) {
            return reader.read();
        }
    }

    private void testModel(String path, int fileVersion, int modelCount, int voxelCount, int materialCount) throws IOException {
        var file = loadVox(path);
        assertEquals(fileVersion, file.getVersion());

        var models = file.getModelInstances();
        assertNotNull(models);
        assertNotNull(models.get(0));
        assertEquals(modelCount, models.size());

        int voxelSum = 0;
        for (var modelInstance : models) {
            voxelSum += modelInstance.model.getVoxels().length;
        }
        assertEquals(voxelCount, voxelSum);

        // First colour is always black
        int[] palette = file.getPalette();
        assertEquals(0x00000000, palette[0]);

        for (int i = 1; i < palette.length; i++) {
            assertNotEquals(0x00000000, palette[i]);
        }

        assertEquals(materialCount, file.getMaterials().size());
    }

    @Test
    public void testChrKnight() throws IOException {
        testModel("/chr_knight.vox", 150, 1, 398, 256);
    }

    @Test
    public void testTeapot() throws IOException {
        testModel("/teapot.vox", 150, 1, 28411, 256);
    }

    @Test
    public void testMonu2() throws IOException {
        testModel("/monu2.vox", 150, 1, 150764, 256);
    }
}
