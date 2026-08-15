package com.cratesystem.animation;

import com.cratesystem.crate.Crate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Kasa acilis animasyonu icin kullanilan envanterleri isaretler.
 * Boylece InventoryGuardListener bu envanterlerdeki tiklama/surukleme islemlerini engelleyebilir.
 */
public class CrateInventoryHolder implements InventoryHolder {

    private Inventory inventory;
    private final Player player;
    private final Crate crate;

    public CrateInventoryHolder(Player player, Crate crate) {
        this.player = player;
        this.crate = crate;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() { return player; }
    public Crate getCrate() { return crate; }
}
