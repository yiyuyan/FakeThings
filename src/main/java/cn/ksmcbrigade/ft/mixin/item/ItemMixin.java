package cn.ksmcbrigade.ft.mixin.item;

import cn.ksmcbrigade.ft.FakeThings;
import cn.ksmcbrigade.ft.entity.FakeBlockEntity;
import cn.ksmcbrigade.ft.interfaces.IBlockItem;
import cn.ksmcbrigade.ft.network.AddFakeBlockPacketMsg;
import cn.ksmcbrigade.ft.network.CAddFakeBlockPacketMsg;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/23 下午8:41
 */
@Mixin(BlockItem.class)
public class ItemMixin implements IBlockItem {
    @Unique
    private Block b;

    @Inject(method = "placeBlock",at = @At("TAIL"))
    private void place(BlockPlaceContext p_40578_, BlockState p_40579_, CallbackInfoReturnable<Boolean> cir){
        if(b!=null){
            p_40578_.getLevel().setBlockAndUpdate(p_40578_.getClickedPos(), FakeThings.FAKE_BLOCK.get().defaultBlockState());
            if(p_40578_.getLevel().getBlockEntity(p_40578_.getClickedPos()) instanceof FakeBlockEntity fakeBlockEntity){
                fakeBlockEntity.setBlock(b);
                try {
                    if(p_40578_.getLevel() instanceof ServerLevel level && ServerLifecycleHooks.getCurrentServer()==null){
                        try {
                            Field field = ServerLifecycleHooks.class.getDeclaredField("currentServer");
                            field.setAccessible(true);
                            field.set(null,level.getServer());
                        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |
                                 IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    if(p_40578_.getPlayer() instanceof ServerPlayer level && ServerLifecycleHooks.getCurrentServer()==null){
                        try {
                            Field field = ServerLifecycleHooks.class.getDeclaredField("currentServer");
                            field.setAccessible(true);
                            field.set(null,level.getServer());
                        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |
                                 IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    if(ServerLifecycleHooks.getCurrentServer()!=null)FakeThings.CHANNEL.send(PacketDistributor.ALL.noArg(), AddFakeBlockPacketMsg.create(p_40578_.getLevel(),p_40578_.getClickedPos(),b,false));
                    if(p_40578_.getLevel().isClientSide) FakeThings.CHANNEL.sendToServer(CAddFakeBlockPacketMsg.create(p_40578_.getLevel(),p_40578_.getClickedPos(),b,false));
                } catch (Exception e) {
                    FakeThings.LOGGER.error("Error in placing the fake block.",e);
                    p_40578_.getLevel().setBlockAndUpdate(p_40578_.getClickedPos(), Blocks.AIR.defaultBlockState());
                    if(p_40578_.getPlayer()!=null) p_40578_.getPlayer().displayClientMessage(Component.literal("Failed to place the block."+e.getMessage()),false);
                }
            }
        }
        b = null;
    }

    @Override
    @Unique
    public void setBlock(Block block) {
        this.b = block;
    }
}
