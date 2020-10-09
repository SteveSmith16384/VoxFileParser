package com.scs.voxlib;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class VoxRootChunk extends VoxChunk {

	private final HashMap<Integer, VoxModelBlueprint> models;
	private final List<VoxModelInstance> model_instances;
	private final int[] palette;
	private final HashMap<Integer, VoxMaterial> materials = new HashMap<>();
	private final HashMap<Integer, VoxOldMaterial> oldMaterials = new HashMap<>();
	private final HashMap<Integer, VoxShapeChunk> shapeChunks = new HashMap<Integer, VoxShapeChunk>();
	private final HashMap<Integer, VoxTransformChunk> transformChunks = new HashMap<Integer, VoxTransformChunk>();
	private final HashMap<Integer, VoxGroupChunk> groupChunks = new HashMap<Integer, VoxGroupChunk>();
	private VoxTransformChunk root_transform;

	public VoxRootChunk(InputStream stream, InputStream childrenStream) throws IOException {		
		VoxChunk first = VoxChunk.readChunk(childrenStream);

		if (first instanceof VoxPackChunk) {
			//VoxPackChunk pack = (VoxPackChunk)first;
			//modelCount = pack.getModelCount(); // Ignore this, it is obsolete
			first = null;
		}

		models = new HashMap<Integer, VoxModelBlueprint>();
		model_instances = new ArrayList<VoxModelInstance>();
		int[] pal = VoxRGBAChunk.DEFAULT_PALETTE;

		while (childrenStream.available() > 0) {
			VoxChunk chunk1;

			if (first != null) {
				// If first != null, then that means that the first chunk was not a PACK chunk,
				// and we've already read a SIZE chunk.
				chunk1 = first;
				first = null;
			} else {
				chunk1 = VoxChunk.readChunk(childrenStream);//, "SIZE");
			}

			if (chunk1 instanceof VoxSizeChunk) {
				VoxSizeChunk size = (VoxSizeChunk) chunk1;

				VoxChunk chunk2 = VoxChunk.readChunk(childrenStream, "XYZI");
				VoxXYZIChunk xyzi = (VoxXYZIChunk)chunk2;

				//if (xyzi.getVoxels().length > 0) { No!  As it throws out all the model IDs
				models.put(models.size(), new VoxModelBlueprint(models.size(), size.getSize(), xyzi.getVoxels()));
				//}
			} else if (chunk1 instanceof VoxRGBAChunk) {
				VoxRGBAChunk rgba = (VoxRGBAChunk)chunk1;
				pal = rgba.getPalette();
			} else {
				processChunk(chunk1);
			}
		}

		palette = pal;

		// Calc world offset by iterating through the scenegraph
		iterateThruScengraph();
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


	HashMap<Integer, VoxMaterial> getMaterials() {
		return materials;
	}


	HashMap<Integer, VoxOldMaterial> getOldMaterials() {
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


}
