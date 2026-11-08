package dev.kit.narratoroff;

import net.minecraft.client.Minecraft;
import net.minecraft.client.NarratorStatus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Keeps the Minecraft narrator off. The Ctrl+B shortcut is hard-coded in the game,
 * so instead of removing the key this mod puts the setting back to OFF right away
 * whenever something turns it on.
 */
@Mod(NarratorOff.MOD_ID)
public class NarratorOff {
    public static final String MOD_ID = "narratoroff";

    public NarratorOff() {
        // The narrator only exists on the client, so only register there.
        if (net.minecraftforge.fml.loading.FMLEnvironment.dist.isClient()) {
            MinecraftForge.EVENT_BUS.register(new ClientHandler());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ClientHandler {
        private static void forceOff() {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null || mc.options == null) {
                return;
            }
            if (mc.options.narrator().get() != NarratorStatus.OFF) {
                mc.options.narrator().set(NarratorStatus.OFF);
                mc.options.save();
            }
        }

        @SubscribeEvent
        public void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                forceOff();
            }
        }

        @SubscribeEvent
        public void onScreenInit(ScreenEvent.Init.Post event) {
            forceOff();
        }

        @SubscribeEvent
        public void onLogin(ClientPlayerNetworkEvent.LoggingIn event) {
            forceOff();
        }
    }
}
