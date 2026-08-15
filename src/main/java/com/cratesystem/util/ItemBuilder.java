package com.cratesystem.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ItemStack olusturmayi kolaylastiran builder.
 */
public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(ItemStack base) {
        this.item = base.clone();
        this.meta = item.getItemMeta();
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(Math.max(1, amount));
        return this;
    }

    public ItemBuilder name(String name) {
        if (name != null && meta != null) {
            meta.displayName(ColorUtils.color(name).decoration(TextDecoration.ITALIC, false));
        }
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        if (lore == null || meta == null) return this;
        List<Component> comps = new ArrayList<>();
        for (String line : lore) {
            comps.add(ColorUtils.color(line).decoration(TextDecoration.ITALIC, false));
        }
        meta.lore(comps);
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        if (meta != null) meta.setEnchantmentGlintOverride(glow ? Boolean.TRUE : null);
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        if (meta != null) meta.addItemFlags(flags);
        return this;
    }

    /**
     * Verilen buyuleri esyaya ekler. Vanilla seviye siniri gormezden gelinir,
     * yani Keskinlik 6, Verimlilik 6, Koruma 5/6 gibi ozel seviyeler mumkundur.
     */
    public ItemBuilder enchants(Map<Enchantment, Integer> enchants) {
        if (meta == null || enchants == null) return this;
        for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
            meta.addEnchant(e.getKey(), e.getValue(), true);
        }
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        if (meta != null) meta.setUnbreakable(unbreakable);
        return this;
    }

    public <T, Z> ItemBuilder pdc(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        if (meta != null) meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
