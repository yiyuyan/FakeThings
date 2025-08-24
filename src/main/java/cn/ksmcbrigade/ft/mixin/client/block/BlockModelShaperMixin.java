package cn.ksmcbrigade.ft.mixin.client.block;

import cn.ksmcbrigade.ft.FTClient;
import cn.ksmcbrigade.ft.FakeThings;
import cn.ksmcbrigade.ft.block.FakeBlock;
import cn.ksmcbrigade.ft.entity.FakeBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/23 下午8:22
 */
@Mixin(BlockRenderDispatcher.class)
public abstract class BlockModelShaperMixin {

    @Shadow public abstract void onResourceManagerReload(ResourceManager p_110909_);

    @Unique
    private Block fakethings$fakeBlock;

    @Inject(
            method = "renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V"
            , at = @At("HEAD"),remap = false)
    public void model(BlockState p_234356_, BlockPos p_234357_, BlockAndTintGetter p_234358_, PoseStack p_234359_, VertexConsumer p_234360_, boolean p_234361_, RandomSource p_234362_, ModelData modelData, RenderType renderType, CallbackInfo ci){
        if(p_234358_.getBlockEntity(p_234357_) instanceof FakeBlockEntity fakeBlockEntity){
            fakethings$fakeBlock = fakeBlockEntity.block;
        }
        else{
            fakethings$fakeBlock = FTClient.fakeBlocks.getOrDefault(p_234357_, null);
            if(p_234356_.getBlock() instanceof FakeBlock && fakethings$fakeBlock==null) fakethings$fakeBlock = FakeThings.FAKE_BLOCK.get();
        }
    }

    @Redirect(
            method = "renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/BakedModel;"))
    public BakedModel getModel(BlockRenderDispatcher instance, BlockState p_110911_){
        BlockState state = p_110911_;
        try {
            if(fakethings$fakeBlock!=null) state = fakethings$fakeBlock.defaultBlockState();
        } catch (Exception e) {
            FakeThings.LOGGER.error("Can't render the block",e);
            this.onResourceManagerReload(Minecraft.getInstance().getResourceManager());
        }
        return instance.getBlockModel(state);
    }
}
