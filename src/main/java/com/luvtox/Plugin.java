package com.luvtox;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import com.luvtox.Limiters.ArmourStandLimiter;
import com.luvtox.Packets.PacketLimiter;


/*
 * mineoptim java plugin
 */
public class Plugin extends JavaPlugin
{
  private static final Logger LOGGER=Logger.getLogger("MineOptim");

  private PacketLimiter packetLimiter;


  public void onEnable()
  {
    // ==============================================================================
    // Create PacketLimiter instance
    packetLimiter = new PacketLimiter(this);
    // Load config
    saveDefaultConfig();
    reloadConfig();
    // Set PacketLimiter options from config
    packetLimiter.setEnabled(getConfig().getBoolean("enabled", true));
    packetLimiter.setMaxPacketsPerSecond(getConfig().getInt("max-packets-per-second", 50));
    LOGGER.info("Loaded PacketLimiter");
    // ==============================================================================
    // ==============================================================================
    // Register ArmourStandLimiter
    getServer().getPluginManager().registerEvents(new ArmourStandLimiter(), this);
    LOGGER.info("Loaded ArmourStandLimiter");
    // ==============================================================================

    
    LOGGER.info("MineOptim has been enabled");
  }

  public void onDisable()
  {
    saveConfig();
    LOGGER.info("MineOptim has been disabled");
  }
}