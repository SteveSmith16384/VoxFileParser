package com.scs.voxlib.chunk;

import com.scs.voxlib.*;
import com.scs.voxlib.mat.VoxOldMaterial;
import com.scs.voxlib.mat.VoxOldMaterialProperty;
import com.scs.voxlib.mat.VoxOldMaterialType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

final class VoxMATTChunk extends VoxChunk {
	
    private final VoxOldMaterial material;

    public VoxMATTChunk(String type, VoxOldMaterial material) {
        super(type);
        this.material = material;
    }

    public static VoxMATTChunk read(String type, InputStream stream) throws IOException {
        int id = StreamUtils.readIntLE(stream);
        int typeIndex = StreamUtils.readIntLE(stream);
        VoxOldMaterialType matType = VoxOldMaterialType.fromIndex(typeIndex)
            .orElseThrow(() -> new InvalidVoxException("Unknown material type " + typeIndex));
        float weight = StreamUtils.readFloatLE(stream);
        int propBits = StreamUtils.readIntLE(stream);
        boolean isTotalPower = VoxOldMaterialProperty.IS_TOTAL_POWER.isSet(propBits);

        int propCount = Integer.bitCount(propBits);

        if (isTotalPower) {
            propCount--; // IS_TOTAL_POWER has no value
        }

        HashMap<VoxOldMaterialProperty, Float> properties = new HashMap<>(propCount);

        for (VoxOldMaterialProperty prop : VoxOldMaterialProperty.values()) {
            if (prop != VoxOldMaterialProperty.IS_TOTAL_POWER && prop.isSet(propBits)) {
                properties.put(prop, StreamUtils.readFloatLE(stream));
            }
        }

        try {
            var material = new VoxOldMaterial(id, weight, matType, properties, isTotalPower);
            return new VoxMATTChunk(type, material);
        } catch (IllegalArgumentException e) {
            throw new InvalidVoxException("Material with ID " + id + " is invalid", e);
        }
    }

    public VoxOldMaterial getMaterial() {
        return material;
    }

    //TODO: write old material, if necessary
}
