package cn.ksmcbrigade.ft.mixin.client.item;

import cn.ksmcbrigade.ft.items.FakeItem;
import cn.ksmcbrigade.ft.items.FakeTechItem;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/23 下午5:35
 */
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow public abstract BakedModel getModel(ItemStack p_174265_, @Nullable Level p_174266_, @Nullable LivingEntity p_174267_, int p_174268_);

    @ModifyVariable(method = "render", at = @At("HEAD"), index = 1, argsOnly = true)
    private ItemStack fakeItem(ItemStack value){
        if(value.getItem() instanceof FakeItem fakeItem){
            ItemStack stack = fakeItem.getItemStack(value,value.getDamageValue());
            if(!(stack.getItem() instanceof FakeTechItem)) return stack;
        }
        return value;
    }

    @Inject(method = "getModel",at = @At("RETURN"),cancellable = true)
    private void model(ItemStack value, Level p_174266_, LivingEntity p_174267_, int p_174268_, CallbackInfoReturnable<BakedModel> cir){
        if(value.getItem() instanceof FakeItem fakeItem){
            ItemStack stack = fakeItem.getItemStack(value,value.getDamageValue());
            if(!(stack.getItem() instanceof FakeTechItem)) cir.setReturnValue(getModel(stack,p_174266_,p_174267_,p_174268_));
        }
    }

}
