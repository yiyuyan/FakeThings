package cn.ksmcbrigade.ft.mixin.client.item;

import cn.ksmcbrigade.ft.items.FakeItem;
import cn.ksmcbrigade.ft.items.FakeTechItem;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/24 上午10:35
 */
@Mixin(ItemColors.class)
public abstract class ItemColorsMixin {
    @Shadow public abstract int getColor(ItemStack p_92677_, int p_92678_);

    @Inject(method = "getColor",at = @At("RETURN"),cancellable = true)
    private void model(ItemStack value, int p_92678_, CallbackInfoReturnable<Integer> cir){
        if(value.getItem() instanceof FakeItem fakeItem){
            ItemStack stack = fakeItem.getItemStack(value,0);
            if(!(stack.getItem() instanceof FakeTechItem)) cir.setReturnValue(getColor(stack,p_92678_));
        }
    }
}
