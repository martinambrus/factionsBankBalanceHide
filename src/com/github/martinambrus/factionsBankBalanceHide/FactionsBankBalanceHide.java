package com.github.martinambrus.factionsBankBalanceHide;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;

public class FactionsBankBalanceHide extends JavaPlugin implements Listener {

    @Override
	public void onEnable() {
    	Bukkit.getLogger().info("[FactionsBankBalanceHide] Version 0.1 enabled.");

    	// save default config if not saved yet
		getConfig().options().copyDefaults(true);
		saveConfig();

    	Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    /***
	 * Checks whether a command
	 * @param e
	 * @return
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBaltop(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().contains("/baltop")) {
			Player player = e.getPlayer();
			e.setCancelled(true);

			if (this.getConfig().getBoolean("showOverrideWarnings", true) == true) {
				Bukkit.getLogger().info("[FactionsBankBalanceHide] Overriding original /baltop command from player " + e.getPlayer().getName() + " to hide Factions balances.");
			}

			VirtualFactionCommandSender sender = new VirtualFactionCommandSender();
			Bukkit.dispatchCommand(sender, e.getMessage().substring(1, e.getMessage().length()));

			List<String> messages = sender.getLastMessage();
			for (String s : messages) {
				if (s.contains(". faction_")) {
					String factionStart = s.substring(s.indexOf(". faction_") + 10, s.length());
					String factionID = factionStart.substring(0, factionStart.indexOf(",")).replace("_", "-");

					if (FactionColl.get().containsId(factionID))
					{
						Faction faction = FactionColl.get().get(factionID);
						s = s.replace(". faction_" + factionID.replace("-", "_"), ". [faction] " + faction.getName());
					}
				}

				player.sendMessage(s);
			}
		}
	}

}
