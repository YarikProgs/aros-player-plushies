package net.aros.playerplushies.client.model;

import net.aros.playerplushies.client.renderer.PlushieBoxItemRenderer;
import net.aros.playerplushies.init.AppItems;
import net.aros.playerplushies.item.PlushieBoxItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

import static net.aros.playerplushies.ArosPlayerPlushies.MOD_ID;

public class PlushieBoxItemModel extends DefaultedItemGeoModel<PlushieBoxItem> {
    public PlushieBoxItemModel() {
        super(Identifier.of(MOD_ID, "plushie_box"));
    }

    @Override
    public Identifier getTextureResource(PlushieBoxItem animatable, @Nullable GeoRenderer<PlushieBoxItem> renderer) {
        String type = !(renderer instanceof PlushieBoxItemRenderer r) || !r.getCurrentItemStack().contains(AppItems.BOX_TYPE.get())
                ? "overworld"
                : r.getCurrentItemStack().get(AppItems.BOX_TYPE);

        return Identifier.of(MOD_ID, "textures/item/boxes/" + type + ".png");
    }
}
