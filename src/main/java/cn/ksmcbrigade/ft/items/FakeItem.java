package cn.ksmcbrigade.ft.items;

import cn.ksmcbrigade.ft.FakeThings;
import cn.ksmcbrigade.ft.interfaces.IBlockItem;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/23 下午4:10
 */
public class FakeItem extends Item implements Vanishable{

    public FakeItem() {
        super(new Properties().durability(Integer.MAX_VALUE));
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().isEnchantable(i);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().canElytraFly(i,entity);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().canEquip(i,armorType,entity);
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().getArmorTexture(i,entity,slot,type);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().getUseAnimation(i);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().getRarity(i);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        ItemStack s = getItemStack(stack,stack.getDamageValue());
        if(s.getItem() instanceof FakeTechItem){
            return Component.translatable("item.ft.fake_item");
        }
        else{
            return s.getItem().getName(s);
        }
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().getMaxStackSize(i);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().getUseDuration(i);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().onEntitySwing(i,entity);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        stack.getOrCreateTag().putInt("fake_damage",amount);
        return i.getItem().damageItem(i,amount,entity,onBroken);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        stack.getOrCreateTag().putInt("fake_damage",damage);
        i.getItem().setDamage(i,damage);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().getMaxDamage(i);
    }

    @Override
    public Component getHighlightTip(ItemStack item, Component displayName) {
        ItemStack i = getItemStack(item,item.getDamageValue());
        if(i.getItem() instanceof FakeTechItem) return Component.translatable("item.ft.fake_item");
        return i.getItem().getHighlightTip(i,displayName);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().isBarVisible(i);
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().getBurnTime(i,recipeType);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().canPerformAction(i,toolAction);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack p_41421_, @Nullable Level p_41422_, @NotNull List<Component> p_41423_, @NotNull TooltipFlag p_41424_) {
        ItemStack stack = getItemStack(p_41421_,p_41421_.getDamageValue());
        if(stack.getItem() instanceof FakeTechItem){
            p_41423_.add(Component.literal("RickRoll!"));
        }
        else{
            stack.getItem().appendHoverText(stack, p_41422_, p_41423_, p_41424_);
        }
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        if(i.getItem().getFoodProperties(i,entity)!=null) return new FoodProperties.Builder().build();
        return null;
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().getDefaultTooltipHideFlags(i);
    }

    @Override
    public boolean canGrindstoneRepair(ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().canGrindstoneRepair(i);
    }

    @Override
    public int getDamage(ItemStack stack) {
        ItemStack i = getItemStack(stack,0);
        return i.getItem().getDamage(i);
    }

    public ItemStack getItemStack(ItemStack instance, int damage){
        CompoundTag tag = instance.getTag();
        if (tag != null && tag.contains("fake_item")) {
            Optional<Holder<Item>> item = ForgeRegistries.ITEMS.getHolder(ResourceLocation.tryParse(tag.getString("fake_item")));
            if (item.isPresent()){
                ItemStack stack = item.get().value().getDefaultInstance();
                if(tag.contains("fake_damage")){
                    stack.setDamageValue(tag.getInt("fake_damage"));
                }
                getAllEnchantments(instance).forEach(stack::enchant);
                return stack;
            }
        }
        return new ItemStack(FakeThings.FAKE_TECH_ITEM.get());
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().isBookEnchantable(i,book);
    }

    @Override
    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().isNotReplaceableByPickAction(i,player,inventorySlot);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().isDamageable(i);
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack stack) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().isRepairable(i);
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack stack2) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().isRepairable(i);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        ItemStack i = getItemStack(stack,stack.getDamageValue());
        return i.getItem().canApplyAtEnchantingTable(i, enchantment);
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState p_41441_, @NotNull Level p_41442_, @NotNull BlockPos p_41443_, Player p_41444_) {
        ItemStack stack = p_41444_.getItemInHand(p_41444_.getUsedItemHand());
        if(stack.getItem() instanceof FakeItem){
            ItemStack i = getItemStack(stack,stack.getDamageValue());
            return i.getItem().canAttackBlock(p_41441_, p_41442_, p_41443_, p_41444_);
        }
        return true;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext p_41427_) {
        ItemStack stack = p_41427_.getItemInHand();
        if(stack.getItem() instanceof FakeItem fakeItem){
            ItemStack i = fakeItem.getItemStack(stack,stack.getDamageValue());
            if(i.getItem() instanceof BlockItem blockItem){
                Block block = blockItem.getBlock();
                if(block != Blocks.AIR) {
                    Item item = Item.byBlock(block);
                    if(item instanceof BlockItem blockItem1){
                        ((IBlockItem) blockItem1).setBlock(block);
                    }
                    return item.useOn(p_41427_);
                }
            }
        }
        return super.useOn(p_41427_);
    }
}
