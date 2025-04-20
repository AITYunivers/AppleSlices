package io.github.yunivers.appleslices.mixin;

import io.github.yunivers.appleslices.AppleSlices;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
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
        AppleSlices.pulseValue = MathHelper.clamp(AppleSlices.pulseUnclamped, 0, 1) * AppleSlices.maxAlpha;
    }
}
