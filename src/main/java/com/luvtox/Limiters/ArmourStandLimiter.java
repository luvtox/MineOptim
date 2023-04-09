package com.luvtox.Limiters;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.HashMap;
import java.util.Map;

public class ArmourStandLimiter implements Listener {

    private Map<Chunk, Integer> armorStandCounts = new HashMap<>();
    private final int MAX_ARMOR_STANDS_PER_CHUNK = 6;

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            Location location = event.getLocation();
            Chunk chunk = location.getChunk();
            if (armorStandCounts.containsKey(chunk) && armorStandCounts.get(chunk) >= MAX_ARMOR_STANDS_PER_CHUNK) {
                event.setCancelled(true);
            } else {
                int currentCount = armorStandCounts.getOrDefault(chunk, 0);
                armorStandCounts.put(chunk, currentCount + 1);
            }
        }
    }
}
