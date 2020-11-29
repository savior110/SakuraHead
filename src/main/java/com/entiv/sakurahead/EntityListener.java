package com.entiv.sakurahead;

import com.entiv.sakurahead.utils.Message;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collection;
import java.util.List;

public class EntityListener implements Listener {

    Main plugin = Main.getInstance();

    @EventHandler
    void onEntityDead(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        EntityType entityType = event.getEntityType();
        Skull entitySkull = getSkull(entityType);

        if (entitySkull == null || killer == null) return;

        double playerChange = plugin.getConfig().getDouble("Player.Change");

        if (entityType.equals(EntityType.PLAYER) && playerChange > Math.random()) {

            givePlayerSkull(killer, (Player) entity);

        } else if (entitySkull.getChange() >= Math.random()) {

            ItemStack skull = entitySkull.getItemStack();
            event.getDrops().add(skull);

            String dropMobHead = plugin.getConfig().getString("Message.DropMobHead");

            plugin.getServer().broadcastMessage(Message.toColor(Message.replace(dropMobHead, "%player%", killer.getName(), "%target%", Message.withoutColor(entitySkull.displayName))));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    void onBreakBlock(BlockBreakEvent event) {

        if (event.isCancelled()) return;

        Collection<ItemStack> drops = event.getBlock().getDrops();

        for (ItemStack itemStack : drops) {

            if (itemStack.getType().equals(Material.PLAYER_HEAD)) {

                NBTItem nbtItem = new NBTItem(itemStack);
                String value = nbtItem.getCompound("SkullOwner").getCompound("Properties").getCompoundList("textures").get(0).getString("Value");

                if (value == null) return;

                ConfigurationSection section = plugin.getConfig().getConfigurationSection("SkullType");
                if (section == null) return;

                for (String entityName : section.getKeys(false)) {

                    String configValue = section.getString(entityName.concat(".Value"));

                    if (value.equals(configValue)) {

                        Skull skull = getSkull(EntityType.valueOf(entityName));
                        if (skull == null) return;

                        event.setDropItems(false);
                        Location location = event.getBlock().getLocation();

                        ItemStack skullItemStack = skull.getItemStack();
                        location.getWorld().dropItem(location, skullItemStack);
                        return;
                    }
                }
            }
        }
    }

    private void givePlayerSkull(Player killer, Player killed) {

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();

        itemMeta.setDisplayName(plugin.getConfig().getString("Player.DisplayName").replace("%killed%", killed.getName()));
        itemMeta.setOwningPlayer(killed);
        itemStack.setItemMeta(itemMeta);

        killer.getInventory().addItem(itemStack);
        String dropPlayerHead = plugin.getConfig().getString("Message.DropPlayerHead").replace("%player%", killer.getName()).replace("%target%", killed.getName());

        Message.sendAllPlayers(dropPlayerHead);
    }

    private Skull getSkull(EntityType entityType) {

        ConfigurationSection skullType = plugin.getConfig().getConfigurationSection("SkullType");

        if (skullType == null) {
            String message = "§9§l" + plugin.getDescription().getName() + "§6§l >> §c生物§e§l " + entityType + "§c 不存在,请检查配置文件";
            Message.sendConsole(message);
            return null;
        }

        double change = skullType.getDouble(entityType + ".Change");
        String displayName = skullType.getString(entityType + ".DisplayName");
        List<String> lore = skullType.getStringList(entityType + ".Lore");
        String value = skullType.getString(entityType + ".Value");

        return new Skull(change, displayName, lore, value);
    }
}