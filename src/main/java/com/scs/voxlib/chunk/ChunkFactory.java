package com.scs.voxlib.chunk;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public final class ChunkFactory {

	public static final String MAIN = "MAIN"; 
	public static final String PACK = "PACK"; 
	public static final String SIZE = "SIZE"; 
	public static final String XYZI = "XYZI"; 
	public static final String RGBA = "RGBA"; 
	public static final String MATL = "MATL"; 
	public static final String nSHP = "nSHP"; 
	public static final String nTRN = "nTRN"; 
	public static final String nGRP = "nGRP"; 
	public static final String LAYR = "LAYR"; 
	
	public static Set<String> supportedTypes = new HashSet<>();
	static {
		supportedTypes.add(MAIN);
		supportedTypes.add(PACK);
		supportedTypes.add(SIZE);
		supportedTypes.add(XYZI);
		supportedTypes.add(RGBA);
		supportedTypes.add(MATL);
		supportedTypes.add(nSHP);
		supportedTypes.add(nTRN);
		supportedTypes.add(nGRP);
		supportedTypes.add(LAYR);
	}

	public static VoxChunk createChunk(String type, InputStream stream, InputStream childrenStream) throws IOException {
		VoxChunk chunk = null;

		//Settings.p("Reading type " + type);
		
		switch (type) {
		case MAIN:
			chunk = VoxRootChunk.read(type, stream, childrenStream);
			break;
		case PACK:
			chunk = VoxPackChunk.read(type, stream);
			break;
		case SIZE:
			chunk = VoxSizeChunk.read(type, stream);
			break;
		case XYZI:
			chunk = VoxXYZIChunk.read(type, stream);
			break;
		case RGBA:
			chunk = VoxRGBAChunk.read(type, stream);
			break;
		case "MATT": // Obsolete
			chunk = VoxMATTChunk.read(type, stream);
			break;
		case MATL:
			chunk = VoxMATLChunk.read(type, stream);
			break;
			
		case nSHP: // Shape Node Chunk
			chunk = VoxShapeChunk.read(type, stream);
			break;
			
		case nTRN: // Transform Node Chunk
			chunk = VoxTransformChunk.read(type, stream);
			break;

		case nGRP: // Group Node Chunk
			chunk = VoxGroupChunk.read(type, stream);
			break;

		case LAYR:
			chunk = VoxLayerChunk.read(type, stream);
			break;

			// These chunks are unsupported and simply skipped.
		case "rOBJ":
		case "rCAM":
		case "NOTE":
			chunk = new VoxDummyChunk(type);
			break;
			
		default:
			System.out.println("Ignoring " + type);
		}

		return chunk;
	}
}
