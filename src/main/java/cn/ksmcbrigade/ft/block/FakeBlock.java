package cn.ksmcbrigade.ft.block;

/*
  &#064;Author: KSmc_brigade
  &#064;Date: 2025/8/23 下午8:03
  */

import cn.ksmcbrigade.ft.FakeThings;
import cn.ksmcbrigade.ft.entity.FakeBlockEntity;
import cn.ksmcbrigade.ft.network.AddFakeBlockPacketMsg;
import cn.ksmcbrigade.ft.network.CAddFakeBlockPacketMsg;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
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
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if(ServerLifecycleHooks.getCurrentServer()!=null)FakeThings.CHANNEL.send(PacketDistributor.ALL.noArg(),AddFakeBlockPacketMsg.create(player.level(),pos,state.getBlock(),true));
        if(level instanceof ClientLevel) FakeThings.CHANNEL.sendToServer(CAddFakeBlockPacketMsg.create(player.level(),pos,state.getBlock(),true));
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
