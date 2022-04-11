package com.dylanpdx.retro64;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
//import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;

public class textureManager {

    static HashMap<String, NativeImageBackedTexture> textureMap = new HashMap<String, NativeImageBackedTexture>();
    static NativeImageBackedTexture defaultSkin=null;

    static NativeImage native_mChar;
    static NativeImageBackedTexture mCharTexture;
    static NativeImageBackedTexture luigiTexture;
    static Identifier luigiAtlas = new Identifier(Retro64.MOD_ID,"textures/model/luigi_atlas.png");
    static Identifier steveAtlas = new Identifier(Retro64.MOD_ID,"textures/model/steve.png");
    static Identifier necoarcAtlas = new Identifier(Retro64.MOD_ID,"textures/model/necoarc_atlas.png");
    static Identifier vibriAtlas = new Identifier(Retro64.MOD_ID,"textures/model/vibri_atlas.png");


    public static AbstractTexture getTextureForModel(int modelID, PlayerEntity player){
        return switch (modelID) {
            case 0 -> getMCharTexture();
            case 1 -> getLuigiTexture();
            case 2 -> getPlayerTexture(player);//Minecraft.getInstance().getTextureManager().getTexture(steveTexture);
            case 3 -> getPlayerTexture(player);//Minecraft.getInstance().getTextureManager().getTexture(steveTexture);
            case 4 -> MinecraftClient.getInstance().getTextureManager().getTexture(necoarcAtlas);
            case 5 -> MinecraftClient.getInstance().getTextureManager().getTexture(vibriAtlas);
            default -> null;
        };
    }

    /**
     * Get the width of the texture for the given model
     * @param modelID the model ID
     * @return the width of the texture
     */
    public static float getTextureWidth(int modelID){
        return switch (modelID) {
            case 0 -> 704;
            case 1 -> 704;
            case 2 -> 320;
            case 3 -> 320;
            case 4 -> 512;
            case 5 -> 192;
            default -> 1;
        };
    }

    /**
     * Get the height of the texture for the given model
     * @param modelID the model ID
     * @return the height of the texture
     */
    public static float getTextureHeight(int modelID){
        return switch (modelID) {
            default -> 64;
        };
    }

    /**
     * Get MChar texture extracted from ROM
     * @return MChar texture
     */
    public static AbstractTexture getMCharTexture() {
        return mCharTexture;
    }

    public static void setMCharTexture(NativeImage img){
        mCharTexture=new NativeImageBackedTexture(img);
        native_mChar=img;
    }

    public static AbstractTexture getLuigiTexture() {
        if (luigiTexture==null)
            luigiTexture=new NativeImageBackedTexture(TexGenerator.convertMCharTexToLuigi(native_mChar));
        return luigiTexture;
    }

    /**
     * Get player's skin as an InputStream
     * @param loc the player's skin location
     * @return the player's skin as an InputStream
     * @throws IOException if the skin cannot be found/read
     */
    public static InputStream getSkinInputStream(Identifier loc) throws IOException {
        if (loc==DefaultSkinHelper.getTexture())
            return MinecraftClient.getInstance().getResourceManager().getResource(DefaultSkinHelper.getTexture()).getInputStream();
        // Getting it like this because the skin manager method returns a null value
        /*
        var locStr=loc.toString().replace("minecraft:skins/","");
        var first2chars=locStr.substring(0,2);
        var skinFolder=ObfuscationReflectionHelper.getPrivateValue(PlayerSkinProvider.class, MinecraftClient.getInstance().getSkinProvider(),mappingsConvert.skinsDirectory);;
        var trueSkinPath = Paths.get(skinFolder.toString(),first2chars,locStr);
        

        return new FileInputStream(trueSkinPath.toFile());
        */
        //TODO: Find an ObfuscationReflectionHelper equivalent for Fabric
        return MinecraftClient.getInstance().getResourceManager().getResource(DefaultSkinHelper.getTexture()).getInputStream();
    }

    public static NativeImageBackedTexture extendSkinTexture(Identifier skinLoc) throws IOException {

        var iio=getSkinInputStream(skinLoc);
        var nativeImg=NativeImage.read(iio);
        NativeImage skinAtlas = new NativeImage(320,64,true);
        for (int y = 0; y < nativeImg.getHeight(); y++) {
            for (int x = 0; x < nativeImg.getWidth(); x++) {
                skinAtlas.setColor(x,y,nativeImg.getColor(x,y));
            }
        }
        if (nativeImg.getHeight()==32){
            // convert texture by copying over arm/leg
            TexGenerator.overlayImage(skinAtlas,nativeImg,16,48,0,16,16,16);
            TexGenerator.overlayImage(skinAtlas,nativeImg,32,48,40,16,16,16);
        }

        skinAtlas = TexGenerator.appendSteveStuffToTex(skinAtlas);

        //skinAtlas.writeToFile(new File("skinAtlas.png"));
        return new NativeImageBackedTexture(skinAtlas);
    }

    public static NativeImageBackedTexture getSteveTexture(){
        if (defaultSkin == null) {
            try {
                defaultSkin = extendSkinTexture(DefaultSkinHelper.getTexture());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return defaultSkin;
    }

    /**
     * Get the texture for the given player (mapped to the Steve/Alex model)
     * @param player the player
     * @return the texture for the given player
     */
    public static NativeImageBackedTexture getPlayerTexture(PlayerEntity player){
        var pname=player.getName().getString();
        if (textureMap.containsKey(pname)){
            if (textureMap.get(pname)==null){
                return getSteveTexture();
            }else{
                return textureMap.get(pname);
            }
        }else{
            textureMap.put(pname,null);

            // download texture
            var gameProfile= player.getGameProfile();
            MinecraftClient.getInstance().getSkinProvider().loadSkin(gameProfile, new PlayerSkinProvider.SkinTextureAvailableCallback() {
                @Override
                public void onSkinTextureAvailable(MinecraftProfileTexture.Type p_118857_, Identifier p_118858_, MinecraftProfileTexture p_118859_) {
                    try {
                        if (p_118857_ != MinecraftProfileTexture.Type.SKIN)
                            return;
                        var etex=extendSkinTexture(p_118858_);
                        if (etex==null)
                            etex=getSteveTexture();
                        textureMap.put(pname,etex);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },false);
            return getSteveTexture();
        }
    }

}
