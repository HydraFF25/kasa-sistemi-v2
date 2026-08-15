package com.cratesystem.listener;

import com.cratesystem.animation.CrateInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * Kasa acilis animasyonu gosterilirken envanterden esya alinmasini/suruklenmesini engeller.
 */
public class InventoryGuardListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof CrateInventoryHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof CrateInventoryHolder) {
            event.setCancelled(true);
        }
    }
}
