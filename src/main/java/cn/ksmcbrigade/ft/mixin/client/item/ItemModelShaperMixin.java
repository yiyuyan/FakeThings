package cn.ksmcbrigade.ft.mixin.client.item;

import cn.ksmcbrigade.ft.items.FakeItem;
import cn.ksmcbrigade.ft.items.FakeTechItem;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/24 上午10:23
 */
@Mixin(ItemModelShaper.class)
public abstract class ItemModelShaperMixin {


    @Shadow public abstract BakedModel getItemModel(ItemStack p_109407_);

    @Inject(method = "getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;",at = @At("RETURN"),cancellable = true)
    private void model(ItemStack value, CallbackInfoReturnable<BakedModel> cir){
        if(value.getItem() instanceof FakeItem fakeItem){
            ItemStack stack = fakeItem.getItemStack(value,0);
            if(!(stack.getItem() instanceof FakeTechItem)) cir.setReturnValue(getItemModel(stack));
        }
    }

}
