package com.scs.voxlib.chunk;

import com.scs.voxlib.InvalidVoxException;
import com.scs.voxlib.StreamUtils;

import java.io.*;
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

		if (length > 0 && stream.read(chunkBytes) != length) {
			throw new InvalidVoxException("Chunk \"" + id + "\" is incomplete");
		}

		stream.read(childrenChunkBytes);

		try (ByteArrayInputStream chunkStream = new ByteArrayInputStream(chunkBytes);
				ByteArrayInputStream childrenStream = new ByteArrayInputStream(childrenChunkBytes)) {
			VoxChunk chunk = ChunkFactory.createChunk(id, chunkStream, childrenStream);
			return chunk;
		}
	}

	public final void writeTo(OutputStream stream) throws IOException {
		try (
			var contentStream = new ByteArrayOutputStream();
			var childStream = new ByteArrayOutputStream();
		) {
			stream.write(type.getBytes(StandardCharsets.UTF_8));
			writeContent(contentStream);
			var contentBytes = contentStream.toByteArray();

			writeChildren(childStream);
			var childBytes = childStream.toByteArray();

			StreamUtils.writeIntLE(contentBytes.length, stream);
			StreamUtils.writeIntLE(childBytes.length, stream);
			stream.write(contentBytes);
			stream.write(childBytes);
		}
	}

	/** Write to the stream the content directly associated with this chunk. */
	protected void writeContent(OutputStream stream) throws IOException {}

	/** Write to the stream the content associated with this chunk's children. */
	protected void writeChildren(OutputStream stream) throws IOException {}
}
