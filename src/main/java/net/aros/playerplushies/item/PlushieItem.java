package net.aros.playerplushies.item;

import com.mojang.authlib.properties.PropertyMap;
import net.aros.playerplushies.init.AppSounds;
import net.aros.playerplushies.loader.PlushieSet;
import net.aros.playerplushies.loader.PlushiesLoader;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static net.aros.playerplushies.ArosPlayerPlushies.MOD_ID;

public class PlushieItem extends BlockItem {
    private final Identifier originalNickname;

    public PlushieItem(Block block, @NotNull Settings settings, Identifier originalNickname) {
        super(block, settings.maxCount(1).fireproof()
                .component(DataComponentTypes.PROFILE, new ProfileComponent(Optional.empty(), Optional.empty(), new PropertyMap())));
        this.originalNickname = originalNickname;
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
        for (PlushieSet set : PlushiesLoader.INSTANCE.getPlushieSets()) {
            if (set.plushies().contains(originalNickname)) {
                category = Text.translatable(set.category().toTranslationKey("boxtype"));
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
