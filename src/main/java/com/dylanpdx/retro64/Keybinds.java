package com.dylanpdx.retro64;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class Keybinds {
    private static KeyBinding keyBindings[];

    public static void register() {
        keyBindings = new KeyBinding[] {
            KeyBindingHelper.registerKeyBinding(new KeyBinding("key."+Retro64.MOD_ID+".actKey", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "key.category.retro64")),
            KeyBindingHelper.registerKeyBinding(new KeyBinding("key."+Retro64.MOD_ID+".mToggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.category.retro64")),
            KeyBindingHelper.registerKeyBinding(new KeyBinding("key."+Retro64.MOD_ID+".mMenu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.category.retro64")),
            //KeyBindingHelper.registerKeyBinding(new KeyBinding("key."+Retro64.MOD_ID+".debugToggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_BRACKET, "key.category.retro64"))
        };
    }

    public static KeyBinding getActKey() {
        return keyBindings[0];
    }

    public static KeyBinding getMToggle() {
        return keyBindings[1];
    }

    public static KeyBinding getMMenu() {
        return keyBindings[2];
    }

    /*public static KeyBinding getDebugToggle() {
        return keyBindings[3];
    }*/

}
