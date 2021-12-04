package com.scs.voxlib;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class VoxChunk {

	private final String type;

	public String getType() {
		return type;
	}

	public VoxChunk(String type) {
		this.type = type;
	}
	
	public static VoxChunk readChunk(InputStream stream) throws IOException {
		return readChunk(stream, null);
	}

	public static VoxChunk readChunk(InputStream stream, String expectedID) throws IOException {
		byte[] chunkID = new byte[4];
		int bytesRead = stream.read(chunkID);
		if (bytesRead != 4) {
			if (bytesRead == -1) {
				// There's no chunk here, this is fine.
				return null;
			}

			throw new InvalidVoxException("Incomplete chunk ID");
		}

		String id = new String(chunkID);

		if (expectedID != null && !expectedID.equals(id)) {
			throw new InvalidVoxException(expectedID + " chunk expected, got " + id);
		}

		int length = StreamUtils.readIntLE(stream);
		int childrenLength = StreamUtils.readIntLE(stream);

		byte[] chunkBytes = new byte[length];
		byte[] childrenChunkBytes = new byte[childrenLength];

		if (stream.read(chunkBytes) != length) {
			throw new InvalidVoxException("Chunk \"" + id + "\" is incomplete");
		}

		stream.read(childrenChunkBytes);

		try (ByteArrayInputStream chunkStream = new ByteArrayInputStream(chunkBytes);
				ByteArrayInputStream childrenStream = new ByteArrayInputStream(childrenChunkBytes)) {
			VoxChunk chunk = ChunkFactory.createChunk(id, chunkStream, childrenStream);
			return chunk;
		}
	}

	public static void writeChunk(OutputStream stream, VoxChunk chunk) throws IOException {
		stream.write(chunk.type.getBytes(StandardCharsets.UTF_8));
		//TODO write the entire chunk
	}
}
