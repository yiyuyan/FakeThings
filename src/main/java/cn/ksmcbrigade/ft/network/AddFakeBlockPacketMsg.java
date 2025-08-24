package cn.ksmcbrigade.ft.network;

/*
  &#064;Author: KSmc_brigade
  &#064;Date: 2025/8/23 下午9:18
 */

import cn.ksmcbrigade.ft.FTClient;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Supplier;

public record AddFakeBlockPacketMsg(BlockPos pos, Block block,boolean remove) {

    public static void encode(AddFakeBlockPacketMsg msg,FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(msg.block)));
        buf.writeBoolean(msg.remove);
    }

    public static AddFakeBlockPacketMsg decode(FriendlyByteBuf buf) {
        return new AddFakeBlockPacketMsg(buf.readBlockPos(),ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation()),buf.readBoolean());
    }

    public static void handle(AddFakeBlockPacketMsg msg,Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()-> {
            if(!msg.remove){
                FTClient.fakeBlocks.put(msg.pos,msg.block);
            }
            else{
                FTClient.fakeBlocks.remove(msg.pos);
            }
        }));
        if(!msg.remove){
            FTClient.fakeBlocks.put(msg.pos,msg.block);
        }
        else{
            FTClient.fakeBlocks.remove(msg.pos);
        }
        ctx.get().setPacketHandled(true);
    }
}
