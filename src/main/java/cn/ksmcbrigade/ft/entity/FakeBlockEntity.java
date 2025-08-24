package cn.ksmcbrigade.ft.entity;

import cn.ksmcbrigade.ft.FakeThings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/23 下午8:07
 */
public class FakeBlockEntity extends BlockEntity {

    public Block block;

    public FakeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(FakeThings.FAKE_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
        this.block = p_155230_.getBlock();
    }

    public void setBlock(Block block){
        this.block = block;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        ResourceLocation blockHolder = ForgeRegistries.BLOCKS.getKey(block);
        if(blockHolder!=null)p_187471_.putString("fake_block", blockHolder.toString());
    }

    @Override
    public void load(@NotNull CompoundTag p_155245_) {
        super.load(p_155245_);
        if(p_155245_.contains("fake_block")){
            String res = p_155245_.getString("fake_block");
            Optional<Holder<Block>> blockHolder = ForgeRegistries.BLOCKS.getHolder(ResourceLocation.tryParse(res));
            blockHolder.ifPresent(holder -> this.block = holder.get());
        }
    }
}
