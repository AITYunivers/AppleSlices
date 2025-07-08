package io.github.yunivers.appleslices.mixin;

import io.github.yunivers.appleslices.AppleSlices;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class WorldMixin
{
    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    public void tickPulse(CallbackInfo ci)
    {
        AppleSlices.pulseUnclamped += AppleSlices.pulseDir * 0.125f;
        if (AppleSlices.pulseUnclamped >= 1.5f)
            AppleSlices.pulseDir = -1;
        if (AppleSlices.pulseUnclamped <= -0.5f)
            AppleSlices.pulseDir = 1;
        AppleSlices.pulseValue = clamp(AppleSlices.pulseUnclamped, 0, 1) * AppleSlices.maxAlpha;
    }

    // Removing StationAPI dependency
    @SuppressWarnings("SameParameterValue")
    @Unique
    private float clamp(float value, float min, float max)
    {
        if (value > max)
            return max;
        return Math.max(value, min);
    }
}
