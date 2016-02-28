package com.github.martinambrus.factionsBankBalanceUUIDReplace;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;

public class FactionsBankBalanceUUIDReplace extends JavaPlugin implements Listener {

    @Override
	public void onEnable() {
    	Bukkit.getLogger().info("[FactionsBankBalanceHide] Version 0.1 enabled.");

    	// save default config if not saved yet
		getConfig().options().copyDefaults(true);
		saveConfig();

		try {
        	Metrics metrics = new Metrics(this);
            metrics.start();
		} catch (IOException e) {
			Bukkit.getLogger().warning("Failed to initialize Metrics.");
		}

    	Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    public void replaceUUIDs(CommandSender csender, String command) {
		if (this.getConfig().getBoolean("showOverrideWarnings", true) == true) {
			Bukkit.getLogger().info("[FactionsBankBalanceHide] Overriding original /baltop command from player " + csender.getName() + " to hide Factions balances.");
		}

		VirtualFactionCommandSender sender = new VirtualFactionCommandSender();

		if (csender instanceof Player) {
			Bukkit.dispatchCommand(sender, command.substring(1, command.length()));
		} else {
			Bukkit.dispatchCommand(sender, command);
		}

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

			csender.sendMessage(s);
		}
    }

    /***
	 * Intercepts /baltop command from a player and replaces UUIDs
	 * of factions in it by their respective names via the Factions API.
	 * @param e
	 * @return
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBaltopPlayer(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().contains("/baltop")) {
			e.setCancelled(true);
			replaceUUIDs(e.getPlayer(), e.getMessage());
		}
	}

	/***
	 * /***
	 * Intercepts /baltop command from a console and replaces UUIDs
	 * of factions in it by their respective names via the Factions API.
	 * @param e
	 * @return
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBaltopConsole(ServerCommandEvent e) {
		if (e.getCommand().contains("/baltop")) {
			try {
				e.getClass().getDeclaredMethod("setCancelled", boolean.class).invoke(e, true);
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				// Bukkit < 1.8 does not allow cancelling console commands
				Bukkit.getLogger().info("[factionsBankBalanceUUIDReplace] Bukkit < 1.8 does not support cancelling console commands. Execution of the original command replaced by calling 'time'");
				e.setCommand("time");
			}
			replaceUUIDs(e.getSender(), e.getCommand());
		}
	}

}
