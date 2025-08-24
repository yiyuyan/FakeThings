package cn.ksmcbrigade.ft.network;

/*
  &#064;Author: KSmc_brigade
  &#064;Date: 2025/8/23 下午9:18
 */

import cn.ksmcbrigade.ft.FTClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClearFakeBlockPacketMsg() {

    public static void encode(ClearFakeBlockPacketMsg msg,FriendlyByteBuf buf) {}

    public static ClearFakeBlockPacketMsg decode(FriendlyByteBuf buf) {
        return new ClearFakeBlockPacketMsg();
    }

    public static void handle(ClearFakeBlockPacketMsg msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()-> FTClient.fakeBlocks.clear()));
        FTClient.fakeBlocks.clear();
        ctx.get().setPacketHandled(true);
    }
}
