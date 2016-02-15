package com.github.martinambrus.factionsBankBalanceHide;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

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

			// now get the command output
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {

				@Override
				public void run() {
					List<String> oldMessages = sender.getLastMessage();
					List<String> newMessages = new ArrayList<String>();
					for (String s : oldMessages) {
						// don't add faction banks
						if (!s.contains(". faction_")) {
							if (s.contains(". ")) {
								newMessages.add("[!pos!]" + s.substring(s.indexOf(".") + 1, s.length()));
							} else {
								newMessages.add(s);
							}
						}
					}

					// now send new messages
					Integer counter = 1;
					for (String s : newMessages) {
						if (s.contains("[!pos!]")) {
							// add position to this record, since it's a
							s = s.replace("[!pos!]", counter + ".");
							counter++;
						}

						player.sendMessage(s);
					}
				}
			}, 2);
		}
	}

}
