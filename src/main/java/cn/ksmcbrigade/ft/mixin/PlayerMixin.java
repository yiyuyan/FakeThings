package cn.ksmcbrigade.ft.mixin;

import cn.ksmcbrigade.ft.items.FakeItem;
import cn.ksmcbrigade.ft.items.FakeTechItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/23 下午5:28
 */
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @ModifyArg(index = 1,method = "attack",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private float getHurtValue(float v){
        ItemStack stack = this.getItemInHand(this.getUsedItemHand());
        if(stack.getItem() instanceof FakeItem fakeItem && !(fakeItem.getItemStack(stack,stack.getDamageValue()).getItem() instanceof FakeTechItem)){
            return 0;
        }
        return v;
    }

    @ModifyArg(index = 1,method = "attack",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private float getHurtValue2(float v){
        ItemStack stack = this.getItemInHand(this.getUsedItemHand());
        if(stack.getItem() instanceof FakeItem fakeItem && !(fakeItem.getItemStack(stack,stack.getDamageValue()).getItem() instanceof FakeTechItem)){
            return 0;
        }
        return v;
    }
}
