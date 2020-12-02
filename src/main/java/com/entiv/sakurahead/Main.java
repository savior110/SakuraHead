package com.entiv.sakurahead;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin {

    private static Main plugin;

    public static Main getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        String[] message = {
                "&a樱花头颅插件&e v" + getDescription().getVersion() + " &a已启用",
                "&a插件制作作者:&e EnTIv &aQQ群:&e 600731934"
        };
        Message.sendConsole(message);

        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        PluginCommand command = Bukkit.getPluginCommand("SakuraHead");

        if (command != null) {
            command.setExecutor(new MainCommand());
            command.setTabCompleter(new MainCommand());
        }
    }

    @Override
    public void onDisable() {
        String[] message = {
                "&a樱花头颅插件&e v" + getDescription().getVersion() + "&a已卸载",
                "&a插件制作作者:&e EnTIv &aQQ群:&e 600731934"
        };
        Message.sendConsole(message);
    }

    public Skull getSkull(EntityType entityType) {

        ConfigurationSection skullType = getConfigurationSection();

        String name = entityType.name();
        if (skullType.getString(name) == null) {
            Message.sendConsole("&9&lSakuraHead &6&l>> &c生物" + name + "不存在, 请检查配置文件");
        }

        double change = skullType.getDouble(entityType + ".Change");
        String displayName = skullType.getString(entityType + ".DisplayName");
        List<String> lore = skullType.getStringList(entityType + ".Lore");
        String value = skullType.getString(entityType + ".Value");

        return new Skull(change, displayName, value);
    }

    public ConfigurationSection getConfigurationSection() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("SkullType");
        if (section == null) {
            throw new NullPointerException("配置文件错误, 请检查配置文件");
        }
        return section;
    }
}
