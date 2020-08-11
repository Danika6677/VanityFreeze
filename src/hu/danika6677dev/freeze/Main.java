package hu.danika6677dev.freeze;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("§8[§cFreeze§8] §7Sikeresen bekapcsolva!");
		Bukkit.getConsoleSender().sendMessage("§7Plugin verzio: §e1.0.0");
		Bukkit.getConsoleSender().sendMessage("§7Plugin by: §eDanika6677");
		Bukkit.getConsoleSender().sendMessage("§cMain §7betoltve!");
		getServer().getPluginManager().registerEvents(this, (Plugin)this);
		getCommand("freeze").setExecutor((CommandExecutor)this);
		saveDefaultConfig();
	}
	
	ArrayList<String> frozen = new ArrayList<>();
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (this.frozen.contains(p.getName())) {
			e.setTo(e.getFrom());
			p.sendMessage("§8▉▉▉▉▉▉▉▉");
			p.sendMessage("§8▉            ▉");
			p.sendMessage("§8▉     §4▉§8     ▉ §eLe lettél fagyasztva!");
			p.sendMessage("§8▉     §4▉§8     ▉ §eCsatlakozz: §a" + getConfig().getString("csatlakozz"));
			p.sendMessage("§8▉     §4▉§8     ▉ §eJátékosneved: §a" + p.getName());
			p.sendMessage("§8▉     §4▉§8     ▉");
			p.sendMessage("§8▉            ▉ §7Ha hacket használsz, inkább valld be!");
			p.sendMessage("§8▉     §4▉§8     ▉ §7Jobban jársz!");
			p.sendMessage("§8▉            ▉");
			p.sendMessage("§8▉▉▉▉▉▉▉▉");
	    } 
	}
	  
	  @EventHandler
	  public void onDamage(EntityDamageByEntityEvent event) {
	    if (event.getEntity() instanceof Player) {
	      Player p = (Player)event.getEntity();
	      if (this.frozen.contains(p.getName())) {
	        event.setCancelled(true);
	        event.getDamager().sendMessage("§4Freeze§7> §cEz a játékos le van fagyasztva, így nem tudod megütni!");
	      } 
	    } 
	  }
	  
	  @EventHandler
	  public void onBreak(BlockBreakEvent event) {
	    Player p = event.getPlayer();
	    if (this.frozen.contains(p.getName())) {
	      event.setCancelled(true);
	      event.getPlayer().sendMessage("§4Freeze§7> §cLe vagy fagyasztva, így nem tudsz kitörni blokkokat!");
	    } 
	  }
	  
	  @EventHandler
	  public void onPlace(BlockPlaceEvent event) {
	    Player p = event.getPlayer();
	    if (this.frozen.contains(p.getName())) {
	      event.setCancelled(true);
	      event.getPlayer().sendMessage("§4Freeze§7> §cLe vagy fagyasztva így nem tudsz lerakni blokkokat!");
	    } 
	  }
	  
	  @EventHandler
	  public void onPlayerQuit(PlayerQuitEvent event) {
	    Player p = event.getPlayer();
	    if (this.frozen.contains(p.getName())) {
	      Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), getConfig().getString("banevent").replaceAll("%player%", p.getPlayer().getName()));
	      for (Player players : Bukkit.getOnlinePlayers()) {
	        if (players.hasPermission("freeze.alert"))
	          players.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("leaveevent").replaceAll("%player%", p.getPlayer().getName()))); 
	      }
	    } 
	  }
	  
	  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if (cmd.getName().equalsIgnoreCase("freeze")) {
	      if (sender.hasPermission("freeze.command")) {
	    	  if (args.length == 0) {
	          sender.sendMessage("§4Freeze§7> §cHásználd: §e/freeze <játékos>");
	          return true;
	        } 
	        Player target = Bukkit.getServer().getPlayer(args[0]);
	        if (target == null) {
	          sender.sendMessage("§4Freeze§7> §cJátékos nem található!");
	          return true;
	        } 
	        if (this.frozen.contains(target.getName())) {
	          this.frozen.remove(target.getName());
	          sender.sendMessage("§4Freeze§7> §cFagyasztás feloldva§e" + " " + target.getName() + "§c-ról/ről.");
	          return true;
	        } 
	        this.frozen.add(target.getName());
	        sender.sendMessage("§4Freeze§7> §cLefagyasztottad§e" + " " + target.getName() + "§c-t.");
	        return true;
	      } 
	      sender.sendMessage("§4Freeze§7> §cNincsen ehhez jogod!");
	    } 
	    return false;
	  }
	}
