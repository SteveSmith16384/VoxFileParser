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
	public static final String MATT = "MATT"; 
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
			chunk = VoxRootChunk.read(stream, childrenStream);
			break;
		case PACK:
			chunk = VoxPackChunk.read(stream);
			break;
		case SIZE:
			chunk = VoxSizeChunk.read(stream);
			break;
		case XYZI:
			chunk = VoxXYZIChunk.read(stream);
			break;
		case RGBA:
			chunk = VoxRGBAChunk.read(stream);
			break;
		case MATT: // Obsolete
			chunk = VoxMATTChunk.read(stream);
			break;
		case MATL:
			chunk = VoxMATLChunk.read(stream);
			break;
			
		case nSHP: // Shape Node Chunk
			chunk = VoxShapeChunk.read(stream);
			break;
			
		case nTRN: // Transform Node Chunk
			chunk = VoxTransformChunk.read(stream);
			break;

		case nGRP: // Group Node Chunk
			chunk = VoxGroupChunk.read(stream);
			break;

		case LAYR:
			chunk = VoxLayerChunk.read(stream);
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
