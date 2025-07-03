package net.aros.playerplushies;

import net.aros.playerplushies.block.PlushieBlock;
import net.aros.playerplushies.init.AppItems;
import net.aros.playerplushies.init.AppSounds;
import net.aros.playerplushies.item.PlushieBoxItem;
import net.aros.playerplushies.item.PlushieItem;
import net.aros.playerplushies.loader.PlushiesLoader;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ArosPlayerPlushies.MOD_ID)
public class ArosPlayerPlushies {
    public static final String MOD_ID = "app";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ArosPlayerPlushies(IEventBus bus) {
        LOGGER.info("Aros' Player Plushies is initializing!");

        AppSounds.register(bus);
        AppItems.register(bus);
        PlushiesLoader.INSTANCE.load();

        NeoForge.EVENT_BUS.addListener(this::onAchievement);
        NeoForge.EVENT_BUS.addListener(this::onKilled);
        bus.addListener(EventPriority.HIGH, this::registerBlocksEvent);
        bus.addListener(EventPriority.NORMAL, this::registerItemsEvent);
    }

//    Item item = new PlushieItem(block, new Item.Settings(), plushie);
//    event.register(RegistryKeys.ITEM, registry -> registry.register(plushie.withSuffixedPath("_plushie"), item));
//    AppItems.ADD_TO_GROUP.add(item);

    private void registerBlocksEvent(RegisterEvent event) {
        PlushiesLoader.INSTANCE.getPlushieSets().forEach(set -> {
            for (Identifier plushie : set.plushies()) {
                event.register(RegistryKeys.BLOCK, registry -> registry.register(plushie.withSuffixedPath("_plushie"), new PlushieBlock(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL))));
            }
            AppItems.CATEGORIES.add(set.category());
        });
    }

    private void registerItemsEvent(RegisterEvent event) {
        PlushiesLoader.INSTANCE.getPlushieSets().forEach(set -> {
            for (Identifier plushie : set.plushies()) {
                Identifier id = plushie.withSuffixedPath("_plushie");
                event.register(RegistryKeys.ITEM, registry -> registry.register(id, new PlushieItem(Registries.BLOCK.get(id), new Item.Settings(), plushie)));
                AppItems.ADD_TO_GROUP.add(() -> Registries.ITEM.get(id));
            }
            AppItems.CATEGORIES.add(set.category());
        });
    }

    private void onAchievement(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity serverPlayer && event.getAdvancement().id().equals(Identifier.of(MOD_ID, "plushies/adv1"))) {
            ItemHandlerHelper.giveItemToPlayer(serverPlayer, AppItems.COLLECTORS_HAND.toStack());
        }
    }

    private void onKilled(LivingDeathEvent event) {
        if (event.getEntity().getWorld().isClient) return;
        ItemStack stack = event.getSource().getWeaponStack();
        if (stack != null && stack.getItem().equals(AppItems.COLLECTORS_HAND.get()) && event.getSource().getAttacker() instanceof PlayerEntity player && player.getRandom().nextDouble() >= 0.5) {
            ItemHandlerHelper.giveItemToPlayer(player, PlushieBoxItem.getRandomStack(player.getRandom()));
        }
    }
}
