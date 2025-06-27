package net.aros.playerplushies.init;

import net.aros.playerplushies.block.PlushieBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;

import static net.aros.playerplushies.ArosPlayerPlushies.MOD_ID;

public class AppBlocks {
    public static final List<String> NICKNAMES = List.of("8sixik_plushie", "aken_yt_plushie", "allman_plushie", "apelsintony_plushie", "arisa_plushie", "aros_plushie", "ballast_plushie", "ctotoh_plushie", "dago_plushie", "dish_plushie", "dr__loptr_plushie", "dushnila_plushie", "dylorix24_plushie", "elifian_plushie", "faldoor_plushie", "faorum_plushie", "gemesfoxes_plushie", "ienumerable_plushie", "justrius_plushie", "just_dude_plushie", "kirilla39_plushie", "klaqbi_plushie", "kriptor71_plushie", "kseouse_plushie", "kuragane_plushie", "laqony_plushie", "legendary_plushie", "leovinchi454_plushie", "lpsoverg_plushie", "magucplay_plushie", "margit_plushie", "mednik0_0_plushie", "mentolass_plushie", "meuvam_plushie", "mint_plushie", "moth_plushie", "nord_act_plushie", "okroshkrrr_plushie", "prince_vladislav_plushie", "progiple_plushie", "raventheprocrastinator_plushie", "reznov_plushie", "riki_lotiev_plushie", "romaha814_plushie", "samuelchik_plushie", "savaalienfish_plushie", "shadowking21228_plushie", "sigma_veb_plushie", "soud_puro_plushie", "sque9e_plushie", "sttcvoid_plushie", "tanyaytka_plushie", "toazter_plushie", "trankler_plushie", "undef1ned_plushie", "veitr_plushie", "velder_plushie", "vorono4ka_plushie", "yoshino_plushie", "ysh_plushie", "dmh_plushie");
    public static final Map<String, List<String>> PLAYER_CATEGORIES = Map.of(
            "aether", List.of("veitr", "okroshkrrr", "reznov", "klaqbi", "meuvam", "mentolass", "sttcvoid", "vorono4ka"),
            "bumble", List.of("dylorix24", "ienumerable", "shadowking21228", "romaha814", "kuragane", "undef1ned", "tanyaytka", "dmh"),
            "nether", List.of("dr__loptr", "leovinchi454", "progiple", "gemesfoxes", "mednik0_0", "sque9e", "velder", "ysh"),
            "overworld", List.of("nord_act", "aros", "faorum", "soud_puro", "samuelchik", "kriptor71", "trankler", "yoshino"),
            "starlight", List.of("allman", "ballast", "prince_vladislav", "laqony", "margit", "sigma_veb", "toazter", "just_dude"),
            "twilight", List.of("apelsintony", "dish", "faldoor", "legendary", "aken_yt", "kseouse", "lpsoverg", "justrius"),
            "under", List.of("8sixik", "ctotoh", "dago", "dushnila", "elifian", "magucplay", "mint", "moth", "arisa", "savaalienfish", "kirilla39", "raventheprocrastinator", "riki_lotiev")
    );

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);

    static {
        NICKNAMES.forEach(name -> BLOCKS.registerBlock(name, PlushieBlock::new, AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
