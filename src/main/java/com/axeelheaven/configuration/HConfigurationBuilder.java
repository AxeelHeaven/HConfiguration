package com.axeelheaven.configuration;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class HConfigurationBuilder extends HConfiguration {

    private YamlConfiguration config;

    public HConfigurationBuilder(File file, Plugin plugin) throws ConfigurationException {
        super(file, plugin);
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public HConfiguration save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.save();
        return this;
    }

    @Override
    public void load() throws ConfigurationException {
        try {
            config = this.loadConfigurationThrows(file);
            config.options().copyDefaults(true);
        } catch (IOException | InvalidConfigurationException e) {
            throw new ConfigurationException("Can't load configuration " + this.file.getName(), e.getCause());
        }
        super.load();
    }

    @Override
    public Object get(String path) {
        return config.get(path);
    }

    @Override
    public <T> T get(String path, T def) {
        return (T) config.get(path, def);
    }

    @Override
    public boolean contains(String path) {
        return config.isSet(path);
    }

    @Override
    public void addDefault(String path, Object defaultValue) {
        config.addDefault(path, defaultValue);
    }

    @Override
    public void addDefault(String path, Object defaultValue, String... comments) {
        super.addDefault(path, defaultValue, comments);
        config.addDefault(path, defaultValue);
    }

    @Override
    public Object getDefault(String path) {
        return config.getDefaults().get(path);
    }

    @Override
    public void set(String path, Object value) {
        super.set(path, value);
        config.set(path, value);
    }

    @Override
    public void set(String path, Object value, String... comments) {
        super.set(path, value, comments);
        config.set(path, value);
    }

    @Override
    public Collection<String> getKeys() {
        return config.getKeys(false);
    }

    @Override
    public Collection<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    @Override
    public byte getByte(String path) {
        return (byte) config.get(path);
    }

    @Override
    public byte getByte(String path, byte def) {
        return (byte) config.get(path, def);
    }

    @Override
    public List<Byte> getByteList(String path) {
        return config.getByteList(path);
    }

    @Override
    public short getShort(String path) {
        return (short) config.get(path);
    }

    @Override
    public short getShort(String path, short def) {
        return (short) config.get(path, def);
    }

    @Override
    public List<Short> getShortList(String path) {
        return config.getShortList(path);
    }

    @Override
    public int getInt(String path) {
        return config.getInt(path);
    }

    @Override
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    @Override
    public List<Integer> getIntList(String path) {
        return config.getIntegerList(path);
    }

    @Override
    public long getLong(String path) {
        return config.getLong(path);
    }

    @Override
    public long getLong(String path, long def) {
        return config.getLong(path, def);
    }

    @Override
    public List<Long> getLongList(String path) {
        return config.getLongList(path);
    }

    @Override
    public float getFloat(String path) {
        return (float) config.get(path);
    }

    @Override
    public float getFloat(String path, float def) {
        return (float) config.get(path, def);
    }

    @Override
    public List<Float> getFloatList(String path) {
        return config.getFloatList(path);
    }

    @Override
    public double getDouble(String path) {
        return config.getDouble(path);
    }

    @Override
    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return config.getDoubleList(path);
    }

    @Override
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        return config.getBooleanList(path);
    }

    @Override
    public char getChar(String path) {
        return (char) config.get(path);
    }

    @Override
    public char getChar(String path, char def) {
        return (char) config.get(path, def);
    }

    @Override
    public List<Character> getCharList(String path) {
        return config.getCharacterList(path);
    }

    @Override
    public String getString(String path) {
        return config.getString(path);
    }

    @Override
    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    @Override
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    @Override
    public List<?> getList(String path) {
        return config.getList(path);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        return config.getList(path, def);
    }

    @Override
    public boolean isConfigurationSection(String path) {
        return config.isConfigurationSection(path);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path, String def) {
        if (config.isConfigurationSection(path))
            return config.getConfigurationSection(path);
        return config.getConfigurationSection(def);
    }

    @Override
    public boolean isSet(String path) {
        return config.isSet(path);
    }

    @Override
    public FileConfigurationOptions options() {
        return config.options();
    }

    @Override
    public ItemStack getItemStack(String path) {
        return config.getItemStack(path);
    }

    @Override
    public ItemStack getItemStack(String path, ItemStack def) {
        return config.getItemStack(path, def);
    }

    @Override
    public boolean isItemStack(String path) {
        return config.isItemStack(path);
    }







    private YamlConfiguration loadConfigurationThrows(File file) throws IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        config.load(file);

        return config;
    }

}
