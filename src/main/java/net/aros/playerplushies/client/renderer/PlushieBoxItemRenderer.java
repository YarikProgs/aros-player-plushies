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
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.mixin.client.BlockEntityWithoutLevelRendererMixin;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.aros.playerplushies.ArosPlayerPlushies.MOD_ID;

public class PlushieBoxItemRenderer extends GeoItemRenderer<PlushieBoxItem> {
    public static final Identifier DEFAULT_SKIN = Identifier.of(MOD_ID, "null");

    public PlushieBoxItemRenderer() {
        super(new PlushieBoxItemModel());
    }

    @Override
    public void actuallyRender(MatrixStack matrices, PlushieBoxItem animatable, BakedGeoModel model, @Nullable RenderLayer layer, VertexConsumerProvider provider, @Nullable VertexConsumer consumer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        matrices.push();
        BuiltinModelItemRenderer renderer;
        matrices.scale(-0.7f, -0.7f, 0.7f);
        matrices.translate(0, -1.6, 0);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
        PlayerEntityModel<ClientPlayerEntity> playerModel = new PlayerEntityModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.PLAYER), false);
        Identifier nickname = currentItemStack.getOrDefault(AppItems.NICKNAME, DEFAULT_SKIN);
        playerModel.render(matrices, provider.getBuffer(RenderLayer.getEntityTranslucentCull(nickname.withPath("textures/item/skins/" + nickname.getPath() + ".png"))), packedLight, packedOverlay, colour);
        matrices.pop();

        super.actuallyRender(matrices, animatable, model, layer, provider, consumer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public @Nullable RenderLayer getRenderType(PlushieBoxItem animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucentCull(texture);
    }

    public static class Extensions implements IClientItemExtensions {
        @ParametersAreNonnullByDefault
        public boolean applyForgeHandTransform(MatrixStack matrices, ClientPlayerEntity player, Arm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            if (itemInHand.contains(AppItems.REVEALING.get())) {
                matrices.translate(0, -0.5, -1);
                return true;
            }
            return false;
        }
    }
}
