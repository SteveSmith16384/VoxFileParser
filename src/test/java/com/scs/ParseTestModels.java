package com.scs;

import com.scs.voxlib.VoxFile;
import com.scs.voxlib.VoxModel;
import com.scs.voxlib.VoxReader;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ParseTestModels {
    private VoxFile loadVox(String path) throws IOException {
        try (InputStream stream = getClass().getResourceAsStream(path);
             VoxReader reader = new VoxReader(stream)) {
            return reader.read();
        }
    }

    private void testModel(String path, int fileVersion, int modelCount, int voxelCount, int materialCount) throws IOException {
        VoxFile file = loadVox(path);
        assertEquals(fileVersion, file.getVersion());

        VoxModel[] models = file.getModels();
        assertNotNull(models);
        assertNotNull(models[0]);
        assertEquals(modelCount, models.length);

        int voxelSum = 0;

        for (VoxModel model : models) {
            voxelSum += model.getVoxels().length;
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
    public void testMonu10() throws IOException {
        testModel("/monu10.vox", 150, 1, 150764, 256);
    }
}
