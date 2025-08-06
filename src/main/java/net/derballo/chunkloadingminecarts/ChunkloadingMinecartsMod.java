package net.derballo.chunkloadingminecarts;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.util.math.*;
import net.minecraft.block.DispenserBlock;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.*;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChunkloadingMinecartsMod implements ModInitializer {
    public static final String MODID = "chunkloadingminecarts";

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

    public static final ChunkTicketType MINECART = Registry.register(Registries.TICKET_TYPE, "minecart", new ChunkTicketType(5, false, ChunkTicketType.Use.LOADING_AND_SIMULATION));

    @Override
    public void onInitialize() {
        DispenserBlock.registerBehavior(Items.GLOWSTONE, new FallibleItemDispenserBehavior() {
            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                if(hasValidPattern(pointer.blockEntity())) {
                    toggleChunkLoaders(pointer);
                    return stack;
                } else {
                    BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
                    World world = pointer.world();
                    BlockState blockState = world.getBlockState(blockPos);
                    this.setSuccess(true);
                    if (blockState.isOf(Blocks.RESPAWN_ANCHOR)) {
                        if (blockState.get(RespawnAnchorBlock.CHARGES) != 4) {
                            RespawnAnchorBlock.charge(null, world, blockPos, blockState);
                            stack.decrement(1);
                        } else {
                            this.setSuccess(false);
                        }

                        return stack;
                    } else {
                        return super.dispenseSilently(pointer, stack);
                    }
                }
            }
        });

        DispenserBlock.registerBehavior(Items.AMETHYST_SHARD, new FallibleItemDispenserBehavior() {
            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                if(hasValidPattern(pointer.blockEntity())) {
                    toggleChunkLoaders(pointer);
                    return stack;
                } else {
                    return super.dispenseSilently(pointer, stack);
                }
            }
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            //MINECART_TICKET_EXPIRY = server.getOverworld().getGameRules().getInt(MINECART_TICKET_EXPIRY_RULE);
            MINECART_TICKET_RADIUS = server.getOverworld().getGameRules().getInt(MINECART_TICKET_RADIUS_RULE);
        });
    }

    private boolean hasValidPattern(DispenserBlockEntity blockEntity){
        return blockEntity.getStack(0).isOf(Items.AIR) && blockEntity.getStack(1).isOf(Items.AMETHYST_SHARD) && blockEntity.getStack(2).isOf(Items.AIR)
                && blockEntity.getStack(3).isOf(Items.AMETHYST_SHARD) && blockEntity.getStack(4).isOf(Items.GLOWSTONE) && blockEntity.getStack(5).isOf(Items.AMETHYST_SHARD)
                && blockEntity.getStack(6).isOf(Items.AIR) && blockEntity.getStack(7).isOf(Items.AMETHYST_SHARD) && blockEntity.getStack(8).isOf(Items.AIR);
    }

    private void toggleChunkLoaders(BlockPointer pointer){
        for (AbstractMinecartEntity entity : pointer.world().getEntitiesByClass(AbstractMinecartEntity.class, new Box(pointer.pos().offset(pointer.state().get(DispenserBlock.FACING))), EntityPredicates.VALID_ENTITY)) {
            ((MinecartChunkloader)entity).chunkloading_minecarts$toggleChunkLoader();
        }
    }
}
