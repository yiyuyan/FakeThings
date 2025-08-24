package cn.ksmcbrigade.ft;

import cn.ksmcbrigade.ft.block.FakeBlock;
import cn.ksmcbrigade.ft.entity.FakeBlockEntity;
import cn.ksmcbrigade.ft.items.FakeItem;
import cn.ksmcbrigade.ft.items.FakeTechItem;
import cn.ksmcbrigade.ft.network.AddFakeBlockPacketMsg;
import cn.ksmcbrigade.ft.network.ClearFakeBlockPacketMsg;
import cn.ksmcbrigade.ft.types.FakeBlockEntityType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingUseTotemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FakeThings.MODID)
public class FakeThings {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "ft";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "ft" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "ft" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "ft" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "ft:fake_block", combining the namespace and path
    public static final RegistryObject<Block> FAKE_BLOCK = BLOCKS.register("fake_block", () -> new FakeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // Creates a new BlockItem with the id "ft:fake_block", combining the namespace and path
    public static final RegistryObject<Item> FAKE_BLOCK_ITEM = ITEMS.register("fake_block", () -> new BlockItem(FAKE_BLOCK.get(), new Item.Properties()){
        @Override
        public void appendHoverText(@NotNull ItemStack p_40572_, @Nullable Level p_40573_, @NotNull List<Component> p_40574_, @NotNull TooltipFlag p_40575_) {
            p_40574_.add(Component.literal("DO NOT USE THIS BLOCK ITEM."));
        }
    });

    // Creates a new food item with the id "ft:fake_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> FAKE_ITEM = ITEMS.register("fake_item", FakeItem::new);
    public static final RegistryObject<Item> FAKE_TECH_ITEM = ITEMS.register("fake_tech_item", FakeTechItem::new);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES,MODID);
    public static final RegistryObject<FakeBlockEntityType> FAKE_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("fake_block_entity_type",()-> new FakeBlockEntityType(FakeBlockEntity::new, Set.copyOf(ForgeRegistries.BLOCKS.getValues()), Util.fetchChoiceType(References.BLOCK_ENTITY,"fake_block_entity_type")));

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation.tryBuild(MODID,"sync"),()->"345",(s)->true,(s)->true);

    // Creates a creative tab with the id "ft:fake_tab" for the fake item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> FAKE_TAB = CREATIVE_MODE_TABS.register("fake_tab", () -> CreativeModeTab.builder().withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> FAKE_ITEM.get().getDefaultInstance()).displayItems((parameters, output) -> {
        output.accept(FAKE_ITEM.get());
        output.accept(FAKE_BLOCK_ITEM.get());
    }).build());

    public FakeThings() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        CHANNEL.registerMessage(0, AddFakeBlockPacketMsg.class,AddFakeBlockPacketMsg::encode,AddFakeBlockPacketMsg::decode,AddFakeBlockPacketMsg::handle);
        CHANNEL.registerMessage(1, ClearFakeBlockPacketMsg.class,ClearFakeBlockPacketMsg::encode,ClearFakeBlockPacketMsg::decode,ClearFakeBlockPacketMsg::handle);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        LOGGER.info("{} Mod Loaded.",FakeThings.class.getName());
    }

    @SubscribeEvent
    public void onMine(LivingDestroyBlockEvent event){
        if(!event.isCanceled() && event.getState().getBlock() instanceof FakeBlock){
            CHANNEL.send(PacketDistributor.ALL.noArg(),new AddFakeBlockPacketMsg(event.getPos(),event.getState().getBlock(),true));
        }
    }

    @SubscribeEvent
    public void onMine(LivingUseTotemEvent event){
        if(event.getEntity() instanceof Player player){
            if(player.getItemInHand(player.getUsedItemHand()).getItem() instanceof FakeItem){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void load(LevelEvent.Load levelEvent){
        CHANNEL.send(PacketDistributor.ALL.noArg(),new ClearFakeBlockPacketMsg());
    }

    @SubscribeEvent
    public void unload(LevelEvent.Unload levelEvent){
        FTTemp.fakeBlocks.forEach((b, block)->levelEvent.getLevel().setBlock(b, Blocks.AIR.defaultBlockState(),11));
        CHANNEL.send(PacketDistributor.ALL.noArg(),new ClearFakeBlockPacketMsg());
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if(!(event.getEntity() instanceof ServerPlayer)) return;
        FTTemp.fakeBlocks.forEach((b,block)->CHANNEL.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer) event.getEntity()),new AddFakeBlockPacketMsg(b,block,false)));
    }

    @SubscribeEvent
    public void registerCommand(RegisterCommandsEvent event){
        event.getDispatcher().register(Commands.literal("give-fake").then(Commands.argument("item", ItemArgument.item(event.getBuildContext())).executes(context -> {
            return command(context,Collections.emptyList(),1,0);
        }).then(Commands.argument("count", IntegerArgumentType.integer(1)).executes(context -> {
            return command(context,Collections.emptyList(),IntegerArgumentType.getInteger(context,"count"),0);
        }).then(Commands.argument("damage",IntegerArgumentType.integer(0)).executes(context -> {
            return command(context,Collections.emptyList(),IntegerArgumentType.getInteger(context,"count"),IntegerArgumentType.getInteger(context,"damage"));
        }).then(Commands.argument("players",EntityArgument.players()).executes(context -> {
            return command(context,EntityArgument.getPlayers(context,"players"),IntegerArgumentType.getInteger(context,"count"),IntegerArgumentType.getInteger(context,"damage"));
        }))))));
    }

    private static int command(CommandContext<CommandSourceStack> context,Collection<ServerPlayer> players,int count,int damage) throws CommandSyntaxException {
        Player player = context.getSource().getPlayer();
        if(player == null && players.isEmpty()){
            context.getSource().sendFailure(Component.literal("Please use this command as a player."));
            return 1;
        }
        ItemStack fakeItem = new ItemStack(FAKE_ITEM.get());
        fakeItem.getOrCreateTag().putString("fake_item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ItemArgument.getItem(context, "item").getItem())).toString());
        fakeItem.getOrCreateTag().putInt("fake_damage",damage);
        if(players.isEmpty()){
            for (int i = 1; i <= count; i++) {
                context.getSource().getPlayerOrException().addItem(fakeItem.copy());
            }
        }
        else{
            for (ServerPlayer serverPlayer : players) {
                for (int i = 1; i <= count; i++) {
                    serverPlayer.addItem(fakeItem.copy());
                }
            }
        }
        return 0;
    }

    // Add the fake block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(FAKE_BLOCK_ITEM);
    }
}
