package cn.ksmcbrigade.ft.items;

import net.minecraft.world.item.*;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/23 下午4:10
 */
public class FakeTechItem extends Item implements Vanishable{

    public FakeTechItem() {
        super(new Properties().durability(Integer.MAX_VALUE));
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }
}
