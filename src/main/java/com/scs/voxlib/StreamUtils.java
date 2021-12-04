package com.scs.voxlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class StreamUtils {

	public static int readIntLE(InputStream stream) throws IOException {
        byte[] bytes = new byte[4];
        if (stream.read(bytes) != 4) {
            throw new IOException("Not enough bytes to read an int");
        }

        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

	public static float readFloatLE(InputStream stream) throws IOException {
        byte[] bytes = new byte[4];
        if (stream.read(bytes) != 4) {
            throw new IOException("Not enough bytes to read a float");
        }

        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

	public static GridPoint3 readVector3i(InputStream stream) throws IOException {
        return new GridPoint3(readIntLE(stream), readIntLE(stream), readIntLE(stream));
    }

	public static GridPoint3 readVector3b(InputStream stream) throws IOException {
        int x = stream.read();
        int y = stream.read();
        int z = stream.read();

        if (x == -1 || y == -1 || z == -1) {
            throw new IOException("Not enough bytes to read a vector3b");
        }

        return new GridPoint3((byte)x, (byte)y, (byte)z);
    }

	public static String readString(InputStream stream) throws IOException {
        int n = readIntLE(stream);
        if (n < 0) {
            throw new IOException("String is too large to read");
        }

        byte[] bytes = new byte[n];
        if (stream.read(bytes) != n) {
            throw new IOException("Not enough bytes to read a string of size " + n);
        }

        return new String(bytes);
    }

	public static HashMap<String, String> readDictionary(InputStream stream) throws IOException {
        int n = readIntLE(stream);
        if (n < 0) {
            throw new InvalidVoxException("Dictionary too large");
        }

        HashMap<String, String> dict = new HashMap<>(n);

        for (int i = 0; i < n; i++) {
            String key = readString(stream);
            String value = readString(stream);
            dict.put(key, value);
        }

        return dict;
    }


    // ======================= WRITE METHODS =======================

    public static void writeIntLE(int v, OutputStream stream) throws IOException {
        stream.write(
            ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(v).array()
        );
    }
    
    public static void writeVector3i(GridPoint3 v, OutputStream stream) throws IOException {
        writeIntLE(v.x, stream);
        writeIntLE(v.y, stream);
        writeIntLE(v.z, stream);
    }

    public static void writeVector3b(GridPoint3 v, OutputStream stream) throws IOException {
        stream.write((byte)v.x);
        stream.write((byte)v.y);
        stream.write((byte)v.z);
    }

    public static void writeString(String s, OutputStream stream) throws IOException {
        writeIntLE(s.length(), stream);
        stream.write(s.getBytes(StandardCharsets.UTF_8));
    }

    public static void writeDictionary(Map<String, String> dict, OutputStream stream) throws IOException {
        writeIntLE(dict.size(), stream);

        for (var entry : dict.entrySet()) {
            writeString(entry.getKey(), stream);
            writeString(entry.getValue(), stream);
        }
    }
}
