package io.nihlen.chunkloadingminecarts.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.vehicle.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.nihlen.chunkloadingminecarts.ChunkloadingMinecartsMod;
import io.nihlen.chunkloadingminecarts.MinecartChunkloader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity implements MinecartChunkloader {

	@Unique
	private boolean isChunkLoader = false;
	@Unique
	private int particleTicker = 0;
	@Unique
	private int chunkTicketExpiryTicks = 0;
	@Unique
	private ChunkPos lastChunkPos = null;

	public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;DDD)V", at = @At("TAIL"))
	private void injectConstructor(CallbackInfo callbackInfo) {
		updateChunkLoader();
	}

	public void chunkloading_minecarts$toggleChunkLoader()
	{
		this.isChunkLoader = !this.isChunkLoader;
		updateChunkLoader();
	}

	private void updateChunkLoader()
	{
		if(this.isChunkLoader)
		{
			var chunkLoaderName = "Chunk Loader";
			EntityType<?> minecartType = this.getType();
			if (minecartType == EntityType.CHEST_MINECART) {
				var entity = (ChestMinecartEntity)(Object)this;
				var firstSlot = entity.getInventory().getFirst();

				var hasCustomName = firstSlot.get(DataComponentTypes.CUSTOM_NAME) != null;

				if (!firstSlot.isEmpty() && hasCustomName) {
					chunkLoaderName = firstSlot.getName().getString();
				}
			} else if (minecartType == EntityType.HOPPER_MINECART) {
				var entity = (HopperMinecartEntity)(Object)this;
				var firstSlot = entity.getInventory().getFirst();

				var hasCustomName = firstSlot.get(DataComponentTypes.CUSTOM_NAME) != null;

				if (!firstSlot.isEmpty() && hasCustomName) {
					chunkLoaderName = firstSlot.getName().getString();
				}
			}

			var nameText = Text.literal(chunkLoaderName);
			this.setCustomName(nameText);
			this.setCustomNameVisible(true);
		}
		else
		{
			this.setCustomName(null);
			this.setCustomNameVisible(false);
			this.lastChunkPos = null;
			this.chunkTicketExpiryTicks = 0;
		}
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
	public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("chunkLoader", this.isChunkLoader);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
	public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		this.isChunkLoader = nbt.getBoolean("chunkLoader");
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		var world = this.getWorld();
		if (this.isChunkLoader && !world.isClient){
			var chunkPos = this.getChunkPos();
			if ((--this.chunkTicketExpiryTicks <= 0) || (this.lastChunkPos != chunkPos))
			{
				((ServerWorld)world).getChunkManager().addTicket(ChunkloadingMinecartsMod.MINECART, chunkPos, ChunkloadingMinecartsMod.MINECART_TICKET_RADIUS + 1, chunkPos);
				this.chunkTicketExpiryTicks = 5; //ChunkloadingMinecartsMod.MINECART_TICKET_EXPIRY;
				this.lastChunkPos = chunkPos;
			}

			this.particleTicker += 1;
			if (this.particleTicker >= 3/*particleInterval*/) {
				this.particleTicker = 0;
				AbstractMinecartEntity entity = (AbstractMinecartEntity)(Object)this;
				((ServerWorld)world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, entity.getX(), entity.getY(), entity.getZ(), 1, 0.25, 0.25, 0.25, 0.15f);
			}
		};
	}

//	@Override
//	public Entity teleportTo(TeleportTarget teleportTarget) {
//
//		var newEntity = super.teleportTo(teleportTarget);
//
//		if (newEntity != null)
//			((AbstractMinecartEntityMixin)newEntity).chunkloading_minecarts$startChunkLoader();
//
//		return newEntity;
//	}
}
