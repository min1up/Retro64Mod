package com.dylanpdx.retro64;

import com.dylanpdx.retro64.capabilities.smc64Capability;
import com.dylanpdx.retro64.capabilities.smc64CapabilityInterface;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    /**
     * Find Water level at the current position
     * @param world Minecraft world
     * @param pos Block position
     * @return Water level
     */
    public static int findWaterLevel(ClientWorld world, BlockPos pos){
        int lvl = -100;
        for (int i = pos.getY();i<255;i++){
            if (world.isWater(pos.withY(i)))
                lvl=i;
            else
                return lvl;
        }
        return -100;
    }

    /**
     * Turn a list of BakedQuads into a list of Vec3d vertices
     * @param quads List of BakedQuads
     * @return List of Vec3d vertices
     */
    public static List<Vec3d> decodeQuads(List<BakedQuad> quads){
        ArrayList<Vec3d> pquads = new ArrayList<>();
        for (BakedQuad quad : quads){
            for (Vec3d pos : decodeQuad(quad)){
                pquads.add(pos);
            }
        }
        return pquads;
    }

    /**
     * Take a single BakedQuad and turn it into an array of Vec3d vertices
     * @param quad BakedQuad to decode
     * @return List of Vec3d vertices
     */
    public static Vec3d[] decodeQuad(BakedQuad quad){
        int[] vertexData=quad.getVertexData();
        Vec3d[] dquad = new Vec3d[4];
        for (int i = 0;i<4;i++){
            float x = fFromByteArray(iToByteArray(vertexData[8 * i]));
            float y = fFromByteArray(iToByteArray(vertexData[8 * i+1]));
            float z = fFromByteArray(iToByteArray(vertexData[8 * i+2]));
            dquad[i]=new Vec3d(x,y,z);
        }
        return dquad;
    }

    /**
     * Get all Vertices of a block from it's BakedModel
     * @param bakedModel BakedModel of the block
     * @param blockState BlockState of the block
     * @param random Random number generator
     * @return List of Vec3d vertices
     */
    public static List<Vec3d> getAllQuads(BakedModel bakedModel, BlockState blockState, Random random){
        ArrayList<Vec3d> quads = new ArrayList<>();
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, null,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.DOWN,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.UP,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.EAST,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.NORTH,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.SOUTH,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.WEST,random)));
        return quads;
    }

    static byte[] iToByteArray(int value) {
        return  ByteBuffer.allocate(4).putInt(value).array();
    }

    static float fFromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getFloat();
    }

    public static int iFromByteArray(byte[] bytes) {
        return  ByteBuffer.wrap(bytes).getInt();
    }


    public static double QuatToYaw(Quaternion q){
        // double yaw = Math.atan2(2.0*(q.j()*q.k() + q.r()*q.i()), q.r()*q.r() - q.i()*q.i() - q.j()*q.j() + q.k()*q.k());
        //double pitch = Math.asin(-2.0*(q.i()*q.k() - q.r()*q.j()));
        double pitch = Math.asin(-2.0*(q.getX()*q.getZ() - q.getW()*q.getY()));
        // double roll = Math.atan2(2.0*(q.i()*q.j() + q.r()*q.k()), q.r()*q.r() + q.i()*q.i() - q.j()*q.j() - q.k()*q.k());
        return pitch;
    }

    /**
     * Rotate a point around a rotation
     * Based on https://github.com/Unity-Technologies/UnityCsReference/blob/master/Runtime/Export/Math/Quaternion.cs#L106
     * @param rotation Rotation to rotate around
     * @param point Point to rotate
     * @return Rotated point
     */
    public static Vec3d rotatePt(Quaternion rotation,Vec3d point){
        float x = rotation.getX() * 2F;
        float y = rotation.getY() * 2F;
        float z = rotation.getZ() * 2F;
        float xx = rotation.getX() * x;
        float yy = rotation.getY() * y;
        float zz = rotation.getZ() * z;
        float xy = rotation.getX() * y;
        float xz = rotation.getX() * z;
        float yz = rotation.getY() * z;
        float wx = rotation.getW() * x;
        float wy = rotation.getW() * y;
        float wz = rotation.getW() * z;

        Vec3d res = new Vec3d(
                (1F - (yy + zz)) * point.x + (xy - wz) * point.y + (xz + wy) * point.z,
                (xy + wz) * point.x + (1F - (xx + zz)) * point.y + (yz - wx) * point.z,
                (xz - wy) * point.x + (yz + wx) * point.y + (1F - (xx + yy)) * point.z);
        return res;
    }

    public static smc64CapabilityInterface getSmc64Capability(PlayerEntity player){
        return player.getCapability(smc64Capability.INSTANCE).map(smc64->{
            return smc64;
        }).orElse(null);
    }

    public static DataInputStream dataStreamAtPos(byte[] data, int pos, int length){
        var truncated = new byte[length];
        System.arraycopy(data,pos,truncated,0,length);
        return new DataInputStream(new ByteArrayInputStream(truncated));
    }
    public static DataInputStream dataStreamAtPos(byte[] data, int pos){
        return dataStreamAtPos(data,pos,data.length-pos);
    }

    /**
     * Makes a Quaternion that looks at a rotation
     * Based on https://answers.unity.com/questions/467614/what-is-the-source-code-of-quaternionlookrotation.html
     * @param forward Vector to look at
     * @param up Vector to use as up
     * @return Quaternion that looks at the rotation
     */
    public static Quaternion QuaternionLookRotation(Vec3d forward, Vec3d up)
    {
        forward.normalize();

        Vec3d vector = forward.normalize();
        Vec3d vector2 = up.crossProduct(vector).normalize();
        Vec3d Vec3d = vector.crossProduct(vector2);
        var m00 = (float)vector2.x;
        var m01 = (float)vector2.y;
        var m02 = (float)vector2.z;
        var m10 = (float)Vec3d.x;
        var m11 = (float)Vec3d.y;
        var m12 = (float)Vec3d.z;
        var m20 = (float)vector.x;
        var m21 = (float)vector.y;
        var m22 = (float)vector.z;


        double num8 = (m00 + m11) + m22;
        var quaternion = Quaternion.fromEulerYxz(0,0,0);
        if (num8 > 0f)
        {
            // I J K R
            // X Y Z W
            var num = (float)Math.sqrt(num8 + 1f);
            quaternion.set(quaternion.getX(),quaternion.getY(),quaternion.getZ(),num * 0.5f);;
            num = 0.5f / num;
            quaternion.set((m12 - m21) * num,(m20 - m02) * num,(m01 - m10) * num,quaternion.getW());
            return quaternion;
        }
        if ((m00 >= m11) && (m00 >= m22))
        {
            var num7 = (float)Math.sqrt(((1f + m00) - m11) - m22);
            var num4 = 0.5f / num7;
            quaternion.set(0.5f * num7,(m01 + m10) * num4,(m02 + m20) * num4,(m12 - m21) * num4);
            return quaternion;
        }
        if (m11 > m22)
        {
            var num6 = (float)Math.sqrt(((1f + m11) - m00) - m22);
            var num3 = 0.5f / num6;
            quaternion.set((m10 + m01) * num3,0.5f * num6,(m21 + m12) * num3,(m20 - m02) * num3);
            //quaternion.x = (m10+ m01) * num3;
            //quaternion.y = 0.5f * num6;
            //quaternion.z = (m21 + m12) * num3;
            //quaternion.w = (m20 - m02) * num3;
            return quaternion;
        }
        var num5 = (float)Math.sqrt(((1f + m22) - m00) - m11);
        var num2 = 0.5f / num5;
        quaternion.set((m20 + m02) * num2,(m21 + m12) * num2,0.5f * num5,(m01 - m10) * num2);
        //quaternion.x = (m20 + m02) * num2;
        //quaternion.y = (m21 + m12) * num2;
        //quaternion.z = 0.5f * num5;
        //quaternion.w = (m01 - m10) * num2;
        return quaternion;
    }
}
