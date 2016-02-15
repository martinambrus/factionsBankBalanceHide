package com.github.martinambrus.factionsBankBalanceHide;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class VirtualFactionCommandSender implements CommandSender {

	private List<String> lastMessages = new ArrayList<String>();
	private String name = "[FactionsBankBalanceHide]";

	@Override
	public PermissionAttachment addAttachment(Plugin arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPermission(String arg0) {
		return true;
	}

	@Override
	public boolean hasPermission(Permission arg0) {
		return true;
	}

	@Override
	public boolean isPermissionSet(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPermissionSet(Permission arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void recalculatePermissions() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttachment(PermissionAttachment arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOp() {
		return true;
	}

	@Override
	public void setOp(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Server getServer() {
		return Bukkit.getServer();
	}

	@Override
	public void sendMessage(String arg0) {
		this.lastMessages.add(arg0);
	}

	@Override
	public void sendMessage(String[] arg0) {
		for (String s : arg0) {
			this.lastMessages.add(s);
		}
	}

	public List<String> getLastMessage() {
		return this.lastMessages;
	}

}
