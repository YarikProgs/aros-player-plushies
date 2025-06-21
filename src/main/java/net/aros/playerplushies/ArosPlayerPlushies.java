package net.aros.playerplushies;

import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ArosPlayerPlushies.MOD_ID)
public class ArosPlayerPlushies {
    public static final String MOD_ID = "app";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ArosPlayerPlushies() {
        LOGGER.info("Aros' Player Plushies is initializing!");
    }
}
