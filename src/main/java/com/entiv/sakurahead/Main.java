package com.entiv.sakurahead;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;

    public static Main getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        String[] message = {
                "§a樱花头颅插件§e v" + getDescription().getVersion() + " §a已启用",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        getServer().getConsoleSender().sendMessage(message);
        Bukkit.getPluginCommand("SakuraHead").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        String[] message = {
                "§a樱花头颅插件§e v" + getDescription().getVersion() + "§a已卸载",
                "§a插件制作作者:§e EnTIv §aQQ群:§e 600731934"
        };
        getServer().getConsoleSender().sendMessage(message);
    }


}
