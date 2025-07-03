package net.aros.playerplushies.item;

import net.aros.playerplushies.client.renderer.PlushieBoxItemRenderer;
import net.aros.playerplushies.init.AppItems;
import net.aros.playerplushies.loader.PlushieSet;
import net.aros.playerplushies.loader.PlushiesLoader;
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
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.function.Consumer;

import static net.aros.playerplushies.ArosPlayerPlushies.MOD_ID;

public class PlushieBoxItem extends Item implements GeoItem {
    public static final Identifier DEFAULT_TYPE = Identifier.of(MOD_ID, "overworld");
    private static final RawAnimation REVEAL = RawAnimation.begin().thenPlayAndHold("reveal");
    public static final int REVEALING_TIME = 4 * SharedConstants.TICKS_PER_SECOND;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PlushieBoxItem(@NotNull Settings settings) {
        super(settings.maxCount(1).fireproof().component(AppItems.BOX_TYPE, DEFAULT_TYPE));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public static ItemStack getRandomStack(Random random) {
        return Util.make(AppItems.PLUSHIE_BOX.toStack(), stack -> stack.set(AppItems.BOX_TYPE, AppItems.CATEGORIES.get(random.nextInt(AppItems.CATEGORIES.size()))));
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
        Identifier name = stack.get(AppItems.NICKNAME);
        stack.remove(AppItems.REVEALING.get());
        stack.decrement(1);
        if (name == null || !Registries.ITEM.containsId(name.withSuffixedPath("_plushie"))) {
            user.sendMessage(Text.translatable(getTranslationKey() + ".fail").formatted(Formatting.GRAY));
            return;
        }
        ItemHandlerHelper.giveItemToPlayer(user, new ItemStack(Registries.ITEM.get(name.withSuffixedPath("_plushie"))));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof ServerPlayerEntity user && stack.contains(AppItems.REVEALING.get())) {
            //noinspection DataFlowIssue
            int time = stack.get(AppItems.REVEALING);
            if ((float) time <= REVEALING_TIME / 4f && !stack.contains(AppItems.NICKNAME.get())) {
                stack.set(AppItems.NICKNAME, chooseRandomPlayer(user.getRandom(), stack.getOrDefault(AppItems.BOX_TYPE, DEFAULT_TYPE)));
                user.playSoundToPlayer(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1, 1);
            }
            if (time > 0) {
                stack.set(AppItems.REVEALING, time - 1);
                return;
            }
            reveal(user, stack);
        }
    }


    @Contract("_, null -> null; null, _ -> fail")
    private @Nullable Identifier chooseRandomPlayer(Random random, Identifier category) {
        for (PlushieSet set : PlushiesLoader.INSTANCE.getPlushieSets()) {
            if (Objects.equals(set.category(), category))
                return set.plushies().get(random.nextInt(set.plushies().size()));
        }
        return null;
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
        return Text.translatable(getTranslationKey(), Text.translatable(stack.getOrDefault(AppItems.BOX_TYPE, DEFAULT_TYPE).toTranslationKey("boxtype")));
    }
}
