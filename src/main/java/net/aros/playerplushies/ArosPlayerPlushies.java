package net.aros.playerplushies;

import net.aros.playerplushies.init.AppBlocks;
import net.aros.playerplushies.init.AppItems;
import net.aros.playerplushies.init.AppSounds;
import net.aros.playerplushies.item.PlushieBoxItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ArosPlayerPlushies.MOD_ID)
public class ArosPlayerPlushies {
    public static final String MOD_ID = "app";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ArosPlayerPlushies(IEventBus bus) {
        LOGGER.info("Aros' Player Plushies is initializing!");

        AppSounds.register(bus);
        AppBlocks.register(bus);
        AppItems.register(bus);

        NeoForge.EVENT_BUS.addListener(this::onAchievement);
        NeoForge.EVENT_BUS.addListener(this::onKilled);
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
            ItemHandlerHelper.giveItemToPlayer(player, PlushieBoxItem.getRandomStack());
        }
    }
}
