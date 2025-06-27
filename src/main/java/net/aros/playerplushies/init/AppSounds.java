package net.aros.playerplushies.init;

import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.aros.playerplushies.ArosPlayerPlushies.MOD_ID;

public class AppSounds {
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PLUSHIE_USE = SOUNDS.register("plushie_use", SoundEvent::of);

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}
