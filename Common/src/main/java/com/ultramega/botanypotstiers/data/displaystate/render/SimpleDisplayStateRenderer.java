package com.ultramega.botanypotstiers.data.displaystate.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ultramega.botanypotstiers.data.displaystate.SimpleDisplayState;
import net.darkhax.bookshelf.api.client.RenderHelper;
import net.darkhax.botanypots.data.displaystate.math.AxisAlignedRotation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SimpleDisplayStateRenderer extends DisplayStateRenderer<SimpleDisplayState> {
    public static final SimpleDisplayStateRenderer RENDERER = new SimpleDisplayStateRenderer();

    private SimpleDisplayStateRenderer() {

    }

    @Override
    public void render(SimpleDisplayState displayState, PoseStack pose, Level level, BlockPos pos, MultiBufferSource bufferSource, int light, int overlay, float progress) {
        pose.pushPose();

        displayState.scale.ifPresent(scale -> pose.scale(scale.x(), scale.y(), scale.z()));
        displayState.offset.ifPresent(offset -> pose.translate(offset.x(), offset.y(), offset.z()));

        for (AxisAlignedRotation rotation : displayState.rotations) {
            // Applies the rotation to the render.
            pose.mulPose(rotation.rotation);

            // Realigns the render with the original axis-aligned position.
            pose.translate(rotation.offset.x(), rotation.offset.y(), rotation.offset.z());
        }

        final BlockState blockState = displayState.getRenderState(progress);

        // Render Fluid
        if (displayState.renderFluid) {
            final FluidState fluidState = blockState.getFluidState();

            if (fluidState != null && !fluidState.isEmpty()) {
                RenderHelper.get().renderFluidBox(pose, fluidState, level, pos, bufferSource, light, OverlayTexture.NO_OVERLAY);
            }
        }

        // Render Model
        if (blockState.getRenderShape() == RenderShape.MODEL) {
            final BakedModel blockModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);
            final VertexConsumer builder = bufferSource.getBuffer(RenderType.cutout());
            final int tintColor = Minecraft.getInstance().getBlockColors().getColor(blockState, level, pos, 0);
            final float red = (float) (tintColor >> 16 & 255) / 255f;
            final float green = (float) (tintColor >> 8 & 255) / 255f;
            final float blue = (float) (tintColor & 255) / 255f;

            Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(pose.last(), builder, blockState, blockModel, red, green, blue, light, OverlayTexture.NO_OVERLAY);
        }

        pose.popPose();
    }
}
