package io.nihlen.chunkloadingminecarts.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.vehicle.*;
import net.minecraft.world.TeleportTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.nihlen.chunkloadingminecarts.ChunkloadingMinecartsMod;
import io.nihlen.chunkloadingminecarts.MinecartEntityExt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity implements MinecartEntityExt {

	@Unique
	private boolean isChunkLoader = false;
	@Unique
	private int particleTicker = 0;
	@Unique
	private final int particleInterval = 3;
	@Unique
	private ChunkPos lastChunkPos = null;

	public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;DDD)V", at = @At("TAIL"))
	private void injectConstructor(CallbackInfo callbackInfo) {
		if (isChunkLoader) {
			chunkloading_minecarts$startChunkLoader();
		}
	}

	public boolean chunkloading_minecarts$isChunkLoader() {
		return this.isChunkLoader;
	}

	public void chunkloading_minecarts$startChunkLoader() {
		if (this.getWorld().isClient) return;

		this.isChunkLoader = true;
	}

	public void chunkloading_minecarts$setChunkLoaderNameFromInventory() {
		EntityType<?> minecartType = this.getType();

		if (minecartType == EntityType.CHEST_MINECART) {
			//noinspection DataFlowIssue - We're sure this is a chest because of the if statement.
			var entity = (ChestMinecartEntity)(Object)this;
			var firstSlot = entity.getInventory().get(0);

			var hasCustomName = firstSlot.get(DataComponentTypes.CUSTOM_NAME) != null;
			
			if (!firstSlot.isEmpty() && hasCustomName) {
				var name = firstSlot.getName().getString();
				chunkloading_minecarts$setChunkLoaderName(name);
				return;
			}
		};

		chunkloading_minecarts$setChunkLoaderName("Chunk Loader");
	}

	public void chunkloading_minecarts$setChunkLoaderName(String name) {
		var nameText = Text.literal(name);
		this.setCustomName(nameText);
		this.setCustomNameVisible(true);
	}

	public void chunkloading_minecarts$stopChunkLoader() {
		chunkloading_minecarts$stopChunkLoader(false);
		this.lastChunkPos = null;
	}
	public void chunkloading_minecarts$stopChunkLoader(Boolean keepName) {
		this.isChunkLoader = false;

		ChunkloadingMinecartsMod.CHUNK_LOADER_MANAGER.removeChunkLoader(this);

		if (!keepName) {
			this.setCustomName(null);
			this.setCustomNameVisible(false);
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
		if (!isChunkLoader) return;

		var chunkPos = this.getChunkPos();
		if (lastChunkPos == null || lastChunkPos != chunkPos) {
			lastChunkPos = chunkPos;
			ChunkloadingMinecartsMod.CHUNK_LOADER_MANAGER.registerChunkLoader(this);
		}

		this.tickParticles();
	}

	@Override
	public void remove(Entity.RemovalReason reason) {
		if (isChunkLoader) {
			this.chunkloading_minecarts$stopChunkLoader();
		}

		super.remove(reason);
	}

	@Override
	public Entity teleportTo(TeleportTarget teleportTarget) {
		var wasChunkLoader = isChunkLoader;
		if (wasChunkLoader)
			this.chunkloading_minecarts$stopChunkLoader(true);

		var newEntity = super.teleportTo(teleportTarget);

		if (wasChunkLoader && newEntity != null)
			((AbstractMinecartEntityMixin)newEntity).chunkloading_minecarts$startChunkLoader();

		return newEntity;
	}

	@Unique
	private void tickParticles() {
		this.particleTicker += 1;
		if (this.particleTicker >= particleInterval) {
			this.particleTicker = 0;
			this.spawnParticles();
		}
	}

	@Unique
	private void spawnParticles() {
		AbstractMinecartEntity entity = (AbstractMinecartEntity)(Object)this;
		ServerWorld world = (ServerWorld)entity.getWorld();
		world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, entity.getX(), entity.getY(), entity.getZ(), 1, 0.25, 0.25, 0.25, 0.15f);
	}
}
