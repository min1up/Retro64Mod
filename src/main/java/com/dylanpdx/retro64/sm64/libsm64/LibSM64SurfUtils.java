package com.dylanpdx.retro64.sm64.libsm64;

import com.dylanpdx.retro64.sm64.SM64SurfaceType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class LibSM64SurfUtils {
    public static SM64Surface generateTri(Vec3f point1, Vec3f point2, Vec3f point3, Vec3f translation, short surface, short terrain)
    {
        //translation = translation + new Vec3f(0, -1, 0);
        Vec3f cp1 = point1.copy();
        Vec3f cp2 = point2.copy();
        Vec3f cp3 = point3.copy();
        cp1.add(translation);
        cp2.add(translation);
        cp3.add(translation);
        cp1 = PUFixer.convertToSM64(cp1);
        cp2 = PUFixer.convertToSM64(cp2);
        cp3 = PUFixer.convertToSM64(cp3);

        SM64Surface surf = new SM64Surface();
        surf.force=0;
        surf.type = surface;
        surf.terrain = terrain;
        // tri 1
        surf.vertices[0] = (int)cp1.getX();
        surf.vertices[1] = (int)cp1.getY();
        surf.vertices[2] = (int)cp1.getZ();
        // tri 2
        surf.vertices[3] = (int)cp2.getX();
        surf.vertices[4] = (int)cp2.getY();
        surf.vertices[5] = (int)cp2.getZ();
        // tri 3
        surf.vertices[6] = (int)cp3.getX();
        surf.vertices[7] = (int)cp3.getY();
        surf.vertices[8] = (int)cp3.getZ();
        return surf;
    }

    public static SM64Surface[] generateQuad(Vec3f a, Vec3f b, Vec3f c, Vec3f d, Vec3f translation, short surface, short terrain)
    {
        return new SM64Surface[]
                {
                        generateTri(a,b,d, translation,surface,terrain),
                        generateTri(d, b, c, translation,surface,terrain)
                };
    }

    public static SM64Surface[] generateQuadF(Vec3f a, Vec3f b, Vec3f c, Vec3f d, Vec3f translation, short surface, short terrain)
    {
        return new SM64Surface[]
                {
                        generateTri(d, b, a, translation,surface,terrain),
                        generateTri(d, c, b, translation,surface,terrain)
                };
    }

    public static SM64Surface[] block(int x,int y,int z){
        return block(x,y,z,1.1f,-0.1f,(short) SM64SurfaceType.Default,(short)0);
    }

    public static Vec3d[] surfToVec(SM64Surface surf){
        Vec3d[] ret = new Vec3d[3];
        ret[0] = new Vec3d(surf.vertices[0],surf.vertices[1],surf.vertices[2]);
        ret[1] = new Vec3d(surf.vertices[3],surf.vertices[4],surf.vertices[5]);
        ret[2] = new Vec3d(surf.vertices[6],surf.vertices[7],surf.vertices[8]);
        return ret;
    }

    public static Vec3d[] surfsToVec(SM64Surface[] surfs){
        Vec3d[] ret = new Vec3d[surfs.length*3];
        for(int i=0;i<surfs.length;i++){
            Vec3d[] temp = surfToVec(surfs[i]);
            ret[i*3] = temp[0];
            ret[i*3+1] = temp[1];
            ret[i*3+2] = temp[2];
        }
        return ret;
    }

    public static SM64Surface[] block(int x, int y, int z, float blockTop, float blockBottom, short surface, short terrain)
    {
        var topQuad = generateQuadF(new Vec3f(0, blockTop, 0), new Vec3f(1, blockTop, 0), new Vec3f(1, blockTop, 1), new Vec3f(0, blockTop, 1), new Vec3f(z, y, x),surface,terrain);
        var bottomQuad = generateQuad(new Vec3f(0, blockBottom, 0), new Vec3f(1, blockBottom, 0), new Vec3f(1, blockBottom, 1), new Vec3f(0, blockBottom, 1), new Vec3f(z, y, x), surface, terrain);

        var sideAQuad = generateQuadF(new Vec3f(0, blockBottom, 0), new Vec3f(1, blockBottom, 0), new Vec3f(1, blockTop, 0), new Vec3f(0, blockTop, 0), new Vec3f(z, y, x), surface, terrain);
        var sideBQuad = generateQuadF(new Vec3f(0, blockBottom, 1), new Vec3f(0, blockBottom, 0), new Vec3f(0, blockTop, 0), new Vec3f(0, blockTop, 1), new Vec3f(z, y, x), surface, terrain);

        var sideCQuad = generateQuadF(new Vec3f(1, blockBottom, 1), new Vec3f(0, blockBottom, 1), new Vec3f(0, blockTop, 1), new Vec3f(1, blockTop, 1), new Vec3f(z, y, x), surface, terrain);
        var sideDQuad = generateQuadF(new Vec3f(1, blockBottom, 0), new Vec3f(1, blockBottom, 1), new Vec3f(1, blockTop, 1), new Vec3f(1, blockTop, 0), new Vec3f(z, y, x), surface, terrain);
        SM64Surface[] surfs = new SM64Surface[]
                {
                        // top
                        topQuad[0],
                        topQuad[1],

                        // bottom
                        bottomQuad[0],
                        bottomQuad[1],
                        // side A
                        sideAQuad[0],
                        sideAQuad[1],
                        // side B
                        sideBQuad[0],
                        sideBQuad[1],
                        // Side C
                        sideCQuad[0],
                        sideCQuad[1],
                        // Side D
                        sideDQuad[0],
                        sideDQuad[1],
                };

        return surfs;
    }

    public static SM64Surface[] plane(int x, int y, int z, float blockBottom, short surface, short terrain)
    {
        var bottomQuad = generateQuadF(new Vec3f(0, blockBottom, 0), new Vec3f(1, blockBottom, 0), new Vec3f(1, blockBottom, 1), new Vec3f(0, blockBottom, 1), new Vec3f(z, y, x), surface, terrain);


        return new SM64Surface[]
                {
                        // bottom
                        bottomQuad[0],
                        bottomQuad[1],
                };
    }
}
