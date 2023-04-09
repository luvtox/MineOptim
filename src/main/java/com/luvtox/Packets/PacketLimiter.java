package com.luvtox.Packets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketLimiter implements Listener {
    private int maxPacketsPerSecond = 50; // Default limit
    private boolean enabled = true;

    private Map<UUID, Integer> packetCounts = new HashMap<>();

    public PacketLimiter(JavaPlugin plugin) {
        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Schedule packet count reset task
        new BukkitRunnable() {
            @Override
            public void run() {
                packetCounts.clear();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Reset packet counts every second
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Add player to packet count map
        packetCounts.put(event.getPlayer().getUniqueId(), 0);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove player from packet count map
        packetCounts.remove(event.getPlayer().getUniqueId());
    }

    public void checkPacketLimit(Player player) {
        if (!enabled) {
            return;
        }

        int packetCount = packetCounts.getOrDefault(player.getUniqueId(), 0);
        if (packetCount > maxPacketsPerSecond) {
            // Kick player and send message
            player.kickPlayer(ChatColor.RED + "[MineOptim] You have been kicked for sending too many packets.");
        } else {
            // Increment packet count for player
            packetCounts.put(player.getUniqueId(), packetCount + 1);
        }
    }

    public int getMaxPacketsPerSecond() {
        return maxPacketsPerSecond;
    }

    public void setMaxPacketsPerSecond(int maxPacketsPerSecond) {
        this.maxPacketsPerSecond = maxPacketsPerSecond;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
