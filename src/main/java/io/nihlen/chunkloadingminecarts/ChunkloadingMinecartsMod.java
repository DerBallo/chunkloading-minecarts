package io.nihlen.chunkloadingminecarts;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.util.math.ChunkPos;

import java.util.Comparator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChunkloadingMinecartsMod implements ModInitializer {
    public static final String MODID = "chunkloadingminecarts";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    //public static int MINECART_TICKET_EXPIRY = 20;
    public static int MINECART_TICKET_RADIUS = 1;

//    public static final GameRules.Key<IntRule> MINECART_TICKET_EXPIRY_RULE = GameRuleRegistry.register(
//            "minecartTicketExpiry",
//            GameRules.Category.MISC,
//            GameRuleFactory.createIntRule(20, 1, 10000, (server, rule) -> {
//                MINECART_TICKET_EXPIRY = rule.get();
//            })
//    );

    public static final GameRules.Key<IntRule> MINECART_TICKET_RADIUS_RULE = GameRuleRegistry.register(
            "minecartTicketRadius",
            GameRules.Category.MISC,
            GameRuleFactory.createIntRule(1, 1, 16, (server, rule) -> {
                MINECART_TICKET_RADIUS = rule.get();
            })
    );

    public static final ChunkTicketType<ChunkPos> MINECART = ChunkTicketType.create("minecart", Comparator.comparingLong(ChunkPos::toLong), 5);

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            //MINECART_TICKET_EXPIRY = server.getOverworld().getGameRules().getInt(MINECART_TICKET_EXPIRY_RULE);
            MINECART_TICKET_RADIUS = server.getOverworld().getGameRules().getInt(MINECART_TICKET_RADIUS_RULE);
        });
    }
}
