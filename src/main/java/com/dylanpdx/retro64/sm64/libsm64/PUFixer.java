package com.dylanpdx.retro64.sm64.libsm64;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class PUFixer {

    /**
     * Takes Coordinate in MC and converts it to SM64 using the scale factor
     * @param pos Coordinate in MC
     * @return Coordinate in SM64
     */
    public static Vec3f convertToSM64(Vec3f pos){
        pos.scale(LibSM64.SCALE_FACTOR);

        return pos;
    }

    /**
     * Takes Coordinate in MC and converts it to SM64 using the scale factor
     * @param pos Coordinate in MC
     * @return Coordinate in SM64
     */
    public static Vec3d convertToSM64(Vec3d pos){
        return pos.multiply(LibSM64.SCALE_FACTOR);
    }

    /**
     * Takes Coordinate in SM64 and converts it to MC using the scale factor
     * @param pos Coordinate in SM64
     * @return Coordinate in MC
     */
    public static Vec3f convertToMC(Vec3f pos)
    {
        pos.scale(1/LibSM64.SCALE_FACTOR);
        return pos;
    }
}
