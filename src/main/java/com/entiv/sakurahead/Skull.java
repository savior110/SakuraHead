package com.entiv.sakurahead;

import com.entiv.sakurahead.utils.ItemBuilder;
import com.entiv.sakurahead.utils.Message;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

class Skull {
    final String displayName;

    final String texturesValue;

    final double change;

    final List<String> lore;

    private final Date data = new Date();

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    Skull(double change, String displayName, List<String> lore, String texturesValue) {

        lore.replaceAll(s -> s.replace("%time%", simpleDateFormat.format(this.data)));

        this.lore = lore;
        this.displayName = Message.toColor(displayName);
        this.texturesValue = texturesValue;
        this.change = change;
    }

    double getChange() {
        return this.change;
    }

    ItemStack getItemStack() {

        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        NBTItem nbtItem = new NBTItem(head);

        NBTCompound skull = nbtItem.addCompound("SkullOwner");
        skull.setString("Name", Message.withoutColor(displayName));
        skull.setString("Id", String.valueOf(UUID.randomUUID()));

        NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
        texture.setString("Value", texturesValue);

        nbtItem.setString("Type", "test");

        ItemStack itemStack = nbtItem.getItem();

        return new ItemBuilder(itemStack).name(displayName).lore(lore).build();
    }
}













