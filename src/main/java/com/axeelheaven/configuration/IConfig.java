package com.axeelheaven.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public interface IConfig {

    Object get(String path);

    <T> Object get(String path, T def);

    boolean contains(String path);

    void addDefault(String path, Object defaultValue);

    void addDefault(String path, Object defaultValue, String... comments);

    Object getDefault(String path);

    void set(String path, Object value);

    void set(String path, Object value, String... comments);

    //getSection?

    List<String> getComments(String path);

    Collection<String> getKeys();

    Collection<String> getKeys(boolean deep);

    byte getByte(String path);

    byte getByte(String path, byte def);

    List<Byte> getByteList(String path);

    short getShort(String path);

    short getShort(String path, short def);

    List<Short> getShortList(String path);

    int getInt(String path);

    int getInt(String path, int def);

    List<Integer> getIntList(String path);

    long getLong(String path);

    long getLong(String path, long def);

    List<Long> getLongList(String path);

    float getFloat(String path);

    float getFloat(String path, float def);

    List<Float> getFloatList(String path);

    double getDouble(String path);

    double getDouble(String path, double def);

    List<Double> getDoubleList(String path);

    boolean getBoolean(String path);

    boolean getBoolean(String path, boolean def);

    List<Boolean> getBooleanList(String path);

    char getChar(String path);

    char getChar(String path, char def);

    List<Character> getCharList(String path);

    String getString(String path);

    String getString(String path, String def);

    List<String> getStringList(String path);


    /*------------------------------------------------------------------------*/
    List<?> getList(String path);

    List<?> getList(String path, List<?> def);

    boolean isConfigurationSection(String path);

    ConfigurationSection getConfigurationSection(String path);

    ConfigurationSection getConfigurationSection(String path, String def);

    boolean isSet(String path);

    FileConfigurationOptions options();

    FileConfiguration getConfig();

    ItemStack getItemStack(String path);

    ItemStack getItemStack(String path, ItemStack def);

    boolean isItemStack(String path);
}
