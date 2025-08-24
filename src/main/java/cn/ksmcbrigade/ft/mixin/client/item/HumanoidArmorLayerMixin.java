package cn.ksmcbrigade.ft.mixin.client.item;

import cn.ksmcbrigade.ft.items.FakeItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/24 上午10:49
 */
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }

    @Redirect(method = "renderArmorPiece",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"))
    public ItemStack getStack(LivingEntity instance, EquipmentSlot equipmentSlot){
        ItemStack result = instance.getItemBySlot(equipmentSlot);
        if(result.getItem() instanceof FakeItem fakeItem){
            ItemStack i = fakeItem.getItemStack(result,0);
            if(i.getItem() instanceof ArmorItem) return i;
        }
        return result;
    }

    @ModifyVariable(method = "getArmorResource", at = @At("HEAD"), index = 2, argsOnly = true,remap = false)
    public ItemStack fakeArmor(ItemStack value){
        if(value.getItem() instanceof FakeItem fakeItem){
            ItemStack i = fakeItem.getItemStack(value,0);
            if(i.getItem() instanceof ArmorItem) return i;
        }
        return value;
    }

    @ModifyVariable(method = "getArmorModelHook", at = @At("HEAD"), index = 2, argsOnly = true,remap = false)
    public ItemStack fakeArmorForged(ItemStack value){
        if(value.getItem() instanceof FakeItem fakeItem){
            ItemStack i = fakeItem.getItemStack(value,0);
            if(i.getItem() instanceof ArmorItem) return i;
        }
        return value;
    }

}
