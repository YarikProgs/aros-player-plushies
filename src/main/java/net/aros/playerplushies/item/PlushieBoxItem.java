package net.aros.playerplushies.item;

import net.aros.playerplushies.client.renderer.PlushieBoxItemRenderer;
import net.aros.playerplushies.init.AppBlocks;
import net.aros.playerplushies.init.AppItems;
import net.minecraft.SharedConstants;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

import static net.aros.playerplushies.ArosPlayerPlushies.MOD_ID;

public class PlushieBoxItem extends Item implements GeoItem {
    private static final RawAnimation REVEAL = RawAnimation.begin().thenPlayAndHold("reveal");
    public static final int REVEALING_TIME = 4 * SharedConstants.TICKS_PER_SECOND;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PlushieBoxItem(Settings settings) {
        super(settings.maxCount(1).fireproof().component(AppItems.BOX_TYPE, "overworld"));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world instanceof ServerWorld serverWorld && hand == Hand.MAIN_HAND && !stack.contains(AppItems.REVEALING.get())) {
            stack.set(AppItems.REVEALING, REVEALING_TIME);
            triggerAnim(user, GeoItem.getOrAssignId(stack, serverWorld), "Revealing", "reveal");
        }
        return super.use(world, user, hand);
    }

    private void reveal(ServerPlayerEntity user, @NotNull ItemStack stack) {
        String name = stack.get(AppItems.NICKNAME);
        stack.remove(AppItems.REVEALING.get());
        stack.decrement(1);
        ItemHandlerHelper.giveItemToPlayer(user,
                new ItemStack(Registries.ITEM.get(Identifier.of(MOD_ID, name + "_plushie"))));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof ServerPlayerEntity user && stack.contains(AppItems.REVEALING.get())) {
            //noinspection DataFlowIssue
            int time = stack.get(AppItems.REVEALING);
            if ((float) time <= REVEALING_TIME / 4f && !stack.contains(AppItems.NICKNAME.get())) {
                stack.set(AppItems.NICKNAME, chooseRandomPlayer(world, stack.getOrDefault(AppItems.BOX_TYPE, "overworld")));
                user.playSoundToPlayer(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1, 1);
            }
            if (time > 0) {
                stack.set(AppItems.REVEALING, time - 1);
                return;
            }
            reveal(user, stack);
        }
    }


    private String chooseRandomPlayer(@NotNull WorldAccess world, String category) {
        List<String> players = AppBlocks.PLAYER_CATEGORIES.getOrDefault(category, List.of("aros"));
        return players.get(world.getRandom().nextInt(players.size()));
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private PlushieBoxItemRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (renderer == null) renderer = new PlushieBoxItemRenderer();
                return renderer;
            }
        });
    }

    @SuppressWarnings("removal")
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new PlushieBoxItemRenderer.Extensions());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Revealing", 0, state -> PlayState.STOP)
                .triggerableAnim("reveal", REVEAL));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable(getTranslationKey(), Text.translatable("boxtype." + MOD_ID + "." + stack.getOrDefault(AppItems.BOX_TYPE, "overworld")));
    }
}
