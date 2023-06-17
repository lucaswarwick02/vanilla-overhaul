package lucaswarwick02;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

public class VOConfig {

    private static final File CONFIG_PATH = FabricLoader.getInstance().getConfigDir().toFile();
    private static final String CONFIG_FILE_NAME = "vanillaOverhaul.json";

    public static VOConfig CONFIG;

    public boolean unbreakableItems = false;

    public float blazeRodChance = 0.5f;
    public float enderPearlChance = 0.5f;
    public float witherSkeletonSkullChance = 0.1f;

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
            } catch ( IOException e) {
                VanillaOverhaulMod.LOGGER.error("Could not load Vanilla Overhaul configuration file.");
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
            VanillaOverhaulMod.LOGGER.error("Could not save Vanilla Overhaul configuration file.");
        }
    }

}
