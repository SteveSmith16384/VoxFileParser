package com.scs.voxlib;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class VoxChunk {
	
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
}
