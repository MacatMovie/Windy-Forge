package xyz.bonfiremc.windy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class WindyConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static WindyConfig INSTANCE = new WindyConfig();

    public boolean spawnWind = true;
    public int minimumWindHeight = 83;
    public boolean windMustSeeSky = true;
    public double spawnRateMultiplier = 1.0D;

    public static WindyConfig get() {
        return INSTANCE;
    }

    public static void load() {
        Path path = WindyMod.getConfigPath();
        try {
            Files.createDirectories(path.getParent());
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path)) {
                    WindyConfig loaded = GSON.fromJson(reader, WindyConfig.class);
                    if (loaded != null) {
                        INSTANCE = loaded;
                    }
                }
            }
            INSTANCE.clamp();
            save();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Windy config", e);
        }
    }

    public static void save() {
        Path path = WindyMod.getConfigPath();
        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save Windy config", e);
        }
    }

    private void clamp() {
        if (minimumWindHeight < -64) minimumWindHeight = -64;
        if (minimumWindHeight > 320) minimumWindHeight = 320;
        if (spawnRateMultiplier < 0.0D) spawnRateMultiplier = 0.0D;
        if (spawnRateMultiplier > 100.0D) spawnRateMultiplier = 100.0D;
    }
}
