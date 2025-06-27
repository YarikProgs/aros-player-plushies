package net.aros.playerplushies.item;

import com.mojang.authlib.properties.PropertyMap;
import net.aros.playerplushies.init.AppBlocks;
import net.aros.playerplushies.init.AppSounds;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.aros.playerplushies.ArosPlayerPlushies.MOD_ID;

public class PlushieItem extends BlockItem {
    public PlushieItem(Block block, @NotNull Settings settings) {
        super(block, settings.maxCount(1).fireproof()
                .component(DataComponentTypes.PROFILE, new ProfileComponent(Optional.empty(), Optional.empty(), new PropertyMap())));
    }

    @Override
    public TypedActionResult<ItemStack> use(@NotNull World world, @NotNull PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            world.playSound(null, user.getBlockPos(), AppSounds.PLUSHIE_USE.get(), SoundCategory.PLAYERS, 1.0f, 0.5f + world.random.nextFloat() * 1.2f);
        }

        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, @NotNull List<Text> tooltip, TooltipType type) {
        Text category = Text.literal("?");
        for (Map.Entry<String, List<String>> entry : AppBlocks.PLAYER_CATEGORIES.entrySet()) {
            if (entry.getValue().contains(Registries.ITEM.getId(this).getPath().split("_plushie")[0])) {
                category = Text.translatable("boxtype." + MOD_ID + "." + entry.getKey());
                break;
            }
        }

        tooltip.add(Text.translatable("desc." + MOD_ID + ".plushie", category).formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public @Nullable EquipmentSlot getEquipmentSlot(@NotNull ItemStack stack) {
        return EquipmentSlot.HEAD;
    }
}
