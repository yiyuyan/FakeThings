package cn.ksmcbrigade.ft.network;

/*
  &#064;Author: KSmc_brigade
  &#064;Date: 2025/8/23 下午9:18
 */

import cn.ksmcbrigade.ft.FTTemp;
import cn.ksmcbrigade.ft.FakeThings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

public record CAddFakeBlockPacketMsg(ResourceKey<Level> levelResourceKey, BlockPos pos, Block block, boolean remove) {

    public static CAddFakeBlockPacketMsg create(Level level, BlockPos pos, Block block, boolean remove){
        return new CAddFakeBlockPacketMsg(level.dimension(),pos,block,remove);
    }

    public static void encode(CAddFakeBlockPacketMsg msg, FriendlyByteBuf buf) {
        buf.writeResourceKey(msg.levelResourceKey);
        buf.writeBlockPos(msg.pos);
        buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(msg.block)));
        buf.writeBoolean(msg.remove);
    }

    public static CAddFakeBlockPacketMsg decode(FriendlyByteBuf buf) {
        return new CAddFakeBlockPacketMsg(buf.readResourceKey(Registries.DIMENSION),buf.readBlockPos(),ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation()),buf.readBoolean());
    }

    public static void handle(CAddFakeBlockPacketMsg msg, Supplier<NetworkEvent.Context> ctx) {
        /*ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()-> {
            if(!msg.remove){
                FTTemp.fakeBlocks.put(msg.pos,msg.block);
            }
            else{
                FTTemp.fakeBlocks.remove(msg.pos);
            }
        }));*/
        if(!FTTemp.fakeBlocks.containsKey(msg.levelResourceKey)) FTTemp.fakeBlocks.put(msg.levelResourceKey,new HashMap<>());
        if(ctx.get().getSender()==null){
            FakeThings.LOGGER.error("Can't find the server player.");
            ctx.get().setPacketHandled(true);
        }
        HashMap<BlockPos,Block> map = (HashMap<BlockPos, Block>) FTTemp.fakeBlocks.get(msg.levelResourceKey).clone();
        if(!msg.remove){
            map.put(msg.pos,msg.block);
            FakeThings.CHANNEL.send(PacketDistributor.PLAYER.with(()->ctx.get().getSender()),new AddFakeBlockPacketMsg(msg.levelResourceKey,msg.pos,msg.block,false));
            //System.out.println("CRECIVED "+msg.levelResourceKey + " "+msg.pos+" "+msg.block);
        }
        else{
            map.remove(msg.pos);
            FakeThings.CHANNEL.send(PacketDistributor.PLAYER.with(()->ctx.get().getSender()),new AddFakeBlockPacketMsg(msg.levelResourceKey,msg.pos,msg.block,true));
           /* StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            new Exception("eerc").printStackTrace(printWriter);
            System.out.println(writer);*/
        }
        FTTemp.fakeBlocks.put(msg.levelResourceKey,map);
        FTTemp.fakeBlocks.forEach((levelResourceKey1, blockPosBlockHashMap) -> blockPosBlockHashMap.forEach((blockPos, block1) -> System.out.println(blockPos+" "+block1)));
        ctx.get().setPacketHandled(true);
    }
}
