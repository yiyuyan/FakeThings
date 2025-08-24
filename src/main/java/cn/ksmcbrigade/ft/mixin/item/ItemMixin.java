package cn.ksmcbrigade.ft.mixin.item;

import cn.ksmcbrigade.ft.FakeThings;
import cn.ksmcbrigade.ft.entity.FakeBlockEntity;
import cn.ksmcbrigade.ft.interfaces.IBlockItem;
import cn.ksmcbrigade.ft.network.AddFakeBlockPacketMsg;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
                FakeThings.CHANNEL.send(PacketDistributor.ALL.noArg(), new AddFakeBlockPacketMsg(p_40578_.getClickedPos(),b,false));
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
