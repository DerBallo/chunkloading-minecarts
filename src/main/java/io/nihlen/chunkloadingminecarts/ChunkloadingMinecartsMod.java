package io.nihlen.chunkloadingminecarts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChunkloadingMinecartsMod implements ModInitializer {
	public static final String MODID = "chunkloadingminecarts";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static final ChunkLoaderManager CHUNK_LOADER_MANAGER = new ChunkLoaderManager();

	public static final ChunkTicketType MINECART_TICKETTYPE =  Registry.register(Registries.TICKET_TYPE, ChunkloadingMinecartsMod.MODID, new ChunkTicketType(0L, true, ChunkTicketType.Use.LOADING_AND_SIMULATION));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ServerLifecycleEvents.SERVER_STARTED.register(CHUNK_LOADER_MANAGER::initialize);
	}
}
