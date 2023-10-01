package net.creative.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ForceBlock {

    private boolean toggleForceBlock = false;
    private long lastKeyPressTime = 0L;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onWorldTick(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            return;
        }
        BlockHitResult hitResult = (BlockHitResult) mc.crosshairTarget;
        BlockPos ghostBlockPos = hitResult.getBlockPos();

        if (GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_P) == GLFW.GLFW_PRESS) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastKeyPressTime >= 300) {
                toggleForceBlock = !toggleForceBlock;
                if (toggleForceBlock) {
                    BlockState ghostBlockState = Blocks.SOUL_SAND.getDefaultState();
                    ((ClientWorld) (Object) this).setBlockState(ghostBlockPos, ghostBlockState, 19);
                } else {
                    toggleForceBlock = false;
                }

            }
        }
    }
}