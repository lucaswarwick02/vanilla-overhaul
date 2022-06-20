package lucaswarwick02;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VanillaOverhaulMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("vanilla-overhaul");
	public static VOConfig CONFIG;
	private static File CONFIG_PATH = FabricLoader.getInstance().getConfigDir().toFile();
	private static String CONFIG_FILE_NAME = "vanillaOverhaul.json";

	@Override
	public void onInitialize() {
		LoadConfig();
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
