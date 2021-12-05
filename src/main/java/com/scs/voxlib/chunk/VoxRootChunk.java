package com.scs.voxlib.chunk;

import com.scs.voxlib.*;
import com.scs.voxlib.mat.VoxMaterial;
import com.scs.voxlib.mat.VoxOldMaterial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class VoxRootChunk extends VoxChunk {

	private final HashMap<Integer, VoxModelBlueprint> models = new HashMap<>();
	private final List<VoxModelInstance> model_instances = new ArrayList<>();
	private int[] palette = VoxRGBAChunk.DEFAULT_PALETTE;
	private final HashMap<Integer, VoxMaterial> materials = new HashMap<>();
	private final HashMap<Integer, VoxOldMaterial> oldMaterials = new HashMap<>();
	private final HashMap<Integer, VoxShapeChunk> shapeChunks = new HashMap<Integer, VoxShapeChunk>();
	private final HashMap<Integer, VoxTransformChunk> transformChunks = new HashMap<Integer, VoxTransformChunk>();
	private final HashMap<Integer, VoxGroupChunk> groupChunks = new HashMap<Integer, VoxGroupChunk>();
	private VoxTransformChunk root_transform;
	private GridPoint3 size;
	private final List<VoxChunk> children = new ArrayList<>();

	public VoxRootChunk() {
		super(ChunkFactory.MAIN);
	}


	public static VoxRootChunk read(InputStream stream, InputStream childrenStream) throws IOException {
		var root = new VoxRootChunk();
		VoxChunk first = VoxChunk.readChunk(childrenStream);

		if (first instanceof VoxPackChunk) {
			//VoxPackChunk pack = (VoxPackChunk)first;
			//modelCount = pack.getModelCount(); // Ignore this, it is obsolete
			first = null;
		}

		while (childrenStream.available() > 0) {
			VoxChunk chunk1;

			if (first != null) {
				// If first != null, then that means that the first chunk was not a PACK chunk,
				// and we've already read a SIZE chunk.
				chunk1 = first;
				first = null;
			} else {
				chunk1 = VoxChunk.readChunk(childrenStream);
			}
			root.appendChunk(chunk1);

			if (chunk1 instanceof VoxSizeChunk) {
				VoxChunk chunk2 = VoxChunk.readChunk(childrenStream, ChunkFactory.XYZI);
				root.appendChunk(chunk2);
			}
		}

		// Calc world offset by iterating through the scenegraph
		root.iterateThruScengraph();
		return root;
	}

	public void appendChunk(VoxChunk chunk) {
		children.add(chunk);

		if (chunk instanceof VoxSizeChunk) {
			this.size = ((VoxSizeChunk) chunk).getSize();
		} else if (chunk instanceof VoxXYZIChunk) {
			VoxXYZIChunk xyzi = (VoxXYZIChunk) chunk;
			models.put(
				models.size(),
				new VoxModelBlueprint(models.size(), size, xyzi.getVoxels())
			);
		} else if (chunk instanceof VoxRGBAChunk) {
			VoxRGBAChunk rgba = (VoxRGBAChunk) chunk;
			palette = rgba.getPalette();
		} else {
			processChunk(chunk);
		}
	}

	private void processChunk(VoxChunk chunk) {
		if (chunk instanceof VoxMATLChunk) {
			VoxMaterial mat = ((VoxMATLChunk) chunk).getMaterial();
			materials.put(mat.getID(), mat);
		} else if (chunk instanceof VoxMATTChunk) {
			VoxOldMaterial mat = ((VoxMATTChunk) chunk).getMaterial();
			oldMaterials.put(mat.getID(), mat);
		} else if (chunk instanceof VoxShapeChunk) {
			VoxShapeChunk shapeChunk = (VoxShapeChunk)chunk;
			this.shapeChunks.put(shapeChunk.id, shapeChunk);
		} else if (chunk instanceof VoxTransformChunk) {
			VoxTransformChunk transformChunk = (VoxTransformChunk)chunk;
			if (this.transformChunks.size() == 0) {
				root_transform = transformChunk;
			}
			this.transformChunks.put(transformChunk.id, transformChunk);
		} else if (chunk instanceof VoxGroupChunk) {
			VoxGroupChunk groupChunk = (VoxGroupChunk)chunk;
			this.groupChunks.put(groupChunk.id, groupChunk);
		}
	}

	
	private GridPoint3 findShapeOrGroupParent(int shape_id) {
		GridPoint3 offset = new GridPoint3(0, 0, 0);

		for(VoxTransformChunk transformChunk : this.transformChunks.values()) {
			if (transformChunk.child_node_id == shape_id) {
				offset.add(transformChunk.transform);
				offset.add(this.findTransformParent(transformChunk.id));
				break;
			}
		}

		return offset;		
	}


	private GridPoint3 findTransformParent(int transform_id) {
		GridPoint3 offset = new GridPoint3(0, 0, 0);

		for(VoxGroupChunk groupChunk : this.groupChunks.values()) {
			if (groupChunk.child_ids.contains(transform_id)) {
				GridPoint3 suboffset = this.findShapeOrGroupParent(groupChunk.id);
				offset.add(suboffset);
				break;
			}
		}

		return offset;		
	}



	public List<VoxModelInstance> getModelInstances() {
		return model_instances;
	}


	public int[] getPalette() {
		return palette;
	}


	public HashMap<Integer, VoxMaterial> getMaterials() {
		return materials;
	}


	public HashMap<Integer, VoxOldMaterial> getOldMaterials() {
		return oldMaterials;
	}


	private void iterateThruScengraph() {
		this.findTransformParent(root_transform.id);
		this.processTransformChunk(root_transform, root_transform.transform);
	}


	private void processTransformChunk(VoxTransformChunk transform_chunk, GridPoint3 pos) {
		GridPoint3 new_pos = new GridPoint3(pos);
		if (this.groupChunks.containsKey(transform_chunk.child_node_id)) {
			processGroupChunk(this.groupChunks.get(transform_chunk.child_node_id), new_pos);
		} else if (this.shapeChunks.containsKey(transform_chunk.child_node_id)) {
			processShapeChunk(this.shapeChunks.get(transform_chunk.child_node_id), new_pos);
		}
	}


	private void processGroupChunk(VoxGroupChunk group_chunk, GridPoint3 pos) {
		for (int child_id : group_chunk.child_ids) {
			VoxTransformChunk trn = this.transformChunks.get(child_id);
			GridPoint3 new_pos = new GridPoint3(pos);
			new_pos.add(trn.transform);
			this.processTransformChunk(trn, new_pos);
		}
	}


	private void processShapeChunk(VoxShapeChunk shape_chunk, GridPoint3 pos) {
		for (int model_id : shape_chunk.model_ids) {
			VoxModelBlueprint model = this.models.get(model_id);
			if (model.getVoxels().length > 0) {
				VoxModelInstance instance = new VoxModelInstance(model, new GridPoint3(pos));
				this.model_instances.add(instance);
			}
		}
	}

	@Override
	protected void writeChildren(OutputStream stream) throws IOException {
		for (var chunk : children) {
			if (ChunkFactory.supportedTypes.contains(chunk.getType())) {
				chunk.writeTo(stream);
			}
		}
	}
}
