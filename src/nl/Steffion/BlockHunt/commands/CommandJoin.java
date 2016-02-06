package nl.Steffion.BlockHunt.commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.Steffion.BlockHunt.data.Arena;

public class CommandJoin extends Command {
	
	public CommandJoin() {
		super("blockhunt join <arena>", "blockhunt.join", true, "Join an arena.");
	}

	@Override
	public boolean runCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		if (plugin.getArenaHandler().getArena(player) != null) {
			player.sendMessage("§cYou're already in an arena!");
			return true;
		}
		
		if (args.length < 2) {
			player.getPlayer().sendMessage("§cUsage: /" + getUsage());
			return true;
		}
		
		String arenaName = "";

		for (int i = 1; i < args.length; i++) {
			arenaName = arenaName + args[i] + " ";
		}
		
		arenaName = arenaName.substring(0, arenaName.length() - 1);
		
		Arena arena = null;

		for (Arena possibleArena : plugin.getArenaHandler().getArenas()) {
			if (possibleArena.getName().equalsIgnoreCase(arenaName)) {
				arena = possibleArena;
			}
		}

		if (arena == null) {
			player.getPlayer().sendMessage("§cNo arena exists with the name '" + arenaName + "'");
			return true;
		}
		
		if (!arena.isSetup()) {
			player.getPlayer().sendMessage("§cThis arena is not completely setup.");
			return true;
		}
		
		arena.addPlayer(player);
		plugin.getPlayerHandler().storePlayerData(player);
		plugin.getPlayerHandler().getPlayerData(player).clear();
		player.getPlayer().teleport(arena.getLobbyLocation());
		
		ItemMeta im;
		ArrayList<String> lore = new ArrayList<String>();
		
		/*
		 * Exit item
		 */
		ItemStack exit = new ItemStack(Material.WOOD_DOOR);
		im = exit.getItemMeta();
		im.setDisplayName("§cExit arena");
		lore.clear();
		lore.add("§7Right-click to exit the arena.");
		im.setLore(lore);
		exit.setItemMeta(im);
		player.getPlayer().getInventory().setItem(8, exit);
		
		player.getPlayer().sendMessage("You've joined '" + arena.getName() + "'.");
		return true;
	}
	
}