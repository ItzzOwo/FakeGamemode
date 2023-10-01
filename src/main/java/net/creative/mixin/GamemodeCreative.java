package net.creative.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.GameMode;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class GamemodeCreative {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final ClientPlayerInteractionManager interactionManager = mc.interactionManager;
    private boolean toggleCreative = false;
    private long lastKeyPressTime = 0L;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        if (mc.player == null) {
            return;
        }

        if (GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_O) == GLFW.GLFW_PRESS) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastKeyPressTime >= 300) {
                toggleCreative = !toggleCreative;
                if (toggleCreative) {
                    interactionManager.setGameMode(GameMode.CREATIVE);
                } else {
                    interactionManager.setGameMode(GameMode.SURVIVAL);
                }

                lastKeyPressTime = currentTime;
            }
        }
    }
}
