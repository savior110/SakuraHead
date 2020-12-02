package com.entiv.sakurahead;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

class Skull {

    final String texturesValue;
    final double change;
    String type;

    Skull(double change, String type, String texturesValue) {

        this.type = type;

        this.texturesValue = texturesValue;
        this.change = change;
    }

    double getChange() {
        return this.change;
    }
    ItemStack getItemStack() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);


        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", texturesValue));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        head.setItemMeta(skullMeta);
        return head;
    }
}













