package com.axeelheaven.configuration;

import lombok.Setter;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.*;

public abstract class HConfiguration implements IConfig {

    protected File file;

    protected Map<String, List<String>> comments = new LinkedHashMap<>();

    @Setter
    private boolean newLinePerKey = true;

    HConfiguration(File file, Plugin plugin) throws ConfigurationException {
        if(!file.exists()) {
            plugin.saveResource(file.getName(), true);
        }
        this.file = file;
        this.load();
    }

    public void reload() {
        try {
            this.load();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public HConfiguration save() {
        List<String> configContent = getConfigContent();
        try (BufferedWriter configWriter = new BufferedWriter(new FileWriter(file))) {
            configWriter.write("");
            for (int lineIndex = 0; lineIndex < configContent.size(); lineIndex++) {
                String configLine = this.replaceComillas(configContent.get(lineIndex));

                //Check if is a config key
                String configKey = null;
                if (!configLine.startsWith("#") && configLine.contains(":") && !this.isStringListFirst(configLine)) {
                    configKey = getPathToKey(configContent, lineIndex, configLine).replaceAll("'", "");
                }

                //If is a config key, check if has comments to be added
                if (configKey != null && this.comments.containsKey(configKey)) {
                    int numOfSpaces = getPrefixSpaceCount(configLine);
                    StringBuilder spacePrefix = new StringBuilder();
                    for (int i = 0; i < numOfSpaces; i++) {
                        spacePrefix.append(" ");
                    }
                    List<String> configComments = this.comments.get(configKey);

                    //First: add comments
                    if (configComments != null) {
                        for (String comment : configComments) {
                            configWriter.append(spacePrefix.toString()).append("# ").append(comment);
                            configWriter.newLine();
                        }
                    }

                }
                if (!configLine.startsWith("#")) {
                    if (this.isStringListFirst(configLine)) {
                        configWriter.append("  ").append(configLine);
                    } else {
                        configWriter.append(configLine);
                    }
                    configWriter.newLine();

                }

                boolean isComment = configLine.startsWith("#");

                //Check if should add new blank line after a primary section key
                if (newLinePerKey && lineIndex < configContent.size() - 1 && !isComment) {
                    String nextConfigLine = configContent.get(lineIndex + 1);
                    if (nextConfigLine != null && !nextConfigLine.startsWith(" ")) {
                        if (!nextConfigLine.startsWith("'") && !nextConfigLine.startsWith("-")) {
                            configWriter.newLine();
                        }
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    private String replaceComillas(final String message) {
        if (message.length() < 1) return message;
        final StringBuilder stringBuilder = new StringBuilder(message);
        boolean onStartContains = false;
        int i = 0;
        while (i < stringBuilder.length()) {
            if (Character.toString(stringBuilder.charAt(i)).equalsIgnoreCase("'")) {
                if (Character.toString(stringBuilder.charAt(i - 2)).equalsIgnoreCase(":") ||
                        Character.toString(stringBuilder.charAt(i - 2)).equalsIgnoreCase("-")) {
                    onStartContains = true;
                }
                break;
            }
            i++;
        }
        if (onStartContains || Arrays.asList("\"", "'").contains(Character.toString(stringBuilder.charAt(stringBuilder.length() - 1)))) {
            if (this.isStringListFirst(message)) {
                i = 0;
                while (i < stringBuilder.length()) {
                    if (Character.toString(stringBuilder.charAt(i)).equalsIgnoreCase("-")) {
                        break;
                    }
                    i++;
                }
                i+=2;
            } else if (message.contains(":") && !this.isStringListFirst(message)) {
                i = 0;
                while (i < stringBuilder.length()) {
                    if (Character.toString(stringBuilder.charAt(i)).equalsIgnoreCase(":")) {
                        break;
                    }
                    i++;
                }
                i+=2;
            }
            if (stringBuilder.length() > i && Character.toString(stringBuilder.charAt(i)).equalsIgnoreCase("'")) {
                stringBuilder.deleteCharAt(i);
                stringBuilder.insert(i, "\"");
            }
            if (Character.toString(stringBuilder.charAt(stringBuilder.length() - 1)).equalsIgnoreCase("'")) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                stringBuilder.insert(stringBuilder.length(), "\"");
            }
        }
        return stringBuilder.toString().replace("''", "'");
    }

    private boolean isStringListFirst(String line) {
        while (line.startsWith(" ")) {
            line = line.replace(" ", "");
        }
        return line.startsWith("-");
    }

    public void load() throws ConfigurationException {
        List<String> configLines = getConfigContent();

        if (configLines.isEmpty()) {
            System.out.println(file.getName() + " doesn't have nothing to load");
            return;
        }
        this.getConfig().options().copyHeader(false);

        boolean hasHeader = !trim(configLines.get(0)).isEmpty();

        Map<String, List<String>> configComments = new LinkedHashMap<>();
        for (int lineIndex = 0; lineIndex < configLines.size(); lineIndex++) {
            String configLine = configLines.get(lineIndex);
            String trimmedLine = trimPrefixSpaces(configLine);
            if (trimmedLine.startsWith("#") && (lineIndex > 0 || !hasHeader)) {
                String configKey = getPathToComment(configLines, lineIndex, configLine);
                if (configKey != null) {
                    List<String> keyComments = configComments.get(configKey);
                    if (keyComments == null) keyComments = new ArrayList<>();
                    keyComments.add(trimmedLine.substring(trimmedLine.startsWith("# ") ? 2 : 1));
                    configComments.put(configKey, keyComments);
                }
            }
        }
        this.comments.putAll(configComments);
    }

    @Override
    public void addDefault(String path, Object defaultValue, String... comments) {
        if (defaultValue != null && comments != null && comments.length > 0 && !this.comments.containsKey(path)) {
            putComments(path, comments);
        }
    }

    @Override
    public void set(String key, Object value) {
        if (value != null) {
            if (getComments(key).size() > 0){
                this.comments.put(key, getComments(key));
            } else {
                this.comments.remove(key);
            }
        } else {
            this.comments.remove(key);
        }
    }

    @Override
    public void set(String key, Object value, String... comments) {
        if (value != null) {
            if (comments != null) {
                if (comments.length > 0) {
                    putComments(key, comments);
                } else {
                    this.comments.remove(key);
                }
            }
        } else {
            this.comments.remove(key);
        }
    }

    @Override
    public List<String> getComments(String path) {
        return comments.containsKey(path) ? new ArrayList<>(comments.get(path)) : new ArrayList<>();
    }

    public void putComments(String path, String... comments) {
        List<String> commentsList = new ArrayList<>();
        for (String comment : comments) {
            if (comment != null) {
                commentsList.add(comment);
            } else {
                commentsList.add("");
            }
        }
        this.comments.put(path, commentsList);
    }

    private List<String> getConfigContent() {
        List<String> configContent = new ArrayList<>();
        try (BufferedReader configReader = new BufferedReader(new FileReader(file))){
            String configReadLine;
            while ((configReadLine = configReader.readLine()) != null) {
                configContent.add(this.replaceComillas(configReadLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configContent;
    }

    private String getPathToComment(List<String> configContents, int lineIndex, String configLine) {
        if (configContents != null && lineIndex >= 0 && lineIndex < configContents.size() - 1 && configLine != null) {
            String trimmedConfigLine = trimPrefixSpaces(configLine);
            if (trimmedConfigLine.startsWith("#")) {
                int currentIndex = lineIndex;
                while (currentIndex < configContents.size() - 1) {
                    currentIndex++;
                    String currentLine = configContents.get(currentIndex);
                    String trimmedCurrentLine = trimPrefixSpaces(currentLine);
                    if (!trimmedCurrentLine.startsWith("#")) {
                        if (trimmedCurrentLine.contains(":")) {
                            return getPathToKey(configContents, currentIndex, currentLine);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }

    private String getPathToKey(List<String> configContents, int lineIndex, String configLine) {
        if (configContents != null && lineIndex >= 0 && lineIndex < configContents.size() && configLine != null) {
            if (!configLine.startsWith("#") && configLine.contains(":")) {
                int spacesBeforeKey = getPrefixSpaceCount(configLine);
                String key = trimPrefixSpaces(configLine.substring(0, configLine.indexOf(':')));
                if (spacesBeforeKey > 0) {
                    int currentIndex = lineIndex;
                    int previousSpacesBeforeCurrentLine = -1;
                    boolean atZeroSpaces = false;

                    while (currentIndex > 0) {
                        currentIndex--;
                        String currentLine = configContents.get(currentIndex);
                        int spacesBeforeCurrentLine = getPrefixSpaceCount(currentLine);
                        if (trim(currentLine).isEmpty())
                            break;
                        if (!trim(currentLine).startsWith("#")) {
                            if (spacesBeforeCurrentLine < spacesBeforeKey) {
                                if (currentLine.contains(":")) {
                                    if (spacesBeforeCurrentLine > 0 || !atZeroSpaces) {
                                        if (spacesBeforeCurrentLine == 0)
                                            atZeroSpaces = true;
                                        if (previousSpacesBeforeCurrentLine == -1
                                                || spacesBeforeCurrentLine < previousSpacesBeforeCurrentLine) {
                                            previousSpacesBeforeCurrentLine = spacesBeforeCurrentLine;
                                            key = trimPrefixSpaces(currentLine.substring(0, currentLine.indexOf(":")))
                                                    + "." + key;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                return key;
            }
        }
        return null;
    }

    private int getPrefixSpaceCount(String aString) {
        int spaceCount = 0;
        if (aString != null && aString.contains(" ")) {
            for (char aCharacter : aString.toCharArray()) {
                if (aCharacter == ' ')
                    spaceCount++;
                else
                    break;
            }
        }
        return spaceCount;
    }

    private String trim(String aString) {
        return aString != null ? aString.trim().replace(System.lineSeparator(), "") : "";
    }

    private String trimPrefixSpaces(String aString) {
        if (aString != null) {
            while (aString.startsWith(" "))
                aString = aString.substring(1);
        }
        return aString;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HConfiguration that = (HConfiguration) o;

        return Objects.equals(file, that.file);

    }

    @Override
    public int hashCode() {
        return file != null ? file.hashCode() : 0;
    }
}
