package com.luvtox.Limiters;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import java.util.HashMap;
import java.util.Map;

public class ArmourStandLimiter implements Listener {

    private Map<Chunk, Integer> armorStandCounts = new HashMap<>();
    private final int MAX_ARMOR_STANDS_PER_CHUNK = 6;
    private final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "MineOptim" + ChatColor.DARK_GRAY + "] ";

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            Location location = event.getLocation();
            Chunk chunk = location.getChunk();
            if (armorStandCounts.containsKey(chunk) && armorStandCounts.get(chunk) >= MAX_ARMOR_STANDS_PER_CHUNK) {
                event.setCancelled(true);
                Entity entity = event.getEntity();
                if (entity instanceof ArmorStand) {
                    entity.remove();
                    if (event.getEntity().getPassengers() != null) {
                        event.getEntity().getPassengers().remove(entity);
                    }
                    Player player = getPlayer(event.getEntity().getUniqueId());
                    if (player != null) {
                        player.sendMessage(PREFIX + ChatColor.RED + "You have reached the maximum number of armor stands allowed in this chunk.");
                    }
                }
            } else {
                int currentCount = armorStandCounts.getOrDefault(chunk, 0);
                armorStandCounts.put(chunk, currentCount + 1);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ArmorStand) {
            Location location = entity.getLocation();
            Chunk chunk = location.getChunk();
            int currentCount = armorStandCounts.getOrDefault(chunk, 0);
            if (currentCount > 0) {
                armorStandCounts.put(chunk, currentCount - 1);
            }
        }
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        ArmorStand armorStand = event.getRightClicked();
        if (armorStand != null) {
            Location location = armorStand.getLocation();
            Chunk chunk = location.getChunk();
            int currentCount = armorStandCounts.getOrDefault(chunk, 0);
            if (currentCount >= MAX_ARMOR_STANDS_PER_CHUNK) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(PREFIX + ChatColor.RED + "You have reached the maximum number of armor stands allowed in this chunk.");
            } else {
                armorStandCounts.put(chunk, currentCount + 1);
            }
        }
    }

    private Player getPlayer(java.util.UUID uuid) {
        for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

}
