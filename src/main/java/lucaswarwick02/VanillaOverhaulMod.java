package lucaswarwick02;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VanillaOverhaulMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("vanilla-overhaul");

	// Compressed Overworld Blocks
	public static final Item COMPRESSED_DIRT = new Item(new FabricItemSettings());
	public static final Item COMPRESSED_COBBLESTONE = new Item(new FabricItemSettings());
	public static final Item COMPRESSED_COBBLED_DEEPSLATE = new Item(new FabricItemSettings());
	public static final Item COMPRESSED_GRANITE = new Item(new FabricItemSettings());
	public static final Item COMPRESSED_DIORITE = new Item(new FabricItemSettings());
	public static final Item COMPRESSED_ANDESITE = new Item(new FabricItemSettings());
	public static final Item COMPRESSED_TUFF = new Item(new FabricItemSettings());
	public static final Item COMPRESSED_SAND = new Item(new FabricItemSettings());
	public static final Item COMPRESSED_GRAVEL = new Item(new FabricItemSettings());
	// Compressed Nether Blocks
	public static final Item COMPRESSED_NETHERRACK = new Item(new FabricItemSettings());
	public static final Item COMPRESSED_BASALT = new Item(new FabricItemSettings());
	public static final Item COMPRESSED_BLACKSTONE = new Item(new FabricItemSettings());

	private static final ItemGroup VANILLA_OVERHAUL_GROUP = FabricItemGroup.builder()
			.icon( () -> new ItemStack(COMPRESSED_COBBLESTONE) )
			.displayName( Text.translatable("itemGroup.vanilla-overhaul.vo_group") )
			.build();

	@Override
	public void onInitialize() {
		VOConfig.LoadConfig();

		Registry.register( Registries.ITEM_GROUP, new Identifier("vanilla-overhaul", "vo_group") ,
				VANILLA_OVERHAUL_GROUP );

		RegisterItems();

		EditLootTables();
	}

	private static void RegisterItems () {
		// ### Compressed Overworld Blocks ###
		// Compressed Dirt
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_dirt"), COMPRESSED_DIRT);

		// Compressed Cobblestone
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_cobblestone"), COMPRESSED_COBBLESTONE);

		// Compressed Cobbled Deepslate
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_cobbled_deepslate"), COMPRESSED_COBBLED_DEEPSLATE);

		// Compressed Granite
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_granite"), COMPRESSED_GRANITE);

		// Compressed Diorite
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_diorite"), COMPRESSED_DIORITE);

		// Compressed Andesite
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_andesite"), COMPRESSED_ANDESITE);

		// Compressed Tuff
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_tuff"), COMPRESSED_TUFF);

		// Compressed Sand
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_sand"), COMPRESSED_SAND);

		// Compressed Gravel
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_gravel"), COMPRESSED_GRAVEL);

		// ### Compressed Nether Blocks ###
		// Compressed Netherrack
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_netherrack"), COMPRESSED_NETHERRACK);

		// Compressed Basalt
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_basalt"), COMPRESSED_BASALT);

		// Compressed Blackstone
		Registry.register(Registries.ITEM, new Identifier("vanilla-overhaul", "compressed_blackstone"), COMPRESSED_BLACKSTONE);

	}

	private static void EditLootTables () {
		LootTableEvents.MODIFY.register(((resourceManager, lootManager, id, tableBuilder, source) -> {
			// Bonus Blaze Rod Chance from Blaze
			if (EntityType.BLAZE.getLootTableId().equals(id)) {
				LootPool.Builder skullPool = LootPool.builder().with(ItemEntry.builder(Items.BLAZE_ROD)).conditionally(RandomChanceLootCondition.builder(VOConfig.CONFIG.blazeRodChance));
				tableBuilder.pool(skullPool);
			}
			// Bonus Ender Pearl Chance from Enderman
			if (EntityType.ENDERMAN.getLootTableId().equals(id)) {
				LootPool.Builder skullPool = LootPool.builder().with(ItemEntry.builder(Items.ENDER_PEARL)).conditionally(RandomChanceLootCondition.builder(VOConfig.CONFIG.enderPearlChance));
				tableBuilder.pool(skullPool);
			}
			// Bonus Wither Skeleton Skull Chance from Wither Skeleton
			if (EntityType.WITHER_SKELETON.getLootTableId().equals(id)) {
				LootPool.Builder skullPool = LootPool.builder().with(ItemEntry.builder(Items.WITHER_SKELETON_SKULL)).conditionally(RandomChanceLootCondition.builder(VOConfig.CONFIG.witherSkeletonSkullChance));
				tableBuilder.pool(skullPool);
			}
		}));
	}
}
