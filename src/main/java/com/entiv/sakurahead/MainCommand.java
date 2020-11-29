package com.entiv.sakurahead;

import com.entiv.sakurahead.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            Main plugin = Main.getInstance();

            plugin.reloadConfig();

            String message = plugin.getConfig().getString("Message.".concat("Reload"));
            Message.send(sender, message);
        }
        System.out.println("123");
        return true;
    }
}
