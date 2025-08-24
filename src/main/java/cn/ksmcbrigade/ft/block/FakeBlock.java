package cn.ksmcbrigade.ft.block;

/*
  &#064;Author: KSmc_brigade
  &#064;Date: 2025/8/23 下午8:03
  */

import cn.ksmcbrigade.ft.FakeThings;
import cn.ksmcbrigade.ft.entity.FakeBlockEntity;
import cn.ksmcbrigade.ft.network.AddFakeBlockPacketMsg;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeBlock extends Block implements EntityBlock {
    public FakeBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos p_153215_, @NotNull BlockState p_153216_) {
        return new FakeBlockEntity(p_153215_,p_153216_);
    }

    @Override
    public void destroy(@NotNull LevelAccessor p_49860_, @NotNull BlockPos p_49861_, @NotNull BlockState p_49862_) {
        FakeThings.CHANNEL.send(PacketDistributor.ALL.noArg(),new AddFakeBlockPacketMsg(p_49861_,p_49862_.getBlock(),true));
        super.destroy(p_49860_, p_49861_, p_49862_);
    }

    @Override
    public void playerWillDestroy(@NotNull Level p_49852_, @NotNull BlockPos p_49853_, @NotNull BlockState p_49854_, @NotNull Player p_49855_) {
        FakeThings.CHANNEL.send(PacketDistributor.ALL.noArg(),new AddFakeBlockPacketMsg(p_49853_,p_49854_.getBlock(),true));
        super.playerWillDestroy(p_49852_, p_49853_, p_49854_, p_49855_);
    }

    @Override
    public void playerDestroy(@NotNull Level p_49827_, @NotNull Player p_49828_, @NotNull BlockPos p_49829_, @NotNull BlockState p_49830_, @Nullable BlockEntity p_49831_, @NotNull ItemStack p_49832_) {
        FakeThings.CHANNEL.send(PacketDistributor.ALL.noArg(),new AddFakeBlockPacketMsg(p_49829_,p_49830_.getBlock(),true));
        super.playerDestroy(p_49827_, p_49828_, p_49829_, p_49830_, p_49831_, p_49832_);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        FakeThings.CHANNEL.send(PacketDistributor.ALL.noArg(),new AddFakeBlockPacketMsg(pos,state.getBlock(),true));
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
