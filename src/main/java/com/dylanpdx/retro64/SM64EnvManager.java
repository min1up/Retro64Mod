package com.dylanpdx.retro64;

import com.dylanpdx.retro64.events.clientControllerEvents;
import com.dylanpdx.retro64.sm64.SM64SurfaceType;
import com.dylanpdx.retro64.sm64.libsm64.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;

public class SM64EnvManager {

    public static boolean initialized = false;

    public static int playerModel=0;

    public static long attackDebounce=0;

    public static MChar selfMChar;

    public static ArrayList<SM64Surface> surfaces = new ArrayList<>();

    public static float lastVol=1;

    public static void updateSurfs(surfaceItem[] surfs){
        updateSurfs(surfs,null);
    }

    /**
     * Update volume
     */
    public static void updateVol(){
        var vol = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER) * MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.PLAYERS);
        if (lastVol!=vol){
            lastVol=vol;
            Libsm64Library.INSTANCE.sm64_set_volume(vol);
        }
    }

    /**
     * Generates a solid floor at the given position; intended to prevent segfaults when there is no floor that exists
     * @param x the x coordinate of the floor
     * @param y the y coordinate of the floor
     * @param z the z coordinate of the floor
     * @return the generated floor surfaces
     */
    public static SM64Surface[] generateSafetyFloor(float x, float y, float z){
        return LibSM64SurfUtils.generateQuad(
                new Vec3f(-10, -0.5f, -10),
                new Vec3f(-10, -0.5f, 10),
                new Vec3f(10, -0.5f, 10),
                new Vec3f(10, -0.5f, -10),
                new Vec3f(x,y,z), SM64SurfaceType.Default, (short)0);
    }

    /**
     * Update the surfaces (send them to the libsm64 library)
     * @param surfaceItems the surfaces to update
     * @param extras extra surfaces to add on top of the given surfaces
     */
    public static void updateSurfs(surfaceItem[] surfaceItems, SM64Surface[] extras){
        surfaces.clear();
        if (extras!=null)
            Collections.addAll(surfaces, extras);
        if (selfMChar!=null)
            // generate floor below the player
            Collections.addAll(surfaces,generateSafetyFloor(selfMChar.state.position[0] / LibSM64.SCALE_FACTOR, -80, selfMChar.state.position[2] / LibSM64.SCALE_FACTOR));
        else
        {
            var playerPos = MinecraftClient.getInstance().player.getPos();
            Collections.addAll(surfaces,generateSafetyFloor((float)playerPos.x,-80,(float)playerPos.z));
        }
        if (RemoteMCharHandler.mChars!=null)
        for(MChar mChar : RemoteMCharHandler.mChars.values()){
            Collections.addAll(surfaces,generateSafetyFloor(mChar.state.position[0] / LibSM64.SCALE_FACTOR, -80, mChar.state.position[2] / LibSM64.SCALE_FACTOR));
        }
        if (surfaceItems!=null)
        {
            int surfCount = surfaceItems.length;
            for (int i = 0; i < surfCount; i++) {
                Vec3d[] blockVertices = surfaceItems[i].verts; // vertices of the block
                if (blockVertices.length==1)
                {
                    if (surfaceItems[i].isCube())
                        Collections.addAll(surfaces,LibSM64SurfUtils.block((int)blockVertices[0].z,(int)blockVertices[0].y,(int)blockVertices[0].x,1f,0.1f,surfaceItems[i].material,surfaceItems[i].terrain));
                    else if (surfaceItems[i].isFlat())
                        Collections.addAll(surfaces,LibSM64SurfUtils.block((int)blockVertices[0].z,(int)blockVertices[0].y,(int)blockVertices[0].x,.03f,0.01f,surfaceItems[i].material,surfaceItems[i].terrain));
                }
                else
                    for (int j = 0; j < blockVertices.length; j+=4)
                    {
                        var one = new Vec3f(blockVertices[j]);
                        var two = new Vec3f(blockVertices[j+1]);
                        var three = new Vec3f(blockVertices[j+2]);
                        var four = new Vec3f(blockVertices[j+3]);
                        var quads = LibSM64SurfUtils.generateQuad(one,two, three, four,new Vec3f(0,0,0), surfaceItems[i].material, surfaceItems[i].terrain);
                        Collections.addAll(surfaces, quads);
                    }
            }
        }
        LibSM64.StaticSurfacesLoad(surfaces.toArray(new SM64Surface[0]));
    }

    public static void updateControls(Vec3d cam_fwd,Vec3d camPos,float joystickMult, boolean act_pressed,boolean jump_pressed, boolean crouch_pressed,
                                      boolean W_pressed, boolean A_pressed, boolean S_pressed, boolean D_pressed){
        selfMChar.inputs.buttonB= (byte) (act_pressed?1:0);
        selfMChar.inputs.buttonA= (byte) (jump_pressed?1:0);
        selfMChar.inputs.buttonZ= (byte) (crouch_pressed?1:0);
        selfMChar.inputs.stickX=0;
        selfMChar.inputs.stickY=0;
        Vec2f v2=null;
        if (Retro64.hasControllerSupport && (clientControllerEvents.input.x>0 || clientControllerEvents.input.y>0)){
            v2 = clientControllerEvents.input;
        }else{
            if (W_pressed) selfMChar.inputs.stickX += 1;
            if (A_pressed) selfMChar.inputs.stickY -= 1;
            if (S_pressed) selfMChar.inputs.stickX -= 1;
            if (D_pressed) selfMChar.inputs.stickY += 1;
            v2 = new Vec2f(selfMChar.inputs.stickX, selfMChar.inputs.stickY).normalize();
        }
        selfMChar.inputs.stickX=v2.x*joystickMult;
        selfMChar.inputs.stickY=v2.y*joystickMult;
        selfMChar.inputs.camLookX= (float) cam_fwd.x;
        selfMChar.inputs.camLookZ= (float) cam_fwd.z;

        camPos= new Vec3d(selfMChar.state.position[0],selfMChar.state.position[1],selfMChar.state.position[2]);//PUFixer.convertToSM64(camPos);
        // camera stuff
        selfMChar.inputs.cameraPosition[0] = (float) camPos.x;
        selfMChar.inputs.cameraPosition[1] = (float) camPos.y;
        selfMChar.inputs.cameraPosition[2] = (float) camPos.z;
    }

    /**
     * Get SHA1 hash of the given file
     * @param file the file to hash
     * @return the SHA1 hash of the file
     * @throws Exception if the file cannot be read
     */
    public static byte[] createSha1(File file) throws Exception  {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream fis = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }
        fis.close();
        return digest.digest();
    }

    /**
     * Get SHA1 hash of the given file as a hex string
     * @param file the file to hash
     * @return the SHA1 hash of the file as a hex string
     * @throws Exception if the file cannot be read
     */
    public static String createSha1String(File file) throws Exception  {
        var sha1 = createSha1(file);
        StringBuilder sb = new StringBuilder();
        for (byte b : sha1) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * find a file that matches the SHA1 hash "9bef1128717f958171a4afac3ed78ee2bb4e86ce"
     * @return the file that matches the SHA1 hash, or null if no file matches
     */
    public static File getROMFile(){
        File[] files = new File("mods").listFiles(f -> {
            try {
                return f.toPath().toString().endsWith("64") && createSha1String(f).equals("9bef1128717f958171a4afac3ed78ee2bb4e86ce");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
        if (files.length == 0) {
            return null;//throw new FileNotFoundException("Could not find valid ROM");
        }
        return files[0];
    }

    /**
     * Initialize the SM64 engine
     * @throws IOException if the ROM file cannot be read
     */
    public static void initLib() throws IOException {
        if (initialized)
            return;
        Retro64.LOGGER.info("Initializing Retro64. LibSM64 version: " + LibSM64.getVersion());
        var romFile = getROMFile();
        if (romFile == null) {
            return;
        }
        if (!romFile.getName().equals("baserom.us.z64")){
            var newPath=Path.of("mods","baserom.us.z64");
            Files.move(romFile.toPath(),newPath);
            romFile=newPath.toFile();
        }
        File audioBinFile = Path.of("mods","audioData.bin").toFile();
        if (audioBinFile.exists()){
            LibSM64.GlobalInitAudioBin(romFile,audioBinFile);
        }else{
            try {
                var extractedFilesDir = assetsExtract.extractToTmp(romFile);
                if (extractedFilesDir == null) {
                    Retro64.LOGGER.info("Could not extract sound assets");
                }
                LibSM64.GlobalInit(romFile.getPath(),extractedFilesDir);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // init faiiled, stop loading
                throw new IOException("ROM loading failed");
            }
        }
        initialized = true;
    }

    public static void sm64Update() {
        updateVol();
        selfMChar.fixedUpdate();
    }


}
