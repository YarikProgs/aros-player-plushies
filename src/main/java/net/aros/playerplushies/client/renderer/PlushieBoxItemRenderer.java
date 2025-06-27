package net.aros.playerplushies.client.renderer;

import net.aros.playerplushies.client.model.PlushieBoxItemModel;
import net.aros.playerplushies.init.AppItems;
import net.aros.playerplushies.item.PlushieBoxItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import static net.aros.playerplushies.ArosPlayerPlushies.MOD_ID;

public class PlushieBoxItemRenderer extends GeoItemRenderer<PlushieBoxItem> {
    public PlushieBoxItemRenderer() {
        super(new PlushieBoxItemModel());
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, PlushieBoxItem animatable, BakedGeoModel model, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        // TODO: 25.06.2025
        poseStack.push();
        poseStack.scale(-0.7f, -0.7f, 0.7f);
        poseStack.translate(0, -1.6, 0);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
        PlayerEntityModel<ClientPlayerEntity> playerModel = new PlayerEntityModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.PLAYER), false);
        playerModel.render(poseStack, bufferSource.getBuffer(RenderLayer.getEntityTranslucentCull(Identifier.of(MOD_ID,
                "textures/item/skins/" + currentItemStack.getOrDefault(AppItems.NICKNAME, "null") + ".png"
        ))), packedLight, packedOverlay, colour);
        poseStack.pop();

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public @Nullable RenderLayer getRenderType(PlushieBoxItem animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucentCull(texture);
    }

    public static class Extensions implements IClientItemExtensions {
        public boolean applyForgeHandTransform(MatrixStack poseStack, ClientPlayerEntity player, Arm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            if (itemInHand.contains(AppItems.REVEALING.get())) {
                poseStack.translate(0, -0.5, -1);
                return true;
            }
            return false;
        }
    }
}
