package cn.ksmcbrigade.ft.mixin.item;

import cn.ksmcbrigade.ft.items.FakeItem;
import cn.ksmcbrigade.ft.items.FakeTechItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/23 下午5:25
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Shadow public abstract int getDamageValue();

    @Shadow public abstract CompoundTag getOrCreateTag();

    @Inject(method = "getTooltipLines",at = @At("HEAD"),cancellable = true)
    public void tooltip(Player p_41652_, TooltipFlag p_41653_, CallbackInfoReturnable<List<Component>> cir){
        ItemStack stack = (ItemStack) (Object)this;
        if(getItem() instanceof FakeItem fakeItem){
            ItemStack i = fakeItem.getItemStack(stack,getDamageValue());
            if(!(i.getItem() instanceof FakeTechItem)){
                ArrayList<Component> components = new ArrayList<>();
                ArrayList<Component> tabName = new ArrayList<>();
                if(p_41652_.isCreative() && !i.isDamaged()){
                    for (Map.Entry<ResourceKey<CreativeModeTab>, CreativeModeTab> resourceKeyCreativeModeTabEntry : BuiltInRegistries.CREATIVE_MODE_TAB.entrySet()) {
                        CreativeModeTab tab = resourceKeyCreativeModeTabEntry.getValue();
                        if(tab!=null && tab!=CreativeModeTabs.searchTab() && !tab.getDisplayItems().stream().filter(f->f.getItem().equals(i.getItem())).toList().isEmpty()){
                            tabName.add(tab.getDisplayName().copy().withStyle(ChatFormatting.BLUE));
                        }
                    }
                }
                List<Component> c = (i.getTooltipLines(p_41652_, p_41653_));
                for (int i1 = 0; i1 < c.size(); i1++) {
                    if(i1==1 && tabName!=null){
                        components.addAll(tabName);
                    }
                    components.add(c.get(i1));
                }
                cir.setReturnValue(components);
            }
        }
    }

    @Inject(method = "setDamageValue",at = @At("TAIL"))
    private void damage(int p_41722_, CallbackInfo ci){
        if(getItem() instanceof FakeItem){
            getOrCreateTag().putInt("fake_damage",p_41722_);
        }
    }
}
