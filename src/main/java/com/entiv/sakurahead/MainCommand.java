package com.entiv.sakurahead;

import com.entiv.sakurahead.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            Main plugin = Main.getInstance();

            if ("reload".equals(args[0])) {

                plugin.reloadConfig();

                String message = plugin.getConfig().getString("Message.".concat("Reload"));
                Message.send(sender, message);

            } else if ("give".equals(args[0])) {

                try {

                    Player player = Objects.requireNonNull(Bukkit.getPlayer(args[1]));

                    Skull skull = plugin.getSkull(EntityType.valueOf(args[2].toUpperCase()));
                    player.getInventory().addItem(skull.getItemStack());

                    Message.send(sender, "发送成功");

                } catch (ArrayIndexOutOfBoundsException e) {
                    Message.send(sender, "Message.".concat("参数不足"));
                } catch (NullPointerException e) {
                    Message.send(sender, "玩家当前不在线");
                } catch (IllegalArgumentException e) {
                    Message.send(sender, "生物类型输入错误");
                }
            }

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {

            List<String> tabComplete = new ArrayList<>();

            tabComplete.add("reload");
            tabComplete.add("give");

            tabComplete.removeIf(s -> !s.startsWith(args[0].toLowerCase()));

            return tabComplete;

        } else if (args.length == 3) {

            Main plugin = Main.getInstance();

            List<String> tabComplete = new ArrayList<>(plugin.getConfigurationSection().getKeys(false));

            if (!args[args.length - 1].trim().isEmpty()) {
                String match = args[args.length - 1].trim().toLowerCase();
                tabComplete.removeIf(name -> !name.toLowerCase().startsWith(match));
            }

            tabComplete.replaceAll(String::toLowerCase);

            return tabComplete;
        }
        return null;
    }
}
