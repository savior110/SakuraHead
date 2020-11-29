package com.entiv.sakurahead;

import com.entiv.sakurahead.utils.Message;
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
                "&a樱花头颅插件&e v" + getDescription().getVersion() + " &a已启用",
                "&a插件制作作者:&e EnTIv &aQQ群:&e 600731934"
        };
        Message.sendConsole(message);

        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        Bukkit.getPluginCommand("SakuraHead").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        String[] message = {
                "&a樱花头颅插件&e v" + getDescription().getVersion() + "&a已卸载",
                "&a插件制作作者:&e EnTIv &aQQ群:&e 600731934"
        };
        Message.sendConsole(message);
    }


}
