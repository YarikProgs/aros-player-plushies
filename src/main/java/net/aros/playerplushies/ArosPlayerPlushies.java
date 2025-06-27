package net.aros.playerplushies;

import net.aros.playerplushies.init.AppBlocks;
import net.aros.playerplushies.init.AppItems;
import net.aros.playerplushies.init.AppSounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
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
    }
}
