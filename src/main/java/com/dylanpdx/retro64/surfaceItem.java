package com.dylanpdx.retro64;

import net.minecraft.util.math.Vec3d;

import java.util.List;

public class surfaceItem {

    Vec3d[] verts;
    byte material;
    short terrain;
    boolean isCube;
    boolean isFlat;

    public surfaceItem(Vec3d[] verts, byte material, short terrain) {
        this.verts = verts;
        this.material = material;
        this.terrain = terrain;
    }

    public surfaceItem(List<Vec3d> verts, byte material, short terrain) {
        this.verts = new Vec3d[verts.size()];
        for (int i = 0; i < verts.size(); i++) {
            this.verts[i] = verts.get(i);
        }
        this.material = material;
        this.terrain = terrain;
    }

    public surfaceItem(Vec3d pos, boolean isCube, boolean isFlat, byte material, short terrain) {
        verts = new Vec3d[]{pos};
        this.isCube = isCube;
        this.isFlat = isFlat;
        this.material = material;
        this.terrain = terrain;
    }

    public boolean isCube() {
        return isCube;
    }

    public boolean isFlat() {
        return isFlat;
    }

}
