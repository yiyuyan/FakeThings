package cn.ksmcbrigade.ft.types;

import cn.ksmcbrigade.ft.entity.FakeBlockEntity;
import com.mojang.datafixers.types.Type;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/23 下午8:06
 */
public class FakeBlockEntityType extends BlockEntityType<FakeBlockEntity> {
    public FakeBlockEntityType(BlockEntitySupplier<? extends FakeBlockEntity> p_155259_, Set<Block> p_155260_, Type<?> p_155261_) {
        super(p_155259_, p_155260_, p_155261_);
    }
}
