package cn.ksmcbrigade.ft.mixin.client.signs;

import cn.ksmcbrigade.ft.items.FakeItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/24 上午11:56
 */
@Mixin(CustomHeadLayer.class)
public abstract class CustomHeadRendererMixin<T extends LivingEntity, M extends EntityModel<T> & HeadedModel> extends RenderLayer<T, M> {
    public CustomHeadRendererMixin(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack init(LivingEntity instance, EquipmentSlot equipmentSlot){
        ItemStack result = instance.getItemBySlot(equipmentSlot);
        if(result.getItem() instanceof FakeItem fakeItem){
            if(fakeItem.getItemStack(result,0).getItem() instanceof ArmorItem) return new ItemStack(Items.AIR);
        }
        return result;
    }
}
