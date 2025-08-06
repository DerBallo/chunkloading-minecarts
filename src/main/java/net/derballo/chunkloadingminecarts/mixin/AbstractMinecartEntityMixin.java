package net.derballo.chunkloadingminecarts.mixin;

import net.minecraft.entity.vehicle.*;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.derballo.chunkloadingminecarts.ChunkloadingMinecartsMod;
import net.derballo.chunkloadingminecarts.MinecartChunkloader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity implements MinecartChunkloader {
	@Unique
	private boolean isChunkLoader = false;
	@Unique
	private int chunkTicketExpiryTicks = 0;
	@Unique
	private ChunkPos lastChunkPos = null;

	public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	public void chunkloading_minecarts$toggleChunkLoader()
	{
		this.isChunkLoader = !this.isChunkLoader;
	}

	@Inject(method = "writeCustomData(Lnet/minecraft/storage/WriteView;)V", at = @At("RETURN"))
	private void writeCustomData(WriteView view, CallbackInfo ci) {
		view.putBoolean("chunkLoader", this.isChunkLoader);
	}

	@Inject(method = "readCustomData(Lnet/minecraft/storage/ReadView;)V", at = @At("RETURN"))
	private void onReadCustomData(ReadView view, CallbackInfo ci) {
		this.isChunkLoader = view.getBoolean("chunkLoader", false);
	}


	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		var world = this.getWorld();
		if (this.isChunkLoader && !world.isClient){
			var chunkPos = this.getChunkPos();
			if ((--this.chunkTicketExpiryTicks <= 0) || (this.lastChunkPos != chunkPos))
			{
				((ServerWorld)world).getChunkManager().addTicket(ChunkloadingMinecartsMod.MINECART, chunkPos, ChunkloadingMinecartsMod.MINECART_TICKET_RADIUS + 1);
				this.chunkTicketExpiryTicks = 5;
				this.lastChunkPos = chunkPos;
			}

			AbstractMinecartEntity entity = (AbstractMinecartEntity)(Object)this;
			((ServerWorld)world).spawnParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 0.1, entity.getZ(), 4, 0.1, 0.1, 0.1, 0.4f);
		}
	}
}
