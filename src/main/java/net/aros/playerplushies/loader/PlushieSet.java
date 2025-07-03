package net.aros.playerplushies.loader;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

import java.util.List;

public record PlushieSet(Identifier category, List<Identifier> plushies) {
    public static final Codec<PlushieSet> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Identifier.CODEC.fieldOf("category").forGetter(PlushieSet::category),
            Identifier.CODEC.listOf().fieldOf("values").forGetter(PlushieSet::plushies)
    ).apply(inst, PlushieSet::new));
}
