package com.scs;

import com.scs.voxlib.VoxFile;
import com.scs.voxlib.VoxReader;
import com.scs.voxlib.VoxWriter;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ReadNWriteTestModels {

    private VoxFile loadVox(String resourcePath) throws IOException {
        var fullPath = getClass().getResource(resourcePath).getPath();
        try (var reader = new VoxReader(new FileInputStream(fullPath))) {
            return reader.read();
        }
    }

    private void testModel(String path, int fileVersion, int modelCount, int voxelCount, int materialCount) throws IOException {
        var file = loadVox(path);

        var tempFilePath = Paths.get("./out" + path);
        try (
            var tempFileOut = Files.newOutputStream(tempFilePath, CREATE, TRUNCATE_EXISTING);
            var writer = new VoxWriter(tempFileOut)
        ) {
            writer.write(file);
        }

        //Reload and test again
        try (var reader = new VoxReader(Files.newInputStream(tempFilePath))) {
            file = reader.read();
        }


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
