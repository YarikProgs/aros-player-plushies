package net.aros.playerplushies.loader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.aros.playerplushies.ArosPlayerPlushies;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PlushiesLoader {
    public static final PlushiesLoader INSTANCE = new PlushiesLoader();
    public static final Gson GSON = new Gson();
    private final List<PlushieSet> loaded = new ArrayList<>();

    public void load() {
        loaded.clear();

        for (IModInfo mod : ModList.get().getMods()) {
            try {
                loadForMod(mod);
            } catch (Exception e) {
                ArosPlayerPlushies.LOGGER.error("Failed to load plushies from {}", mod.getModId(), e);
            }
        }
    }

    private void loadForMod(IModInfo info) throws Exception {
        Path path = info.getOwningFile().getFile().findResource(String.format("data/%s/plushies", info.getModId()));
        if (path == null) return; // Mod has not defined any plushies

        try (Stream<@NotNull Path> stream = Files.list(path)) {
            stream.filter(pth -> Files.isRegularFile(pth) && pth.toString().endsWith(".json")).forEach(this::loadFile);
        }
    }

    private void loadFile(@NotNull Path path) {
        try {
            loaded.add(PlushieSet.CODEC.parse(JsonOps.INSTANCE, GSON.fromJson(Files.newBufferedReader(path), JsonElement.class)).getOrThrow());
        } catch (IOException e) {
            ArosPlayerPlushies.LOGGER.error("Failed to load plushie from {}", path.toAbsolutePath(), e);
        }
    }

    public List<PlushieSet> getPlushieSets() {
        return List.copyOf(loaded);
    }
}
