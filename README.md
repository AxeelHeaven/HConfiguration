# HConfiguration

HConfiguration is a custom configuration handler for Spigot plugins. It extends the configuration management capabilities by allowing inline comments (denoted by `#`) to be preserved and manipulated, something not natively supported by Spigot's YAML configuration system.

## Features

- **Inline Comments:** Supports adding, editing, and preserving comments in configuration files.
- **Automatic Resource Management:** Ensures the configuration file is created and loaded from resources if it doesn't exist.
- **Customizable Formatting:** Configurations can be saved with or without blank lines between keys for better readability.
- **Advanced Parsing:** Supports nested keys and multi-line values while preserving the file structure.
- **Reload and Save:** Easy methods for reloading or saving configurations dynamically.

## Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Add the `HConfiguration` class to your plugin's source code.

## Usage

### Initializing HConfiguration

To use `HConfiguration`, create an instance by passing the configuration file and plugin instance:

```java
File configFile = new File(getDataFolder(), "config.yml");
HConfiguration config = new HConfiguration(configFile, this);
```

### Adding Comments

You can add comments to specific keys in the configuration:

```java
config.addDefault("key", "value", "This is a comment for the key.", "Another comment line.");
```

### Setting and Getting Values

To set a value:

```java
config.set("key", "value", "Optional comment for this key");
```

To get a value:

```java
String value = config.get("key");
```

### Saving Configuration

Ensure changes are saved to the file:

```java
config.save();
```

### Reloading Configuration

Reload the configuration file:

```java
config.reload();
```

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

