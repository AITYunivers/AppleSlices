package io.github.yunivers.appleslices.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.yunivers.appleslices.AppleSlices;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawContext
{
	@Shadow private Minecraft minecraft;

	@Shadow private Random random;

	@Shadow private int ticks;

	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/player/ClientPlayerEntity;isInFluid(Lnet/minecraft/block/material/Material;)Z"
		)
	)
	public void renderAppleSlices(float tickDelta, boolean screenOpen, int mouseX, int mouseY, CallbackInfo ci, @Local ScreenScaler scaler)
	{
		ClientPlayerEntity player = minecraft.player;
		ItemStack heldItem = player.inventory.getSelectedItem();
		int healAmount;
		if (heldItem != null && heldItem.getItem() instanceof FoodItem food)
			healAmount = food.getHealthRestored();
		else return;

		int screenWidth = scaler.getScaledWidth();
		int screenHeight = scaler.getScaledHeight();
		int hearts = player.health;
		int healedHearts = hearts + healAmount;
		random.setSeed(ticks * 312871L);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4d(1, 1, 1, AppleSlices.pulseValue);
		for (int heart = 0; heart < 10; heart++)
		{
			int drawX = screenWidth / 2 - 91 + heart * 8;
			int drawY = screenHeight - 32;

			if (hearts <= 4)
				drawY += this.random.nextInt(2);

			if (heart * 2 + 1 >= hearts)
			{
				if (heart * 2 + 1 < healedHearts)
					this.drawTexture(drawX, drawY, 52, 0, 9, 9);
				if (heart * 2 + 1 == healedHearts)
					this.drawTexture(drawX, drawY, 61, 0, 9, 9);
			}
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
		GL11.glColor4d(1, 1, 1, 1);
	}
}
