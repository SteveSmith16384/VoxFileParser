package com.scs.voxlib;

import java.io.IOException;
import java.io.InputStream;

final class ChunkFactory {

	static VoxChunk createChunk(String id, InputStream stream, InputStream childrenStream) throws IOException {
		VoxChunk chunk = null;

		//Settings.p("Reading type " + id);
		
		switch (id) {
		case "MAIN":
			chunk = new VoxRootChunk(stream, childrenStream);
			break;
		case "PACK":
			chunk = new VoxPackChunk(stream);
			break;
		case "SIZE":
			chunk = new VoxSizeChunk(stream);
			break;
		case "XYZI":
			chunk = new VoxXYZIChunk(stream);
			break;
		case "RGBA":
			chunk = new VoxRGBAChunk(stream);
			break;
		case "MATT": // Obsolete
			chunk = new VoxMATTChunk(stream);
			break;
		case "MATL":
			chunk = new VoxMATLChunk(stream);
			break;
			
		case "nSHP": // Shape Node Chunk
			chunk = new VoxShapeChunk(stream);
			break;
			
		case "nTRN": // Transform Node Chunk
			chunk = new VoxTransformChunk(stream);
			break;

		case "nGRP": // Group Node Chunk
			chunk = new VoxGroupChunk(stream);
			break;

		case "LAYR":
			chunk = new VoxLayerChunk(stream);
			break;

			// These chunks are unsupported and simply skipped.
		case "rOBJ":
		case "rCAM":
		case "NOTE":
			chunk = new VoxDummyChunk();
			break;
			
		default:
			System.out.println("Ignoring " + id);
		}

		return chunk;
	}
}
