package lucaswarwick02;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VanillaOverhaulMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("vanilla-overhaul");
	public static VOConfig CONFIG;
	private static File CONFIG_PATH = FabricLoader.getInstance().getConfigDir().toFile();
	private static String CONFIG_FILE_NAME = "vanillaOverhaul.json";

	// Compressed Overworld Blocks
	public static final Item COMPRESSED_DIRT = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMPRESSED_COBBLESTONE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMPRESSED_COBBLED_DEEPSLATE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMPRESSED_GRANITE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMPRESSED_DIORITE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMPRESSED_ANDESITE = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMPRESSED_TUFF = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMPRESSED_SAND = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMPRESSED_GRAVEL = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	// Compressed Nether Blocks
	public static final Item COMPRESSED_NETHERRACK = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMPRESSED_BASALT = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Item COMPRESSED_BLACKSTONE = new Item(new FabricItemSettings().group(ItemGroup.MISC));

	// Loot Table Identifiers
	private static final Identifier WITHER_SKELETON_SKULL_LOOT_TABLE_ID = Blocks.COAL_ORE.getLootTableId();

	@Override
	public void onInitialize() {
		LoadConfig();

		// Compressed Overworld Blocks
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_dirt"), COMPRESSED_DIRT);
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_cobblestone"), COMPRESSED_COBBLESTONE);
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_cobbled_deepslate"), COMPRESSED_COBBLED_DEEPSLATE);
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_granite"), COMPRESSED_GRANITE);
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_diorite"), COMPRESSED_DIORITE);
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_andesite"), COMPRESSED_ANDESITE);
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_tuff"), COMPRESSED_TUFF);
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_sand"), COMPRESSED_SAND);
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_gravel"), COMPRESSED_GRAVEL);
		// Compressed Nether Blocks
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_netherrack"), COMPRESSED_NETHERRACK);
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_basalt"), COMPRESSED_BASALT);
		Registry.register(Registry.ITEM, new Identifier("vanilla-overhaul", "compressed_blackstone"), COMPRESSED_BLACKSTONE);

		// Loot Table Editing
		LootTableEvents.MODIFY.register(((resourceManager, lootManager, id, tableBuilder, source) -> {
			// Bonus Blaze Rod Chance from Blaze
			if (EntityType.BLAZE.getLootTableId().equals(id)) {
				LootPool.Builder skullPool = LootPool.builder().with(ItemEntry.builder(Items.BLAZE_ROD)).conditionally(RandomChanceLootCondition.builder(VanillaOverhaulMod.CONFIG.blazeRodChance));
				tableBuilder.pool(skullPool);
			}
			// Bonus Ender Pearl Chance from Enderman
			if (EntityType.ENDERMAN.getLootTableId().equals(id)) {
				LootPool.Builder skullPool = LootPool.builder().with(ItemEntry.builder(Items.ENDER_PEARL)).conditionally(RandomChanceLootCondition.builder(VanillaOverhaulMod.CONFIG.enderPearlChance));
				tableBuilder.pool(skullPool);
			}
			// Bonus Wither Skeleton Skull Chance from Wither Skeleton
			if (EntityType.WITHER_SKELETON.getLootTableId().equals(id)) {
				LootPool.Builder skullPool = LootPool.builder().with(ItemEntry.builder(Items.WITHER_SKELETON_SKULL)).conditionally(RandomChanceLootCondition.builder(VanillaOverhaulMod.CONFIG.witherSkeletonSkullChance));
				tableBuilder.pool(skullPool);
			}
		}));

	}

	/**
	 * Loads the configuration from a file
	 * Credits to Wutdahack's 'ActuallyUnbreaking' mod
	 */
	public static void LoadConfig () {
		File configFile = new File(CONFIG_PATH, CONFIG_FILE_NAME);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (configFile.exists()) {
			try {
				FileReader fileReader = new FileReader(configFile);
				CONFIG = gson.fromJson(fileReader, VOConfig.class);
				fileReader.close();
			} catch (IOException e) {
				LOGGER.error("Could not load Vanilla Overhaul configuration file.");
			}
		}
		else {
			CONFIG = new VOConfig();
			SaveConfig();
		}
	}

	/**
	 * Saves the current configuration to a file
	 * Credits to Wutdahack's 'ActuallyUnbreaking' mod
	 */
	public static void SaveConfig () {
		File configFile = new File(CONFIG_PATH, CONFIG_FILE_NAME);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (!configFile.getParentFile().exists()) {
			configFile.getParentFile().mkdir();
		}
		try {
			FileWriter fileWriter = new FileWriter(configFile);
			fileWriter.write(gson.toJson(CONFIG));
			fileWriter.close();
		} catch (IOException e) {
			LOGGER.error("Could not save Vanilla Overhaul configuration file.");
		}
	}
}
