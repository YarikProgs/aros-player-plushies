package net.aros.playerplushies.init;

import com.mojang.serialization.Codec;
import net.aros.playerplushies.item.CollectorsHandItem;
import net.aros.playerplushies.item.PlushieBoxItem;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.aros.playerplushies.ArosPlayerPlushies.MOD_ID;

public class AppItems {
    public static final List<ItemConvertible> ADD_TO_GROUP = new ArrayList<>();
    public static final List<Identifier> CATEGORIES = new ArrayList<>();

    private static final DeferredRegister<ItemGroup> GROUPS = DeferredRegister.create(Registries.ITEM_GROUP, MOD_ID);
    private static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(RegistryKeys.DATA_COMPONENT_TYPE, MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredHolder<ComponentType<?>, ComponentType<Integer>> REVEALING = COMPONENTS
            .registerComponentType("revealing", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));
    public static final DeferredHolder<ComponentType<?>, ComponentType<Identifier>> BOX_TYPE = COMPONENTS
            .registerComponentType("box_type", builder -> builder.codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC));
    public static final DeferredHolder<ComponentType<?>, ComponentType<Identifier>> NICKNAME = COMPONENTS
            .registerComponentType("nickname", builder -> builder.codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC));

    public static final DeferredItem<PlushieBoxItem> PLUSHIE_BOX = ITEMS.registerItem("plushie_box", PlushieBoxItem::new);
    public static final DeferredItem<CollectorsHandItem> COLLECTORS_HAND = ITEMS.registerItem("collectors_hand", CollectorsHandItem::new);

    public static final DeferredHolder<ItemGroup, ItemGroup> PLUSHIES_GROUP = GROUPS.register("plushies", () -> ItemGroup
            .builder()
            .displayName(Text.translatable("itemGroup." + MOD_ID))
            .icon(() -> ITEMS.getEntries()
                    .stream()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                        Collections.shuffle(collected);
                        return collected.stream();
                    }))
                    .limit(1)
                    .findFirst().orElseThrow().get().getDefaultStack()
            )
            .entries((params, output) -> {
                for (Identifier category : CATEGORIES)
                    output.add(Util.make(PLUSHIE_BOX.toStack(), stack -> stack.set(BOX_TYPE, category)));

                output.add(COLLECTORS_HAND.toStack());
                ADD_TO_GROUP.forEach(output::add);
            })
            .build()
    );

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        COMPONENTS.register(bus);
        GROUPS.register(bus);
    }
}
