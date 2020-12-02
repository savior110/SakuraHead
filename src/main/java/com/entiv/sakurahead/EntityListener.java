package com.entiv.sakurahead;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class EntityListener implements Listener {

    Main plugin = Main.getInstance();

    @EventHandler
    void onEntityDead(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();
        EntityType entityType = entity.getType();

        Player killer = entity.getKiller();

        if (killer == null || entityType == EntityType.ARMOR_STAND) return;

        double playerChange = plugin.getConfig().getDouble("Player.Change");
        Skull entitySkull = Main.getInstance().getSkull(entityType);

        if (entityType.equals(EntityType.PLAYER) && playerChange > Math.random()) {

            givePlayerSkull(killer, (Player) entity);

        } else if (entitySkull.getChange() >= Math.random()) {

            ItemStack skull = entitySkull.getItemStack();
            Location location = entity.getLocation();
            location.getWorld().dropItem(location, skull);

            String dropMobHead = plugin.getConfig().getString("Message.DropMobHead");

            plugin.getServer().broadcastMessage(Message.toColor(Message.replace(dropMobHead, "%player%", killer.getName(), "%target%", Message.withoutColor(entitySkull.type))));
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


}
