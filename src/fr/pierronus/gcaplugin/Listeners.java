package fr.pierronus.gcaplugin;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.codingforcookies.armorequip.ArmorEquipEvent;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.wiefferink.areashop.AreaShop;
import me.wiefferink.areashop.regions.GeneralRegion;

import com.SirBlobman.combatlogx.api.ICombatLogX;
import com.SirBlobman.combatlogx.api.event.PlayerTagEvent;
import com.SirBlobman.combatlogx.api.event.PlayerUntagEvent;
import com.SirBlobman.combatlogx.api.event.PlayerUntagEvent.UntagReason;
import com.SirBlobman.combatlogx.api.utility.ICombatManager;

public class Listeners implements org.bukkit.event.Listener {
	AreaShop areaShop = (AreaShop)Bukkit.getServer().getPluginManager().getPlugin("AreaShop");
	
	String alert = "§7(§c!§7) ";
	Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
	public String grade;
	public String gradepolice;
	World world = Bukkit.getWorld("world");
	ArrayList<Player> radioplayer = new ArrayList<Player>();
	Map<UUID, Consumer<String>> map = new HashMap<>();
	ICombatLogX plugin = getAPI();
    ICombatManager combatManager = plugin.getCombatManager();
	@EventHandler
	public void onUntag(PlayerUntagEvent e) {
		
		
	    Player p = e.getPlayer();
	    
	    if(tagged.containsKey(p.getName())) {
	    	tagged.remove(p.getName());
	    }
	    final ItemStack playerHelmet = p.getEquipment().getHelmet();
		final Material playerHelmetType = playerHelmet == null ? null : playerHelmet.getType();
		final boolean playerHasLeatherHelmet = playerHelmetType == Material.LEATHER_HELMET;
	    UntagReason untagReason = e.getUntagReason();
	    
	    if(untagReason == UntagReason.QUIT) {
	        p.setHealth(0);
	        if(playerHasLeatherHelmet) {
	        	Bukkit.broadcastMessage("§4§lUn inconnu vient de se déconnecter en combat");
	        }else {
	        	Bukkit.broadcastMessage("§4§l" + p.getName() + "§f§4 vient de se déconnecter en combat");
	        }
	        
	    }
	    if(untagReason == UntagReason.EXPIRE) {
	    	p.sendMessage("§aVous n'êtes plus en combat");
	    }
	}
	public boolean isEnabled() {
	    PluginManager pluginManager = Bukkit.getPluginManager();
	    return pluginManager.isPluginEnabled("CombatLogX");
	}

	public ICombatLogX getAPI() {
	    PluginManager pluginManager = Bukkit.getPluginManager();
	    Plugin plugin = pluginManager.getPlugin("CombatLogX");
	    return (ICombatLogX) plugin;
	}
	public boolean isInCombat(Player player) {
	    return combatManager.isInCombat(player);
	}
	public HashMap<String, Long> tagged = new HashMap<String, Long>();
	@SuppressWarnings("unused")
	@EventHandler
	public void onTag(PlayerTagEvent e) {
	    Player k = e.getPlayer();
	    Player p = (Player) e.getEnemy();
	    if(p== null) {
	    	return;
	    }
	    if(tagged.containsKey(k.getName())) {
	    	
	    }else {
	        
	    	tagged.put(k.getName(), 10L);
			if(p == null) {
				assert true;
			}else {
			tagged.put(p.getName(), 10L);
			}
	    final ItemStack playerHelmet = p.getEquipment().getHelmet();
		final ItemStack killerHelmet = k.getEquipment().getHelmet();
		
		final Material playerHelmetType = playerHelmet == null ? null : playerHelmet.getType();
		final Material killerHelmetType = killerHelmet == null ? null : killerHelmet.getType();
		
		final boolean playerHasLeatherHelmet = playerHelmetType == Material.LEATHER_HELMET;
		final boolean killerHasLeatherHelmet = killerHelmetType == Material.LEATHER_HELMET;
		
		if(playerHasLeatherHelmet) {
			if(killerHasLeatherHelmet) {
				p.sendMessage("§cVous venez d'être attaqué par §n§cun inconnu");
				k.sendMessage("§cVous venez d'attaquer §n§cun inconnu");
			}else {
				p.sendMessage("§cVous venez d'être attaqué par §n§c" + k.getName());
				k.sendMessage("§cVous venez d'attaquer §n§cun inconnu");
			}
		}else {
			if(killerHasLeatherHelmet) {
				p.sendMessage("§cVous venez d'être attaqué par §n§cun inconnu");
				k.sendMessage("§cVous venez d'attaquer §n§c" + p.getName());
			}else {
				p.sendMessage("§cVous venez d'être attaqué par §n§c" + k.getName());
				k.sendMessage("§cVous venez d'attaquer §n§c" + p.getName());
			}
		}
	    }
	}
	
	public void hologramUpdate() {
ArrayList<String> list = Main.getInstance().data.getTenBiggest();
Location loc = new Location(Bukkit.getWorld("world"),75,73,-241);
if(DHAPI.getHologram("recherche") == null) {
	DHAPI.createHologram("recherche", loc);
}
Hologram hologram = DHAPI.getHologram("recherche");
hologram.removePage(0);
hologram.addPage();
DHAPI.addHologramLine(hologram, "§b§lJoueurs les plus recherchés");



for(int i=0; i<= list.size() - 1;i=i+2) {
	String star = "✪";
	DHAPI.addHologramLine(hologram, list.get(i) + " - §4" + StringUtils.repeat(star, Integer.valueOf(list.get(i+1))));

}
	}
	@EventHandler
	public void policeButton(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		int x = player.getLocation().getBlockX();
		int y = player.getLocation().getBlockY();
		int z = player.getLocation().getBlockZ();
		if(!(y < 63 || y > 109) && !(x < -341 || x > -309) && !(z < -247 || z > 227) ){
			if(action == Action.RIGHT_CLICK_BLOCK) {
				if(event.getClickedBlock().getType() == Material.STONE_BUTTON) {
					if(player.hasPermission("gca.police")) {
						player.sendMessage(alert+"§bVous avez ouvert la porte d'une cellule");
					}else {
						event.setCancelled(true);
						player.sendMessage(alert+"§cVous n'êtes pas §bpolicier");
					}
				}
			}
		}
	}
	@SuppressWarnings("unused")
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		hologramUpdate();
		Player p = e.getPlayer();
		p.setGameMode(GameMode.SURVIVAL);
		if(p == null) {
			return;
		}
		e.setJoinMessage(null);
		final ItemStack playerHelmet = p.getEquipment().getHelmet();
		final Material playerHelmetType = playerHelmet == null ? null : playerHelmet.getType();
		final boolean playerHasLeatherHelmet = playerHelmetType == Material.LEATHER_HELMET;
		if(p.hasPlayedBefore()) {
if(playerHasLeatherHelmet) {
				for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
					System.out.println("+" + p.getName() + " (cagoulé)");
					if(pl.hasPermission("gca.concagoule")) {
						pl.sendMessage("§4(Cagoulé)§f §a§l+ §f" + p.getName());
					}
				}
			}else {
				Bukkit.broadcastMessage("§a§l+ §f" + p.getName());
			}
		}else {
			Bukkit.broadcastMessage("§5Bienvenue §2§l" + p.getName() + " §5sur GCA");
			Bukkit.dispatchCommand(console, "lp user " + p.getName() + " parent set migrant");
			ItemStack tel = new ItemStack(Material.SHEARS);
    		ItemMeta telM = tel.getItemMeta();
    		telM.setDisplayName("§b§lTéléphone");
    		tel.setItemMeta(telM);
    	    p.getInventory().addItem(tel);
            p.getInventory().addItem(getItem(Material.PRISMARINE_SHARD,"§a90$"));
            p.getInventory().addItem(getItem(Material.EMERALD,"§a10$"));
		}
		if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType() == Material.LEATHER_HELMET) {
			score.getTeam("nhide").addEntry(p.getName());
		}
		Main.getInstance().data.createPlayer(p);
	}
	
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		@SuppressWarnings("unused")
		Consumer<String> callback = map.remove(e.getPlayer().getUniqueId());
		e.setQuitMessage(null);
		final ItemStack playerHelmet = p.getEquipment().getHelmet();
		final Material playerHelmetType = playerHelmet == null ? null : playerHelmet.getType();
		final boolean playerHasLeatherHelmet = playerHelmetType == Material.LEATHER_HELMET;
		Bukkit.getScheduler().cancelTask(taskid);
		if(p.hasPotionEffect(PotionEffectType.SLOW) && p.hasPotionEffect(PotionEffectType.BLINDNESS)) {
			p.removePotionEffect(PotionEffectType.SLOW);
		    p.removePotionEffect(PotionEffectType.BLINDNESS);
			
		}
		if(playerHasLeatherHelmet) {
			for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
				System.out.println("-" + p.getName() + " (cagoulé)");
				if(pl.hasPermission("gca.concagoule")) {
					pl.sendMessage("§4(Cagoulé)§f §c§l- §f" + p.getName());
				}
			}
		}else {
			Bukkit.broadcastMessage("§c§l- §f" + p.getName());
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		hologramUpdate();
		Player p = e.getEntity();
		Player k = p.getKiller();
		Bukkit.getScheduler().cancelTask(taskid);
		e.setDeathMessage(null);
		
		if(k == null) {
			return;
		}
		if(!(k instanceof Player)) {
			return;
		}
		
		
		
		final ItemStack playerHelmet = p.getEquipment().getHelmet();
		final ItemStack killerHelmet = k.getEquipment().getHelmet();
		
		final Material playerHelmetType = playerHelmet == null ? null : playerHelmet.getType();
		final Material killerHelmetType = killerHelmet == null ? null : killerHelmet.getType();
		
		final boolean playerHasLeatherHelmet = playerHelmetType == Material.LEATHER_HELMET;
		final boolean killerHasLeatherHelmet = killerHelmetType == Material.LEATHER_HELMET;
		
		
		Main.getInstance().data.addMorts(p.getUniqueId());
		Main.getInstance().data.addKill(k.getUniqueId());
		int rech_k = Main.getInstance().data.getRech(k.getUniqueId());
		int rech_p = Main.getInstance().data.getRech(p.getUniqueId());
		if(k.hasPermission("gca.police")) {
			Main.getInstance().data.resetRech(p.getUniqueId());
			
			if(rech_p > 0) {
				Main.getInstance().data.delRech(p.getUniqueId());;
			}
			p.sendMessage("§cVous avez été tué par §ble policier " + k.getName());
		}else {
			if(rech_k < 5) {
				Main.getInstance().data.addRech(k.getUniqueId());
			}
			k.sendMessage("! Vous êtes maintenant à §c" + Main.getInstance().data.getRech(k.getUniqueId()) + " étoiles!");
		}
		if(playerHasLeatherHelmet) {
			if(killerHasLeatherHelmet) {
				k.sendMessage("§cVous avez tué un inconnu");
				p.sendMessage("§cVous avez été tué par un inconnu");
				return;
			}else {
				k.sendMessage("§cVous avez tué un inconnu");
				p.sendMessage("§cVous avez été tué par " + k.getName());
				Bukkit.broadcastMessage("§cUn inconnu a été tué par " + k.getName());
			}
		}else {
			if(killerHasLeatherHelmet) {
				Bukkit.broadcastMessage("§c" + p.getName() + " a été tué par un inconnu");
				k.sendMessage("§cVous avez tué " + p.getName());
				p.sendMessage("§cVous avez été tué par un inconnu");
			}else {
		        Bukkit.broadcastMessage("§c" + p.getName() + " a été tué par " + k.getName());
		        k.sendMessage("§cVous avez tué " + p.getName());
				p.sendMessage("§cVous avez été tué par " + p.getName());
			}
		}
		hologramUpdate();
	}
	@EventHandler
	public void hasItem(ArmorEquipEvent e) {
		Player p = e.getPlayer();
		ItemStack old = e.getOldArmorPiece();
		ItemStack nouv = e.getNewArmorPiece();
		
		if(nouv != null && nouv.getType() == Material.LEATHER_HELMET) {
			p.sendMessage("§7§l[INFO] §f§aLes joueurs ne voient plus votre nom");
			Team t = score.getTeam("nhide");
			if (t == null) {
				t = score.registerNewTeam("nhide");
				t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
			}
			score.getTeam("nhide").addEntry(e.getPlayer().getName());
		}
		
		if(old != null && old.getType() == Material.LEATHER_HELMET) {
			p.sendMessage("§7§l[INFO] §f§cLes joueurs voient votre nom");
			Team t = score.getTeam("nhide");
			if (t == null) {
				t = score.registerNewTeam("nhide");
				t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
			}
			score.getTeam("nhide").removeEntry(e.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		hologramUpdate();
		
		if(e.getBlockPlaced().getType() == Material.NETHER_WARTS) {
			
			e.setCancelled(true);
		}
		if(e.getBlockPlaced().getType() == Material.COMMAND) {
			e.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Consumer<String> callback = map.remove(event.getPlayer().getUniqueId());

		if(callback != null){
		  callback.accept(event.getMessage());
		  event.setCancelled(true);
		  return;
		}
		Player player = event.getPlayer();
		
		if(radioplayer.contains(player)) {
			event.setCancelled(true);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(radioplayer.contains(p)) {
					p.sendMessage("§b[Police] §f§b§n"+player.getName() + "§f: "+ event.getMessage());
				}
			}
		}else {
			
		}
	}
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		Location spawn = new Location(p.getWorld(), 132.5, 69.5, -169.5, 0f, 0f);
		p.teleport(spawn);
	}
	
	
	public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	

	public String chat;
	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	public int taskid;
 
	
 
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack it = event.getItem();
        Action action = event.getAction();
        ItemStack it1 = player.getInventory().getChestplate();
        
        
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
        	if(player.hasPotionEffect(PotionEffectType.BLINDNESS) && player.hasPotionEffect(PotionEffectType.SLOW)) {
        		event.setCancelled(true);
        	}
        }
		
		if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			if(player.getInventory().getItemInHand().getType() == Material.SUGAR) {
				//cocaine
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 3));
				player.getInventory().getItemInHand().setAmount((player.getInventory().getItemInHand().getAmount()) - 1);
				player.sendMessage("§bVous vous sentez rapideeeee");
			}
			if(player.getInventory().getItemInHand().getType() == Material.WHEAT) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 2));
				player.getInventory().getItemInHand().setAmount((player.getInventory().getItemInHand().getAmount()) - 1);
				player.sendMessage("§aVous vous sentez trèèèèès bien");
			}
		}
		
		if(it != null && it.getType().equals(Material.PRISMARINE_CRYSTALS) && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lRadio")) {
			 if(player.hasPermission("gca.police")) {
				 if(radioplayer.contains(player)) {
					 player.sendMessage(alert+"§cVous ne parlez plus en radio §f§bPolice");
					 radioplayer.remove(player);
				 }else {
					 radioplayer.add(player);
					 player.sendMessage(alert+"§bVous parlez maintenant en radio §b§lPolice");
				 }
			 }
		 }
		if(it != null && it.getType().equals(Material.COMPASS) && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§bLocaliser")) {
			if(player.hasPermission("gca.police")) {
			event.setCancelled(true);
			Inventory menu = Bukkit.createInventory(null, 54, "§bLocalisation des joueurs");
			int i = 0;
            for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
            	double distance = pl.getLocation().distance(player.getLocation());
            	int rech = Main.getInstance().data.getRech(pl.getUniqueId());
            	if(distance > 30) {
            	if(rech == 0 && distance < 50) {
            		menu.setItem(i, showCompass(pl, distance, rech));
            		i=i+1;
            	}
            	if(rech == 1 && distance < 100) {
            		menu.setItem(i, showCompass(pl, distance, rech));
            		i=i+1;
            	}
            	if(rech == 2 && distance < 200) {
            		menu.setItem(i, showCompass(pl, distance, rech));
            		i=i+1;
            	}
            	if(rech == 3 && distance < 300) {
            		menu.setItem(i, showCompass(pl, distance, rech));
            		i=i+1;
            	}
            	if(rech == 4 && distance < 400) {
            		menu.setItem(i, showCompass(pl, distance, rech));
            		i=i+1;
            	}
            	if(rech == 5 && distance < 500) {
            		menu.setItem(i, showCompass(pl, distance, rech));
            		i=i+1;
            	}
            	}
            }
            player.openInventory(menu);
		}
		}
		if(it != null && it.getType() == Material.BLAZE_ROD && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§e§lEverything Everywhere All at Once")) {
			if(player.getName().equalsIgnoreCase("Pierronus")) {
				ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				if(Bukkit.getServer().getOnlinePlayers().size() < 55) {
					Inventory menu = Bukkit.createInventory(null, 54, "§e§lOutil universel EEAO");
					int i = 0;
					for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
						meta.setOwner(pl.getName());
						meta.setDisplayName(pl.getName());
						skull.setItemMeta(meta);
						menu.setItem(i, skull);
						i+=1;
					}
					player.closeInventory();
					player.openInventory(menu);
				}else {
					player.sendMessage("TROP DE JOUEURS (>54) CONTACTER PIERRONUS");
				}
			}
		}
		if(it != null && it.getType() == Material.SHEARS && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§b§lTéléphone")) {
			Inventory inv = Bukkit.createInventory(null, 45, "§b§lTéléphone");
			inv.setItem(14, getItems(Material.BLAZE_POWDER, "§3Skills",1));
			inv.setItem(16, getItems(Material.NETHER_STAR, "§bGuide",1));
	 
			inv.setItem(10, getItems(Material.BANNER, "§cClan",1));
			ItemStack lai = new ItemStack(Material.WOOL, 1);
			ItemMeta laiM = lai.getItemMeta();
			laiM.setDisplayName("§b§lAppeler la POLICE");
			laiM.addEnchant(Enchantment.DAMAGE_ALL, 100, true);
			laiM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			lai.setItemMeta(laiM);
			inv.setItem(29, lai);
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(player.getName());
			meta.setDisplayName("§bVotre profil");
			skull.setItemMeta(meta);
			inv.setItem(27, skull);
			ItemStack dia = new ItemStack(Material.DIAMOND_PICKAXE, 1);
			ItemMeta diaM = dia.getItemMeta();
			diaM.setDisplayName("§bMétiers");
			diaM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			dia.setItemMeta(diaM);
			inv.setItem(33, dia);
	        ItemStack ban = new ItemStack(Material.EMERALD, 1);
			ItemMeta banM = ban.getItemMeta();
			banM.setDisplayName("§aBanque");
			String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
			banM.setLore(Arrays.asList(bal));
			ban.setItemMeta(banM);
			inv.setItem(12, ban);
			
			ItemStack gm = new ItemStack(Material.PAPER);
			ItemMeta gmM  = gm.getItemMeta();
			gmM.setDisplayName("§aGoogle Maps");
			gmM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			gmM.setLore(Arrays.asList("§aGoogle Maps","§amais en mieux (troll)"));
			gm.setItemMeta(gmM);
			inv.setItem(31, gm);
			
			if(it1 != null && it1.getType() == Material.LEATHER_CHESTPLATE && it1.hasItemMeta() && it1.getItemMeta().getDisplayName().equalsIgnoreCase("§4§lGilet Kamikaze")) {
				inv.setItem(36, getItems(Material.REDSTONE_BLOCK, "§4§lACTIVER GILET KAMIKAZE",1));
			}
			ItemStack stocks = new ItemStack(Material.BOOK, 1);
			ItemMeta stocksM = stocks.getItemMeta();
			stocksM.setDisplayName("§eActions");
			stocksM.setLore(Arrays.asList("§7Achetez et revendez vos","§7actions et devenez millionaire"));
			stocks.setItemMeta(stocksM);
			inv.setItem(35, stocks);
			player.openInventory(inv);
		}
	}
	public void changePermis(Player player, Player target, boolean cond, boolean set) {
		//cond : 1 = armes, 0 = voiture
		if(cond) {
			if(set) {
				Bukkit.dispatchCommand(console, "lp user " + target.getName() + " permission set gca.armes");
			}else {
				Bukkit.dispatchCommand(console, "lp user " + target.getName() + " permission unset gca.armes");
			}
		}else {
			if(set) {
				Bukkit.dispatchCommand(console, "lp user " + target.getName() + " permission set gca.voiture");
			}else {
				Bukkit.dispatchCommand(console, "lp user " + target.getName() + " permission unset gca.voiture");
			}
		}
		Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
			@Override
			public void run() {
				openEEAO(player,target);
			}
		}, 5);
	}
	public void changeRech(Player player, Player target, boolean cond) {
		if(cond) {
			Main.getInstance().data.addRech(target.getUniqueId());
		}else {
			Main.getInstance().data.delRech(target.getUniqueId());
		}
		openEEAO(player, target);
	}
	
	public void openEEAO(Player player, Player target) {
		Inventory inv = Bukkit.createInventory(null, 54, target.getName().toString());
		int rech = Main.getInstance().data.getRech(target.getUniqueId());
		inv.setItem(36, getItem(Material.EYE_OF_ENDER, "§dNiveau de recherche"));
		
	    if(rech == 0) {
	    	inv.setItem(44, getItem(Material.WOOL, (short) 5, "§a+1",Arrays.asList(),1));
	    }
	    if(rech >= 5) {
	    inv.setItem(38, getItem(Material.WOOL, (short) 14, "§c-1",Arrays.asList(),1));
	    }
	    
	    if(rech > 0 && rech <5) {
	    	inv.setItem(38, getItem(Material.WOOL, (short) 14, "§c-1",Arrays.asList(),1));
	    	inv.setItem(44, getItem(Material.WOOL, (short) 5, "§a+1",Arrays.asList(),1));
	    }
	    
	    
		if(rech == 1) {
			inv.setItem(39, getItem(Material.NETHER_STAR, "§c§lNiveau 1"));
		}
		if(rech == 2) {
			inv.setItem(39, getItem(Material.NETHER_STAR, "§c§lNiveau 1"));
			inv.setItem(40, getItem(Material.NETHER_STAR, "§c§lNiveau 2"));
		}
		if(rech == 3) {
			inv.setItem(39, getItem(Material.NETHER_STAR, "§c§lNiveau 1"));
			inv.setItem(40, getItem(Material.NETHER_STAR, "§c§lNiveau 2"));
			inv.setItem(41, getItem(Material.NETHER_STAR, "§c§lNiveau 3"));
		}
		if(rech == 4) {
			inv.setItem(39, getItem(Material.NETHER_STAR, "§c§lNiveau 1"));
			inv.setItem(40, getItem(Material.NETHER_STAR, "§c§lNiveau 2"));
			inv.setItem(41, getItem(Material.NETHER_STAR, "§c§lNiveau 3"));
			inv.setItem(42, getItem(Material.NETHER_STAR, "§4§lNiveau 4"));
		}
		if(rech == 5) {
			inv.setItem(39, getItem(Material.NETHER_STAR, "§c§lNiveau 1"));
			inv.setItem(40, getItem(Material.NETHER_STAR, "§c§lNiveau 2"));
			inv.setItem(41, getItem(Material.NETHER_STAR, "§c§lNiveau 3"));
			inv.setItem(42, getItem(Material.NETHER_STAR, "§4§lNiveau 4"));
			inv.setItem(43, getItem(Material.NETHER_STAR, "§4§lNiveau 5"));
		}
		
		inv.setItem(45, getItem(Material.BOOK, "§d§lPermis Actuels :"));
        if(target.hasPermission("gca.armes")) {
            ItemStack arme = new ItemStack(Material.GOLD_SPADE);
            ItemMeta armeM = arme.getItemMeta();
            armeM.setDisplayName("§c§lPermis d'Armes (PdA)");
            armeM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            armeM.setLore(Arrays.asList("","§7Joueur: §c§l" + player.getName(),"§7Type: §cPetites Armes", "§7Acheté à: §a§l20 000$","","§8§o§lPossédé",""));
            arme.setItemMeta(armeM);
            inv.setItem(50, arme);
            inv.setItem(51, getItem(Material.WOOL, (short) 14, "§cEnlever le permis d'armes",Arrays.asList(),1));
        }else {
        	inv.setItem(51, getItem(Material.WOOL, (short) 5, "§aDonner le permis d'armes",Arrays.asList(),1));
        }
        if(target.hasPermission("gca.voiture")) {
            ItemStack voi = new ItemStack(Material.MINECART);
            ItemMeta voiM = voi.getItemMeta();
            voiM.setDisplayName("§6§lPermis Voiture (PV)");
            voiM.setLore(Arrays.asList("","§7Joueur: §c§l" + player.getName(),"§7Type: §6Voitures", "§7Acheté à: §a§l15 000$","","§8§o§lPossédé",""));
            voi.setItemMeta(voiM);
            inv.setItem(48, voi);
            inv.setItem(49, getItem(Material.WOOL, (short) 14, "§cEnlever le permis voiture",Arrays.asList(),1));
        }else {
        	inv.setItem(49, getItem(Material.WOOL, (short) 5, "§aDonner le permis voiture",Arrays.asList(),1));
        }
        
		player.openInventory(inv);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent e){
	    if(e.getMessage().toLowerCase().startsWith("/minecraft:kill")){
e.getPlayer().sendMessage("§cGCAPlugin : Ne pas utiliser!");
e.setCancelled(true);
	    }
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		hologramUpdate();
		InventoryAction a = event.getAction();
		Player player = (Player) event.getWhoClicked();
		ItemStack current = event.getCurrentItem();
		World world = player.getWorld();
		Location ploc = player.getLocation();
		String invtitle = player.getOpenInventory().getTitle();
		int locX = player.getLocation().getBlockX();
		int locY = player.getLocation().getBlockY();
		int locZ = player.getLocation().getBlockZ();
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(player.getName());
		meta.setDisplayName("§bVotre profil");
		skull.setItemMeta(meta);
		InventoryView banquee = player.getOpenInventory();
		if(current == null) return;
		if(current.getType() == Material.BRICK) {
			if(invtitle.equalsIgnoreCase("§cMes propriétés")) {
				event.setCancelled(true);
			}
		}
		if(current.getType() == Material.WOOL  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a+1")) {
			event.setCancelled(true);
			changeRech(player,Bukkit.getPlayer(invtitle),true);
		}
		if(current.getType() == Material.WOOL && current.getItemMeta().getDisplayName().equalsIgnoreCase("§c-1")) {
			event.setCancelled(true);
			changeRech(player,Bukkit.getPlayer(invtitle),false);
		}
		if(current.getType() == Material.WOOL && current.getItemMeta().getDisplayName().equalsIgnoreCase("§aDonner le permis d'armes")) {
			event.setCancelled(true);
			changePermis(player, Bukkit.getPlayer(invtitle), true, true);
		}
		if(current.getType() == Material.WOOL && current.getItemMeta().getDisplayName().equalsIgnoreCase("§aDonner le permis voiture")) {
			event.setCancelled(true);
			changePermis(player, Bukkit.getPlayer(invtitle), false, true);
		}
		if(current.getType() == Material.WOOL && current.getItemMeta().getDisplayName().equalsIgnoreCase("§cEnlever le permis d'armes")) {
			event.setCancelled(true);
			changePermis(player, Bukkit.getPlayer(invtitle), true, false);
		}
		if(current.getType() == Material.WOOL && current.getItemMeta().getDisplayName().equalsIgnoreCase("§cEnlever le permis voiture")) {
			event.setCancelled(true);
			changePermis(player, Bukkit.getPlayer(invtitle), false, false);
		}
		if(current.getType() == Material.SKULL_ITEM) {
			if(invtitle.equalsIgnoreCase("§e§lOutil universel EEAO")) {
				String name = current.getItemMeta().getDisplayName();
				event.setCancelled(true);
				openEEAO(player,Bukkit.getPlayer(name));
			}
		}
		
		if(current.hasItemMeta() && current.getItemMeta().hasDisplayName()) {
			
		if(a == InventoryAction.PICKUP_ALL || a == InventoryAction.PICKUP_ONE || a == InventoryAction.HOTBAR_MOVE_AND_READD ||
				a == InventoryAction.HOTBAR_SWAP || a == InventoryAction.MOVE_TO_OTHER_INVENTORY || a == InventoryAction.SWAP_WITH_CURSOR || 
				a == InventoryAction.UNKNOWN || a ==InventoryAction.PLACE_SOME || a == InventoryAction.PLACE_ONE || a == InventoryAction.PICKUP_HALF || a == InventoryAction.NOTHING 
				|| a == InventoryAction.PICKUP_SOME || a == InventoryAction.PLACE_ALL || a == InventoryAction.SWAP_WITH_CURSOR || a == InventoryAction.COLLECT_TO_CURSOR ||
				a == InventoryAction.DROP_ALL_CURSOR || a == InventoryAction.DROP_ALL_SLOT  || a == InventoryAction.DROP_ONE_CURSOR || a == InventoryAction.DROP_ONE_SLOT) {
			
			if(invtitle.equalsIgnoreCase("§aBanque") || invtitle.equalsIgnoreCase("§b§lTéléphone") ||
					invtitle.equalsIgnoreCase("§b§lTéléphone §f§8> §dProfil") || invtitle.equalsIgnoreCase("§b§lTéléphone §f§8> §bMétiers") || 
					invtitle.equalsIgnoreCase("§5Grades") || invtitle.equalsIgnoreCase("")|| invtitle.equalsIgnoreCase("§cAcheter le §c§lpermis d'armes") ||
					invtitle.equalsIgnoreCase("§6Acheter le §6§lpermis voiture") || invtitle.startsWith("§8§lInfos de") || invtitle.equalsIgnoreCase("§6Bourse") || 
					invtitle.equalsIgnoreCase("§b§lTéléphone §f§8> §aGoogle Maps") || invtitle.equalsIgnoreCase("§b§lTéléphone §f§8> §aGoogle Maps §f§8> §bMétiers") ||
					invtitle.equalsIgnoreCase("§1§lImmocraft") || invtitle.equalsIgnoreCase("§4Mineur") || invtitle.equalsIgnoreCase("§aFermier") || 
					invtitle.equalsIgnoreCase("§2Bûcheron") || invtitle.equalsIgnoreCase("§bPêcheur") || invtitle.equalsIgnoreCase("§bLocalisation des joueurs"))
			        {
				if(event.getRawSlot() > event.getInventory().getSize()) { event.setCancelled(true); }
				event.setCancelled(true);
			}
		}
		
		if(current.getType() == Material.EYE_OF_ENDER && current.getItemMeta().getDisplayName().equalsIgnoreCase("§6§lVos points GCA")) {
			event.setCancelled(true);
		}
		if(current.getType() == Material.REDSTONE_BLOCK && current.getItemMeta().getDisplayName().equalsIgnoreCase("§4§lBoutique à venir...")) {
			event.setCancelled(true);
		}
			if(current.getType() == Material.BOOK  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§eActions")) {
				player.closeInventory();
				String command = "sudo " + player.getName() + " brs";
				Bukkit.dispatchCommand(console, command);
			}
			if(current.getType() == Material.EMPTY_MAP && current.getItemMeta().getDisplayName().equalsIgnoreCase("§fComment jouer ?")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.GOLD_SPADE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§cComment obtenir les armes ?")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.PRISMARINE_SHARD && current.getItemMeta().getDisplayName().equalsIgnoreCase("§aComment payer ?")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.PRISMARINE_CRYSTALS && current.getItemMeta().getDisplayName().equalsIgnoreCase("§bComment devenir policier ?")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.BOOK && current.getItemMeta().getDisplayName().equalsIgnoreCase("§6Comment marchent les permis ?")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.SULPHUR && current.getItemMeta().getDisplayName().equalsIgnoreCase("§aComment avoir de l'argent ?")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.ENDER_PEARL && current.getItemMeta().getDisplayName().equalsIgnoreCase("§eSuggestions ?")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.DIAMOND && current.getItemMeta().getDisplayName().equalsIgnoreCase("§9§lCliquez pour rejoindre le Discord")) {
				event.setCancelled(true);
				player.closeInventory();
				player.sendMessage("https://discord.gg/QcATsUa87D");
			}
			if(current.getType() == Material.WOOL  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§b§lAppeler la POLICE")) {
				event.setCancelled(true);
				int cooldownTime = 180;
				if(cooldowns.containsKey(player.getName())) {
					 long secondsLeft = ((cooldowns.get(player.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
					 if(secondsLeft>0) {
						 player.sendMessage("§b§l[Téléphone] §bVous devez attendre §b§n"+ secondsLeft +" secondes§f §bavant d'appeler la §b§lpolice");
					 }else {
						 cooldowns.remove(player.getName());
					 }
				}
				else {
					player.closeInventory();
					player.sendMessage("Ecrivez la raison dans le chat (tout abus sera sanctionné)");
					map.put(player.getUniqueId(), message -> {
						player.sendMessage("§b§l[GCA] §bVous venez d'appeler la police.");
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(radioplayer.contains(p)) {
								p.sendMessage("§c§l" + player.getName() + " §b§lvous appelle ! Ses coordonnées :"
										+ "\n§c§lX: §c" + locX +  "§7| §c§lY: §c" + locY + " §7| §c§lZ: §c" + locZ+ "\n§fRaison: " + message);
							}
						}
						cooldowns.put(player.getName(), System.currentTimeMillis());

						});
 
 
			}
			}
			if(current.getType() == UMaterial.RAW_FISH.getMaterial()&& current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 1 poisson")) {
        		event.setCancelled(true);
        		if(player.getInventory().containsAtLeast(new ItemStack(Material.RAW_FISH,1,(short)0 ), 1)) {
        			player.getInventory().removeItem(new ItemStack(Material.RAW_FISH, 1, (short) 0));
        			Main.econ.depositPlayer(player, 50);
        			player.sendMessage(alert + "§aVous avez vendu 1 poisson");        		
        			}else {
        				player.sendMessage(alert + "Vous n'avez pas assez sur vous");
        			}
        		
        	}
            if(current.getType() == UMaterial.RAW_FISH.getMaterial()&& current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 32 poissons")) {
            	event.setCancelled(true);
        		if(player.getInventory().containsAtLeast(new ItemStack(Material.RAW_FISH,1,(short) 0), 32)) {
        			for(int i=0; i<32; i++) {
        				player.getInventory().removeItem(new ItemStack(Material.RAW_FISH, 1, (short) 0));
        			}
        			Main.econ.depositPlayer(player, 1600);
        			player.sendMessage(alert + "§aVous avez vendu 32 poissons");        		
        			}else {
        				player.sendMessage(alert + "Vous n'avez pas assez sur vous");
        			}
        	}
if(current.getType() == Material.RAW_FISH && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 1 poisson tropical")) {
	event.setCancelled(true);
	System.out.println("YES");
	if(player.getInventory().containsAtLeast(new ItemStack(Material.RAW_FISH, 1, (short) 2), 1)) {
		player.getInventory().removeItem(new ItemStack(Material.RAW_FISH, 1, (short) 2));
		Main.econ.depositPlayer(player, 125);
		player.sendMessage(alert + "§aVous avez vendu 1 poisson tropical");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
        	}

if(current.getType() == Material.RAW_FISH && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 32 poisson tropicaux")) {
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.RAW_FISH, 1, (short) 2), 32)) {
		for(int i=0; i<32; i++) {
			player.getInventory().removeItem(new ItemStack(Material.RAW_FISH, 1, (short) 2));
		}
		Main.econ.depositPlayer(player,4000);
		player.sendMessage(alert + "§aVous avez vendu 32 poissons tropicaux");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
}

if(current.getType() == UMaterial.PUFFERFISH.getMaterial()&& current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 1 poisson-globe")) {
	System.out.println("a");
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.RAW_FISH, 1, (short) 3), 1)) {
		player.getInventory().removeItem(new ItemStack(Material.RAW_FISH, 1, (short) 3));
		Main.econ.depositPlayer(player, 200);
		player.sendMessage(alert + "§aVous avez vendu 1 poisson-globe");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
}
if(current.getType() == UMaterial.PUFFERFISH.getMaterial()&& current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 32 poisson-globes")) {
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.RAW_FISH, 1, (short) 3), 32)) {
		for(int i=0; i<32; i++) {
			player.getInventory().removeItem(new ItemStack(Material.RAW_FISH, 1, (short) 3));
		}
		Main.econ.depositPlayer(player, 6400);
		player.sendMessage(alert + "§aVous avez vendu 32 poisson-globes");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
}
        	
        	if(current.getType() == Material.CARROT_ITEM  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 64 carottes")) {
        		event.setCancelled(true);
        		if(player.getInventory().containsAtLeast(new ItemStack(Material.CARROT_ITEM,1,(short) 0), 64)) {
        			for(int i = 0; i < 64; i++) {
            			player.getInventory().removeItem(new ItemStack(Material.CARROT_ITEM, 1, (short) 0));

        			}
        			Main.econ.depositPlayer(player, 16);
        			player.sendMessage(alert + "§aVous avez vendu 64 carottes");        		
        			}else {
        				player.sendMessage(alert + "Vous n'avez pas assez sur vous");
        			}
        		
        	}
        	if(current.getType() == Material.CARROT_ITEM && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 32 carottes")) {
        		event.setCancelled(true);
        		if(player.getInventory().containsAtLeast(new ItemStack(Material.CARROT_ITEM,1,(short) 0), 32)) {
        			for(int i = 0; i < 32; i++) {
            			player.getInventory().removeItem(new ItemStack(Material.CARROT_ITEM, 1, (short) 0));

        			}
        			Main.econ.depositPlayer(player, 8);
        			player.sendMessage(alert + "§aVous avez vendu 32 carottes");        		
        			}else {
        				player.sendMessage(alert + "Vous n'avez pas assez sur vous");
        			}
        		
        	}
        	if(current.getType() == Material.CARROT_ITEM && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 4 carottes")) {
        		event.setCancelled(true);
        		if(player.getInventory().containsAtLeast(new ItemStack(Material.CARROT_ITEM,1,(short) 0), 4)) {
        			for(int i = 0; i < 4; i++) {
            			player.getInventory().removeItem(new ItemStack(Material.CARROT_ITEM, 1, (short) 0));

        			}
        			Main.econ.depositPlayer(player, 1);
        			player.sendMessage(alert + "§aVous avez vendu 4 carottes");        		
        			}else {
        				player.sendMessage(alert + "Vous n'avez pas assez sur vous");
        			}
        		
        	}
        	if(current.getType() == Material.DIAMOND_PICKAXE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§bAcheter une pioche")) {
        		if(Main.econ.getBalance(player) >= 200) {
					if(player.getInventory().firstEmpty() == -1) {
						player.sendMessage(alert + "§cVotre inventaire est plein");
					}else {
						Main.econ.withdrawPlayer(player, 200);
                        player.getInventory().addItem(new ItemStack(Material.DIAMOND_PICKAXE, 1));
					}
				}else {
					player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
				}
        	}
        	if(current.getType() == Material.DIAMOND_AXE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§bAcheter une hache")) {
        		if(Main.econ.getBalance(player) >= 200) {
					if(player.getInventory().firstEmpty() == -1) {
						player.sendMessage(alert + "§cVotre inventaire est plein");
					}else {
						Main.econ.withdrawPlayer(player, 200);
                        player.getInventory().addItem(new ItemStack(Material.DIAMOND_AXE, 1));
					}
				}else {
					player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
				}
        	}
        	if(current.getType() == Material.FISHING_ROD && current.getItemMeta().getDisplayName().equalsIgnoreCase("§bAcheter une canne à pêche")) {
        		if(Main.econ.getBalance(player) >= 200) {
					if(player.getInventory().firstEmpty() == -1) {
						player.sendMessage(alert + "§cVotre inventaire est plein");
					}else {
						Main.econ.withdrawPlayer(player, 200);
                        player.getInventory().addItem(new ItemStack(Material.FISHING_ROD, 1));
					}
				}else {
					player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
				}
        	}
        	
        	if(current.getType() == UMaterial.ACACIA_LOG.getMaterial() && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 1 Buche d'acacia")) {
        		event.setCancelled(true);
        		if(player.getInventory().containsAtLeast(new ItemStack(Material.LOG_2,1,(short) 0), 1)) {
        			player.getInventory().removeItem(new ItemStack(Material.LOG_2, 1, (short) 0));
        			Main.econ.depositPlayer(player, 2);
        			player.sendMessage(alert + "§aVous avez vendu 1 buche d'acacia");        		
        			}else {
        				player.sendMessage(alert + "Vous n'avez pas assez sur vous");
        			}
        		
        	}
            if(current.getType() == UMaterial.ACACIA_LOG.getMaterial() && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 32 Buche d'acacia")) {
            	event.setCancelled(true);
        		if(player.getInventory().containsAtLeast(new ItemStack(Material.LOG_2,1,(short) 0), 32)) {
        			for(int i=0; i<32; i++) {
        				player.getInventory().removeItem(new ItemStack(Material.LOG_2, 1, (short) 0));
        			}
        			Main.econ.depositPlayer(player, 64);
        			player.sendMessage(alert + "§aVous avez vendu 32 buches d'acacia");        		
        			}else {
        				player.sendMessage(alert + "Vous n'avez pas assez sur vous");
        			}
        	}
if(current.getType() == UMaterial.SPRUCE_LOG.getMaterial() && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 1 Buche de sapin")) {
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.LOG, 1, (short) 1), 1)) {
		player.getInventory().removeItem(new ItemStack(Material.LOG, 1, (short) 1));
		Main.econ.depositPlayer(player, 8);
		player.sendMessage(alert + "§aVous avez vendu 1 buche de sapin");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
        	}
if(current.getType() == UMaterial.SPRUCE_LOG.getMaterial() && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 32 Buche de sapin")) {
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.LOG, 1, (short) 1), 32)) {
		for(int i=0; i<32; i++) {
			player.getInventory().removeItem(new ItemStack(Material.LOG, 1, (short) 1));
		}
		Main.econ.depositPlayer(player, 256);
		player.sendMessage(alert + "§aVous avez vendu 32 buches de sapin");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
}
if(current.getType() == UMaterial.BIRCH_LOG.getMaterial() && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 1 Buche de bouleau")) {
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.LOG, 1, (short) 2), 1)) {
		player.getInventory().removeItem(new ItemStack(Material.LOG, 1, (short) 2));
		Main.econ.depositPlayer(player, 16);
		player.sendMessage(alert + "§aVous avez vendu 1 buche de bouleau");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
}
if(current.getType() == UMaterial.BIRCH_LOG.getMaterial() && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 32 Buche de bouleau")) {
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.LOG, 1, (short) 2), 32)) {
		for(int i=0; i<32; i++) {
			player.getInventory().removeItem(new ItemStack(Material.LOG, 1, (short) 2));
		}
		Main.econ.depositPlayer(player, 512);
		player.sendMessage(alert + "§aVous avez vendu 32 buches de bouleau");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
}
        	if(current.getType() == Material.COAL_ORE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 1 Minerai de Charbon")) {
        		event.setCancelled(true);
        		if(player.getInventory().containsAtLeast(new ItemStack(Material.COAL_ORE), 1)) {
        			player.getInventory().removeItem(new ItemStack(Material.COAL_ORE, 1));
        			Main.econ.depositPlayer(player, 2);
        			player.sendMessage(alert + "§aVous avez vendu 1 minerai de charbon");        		
        			}else {
        				player.sendMessage(alert + "Vous n'avez pas assez sur vous");
        			}
        		
        	}
            if(current.getType() == Material.COAL_ORE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 32 Minerais de Charbon")) {
            	event.setCancelled(true);
        		if(player.getInventory().containsAtLeast(new ItemStack(Material.COAL_ORE), 32)) {
        			for(int i=0; i<32; i++) {
        				player.getInventory().removeItem(new ItemStack(Material.COAL_ORE, 1));
        			}
        			Main.econ.depositPlayer(player, 64);
        			player.sendMessage(alert + "§aVous avez vendu 32 minerais de charbon");        		
        			}else {
        				player.sendMessage(alert + "Vous n'avez pas assez sur vous");
        			}
        	}
if(current.getType() == Material.IRON_ORE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 1 Minerai de Fer")) {
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.IRON_ORE), 1)) {
		player.getInventory().removeItem(new ItemStack(Material.IRON_ORE, 1));
		Main.econ.depositPlayer(player, 8);
		player.sendMessage(alert + "§aVous avez vendu 1 minerai de fer");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
        	}
if(current.getType() == Material.IRON_ORE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 32 Minerais de Fer")) {
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.IRON_ORE), 32)) {
		for(int i=0; i<32; i++) {
			player.getInventory().removeItem(new ItemStack(Material.IRON_ORE, 1));
		}
		Main.econ.depositPlayer(player, 256);
		player.sendMessage(alert + "§aVous avez vendu 32 minerais de fer");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
}
if(current.getType() == Material.EMERALD_ORE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 1 Minerai d'Emeraude")) {
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.EMERALD_ORE), 1)) {
		player.getInventory().removeItem(new ItemStack(Material.EMERALD_ORE, 1));
		Main.econ.depositPlayer(player, 16);
		player.sendMessage(alert + "§aVous avez vendu 1 minerai d'émeraude");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
}
if(current.getType() == Material.EMERALD_ORE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lVendre 32 Minerais d'Emeraude")) {
	event.setCancelled(true);
	if(player.getInventory().containsAtLeast(new ItemStack(Material.EMERALD_ORE), 32)) {
		for(int i=0; i<32; i++) {
			player.getInventory().removeItem(new ItemStack(Material.EMERALD_ORE, 1));
		}
		Main.econ.depositPlayer(player, 512);
		player.sendMessage(alert + "§aVous avez vendu 32 minerais d'émeraude");        		
		}else {
			player.sendMessage(alert + "Vous n'avez pas assez sur vous");
		}
}
			if(current.getType() == Material.BLAZE_POWDER  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§3Skills")) {
				event.setCancelled(true);
				player.closeInventory();
				String command = "sudo " + player.getName() + " skills";
				Bukkit.dispatchCommand(console, command);
			}
			if(current.getType().equals(Material.BOOK) && current.getItemMeta().getDisplayName().equalsIgnoreCase("§d§lPermis Actuels :")) {
                event.setCancelled(true);
                
            }
			
            if(current.getType() == Material.GOLD_SPADE  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lPermis d'Armes (PdA)")) {
                event.setCancelled(true);
            }
            if(current.getType() == Material.MINECART  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§6§lPermis Voiture (PV)")) {
                event.setCancelled(true);
            }
			if(current.getType() == Material.EMERALD  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§aBanque")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.GOLD_SPADE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lArmurerie de Greenfield")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 3, 69, -227);
				
				List<Location> points = Arrays.asList(one);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§cL'§c§larmurerie §f§7la plus proche est à §c§l"+ String.valueOf(dis) + " §f§cblocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				
				
			}
			if(current.getType() == Material.MINECART && current.getItemMeta().getDisplayName().equalsIgnoreCase("§6§lConcessionnaire de Greenfield")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), -46, 69, -164);
				
				List<Location> points = Arrays.asList(one);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§6Le §6§lConcessionnaire §f§7le plus proche est à §6§l"+ String.valueOf(dis) + " §f§6blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.EMERALD_BLOCK && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lBanques de Greenfield (10)")) {
				event.setCancelled(true);
				Location one = new Location(player.getWorld(), -64, 69, -202);
				Location two = new Location(player.getWorld(), 229, 69, -306);
				Location three = new Location(player.getWorld(), 747, 69, 55);
				Location four = new Location(player.getWorld(), 245, 69, 1103);
				Location five = new Location(player.getWorld(), -338, 69, 417);
				Location six = new Location(player.getWorld(), 446, 69, -575);
				Location seven = new Location(player.getWorld(), -424, 69, -148);
				Location eight = new Location(player.getWorld(), -617, 69, -425);
				Location nine = new Location(player.getWorld(), 75, 69, -453);
				Location ten = new Location(player.getWorld(), -3, 71, 56);
				
				//747 69 55
				//245 69 1103
				//-338 69 417
				//446 69 -575
				//-424 69 -148
				//-617 69 -425
				//75 69 -453
				//-3 71 56
				
				List<Location> points = Arrays.asList(one, two, three, four, five, six, seven, eight, nine, ten);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§aLa §a§lBanque §f§7la plus proche est à §a§l"+ String.valueOf(dis) + " §f§ablocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
			}
			if(current.getType() == Material.WOOL && current.getItemMeta().getDisplayName().equalsIgnoreCase("§1§lCommissariat de Greenfield")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 79, 69, -224);
				
				List<Location> points = Arrays.asList(one);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§1Le §1§lCommissariat §f§7le plus proche est à §1§l"+ String.valueOf(dis) + " §f§1blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.GOLD_BLOCK && current.getItemMeta().getDisplayName().equalsIgnoreCase("§e§lBourse")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 48, 69, -1);
				
				List<Location> points = Arrays.asList(one);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§eLa §e§lBourse §f§7la plus proche est à §e§l"+ String.valueOf(dis) + " §f§eblocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.DIAMOND_PICKAXE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§b§lMétiers")) {
				event.setCancelled(true);
				Inventory metiers = Bukkit.createInventory(null, 9, "§b§lTéléphone §f§8> §aGoogle Maps §f§8> §bMétiers");
				metiers.setItem(1, getItemwLore(Material.DIAMOND_PICKAXE, "§4§lMine", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§4X:-152 | Y:69 | Z:19")));
				metiers.setItem(3, getItemwLore(Material.DIAMOND_AXE, "§2§lForêt", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§2X:-285 | Y:69 | Z:-90")));
				metiers.setItem(5, getItemwLore(Material.DIAMOND_HOE, "§a§lFerme", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§aX:-97 | Y:69 | Z:277")));
				metiers.setItem(7, getItemwLore(Material.FISHING_ROD, "§b§lJetée", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§bX:395 | Y:65 | Z:-121")));
				player.openInventory(metiers);
			}
			if(current.getType() == Material.FISHING_ROD && current.getItemMeta().getDisplayName().equalsIgnoreCase("§b§lJetée")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 395, 65, -121);
				
				List<Location> points = Arrays.asList(one);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§bLa §b§lJetée §f§7la plus proche est à §b§l"+ String.valueOf(dis) + " §f§bblocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.DIAMOND_HOE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lFerme")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), -97, 69, 277);
				
				List<Location> points = Arrays.asList(one);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§aLa §a§lFerme §f§7la plus proche est à §a§l"+ String.valueOf(dis) + " §f§ablocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.BOOK && current.getItemMeta().getDisplayName().equalsIgnoreCase("§9§lMusée d'armement")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 113, 71, -1);
				
				List<Location> points = Arrays.asList(one);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§9Le §9§lMusée d'armement §f§7le plus proche est à §9§l"+ String.valueOf(dis) + " §f§9blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			
			if(current.getType() == Material.DIAMOND_AXE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§2§lForêt")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), -285, 69, -90);
				
				List<Location> points = Arrays.asList(one);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§2La §2§lForêt §f§7la plus proche est à §2§l"+ String.valueOf(dis) + " §f§2blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.DIAMOND_PICKAXE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§4§lMine")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), -152, 69, 19);
				
				List<Location> points = Arrays.asList(one);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§4La §4§lMine §f§7la plus proche est à §4§l"+ String.valueOf(dis) + " §f§4blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.BRICK && current.getItemMeta().getDisplayName().equalsIgnoreCase("§d§lTrouver un appartement près de vous")) {
				event.setCancelled(true);
				Inventory immocraft = Bukkit.createInventory(null, 27, "§1§lImmocraft");
				immocraft.setItem(0, getItemwLore(Material.NETHERRACK, "§c§lLe Rouge §f§7Appartements", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§cX:34 | Y:69 | Z:-203","§cX:115 | Y:69 | Z:-306","§cX:149 | Y:69 | Z:-464")));
				immocraft.setItem(2, getItemwLore(Material.END_ROD, "§f§lKingston Towers §f§7Appartements", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§fX:69 | Y:69 | Z:-266","§fX:-26 | Y:69 | Z:-64","§fX:117 | Y:69 | Z:-347")));
				immocraft.setItem(4, getItemwLore(Material.LEAVES, "§a§lMercure §f§7Appartements", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§aX:69 | Y:69 | Z:-286","§aX:143 | Y:69 | Z:-48","§aX:292 | Y:69 | Z:-173")));
				immocraft.setItem(6, getItemwLore(Material.SEA_LANTERN, "§3§lHilton §f§7Appartements", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§3X:-55 | Y:69 | Z:-174","§3X:119 | Y:69 | Z:-236","§3X:212 | Y:69 | Z:-432","§3X:140 | Y:69 | Z:-639")));
				immocraft.setItem(8, getItemwLore(Material.REDSTONE_BLOCK, "§4§lComfotel §f§7Appartements", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§4X:2 | Y:69 | Z:-120","§4X:328 | Y:69 | Z:-173","§4X:85 | Y:69 | Z:-647","§4X:117 | Y:69 | Z:-637","§4X:89 | Y:69 | Z:-593")));
				immocraft.setItem(18, getItemwLore(Material.NETHER_STAR, "§c§lST§f§3§lA§f§c§lR §f§7Appartements", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§cX:5 | Y:70 | Z:-51","§cX:217 | Y:69 | Z:-131","§cX:117 | Y:69 | Z:-484","§cX:201 | Y:69 | Z:-664")));
				immocraft.setItem(20, getItemwLore(Material.WATER_BUCKET, "§1§lBlue §f§0§lOrigins §f§7Appartements", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§1X:113 | Y:69 | Z:-132","§1X:10 | Y:69 | Z:-133","§1X:74 | Y:69 | Z:-108")));
				immocraft.setItem(22, getItemwLore(Material.BED, "§d§lSofytel §f§7Appartements", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§dX:235 | Y:69 | Z:-175","§dX:69 | Y:69 | Z:-323","§dX:141 | Y:69 | Z:-499")));
				immocraft.setItem(24, getItemwLore(Material.SANDSTONE, "§6§lLe Care §f§7Appartements", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§6X:142 | Y:69 | Z:-332","§6X:117 | Y:69 | Z:-390")));
				immocraft.setItem(26, getItemwLore(Material.EYE_OF_ENDER, "§5§lObscurity §f§7Appartements", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§5X:141 | Y:69 | Z:-402","§5X:66 | Y:69 | Z:-657")));
				player.openInventory(immocraft);
				
				
			}
			if(current.getType() == Material.EYE_OF_ENDER && current.getItemMeta().getDisplayName().equalsIgnoreCase("§5§lObscurity §f§7Appartements")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 141, 69, -402);
				Location two = new Location(player.getWorld(), 66, 69, -657);
				List<Location> points = Arrays.asList(one, two);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§5Le bâtiment §5§lObscurity §f§7la plus proche est à §5§l"+ String.valueOf(dis) + " §f§5blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.SANDSTONE && current.getItemMeta().getDisplayName().equalsIgnoreCase("§6§lLe Care §f§7Appartements")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 142, 69, -332);
				Location two = new Location(player.getWorld(), 117, 69, -390);
				List<Location> points = Arrays.asList(one, two);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§6Le bâtiment §6§lLe Care §f§7la plus proche est à §6§l"+ String.valueOf(dis) + " §f§6blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.BED && current.getItemMeta().getDisplayName().equalsIgnoreCase("§d§lSofytel §f§7Appartements")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 235, 69, -175);
				Location two = new Location(player.getWorld(), 69, 69, -323);
				Location three = new Location(player.getWorld(), 141, 69, -499);
				List<Location> points = Arrays.asList(one, two, three);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§dLe bâtiment §d§lSofytel §f§7la plus proche est à §d§l"+ String.valueOf(dis) + " §f§dblocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.WATER_BUCKET && current.getItemMeta().getDisplayName().equalsIgnoreCase("§1§lBlue §f§0§lOrigins §f§7Appartements")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 113, 69, -132);
				Location two = new Location(player.getWorld(), 10, 69, -133);
				Location three = new Location(player.getWorld(), 74, 69, -108);
				List<Location> points = Arrays.asList(one, two, three);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§9Le bâtiment §1§lBlue §f§0Origins §f§7la plus proche est à §9§l"+ String.valueOf(dis) + " §f§9blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.NETHER_STAR && current.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lST§f§3§lA§f§c§lR §f§7Appartements")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 5, 70, -51);
				Location two = new Location(player.getWorld(), 217, 69, -131);
				Location three = new Location(player.getWorld(), 117, 69, -484);
				Location four = new Location(player.getWorld(), 201, 69, -6364);
				List<Location> points = Arrays.asList(one, two, three, four);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§cLe bâtiment §c§lST§f§3§lA§f§c§lR §f§7la plus proche est à §c§l"+ String.valueOf(dis) + " §f§cblocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.REDSTONE_BLOCK && current.getItemMeta().getDisplayName().equalsIgnoreCase("§4§lComfotel §f§7Appartements")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 2, 69, -120);
				Location two = new Location(player.getWorld(), 328, 69, -173);
				Location three = new Location(player.getWorld(), 85, 69, -647);
				Location four = new Location(player.getWorld(), 117, 69, -637);
				Location five = new Location(player.getWorld(), 89, 69, -593);
				List<Location> points = Arrays.asList(one, two, three, four, five);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§4Le bâtiment §4§lComfortel §f§7la plus proche est à §4§l"+ String.valueOf(dis) + " §f§4blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.SEA_LANTERN && current.getItemMeta().getDisplayName().equalsIgnoreCase("§3§lHilton §f§7Appartements")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), -55, 69, -174);
				Location two = new Location(player.getWorld(), 119, 69, -236);
				Location three = new Location(player.getWorld(), 212, 69, -432);
				Location four = new Location(player.getWorld(), 140, 69, -639);
				
				List<Location> points = Arrays.asList(one, two, three, four);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§3Le bâtiment §3§lHilton §f§7la plus proche est à §3§l"+ String.valueOf(dis) + " §f§3blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.LEAVES && current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lMercure §f§7Appartements")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 69, 69, -286);
				Location two = new Location(player.getWorld(), 143, 69, -48);
				Location three = new Location(player.getWorld(), 292, 69, -173);
				
				List<Location> points = Arrays.asList(one, two, three);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				player.sendMessage("§aLe bâtiment §a§lMercure §f§7la plus proche est à §a§l"+ String.valueOf(dis) + " §f§ablocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.END_ROD && current.getItemMeta().getDisplayName().equalsIgnoreCase("§f§lKingston Towers §f§7Appartements")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 69, 69, -266);
				Location two = new Location(player.getWorld(), -26, 69, -64);
				Location three = new Location(player.getWorld(), 117, 69, -347);
				
				List<Location> points = Arrays.asList(one, two, three);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§fLe bâtiment §f§lKingston §f§7la plus proche est à §f§l"+ String.valueOf(dis) + " §fblocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.NETHERRACK && current.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lLe Rouge §f§7Appartements")) {
				event.setCancelled(true);
				
				Location one = new Location(player.getWorld(), 34, 69, -203);
				Location two = new Location(player.getWorld(), 115, 69, -306);
				Location three = new Location(player.getWorld(), 149, 69, -464);
				
				List<Location> points = Arrays.asList(one, two, three);
				Location closest = closest(ploc, points);
				int closX = closest.getBlockX();
				int closY = closest.getBlockY();
				int closZ = closest.getBlockZ();
				
				double distance = ploc.distance(closest);
				int dis = (int) Math.round(distance);
				
				
				player.sendMessage("§cLe bâtiment §c§lLe Rouge §f§7la plus proche est à §c§l"+ String.valueOf(dis) + " §f§cblocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
				player.closeInventory();
				
			}
			if(current.getType() == Material.PAPER && current.getItemMeta().getDisplayName().equalsIgnoreCase("§aGoogle Maps")) {
				event.setCancelled(true);
				Inventory gm = Bukkit.createInventory(null, 54, "§b§lTéléphone §f§8> §aGoogle Maps");
				gm.setItem(10, getItemwLore(Material.GOLD_SPADE, "§c§lArmurerie de Greenfield",Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§cX:3 | Y:69 | Z:-227")));
				gm.setItem(13, getItemwLore(Material.EMERALD_BLOCK, "§a§lBanques de Greenfield (10)",Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§aX:-64 | Y:69 | Z:-202","§aX:229 | Y:69 | Z:-306"
						,"§aX:747 | Y:69 | Z:55","§aX:245 | Y:69 | Z:1103","§aX:-338 | Y:69 | Z:417","§aX:446 | Y:69 | Z:-575",
						"§aX:-424 | Y:69 | Z:-148","§aX:-617 | Y:69 | Z:-425","§aX:75 | Y:69 | Z:-453","§aX:-3 | Y:71 | Z:56")));
				//747 69 55
				//245 69 1103
				//-338 69 417
				//446 69 -575
				//-424 69 -148
				//-617 69 -425
				//75 69 -453
				//-3 71 56
				gm.setItem(16, getItemwLore(Material.MINECART, "§6§lConcessionnaire de Greenfield",Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§6X:-46 | Y:69 | Z:-164")));
				gm.setItem(28, getItemstackwLore(getItemstack(Material.WOOL, (byte) 3), "§1§lCommissariat de Greenfield", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§bX:79 | Y:69 | X:-224")));
				gm.setItem(31, getItemwLore(Material.DIAMOND_PICKAXE, "§b§lMétiers", Arrays.asList("§7Clique pour voir la liste des métiers")));
				gm.setItem(34, getItemwLore(Material.GOLD_BLOCK, "§e§lBourse", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§eX:49 | Y:69 | X:-1")));
				gm.setItem(46, getItemwLore(Material.BOOK, "§9§lMusée d'armement", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§9X:113 | Y:71 | Z:-1")));
				gm.setItem(49, getItemwLore(Material.IRON_FENCE, "§7§lMaison d'arrêt de Greenfield", Arrays.asList("","§cCliquez pour voir l'endroit le plus proche","","§7X:-332 | Y:69 | Z:-222")));
				gm.setItem(52, getItemwLore(Material.BRICK, "§d§lTrouver un appartement près de vous", Arrays.asList("§7Cliquez pour voir la liste des immeubles")));
				gm.setItem(53, getItemwLore(Material.LOG, "§cMes propriétés",Arrays.asList("§7Cliquez pour voir la liste de vos propriétés")));
				//mettre musees (armement par ex)
				//et faire en damier si pas de place
				//pour metiers : open inventory et mettre chaque emplacement de chaque metier
				player.openInventory(gm);
			}

	if(current.getType() == Material.IRON_FENCE && current.hasItemMeta() && current.getItemMeta().getDisplayName().equalsIgnoreCase("§7§lMaison d'arrêt de Greenfield")) {
		//§7X:-332 | Y:69 | Z:-222
		event.setCancelled(true);
		
		Location one = new Location(player.getWorld(), -332, 69, -222);
		
		List<Location> points = Arrays.asList(one);
		Location closest = closest(ploc, points);
		int closX = closest.getBlockX();
		int closY = closest.getBlockY();
		int closZ = closest.getBlockZ();
		
		double distance = ploc.distance(closest);
		int dis = (int) Math.round(distance);
		
		
		player.sendMessage("§7La §7§lMaison d'arrêt §f§7la plus proche est à §7§l"+ String.valueOf(dis) + " §f§7blocks" + "\n§c§lX: §c" + closX +  "§7 | §c§lY: §c" + closY + " §7| §c§lZ: §c" + closZ);
		player.closeInventory();
	}
	if(current.getType() == Material.LOG && current.getItemMeta().getDisplayName().equalsIgnoreCase("§cMes propriétés")) {
		event.setCancelled(true);
		Inventory inv = Bukkit.createInventory(null, 54, "§cMes propriétés");
		int i=0;
		for(GeneralRegion rg: areaShop.getFileManager().getRegions()) {
			if(rg.getOwner() != null) {
				if(player.getUniqueId().toString().equalsIgnoreCase(rg.getOwner().toString())) {
					List<Location> loclist = rg.getSignsFeature().getSignLocations();
					Location loc = loclist.get(0);
					inv.setItem(i, getItemwLore(Material.BRICK, rg.getName(), Arrays.asList("§fX:" + loc.getBlockX() + ", Y:" + loc.getBlockY() + ", Z:" + loc.getBlockZ())));
					i+=1;
				}
			}
		}
		player.openInventory(inv);
	}
	if(current.getType() == Material.NETHER_STAR  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§bGuide")) {
		event.setCancelled(true);
		player.closeInventory();
		String command = "sudo " + player.getName() + " guide";
		Bukkit.dispatchCommand(console, command);
	}
			if(current.getType() == Material.BANNER  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§cClan")) {
				event.setCancelled(true);
				player.closeInventory();
				String command = "sudo " + player.getName() + " clan";
				Bukkit.dispatchCommand(console, command);
			}
			
			if(current.getType() == Material.SEA_LANTERN  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§bNos réseaux")) {
				event.setCancelled(true);
				player.closeInventory();
				player.sendMessage("§9§lDiscord : §fhttps://discord.gg/QcATsUa87D");
				player.sendMessage("§f§lSite web: §fhttps://grandcraftauto.mtxserv.com/");
			}
			if(current.getType() == Material.PAPER  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§dPermis")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.SKULL_ITEM  &&current.getItemMeta().getDisplayName().equalsIgnoreCase("§bVotre profil")) {
				event.setCancelled(true);
				Inventory inv = Bukkit.createInventory(null, 27, "§b§lTéléphone §f§8> §dProfil");
				
				
				if(player.hasPermission("gca.migrant")) {grade = "§7Migrant";       } //§7Migrant
				if(player.hasPermission("gca.voyou")) {grade = "§aVoyou";       } //§a§lVoyou
				if(player.hasPermission("gca.hll")) {grade = "§5Hors-la-Loi"  ;     } //§5§lHors-la-Loi
				if(player.hasPermission("gca.contrebandier")) {grade = "§6Contrebandier";       } //§6§lContrebandier
				if(player.hasPermission("gca.braqueur")) {grade = "§1Braqueur";       } //§1§lBraqueur
				if(player.hasPermission("gca.gangster")) {grade = "§cGangster";       } //§c§lGangster
				if(player.hasPermission("gca.mafieux")) {grade = "§dMafieux";       } //§d§lMafieux
				
				if(player.getName().equalsIgnoreCase("Scrym")|| player.getName().equalsIgnoreCase("Pierronus")) {
					grade = "§4§l" + player.getName();
				}
				inv.setItem(1, getItem(Material.IRON_HELMET, String.valueOf(grade)));
				
				inv.setItem(10, getItem(Material.EYE_OF_ENDER, "§dNiveau de recherche"));
			    int rech = Main.getInstance().data.getRech(player.getUniqueId());
			    
				if(rech == 1) {
					inv.setItem(12, getItem(Material.NETHER_STAR, "§c§lNiveau 1"));
				}
				if(rech == 2) {
					inv.setItem(12, getItem(Material.NETHER_STAR, "§c§lNiveau 1"));
					inv.setItem(13, getItem(Material.NETHER_STAR, "§c§lNiveau 2"));
				}
				if(rech == 3) {
					inv.setItem(12, getItem(Material.NETHER_STAR, "§c§lNiveau 1"));
					inv.setItem(13, getItem(Material.NETHER_STAR, "§c§lNiveau 2"));
					inv.setItem(14, getItem(Material.NETHER_STAR, "§c§lNiveau 3"));
				}
				if(rech == 4) {
					inv.setItem(12, getItem(Material.NETHER_STAR, "§c§lNiveau 1"));
					inv.setItem(13, getItem(Material.NETHER_STAR, "§c§lNiveau 2"));
					inv.setItem(14, getItem(Material.NETHER_STAR, "§c§lNiveau 3"));
					inv.setItem(15, getItem(Material.NETHER_STAR, "§4§lNiveau 4"));
				}
				if(rech == 5) {
					inv.setItem(12, getItem(Material.NETHER_STAR, "§c§lNiveau 1"));
					inv.setItem(13, getItem(Material.NETHER_STAR, "§c§lNiveau 2"));
					inv.setItem(14, getItem(Material.NETHER_STAR, "§c§lNiveau 3"));
					inv.setItem(15, getItem(Material.NETHER_STAR, "§4§lNiveau 4"));
					inv.setItem(16, getItem(Material.NETHER_STAR, "§4§lNiveau 5"));
				}
				
				inv.setItem(19, getItem(Material.BOOK, "§d§lPermis Actuels :"));
                if(player.hasPermission("gca.armes")) {
                    ItemStack arme = new ItemStack(Material.GOLD_SPADE);
                    ItemMeta armeM = arme.getItemMeta();
                    armeM.setDisplayName("§c§lPermis d'Armes (PdA)");
                    armeM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    armeM.setLore(Arrays.asList("","§7Joueur: §c§l" + player.getName(),"§7Type: §cPetites Armes", "§7Acheté à: §a§l20 000$","","§8§o§lPossédé",""));
                    arme.setItemMeta(armeM);
                    inv.setItem(21, arme);
                }
                if(player.hasPermission("gca.voiture")) {
                    ItemStack voi = new ItemStack(Material.MINECART);
                    ItemMeta voiM = voi.getItemMeta();
                    voiM.setDisplayName("§6§lPermis Voiture (PV)");
                    voiM.setLore(Arrays.asList("","§7Joueur: §c§l" + player.getName(),"§7Type: §6Voitures", "§7Acheté à: §a§l15 000$","","§8§o§lPossédé",""));
                    voi.setItemMeta(voiM);
                    inv.setItem(22, voi);
                }
				
				player.openInventory(inv);
				
				
			}
 
			if(current.getType() == Material.EMERALD  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§aDéposer 10$")) {
				event.setCancelled(true);
				
				//depot 10
				if(player.getInventory().containsAtLeast(getItem(Material.EMERALD, "§a10$"), 1)) {
					player.getInventory().removeItem(getItem(Material.EMERALD, "§a10$"));
					Main.econ.depositPlayer(player, 10);
					ItemStack ban = new ItemStack(Material.EMERALD, 1);
					ItemMeta banM = ban.getItemMeta();
					banM.setDisplayName("§aBanque");
					String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
					banM.setLore(Arrays.asList(bal));
					ban.setItemMeta(banM);
					banquee.setItem(31, ban);
				}else {
					player.sendMessage(alert + "§cVous n'avez pas ça sur vous");
				}
			}
			if(current.getType() == Material.SULPHUR  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§aDéposer 10 000$")) {
				event.setCancelled(true);
				//depot 10k
				ItemStack emerald = new ItemStack(Material.SULPHUR);
                ItemMeta emeraldM = emerald.getItemMeta();
                emeraldM.setDisplayName("§210 000$");
                emeraldM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                emeraldM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                emeraldM.addEnchant(Enchantment.DEPTH_STRIDER, 1, false);
                emerald.setItemMeta(emeraldM);
                if(player.getInventory().containsAtLeast(emerald, 1)) {
					player.getInventory().removeItem(emerald);
					Main.econ.depositPlayer(player, 10000);
					ItemStack ban = new ItemStack(Material.EMERALD, 1);
					ItemMeta banM = ban.getItemMeta();
					banM.setDisplayName("§aBanque");
					String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
					banM.setLore(Arrays.asList(bal));
					ban.setItemMeta(banM);
					banquee.setItem(31, ban);
				}else {
					player.sendMessage(alert+"§cVous n'avez pas ça sur vous");
				}
			}
			if(current.getType() == Material.PRISMARINE_SHARD  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§aDéposer 90$")) {
				event.setCancelled(true);
				//depot 90
				if(player.getInventory().containsAtLeast(getItem(Material.PRISMARINE_SHARD, "§a90$"), 1)) {
					player.getInventory().removeItem(getItem(Material.PRISMARINE_SHARD, "§a90$"));
					Main.econ.depositPlayer(player, 90);
					ItemStack ban = new ItemStack(Material.EMERALD, 1);
					ItemMeta banM = ban.getItemMeta();
					banM.setDisplayName("§aBanque");
					String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
					banM.setLore(Arrays.asList(bal));
					ban.setItemMeta(banM);
					banquee.setItem(31, ban);
				}else {
					player.sendMessage(alert+"§cVous n'avez pas ça sur vous");
				}
			}
			
			
			
			double money = Main.econ.getBalance(player.getName());
			if(current.getType() == Material.EMERALD  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§cRetirer 10$")) {
				event.setCancelled(true);
				//retrait 10
                
				
				if(money >= 10) {
					if(player.getInventory().firstEmpty() == -1) {
						int i = 0;
						while(true) {
							ItemStack actual_item = player.getInventory().getItem(i);
							
							if(actual_item != null && actual_item.getType() == Material.EMERALD && actual_item.hasItemMeta() && actual_item.getItemMeta().getDisplayName().equalsIgnoreCase("§a10$")) {
								if(actual_item.getAmount() < 64) {
									Main.econ.withdrawPlayer(player, 10);
			            			player.getInventory().addItem(getItem(Material.EMERALD, "§a10$"));
			            			ItemStack ban = new ItemStack(Material.EMERALD, 1);
			    					ItemMeta banM = ban.getItemMeta();
			    					banM.setDisplayName("§aBanque");
			    					String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
			    					banM.setLore(Arrays.asList(bal));
			    					ban.setItemMeta(banM);
			    					banquee.setItem(31, ban);
									break;
								}
							}
							i++;
							if(i>36) {
								player.sendMessage(alert + "Votre inventaire est plein");
								break;
								
							}
						}
					}else {
						Main.econ.withdrawPlayer(player, 10);
            			player.getInventory().addItem(getItem(Material.EMERALD, "§a10$"));
            			ItemStack ban = new ItemStack(Material.EMERALD, 1);
    					ItemMeta banM = ban.getItemMeta();
    					banM.setDisplayName("§aBanque");
    					String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
    					banM.setLore(Arrays.asList(bal));
    					ban.setItemMeta(banM);
    					banquee.setItem(31, ban);
					}
				}else {
					player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
				}
			}
			
			
			if(current.getType() == Material.SULPHUR  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§cRetirer 10 000$")) {
				event.setCancelled(true);
				//retrait 10k
				ItemStack emerald = new ItemStack(Material.SULPHUR);
                ItemMeta emeraldM = emerald.getItemMeta();
                emeraldM.setDisplayName("§210 000$");
                emeraldM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                emeraldM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                emeraldM.addEnchant(Enchantment.DEPTH_STRIDER, 1, false);
                emerald.setItemMeta(emeraldM);
                if(money >= 10000) {
					if(player.getInventory().firstEmpty() == -1) {
						int i = 0;
						while(true) {
							ItemStack actual_item = player.getInventory().getItem(i);
							
							if(actual_item != null && actual_item.getType() == Material.SULPHUR && actual_item.hasItemMeta() && actual_item.getItemMeta().getDisplayName().equalsIgnoreCase("§210 000$")) {
								if(actual_item.getAmount() < 64) {
									Main.econ.withdrawPlayer(player, 10000);
			            			player.getInventory().addItem(emerald);
			            			ItemStack ban = new ItemStack(Material.EMERALD, 1);
			    					ItemMeta banM = ban.getItemMeta();
			    					banM.setDisplayName("§aBanque");
			    					String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
			    					banM.setLore(Arrays.asList(bal));
			    					ban.setItemMeta(banM);
			    					banquee.setItem(31, ban);
									break;
								}
							}
							i++;
							if(i>36) {
								player.sendMessage(alert + "Votre inventaire est plein");
								break;
								
							}
						}
					}else {
						Main.econ.withdrawPlayer(player, 10000);
            			player.getInventory().addItem(emerald);
            			ItemStack ban = new ItemStack(Material.EMERALD, 1);
    					ItemMeta banM = ban.getItemMeta();
    					banM.setDisplayName("§aBanque");
    					String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
    					banM.setLore(Arrays.asList(bal));
    					ban.setItemMeta(banM);
    					banquee.setItem(31, ban);
					}
				}else {
					player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
				}
                
                 
			}
			if(current.getType() == Material.PRISMARINE_SHARD  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§cRetirer 90$")) {
				event.setCancelled(true);
				//retrait 90
				
				if(money >= 90) {
					if(player.getInventory().firstEmpty() == -1) {
						int i = 0;
						while(true) {
							ItemStack actual_item = player.getInventory().getItem(i);
							
							if(actual_item != null && actual_item.getType() == Material.PRISMARINE_SHARD && actual_item.hasItemMeta() && actual_item.getItemMeta().getDisplayName().equalsIgnoreCase("§a90$")) {
								if(actual_item.getAmount() < 64) {
									Main.econ.withdrawPlayer(player, 90);
			            			player.getInventory().addItem(getItem(Material.PRISMARINE_SHARD,"§a90$"));
			            			ItemStack ban = new ItemStack(Material.EMERALD, 1);
			    					ItemMeta banM = ban.getItemMeta();
			    					banM.setDisplayName("§aBanque");
			    					String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
			    					banM.setLore(Arrays.asList(bal));
			    					ban.setItemMeta(banM);
			    					banquee.setItem(31, ban);
									break;
								}
							}
							i++;
							if(i>36) {
								player.sendMessage(alert + "Votre inventaire est plein");
								break;
								
							}
						}
					}else {
						Main.econ.withdrawPlayer(player, 90);
            			player.getInventory().addItem(getItem(Material.PRISMARINE_SHARD, "§a90$"));
            			ItemStack ban = new ItemStack(Material.EMERALD, 1);
    					ItemMeta banM = ban.getItemMeta();
    					banM.setDisplayName("§aBanque");
    					String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
    					banM.setLore(Arrays.asList(bal));
    					ban.setItemMeta(banM);
    					banquee.setItem(31, ban);
					}
				}else {
					player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
				}
				
			}
			if(current.getType() == Material.GOLD_SPADE  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lPermis d'Armes (PdA)")) {
			    event.setCancelled(true);
			    ItemStack arme = new ItemStack(Material.GOLD_SPADE);
                ItemMeta armeM = arme.getItemMeta();
                armeM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                armeM.setDisplayName("§c§lPermis d'Armes (PdA)");
                armeM.setLore(Arrays.asList("","§§7Type: §cPetites Armes","","§a§lAcheté"));
                arme.setItemMeta(armeM);
				if(player.hasPermission("gca.armes")) {
					player.sendMessage(alert+"§cVous possédez déjà ce permis");
					player.closeInventory();
				}else {
					if(money >= 20000) {
						Main.econ.withdrawPlayer(player, 20000);
						Bukkit.dispatchCommand(console, "lp user " + player.getName() + " permission set gca.armes");
						InventoryView inv = player.getOpenInventory();
						inv.setItem(4, arme);
					}else {
						player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
					}
				}
			
			}
			
			
			if(current.getType() == Material.MINECART  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§6§lPermis Voiture (PV)")) {
			    event.setCancelled(true);
					ItemStack voi = new ItemStack(Material.MINECART);
	                ItemMeta voiM = voi.getItemMeta();
	                voiM.setDisplayName("§6§lPermis Voiture (PV)");
	                voiM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	                voiM.setLore(Arrays.asList("","§7Type: §6Voitures","","§a§lAcheté!"));
	                voi.setItemMeta(voiM);
	                
					if(player.hasPermission("gca.voiture")) {
						player.sendMessage(alert+"§cVous possédez déjà ce permis");
						player.closeInventory();
					}else {
						if(money >= 15000) {
							Main.econ.withdrawPlayer(player, 15000);
							Bukkit.dispatchCommand(console, "lp user " + player.getName() + " permission set gca.voiture");
							InventoryView inv = player.getOpenInventory();
							inv.setItem(4, voi);
							
						}else {
							player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
						}
					}
				
				
				}
			String currentname = current.getItemMeta().getDisplayName();
			
			if(current.getType() == Material.NETHER_STAR && current.hasItemMeta() &&  currentname.equalsIgnoreCase("§c§lNiveau 1")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.NETHER_STAR && current.hasItemMeta() && currentname.equalsIgnoreCase("§c§lNiveau 2")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.NETHER_STAR && current.hasItemMeta() && currentname.equalsIgnoreCase("§c§lNiveau 3")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.NETHER_STAR && current.hasItemMeta() && currentname.equalsIgnoreCase("§4§lNiveau 4")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.NETHER_STAR && current.hasItemMeta() && currentname.equalsIgnoreCase("§4§lNiveau 5")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.EYE_OF_ENDER && current.hasItemMeta() && currentname.equalsIgnoreCase("§dNiveau de recherche")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.DIAMOND_AXE && current.hasItemMeta() && currentname.equalsIgnoreCase("§2Bûcheron")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.DIAMOND_PICKAXE && current.hasItemMeta() && currentname.equalsIgnoreCase("§4Mineur")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.FISHING_ROD && current.hasItemMeta() && currentname.equalsIgnoreCase("§bPêcheur")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.EYE_OF_ENDER && current.hasItemMeta() && currentname.equalsIgnoreCase("§9§lInformations")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.BOOK && current.hasItemMeta() && currentname.equalsIgnoreCase("§a§lPortfolio")) {
				event.setCancelled(true);
				Bukkit.dispatchCommand(console, "sudo " + player.getName() + " stocks portfolio");
			}
			if(current.getType() == Material.EMERALD && current.hasItemMeta() && currentname.equalsIgnoreCase("§c§lListe des actions")) {
				event.setCancelled(true);
				Bukkit.dispatchCommand(console, "sudo " + player.getName() + " pop");
			}
			if(current.getType() == Material.DIAMOND_HOE && current.hasItemMeta() && currentname.equalsIgnoreCase("§aFarmeur")) {
				event.setCancelled(true);
			}
			if(current.getType() == Material.EYE_OF_ENDER && current.hasItemMeta() && currentname.startsWith("§dRegarder l'inventaire de ")) {
				event.setCancelled(true);
				String output = currentname.substring(27);
				Player clicked = Bukkit.getPlayer(output);
				Inventory inv = clicked.getInventory();
				player.openInventory(inv);
			}
			
			if(current.getType() == Material.IRON_FENCE && current.hasItemMeta() && currentname.startsWith("§cMettre en prison ")){
				event.setCancelled(true);
				String output = currentname.substring(19);
				Player clicked = Bukkit.getPlayer(output);
				UUID uuid = clicked.getUniqueId();
				Duration d = Duration.ofMinutes(10);
				if(isInCombat(clicked)) {
					combatManager.untag(clicked, PlayerUntagEvent.UntagReason.EXPIRE);
				}
				Bukkit.dispatchCommand(console, "essentials:jail " + clicked.getName() + " jail1 600");
				
				player.sendMessage("Le joueur a été mis en prison.");
				clicked.sendMessage("Vous avez été mis en prison pour 10 minutes. Veuillez rester en ligne 10 minutes.");
				
				
			}
			
			//GRADES | 
		//	User user = lp.getUserManager().getUser(player.getUniqueId());
		//	Group group = lp.getGroupManager().getGroup("migrant");
         	
			if(current.getType() == Material.WHEAT && current.hasItemMeta() && currentname.equalsIgnoreCase("§a§lVoyou")) {
				event.setCancelled(true);
				if(player.hasPermission("gca.migrant")){
					if(money >= 35000) {
						Main.econ.withdrawPlayer(player, 35000);
						Bukkit.dispatchCommand(console, "lp user " + player.getName() + " group set voyou");
					}else {
						player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
					}
				}else {
					player.sendMessage(alert+"§cVous ne pouvez pas acheter ce grade");
				}
			}
			if(current.getType() == Material.GOLD_SPADE && current.hasItemMeta() && currentname.equalsIgnoreCase("§5§lHors-la-Loi")) {
				event.setCancelled(true);
				if(player.hasPermission("gca.voyou")){
					if(money >= 100000) {
						Main.econ.withdrawPlayer(player, 100000);
						Bukkit.dispatchCommand(console, "lp user " + player.getName() + " group set hll");
					}else {
						player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
					}
				}else {
					player.sendMessage(alert+"§cVous ne pouvez pas acheter ce grade");
				}
				     
			}
			if(current.getType() == Material.FEATHER && current.hasItemMeta() && currentname.equalsIgnoreCase("§6§lContrebandier")) {
				event.setCancelled(true);
				if(player.hasPermission("gca.hll")){
					if(money >= 250000) {
						Main.econ.withdrawPlayer(player, 250000);
						Bukkit.dispatchCommand(console, "lp user " + player.getName() + " group set contrebandier");
					}else {
						player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
					}
				}else {
					player.sendMessage(alert+"§cVous ne pouvez pas acheter ce grade");
				}
				     
			}
			if(current.getType() == Material.WOOD_SWORD && current.hasItemMeta() && currentname.equalsIgnoreCase("§1§lBraqueur")) {
				event.setCancelled(true);
				if(player.hasPermission("gca.contrebandier")){
					if(money >= 700000) {
						Main.econ.withdrawPlayer(player, 700000);
						Bukkit.dispatchCommand(console, "lp user " + player.getName() + " group set braqueur");
					}else {
						player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
					}
				}else {
					player.sendMessage(alert+"§cVous ne pouvez pas acheter ce grade");
				}
				     
			}
			if(current.getType() == Material.SUGAR && current.hasItemMeta() && currentname.equalsIgnoreCase("§c§lGangster")) {
				event.setCancelled(true);
				if(player.hasPermission("gca.braqueur")){
					if(money >= 1500000) {
						Main.econ.withdrawPlayer(player, 1500000);
						Bukkit.dispatchCommand(console, "lp user " + player.getName() + " group set gangster");
					}else {
						player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
					}
				}else {
					player.sendMessage(alert+"§cVous ne pouvez pas acheter ce grade");
				}
				     
			}
			if(current.getType() == Material.SULPHUR && current.hasItemMeta() && currentname.equalsIgnoreCase("§d§lMafieux")) {
				event.setCancelled(true);
				if(player.hasPermission("gca.gangster")){
					if(money >= 5000000) {
						Main.econ.withdrawPlayer(player, 5000000);
						Bukkit.dispatchCommand(console, "lp user " + player.getName() + " group set mafieux");
					}else {
						player.sendMessage(alert+"§cVous n'avez pas assez d'argent");
					}
				}else {
					player.sendMessage(alert+"§cVous ne pouvez pas acheter ce grade");
				}
			}
			
			
			if(current.getType() == Material.IRON_HELMET) {
				
				switch(currentname) {
				case "§7Mafieux": event.setCancelled(true);
				case "§aVoyou": event.setCancelled(true);
				case "§5Hors-la-Loi": event.setCancelled(true);
				case "§6Contrebandier": event.setCancelled(true);
				case "§1Braqueur": event.setCancelled(true);
				case "§cGangster": event.setCancelled(true);
				case "§dMafieux": event.setCancelled(true);
				case "§4§lPierronus": event.setCancelled(true);
				case "§4§lScrym": event.setCancelled(true);
				}
			}
			if(current.getType() == Material.REDSTONE_BLOCK  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§4§lACTIVER GILET KAMIKAZE")) {
				event.setCancelled(true);
				player.closeInventory();
				double locX1 = player.getLocation().getX();
				double locY1 = player.getLocation().getY();
				double locZ1 = player.getLocation().getZ();
				player.getInventory().setChestplate(null);
					world.createExplosion(locX1, locY1, locZ1, 10.0F, false, false);
					if(player.isDead()) {
						player.sendMessage("§4Tu as été explosé par ton kamikaze");
					}
				
			}
			
			
			
			
			if(current.getType() == Material.DIAMOND_PICKAXE  && current.getItemMeta().getDisplayName().equalsIgnoreCase("§bMétiers")) {
				event.setCancelled(true);
				player.closeInventory();
				Inventory metiers = Bukkit.createInventory(null, 27, "§b§lTéléphone §f§8> §bMétiers");
				
				int xpbucheron = Main.getInstance().data.getXpBucheron(player.getUniqueId());
				int xppecheur = Main.getInstance().data.getXpPecheur(player.getUniqueId());
				int xpmineur = Main.getInstance().data.getXpMineur(player.getUniqueId());
				int xpfarmeur = Main.getInstance().data.getXpFarmeur(player.getUniqueId());
				

				int lvlbucheron;
				int lvlpecheur;
				int lvlmineur;
				int lvlfarmeur;
				
				String restantmineur;
				String restantpecheur;
				String restantbucheron;
				String restantfarmeur;
				
				restantbucheron = "";
				restantpecheur = "";
				restantmineur = "";
				restantfarmeur = "";
				
				List<String> pecheurlist;
				List<String> bucheronlist;
				List<String> mineurlist;
				List<String> farmeurlist;
				
				pecheurlist = Arrays.asList();
				mineurlist = Arrays.asList();
				bucheronlist = Arrays.asList();
				farmeurlist = Arrays.asList();
				
				lvlbucheron = 0;
				lvlpecheur = 0;
				lvlmineur = 0;
				lvlfarmeur = 0;
				
				if(xpfarmeur < 10000) {
					lvlfarmeur = 0;
					restantfarmeur = "§6 " + String.valueOf(xpfarmeur) + " / §610000";
					farmeurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlfarmeur), "§7§lXP: §6" + restantfarmeur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpfarmeur < 20000 && xpfarmeur >= 10000) {
					lvlfarmeur = 1;
					restantfarmeur = "§6 " + String.valueOf(xpfarmeur) + " / §620000";
					farmeurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlfarmeur), "§7§lXP: §6" + restantfarmeur + " §6XP", "", "§a§lLevel 1: §aLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpfarmeur < 35000 && xpfarmeur >= 20000) {
					lvlfarmeur = 2;
					restantfarmeur = "§6 " + String.valueOf(xpfarmeur) + " / §635000";
					farmeurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlfarmeur), "§7§lXP: §6" + restantfarmeur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§a§lLevel 2: §aLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpfarmeur < 55000 && xpfarmeur >= 35000) {
					lvlfarmeur = 3;
					restantfarmeur = "§6 " + String.valueOf(xpfarmeur) + " / §655000";
					farmeurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlfarmeur), "§7§lXP: §6" + restantfarmeur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§a§lLevel 3: §aLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpfarmeur < 80000 && xpfarmeur >= 55000) {
					lvlfarmeur = 4;
					restantfarmeur = "§6 " + String.valueOf(xpfarmeur) + " / §680000";
					farmeurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlfarmeur), "§7§lXP: §6" + restantfarmeur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§a§lLevel 4: §aLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpfarmeur < 115000 && xpfarmeur >= 80000) {
					lvlfarmeur = 5;
					restantfarmeur = "§6 " + String.valueOf(xpfarmeur) + " / §6115000";
					farmeurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlfarmeur), "§7§lXP: §6" + restantfarmeur +" §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§a§lLevel 5: §aLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpfarmeur < 150000 && xpfarmeur >= 115000) {
					lvlfarmeur = 6;
					restantfarmeur = "§6 " + String.valueOf(xpfarmeur) + " / §6150000";
					farmeurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlfarmeur), "§7§lXP: §6" + restantfarmeur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§a§lLevel 6: §aLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpfarmeur < 200000 && xpfarmeur >= 150000) {
					lvlfarmeur = 7;
					restantfarmeur = "§6 " + String.valueOf(xpfarmeur) + " / §6200000";
					farmeurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlfarmeur), "§7§lXP: §6" + restantfarmeur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§a§lLevel 7: §aLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpfarmeur >= 200000) {
					lvlfarmeur = 8;
					restantfarmeur = "§6 " + String.valueOf(xpfarmeur) + " / §6LEVEL MAX";
					farmeurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlfarmeur), " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§a§lLevel 8: §aLoots x2 100%");
				}
				
				
				
				
				if(xpmineur < 10000) {
					lvlmineur = 0;
					restantmineur = "§6 " + String.valueOf(xpmineur) + " / §610000";
					mineurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlmineur), "§7§lXP: §6" + restantmineur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpmineur < 20000 && xpmineur >= 10000) {
					lvlmineur = 1;
					restantmineur = "§6 " + String.valueOf(xpmineur) + " / §620000";
					mineurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlmineur), "§7§lXP: §6" + restantmineur + " §6XP", "", "§a§lLevel 1: §aLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpmineur < 35000 && xpmineur >= 20000) {
					lvlmineur = 2;
					restantmineur = "§6 " + String.valueOf(xpmineur) + " / §635000";
					mineurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlmineur), "§7§lXP: §6" + restantmineur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§a§lLevel 2: §aLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpmineur < 55000 && xpmineur >= 35000) {
					lvlmineur = 3;
					restantmineur = "§6 " + String.valueOf(xpmineur) + " / §655000";
					mineurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlmineur), "§7§lXP: §6" + restantmineur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§a§lLevel 3: §aLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpmineur < 80000 && xpmineur >= 55000) {
					lvlmineur = 4;
					restantmineur = "§6 " + String.valueOf(xpmineur) + " / §680000";
					mineurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlmineur), "§7§lXP: §6" + restantmineur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§a§lLevel 4: §aLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpmineur < 115000 && xpmineur >= 80000) {
					lvlmineur = 5;
					restantmineur = "§6 " + String.valueOf(xpmineur) + " / §6115000";
					mineurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlmineur), "§7§lXP: §6" + restantmineur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§a§lLevel 5: §aLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpmineur < 150000 && xpmineur >= 115000) {
					lvlmineur = 6;
					restantmineur = "§6 " + String.valueOf(xpmineur) + " / §6150000";
					mineurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlmineur), "§7§lXP: §6" + restantmineur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§a§lLevel 6: §aLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpmineur < 200000 && xpmineur >= 150000) {
					lvlmineur = 7;
					restantmineur = "§6 " + String.valueOf(xpmineur) + " / §6200000";
					mineurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlmineur), "§7§lXP: §6" + restantmineur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§a§lLevel 7: §aLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpmineur >= 200000) {
					lvlmineur = 8;
					restantmineur = "§6 " + String.valueOf(xpmineur) + " / §6LEVEL MAX";
					mineurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlmineur), "§7§lXP: §6" + restantmineur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§a§lLevel 8: §aLoots x2 100%");
				}
				
				
				
				if(xpbucheron < 10000) {
					lvlbucheron = 0;
					restantbucheron = "§6 " + String.valueOf(xpbucheron) + " / §610000";
					bucheronlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlbucheron), "§7§lXP: §6" + restantbucheron + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpbucheron < 20000 && xpbucheron >= 10000) {
					lvlbucheron = 1;
					restantbucheron = "§6 " + String.valueOf(xpbucheron) + " / §620000";
					bucheronlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlbucheron), "§7§lXP: §6" + restantbucheron + " §6XP", "", "§a§lLevel 1: §aLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpbucheron < 35000 && xpbucheron >= 20000) {
					lvlbucheron = 2;
					restantbucheron = "§6 " + String.valueOf(xpbucheron) + " / §635000";
					bucheronlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlbucheron), "§7§lXP: §6" + restantbucheron + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§a§lLevel 2: §aLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpbucheron < 55000 && xpbucheron >= 35000) {
					lvlbucheron = 3;
					restantbucheron = "§6 " + String.valueOf(xpbucheron) + " / §655000";
					bucheronlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlbucheron), "§7§lXP: §6" + restantbucheron + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§a§lLevel 3: §aLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpbucheron < 80000 && xpbucheron >= 55000) {
					lvlbucheron = 4;
					restantbucheron = "§6 " + String.valueOf(xpbucheron) + " / §680000";
					bucheronlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlbucheron), "§7§lXP: §6" + restantbucheron + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§a§lLevel 4: §aLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpbucheron < 115000 && xpbucheron >= 80000) {
					lvlbucheron = 5;
					restantbucheron = "§6 " + String.valueOf(xpbucheron) + " / §6115000";
					bucheronlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlbucheron), "§7§lXP: §6" + restantbucheron + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§a§lLevel 5: §aLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpbucheron < 150000 && xpbucheron >= 115000) {
					lvlbucheron = 6;
					restantbucheron = "§6 " + String.valueOf(xpbucheron) + " / §6150000";
					bucheronlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlbucheron), "§7§lXP: §6" + restantbucheron + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§a§lLevel 6: §aLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpbucheron < 200000 && xpbucheron >= 150000) {
					lvlbucheron = 7;
					restantbucheron = "§6 " + String.valueOf(xpbucheron) + " / §6200000";
					bucheronlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlbucheron), "§7§lXP: §6" + restantbucheron + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§a§lLevel 7: §aLoots x2 85%", "§c§lLevel 8: §cLoots x2 100%");
				}
				if(xpbucheron >= 200000) {
					lvlbucheron = 8;
					restantbucheron = "§6 " + String.valueOf(xpbucheron) + " / §6LEVEL MAX";
					bucheronlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlbucheron), "§7§lXP: §6" + restantbucheron + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 25%",
							"§c§lLevel 4: §cLoots x2 40%", "§c§lLevel 5: §cLoots x2 55%", "§c§lLevel 6: §cLoots x2 70%", "§c§lLevel 7: §cLoots x2 85%", "§a§lLevel 8: §aLoots x2 100%");
				}
				
				
				
				if(xppecheur < 10000) {
					lvlpecheur = 0;
					restantpecheur = "§6 " + String.valueOf(xppecheur) + " / §610000";
					pecheurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlpecheur), "§7§lXP: §6" + restantpecheur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 20%",
							"§c§lLevel 4: §cLoots x2 30%", "§c§lLevel 5: §cLoots x2 40%", "§c§lLevel 6: §cLoots x2 55%", "§c§lLevel 7: §cLoots x2 70%", "§c§lLevel 8: §cLoots x2 90%");
				}
				if(xppecheur < 20000 && xppecheur >= 10000) {
					lvlpecheur = 1;
					restantpecheur = "§6 " + String.valueOf(xppecheur) + " / §620000";
					pecheurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlpecheur), "§7§lXP: §6" + restantpecheur + " §6XP", "", "§a§lLevel 1: §aLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 20%",
							"§c§lLevel 4: §cLoots x2 30%", "§c§lLevel 5: §cLoots x2 40%", "§c§lLevel 6: §cLoots x2 55%", "§c§lLevel 7: §cLoots x2 70%", "§c§lLevel 8: §cLoots x2 90%");
				}
				if(xppecheur < 35000 && xppecheur >= 20000) {
					lvlpecheur = 2;
					restantpecheur = "§6 " + String.valueOf(xppecheur) + " / §635000";
					pecheurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlpecheur), "§7§lXP: §6" + restantpecheur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§a§lLevel 2: §aLoots x2 10%", "§c§lLevel 3: §cLoots x2 20%",
							"§c§lLevel 4: §cLoots x2 30%", "§c§lLevel 5: §cLoots x2 40%", "§c§lLevel 6: §cLoots x2 55%", "§c§lLevel 7: §cLoots x2 70%", "§c§lLevel 8: §cLoots x2 90%");
				}
				if(xppecheur < 55000 && xppecheur >= 35000) {
					lvlpecheur = 3;
					restantpecheur = "§6 " + String.valueOf(xppecheur) + " / §655000";
					pecheurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlpecheur), "§7§lXP: §6" + restantpecheur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§a§lLevel 3: §aLoots x2 20%",
							"§c§lLevel 4: §cLoots x2 30%", "§c§lLevel 5: §cLoots x2 40%", "§c§lLevel 6: §cLoots x2 55%", "§c§lLevel 7: §cLoots x2 70%", "§c§lLevel 8: §cLoots x2 90%");
				}
				if(xppecheur < 80000 && xppecheur >= 55000) {
					lvlpecheur = 4;
					restantpecheur = "§6 " + String.valueOf(xppecheur) + " / §680000";
					pecheurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlpecheur), "§7§lXP: §6" + restantpecheur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 20%",
							"§a§lLevel 4: §aLoots x2 30%", "§c§lLevel 5: §cLoots x2 40%", "§c§lLevel 6: §cLoots x2 55%", "§c§lLevel 7: §cLoots x2 70%", "§c§lLevel 8: §cLoots x2 90%");
				}
				if(xppecheur < 115000 && xppecheur >= 80000) {
					lvlpecheur = 5;
					restantpecheur = "§6 " + String.valueOf(xppecheur) + " / §6115000";
					pecheurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlpecheur), "§7§lXP: §6" + restantpecheur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 20%",
							"§c§lLevel 4: §cLoots x2 30%", "§a§lLevel 5: §aLoots x2 40%", "§c§lLevel 6: §cLoots x2 55%", "§c§lLevel 7: §cLoots x2 70%", "§c§lLevel 8: §cLoots x2 90%");
				}
				if(xppecheur < 150000 && xppecheur >= 115000) {
					lvlpecheur = 6;
					restantpecheur = "§6 " + String.valueOf(xppecheur) + " / §6150000";
					pecheurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlpecheur), "§7§lXP: §6" + restantpecheur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 20%",
							"§c§lLevel 4: §cLoots x2 30%", "§c§lLevel 5: §cLoots x2 40%", "§a§lLevel 6: §aLoots x2 55%", "§c§lLevel 7: §cLoots x2 70%", "§c§lLevel 8: §cLoots x2 90%");
				}
				if(xppecheur < 200000 && xppecheur >= 150000) {
					lvlpecheur = 7;
					restantpecheur = "§6 " + String.valueOf(xppecheur) + " / §6200000";
					pecheurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlpecheur), "§7§lXP: §6" + restantpecheur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 20%",
							"§c§lLevel 4: §cLoots x2 30%", "§c§lLevel 5: §cLoots x2 40%", "§c§lLevel 6: §cLoots x2 55%", "§a§lLevel 7: §aLoots x2 70%", "§c§lLevel 8: §cLoots x2 90%");
				}
				if(xppecheur >= 200000) {
					lvlpecheur = 8;
					restantpecheur = "§6 " + String.valueOf(xppecheur) + " / §6LEVEL MAX";
					pecheurlist = Arrays.asList("§7§lNiveau: §b" + String.valueOf(lvlpecheur), "§7§lXP: §6" + restantpecheur + " §6XP", "", "§c§lLevel 1: §cLoots x2 5%", "§c§lLevel 2: §cLoots x2 10%", "§c§lLevel 3: §cLoots x2 20%",
							"§c§lLevel 4: §cLoots x2 30%", "§c§lLevel 5: §cLoots x2 40%", "§c§lLevel 6: §cLoots x2 55%", "§c§lLevel 7: §cLoots x2 70%", "§a§lLevel 8: §aLoots x2 90%");
				}
				ItemStack mineur = new ItemStack(Material.DIAMOND_PICKAXE);
				ItemMeta mineurM = mineur.getItemMeta();
				mineurM.setDisplayName("§4Mineur");
				mineurM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				mineurM.setLore(mineurlist);
				mineur.setItemMeta(mineurM);
				metiers.setItem(12, mineur);
				
				ItemStack pecheur = new ItemStack(Material.FISHING_ROD);
				ItemMeta pecheurM = pecheur.getItemMeta();
				pecheurM.setDisplayName("§bPêcheur");
				pecheurM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				pecheurM.setLore(pecheurlist);
				pecheur.setItemMeta(pecheurM);
				metiers.setItem(14, pecheur);
				ItemStack bucheron = new ItemStack(Material.DIAMOND_AXE);
				ItemMeta bucheronM = bucheron.getItemMeta();
				bucheronM.setDisplayName("§2Bûcheron");
				bucheronM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				bucheronM.setLore(bucheronlist);
				bucheron.setItemMeta(bucheronM);
				metiers.setItem(10, bucheron);
				ItemStack farmeur = new ItemStack(Material.DIAMOND_HOE);
				ItemMeta farmeurM = farmeur.getItemMeta();
				farmeurM.setDisplayName("§aFarmeur");
				farmeurM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				farmeurM.setLore(farmeurlist);
				farmeur.setItemMeta(farmeurM);
				metiers.setItem(16, farmeur);
				
				player.openInventory(metiers);
			}
			}
}
 
	public ItemStack getItems(Material material, String customName, Integer count) {
		ItemStack it = new ItemStack(material, 1);
		ItemMeta itM = it.getItemMeta();
		if(customName != null) itM.setDisplayName(customName);
		it.setItemMeta(itM);
 
		return it;
	}
	public ItemStack showCompass(Player player, double distance, int rech) {
		ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(player.getName());
		String recherche = "§aAucun";
		String toadd = "";
		for(int i=1; i<=rech;i++) {
			toadd = toadd + "✪";
		}
		if(rech == 1) {
			recherche = "§e"+toadd;
		}
		if(rech == 2) {
			recherche = "§6"+toadd;
		}
		if(rech == 3) {
			recherche = "§c"+toadd;
		}
		if(rech == 4) {
			recherche = "§4"+toadd;
		}
		if(rech == 5) {
			recherche = "§4"+toadd;
		}
		
		//switch(rech) {
		//case 1: recherche = "§e"+toadd;
		//case 2: recherche = "§6"+toadd;
		//case 3: recherche = "§c"+toadd;
		//case 4: recherche = "§4"+toadd;
		//case 5: recherche = "§4"+toadd;
		//}
		
		itM.setLore(Arrays.asList("","§7Niveau de recherche : " + recherche,"§fX:" + String.valueOf(player.getLocation().getBlockX()) + "§f , Y:" + String.valueOf(player.getLocation().getBlockY()) + "§f , Z:" + String.valueOf(player.getLocation().getBlockZ())));
		it.setItemMeta(itM);
		return it;
		
	}
	public ItemStack getItem(Material material, String customName) {
		ItemStack it = new ItemStack(material);
		ItemMeta itM = it.getItemMeta();
		if(customName != null) itM.setDisplayName(customName);
		it.setItemMeta(itM);
 
		return it;
	}
	public ItemStack getItemwLore(Material material, String customName, List<String> lore) {
		ItemStack it = new ItemStack(material, 1);
		ItemMeta itM = it.getItemMeta();
		if(customName != null) itM.setDisplayName(customName);
		if(lore != null) itM.setLore(lore);
		it.setItemMeta(itM);
 
		return it;
	}
	public ItemStack getItemstackwLore(ItemStack it, String customName, List<String> lore) {
		ItemMeta itM = it.getItemMeta();
		if(customName != null) itM.setDisplayName(customName);
		if(lore != null) itM.setLore(lore);
		it.setItemMeta(itM);
 
		return it;
	}
	public ItemStack getItemstack(Material material,Byte bte) {
		ItemStack it = new ItemStack(material, 1);
		it.getData().setData(bte);
 
		return it;
	}
	public Location closest(Location target, List<Location> points) {
	    double smallestDistance = Double.MAX_VALUE;
	    Location closestLocation = null;

	    for(Location point : points) {
	      double distance = target.distanceSquared(point);
	      if(distance < smallestDistance) {
	        smallestDistance = distance;
	        closestLocation = point;
	      }
	    }

	    return closestLocation;
	  }
	public ItemStack getItem(Material material, Short bte, String customName, List<String> lore, int amount) {
		ItemStack it = new ItemStack(material, amount, bte);
		ItemMeta itM = it.getItemMeta();
		if(customName != null) itM.setDisplayName(customName);
		if(lore != null) itM.setLore(lore);
		it.setItemMeta(itM);
 
		return it;
	}
	@EventHandler
	public void onPhone(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack it = event.getItem();
		Action action = event.getAction();
		
		if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			if(player.getInventory().getItemInHand().getType() == Material.SUGAR) {
				//cocaine
				
				if(player.hasPotionEffect(PotionEffectType.SPEED)) {
					player.removePotionEffect(PotionEffectType.SPEED);
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 3));
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 3));
				if(player.getInventory().getItemInHand().getAmount() == 1) {
					player.getInventory().setItemInHand(new ItemStack(Material.AIR));
					player.sendMessage("§bVous vous sentez rapideeeee");
				}else {
					player.getInventory().getItemInHand().setAmount((player.getInventory().getItemInHand().getAmount()) - 1);
					player.sendMessage("§bVous vous sentez rapideeeee");
				}
			}
			if(player.getInventory().getItemInHand().getType() == Material.WHEAT) {
				if(player.hasPotionEffect(PotionEffectType.REGENERATION)) {
					player.removePotionEffect(PotionEffectType.REGENERATION);
					player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 3));
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 2));
				if(player.getInventory().getItemInHand().getAmount() == 1) {
					player.getInventory().setItemInHand(new ItemStack(Material.AIR));
					player.sendMessage("§aVous vous sentez trèèèèès bien");
				}else {
					player.getInventory().getItemInHand().setAmount((player.getInventory().getItemInHand().getAmount()) - 1);
					player.sendMessage("§aVous vous sentez trèèèèès bien");
				}
				
			}
		}
	}
	public void add10(Player player) {
		if(player.getInventory().firstEmpty() == -1) {
			int i = 0;
			while(true) {
				ItemStack actual_item = player.getInventory().getItem(i);
				
				if(actual_item != null && actual_item.getType() == Material.EMERALD && actual_item.hasItemMeta() && actual_item.getItemMeta().getDisplayName().equalsIgnoreCase("§a10$")) {
					if(actual_item.getAmount() < 64) {
						Main.econ.withdrawPlayer(player, 10);
            			player.getInventory().addItem(getItem(Material.EMERALD, "§a10$"));
						break;
					}
				}
				i++;
				if(i>36) {
					player.sendMessage(alert + "Votre inventaire est plein");
					break;
					
				}
			}
		}else {
			Main.econ.withdrawPlayer(player, 10);
			player.getInventory().addItem(getItem(Material.EMERALD, "§a10$"));
		}
	}
	public void add10k(Player player) {
		ItemStack emerald = new ItemStack(Material.SULPHUR);
        ItemMeta emeraldM = emerald.getItemMeta();
        emeraldM.setDisplayName("§210 000$");
        emeraldM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        emeraldM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        emeraldM.addEnchant(Enchantment.DEPTH_STRIDER, 1, false);
        emerald.setItemMeta(emeraldM);
		if(player.getInventory().firstEmpty() == -1) {
			int i = 0;
			while(true) {
				ItemStack actual_item = player.getInventory().getItem(i);
				
				if(actual_item != null && actual_item.getType() == Material.SULPHUR && actual_item.hasItemMeta() && actual_item.getItemMeta().getDisplayName().equalsIgnoreCase("§210 000$")) {
					if(actual_item.getAmount() < 64) {
						Main.econ.withdrawPlayer(player, 10000);
            			player.getInventory().addItem(emerald);
						break;
					}
				}
				i++;
				if(i>36) {
					player.sendMessage(alert + "Votre inventaire est plein");
					break;
					
				}
			}
		}else {
			Main.econ.withdrawPlayer(player, 10000);
			player.getInventory().addItem(emerald);
			ItemStack ban = new ItemStack(Material.EMERALD, 1);
		}
	}
	public void add90(Player player) {
		if(player.getInventory().firstEmpty() == -1) {
			int i = 0;
			while(true) {
				ItemStack actual_item = player.getInventory().getItem(i);
				
				if(actual_item != null && actual_item.getType() == Material.PRISMARINE_SHARD && actual_item.hasItemMeta() && actual_item.getItemMeta().getDisplayName().equalsIgnoreCase("§a90$")) {
					if(actual_item.getAmount() < 64) {
						Main.econ.withdrawPlayer(player, 90);
            			player.getInventory().addItem(getItem(Material.PRISMARINE_SHARD,"§a90$"));
						break;
					}
				}
				i++;
				if(i>36) {
					player.sendMessage(alert + "Votre inventaire est plein");
					break;
					
				}
			}
		}else {
			Main.econ.withdrawPlayer(player, 90);
			player.getInventory().addItem(getItem(Material.PRISMARINE_SHARD, "§a90$"));
		}
	}
	@EventHandler
    public void onBandageUse(PlayerInteractEntityEvent e) {
    Player player = (Player) e.getPlayer();
    Player clicked = (Player) e.getRightClicked();
    
 
    if(clicked == null) {
    	return;
    }
    
    if(!player.hasPermission("essentials.tpa")) {
    	return;
    }
    PotionEffect blindness = PotionEffectType.BLINDNESS.createEffect(200, 10);
    PotionEffect slowness = PotionEffectType.SLOW.createEffect(200, 100);
    @SuppressWarnings("deprecation")
	ItemStack item = player.getItemInHand();
    ItemStack sw = new ItemStack(Material.BLAZE_ROD, 1);
    ItemMeta swM = sw.getItemMeta();
	swM.setDisplayName("§a§lMenottes");
	sw.setItemMeta(swM);
if (clicked instanceof Player)
{
	if(e.getHand().equals(EquipmentSlot.HAND)) {
if(item != null && item.getType().equals(Material.BLAZE_ROD) && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lMenottes")) {
	if(player.hasPermission("gca.police")) {
		if(clicked.hasPermission("gca.police")) {
			player.sendMessage("§6§l[GCA] §f§cVous ne pouvez pas menotter d'autres policiers");
		}
		else {
			if(clicked.hasPotionEffect(PotionEffectType.SLOW) && clicked.hasPotionEffect(PotionEffectType.BLINDNESS)) {
				player.sendMessage("§6§l[GCA] §f§cVous avez dé-menotté " + clicked.getName());
				clicked.sendMessage("§6§l[GCA] §f§aVous avez été dé-menotté par " + player.getName());
				clicked.removePotionEffect(PotionEffectType.BLINDNESS);
				clicked.removePotionEffect(PotionEffectType.SLOW);
				Bukkit.getScheduler().cancelTask(taskid);
			}
		else {
			player.sendMessage("§6§l[GCA] §f§aVous avez menotté " + clicked.getName());
			clicked.sendMessage("§6§l[GCA] §f§cVous avez été menotté par " + player.getName() + "§c. \n§c§lNe vous déconnectez pas sous peine d'emprisonnement!");
			clicked.addPotionEffect(slowness);
			clicked.addPotionEffect(blindness);
			
			taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable(){
 
	            @Override
	            public void run() {
	            	Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getPitch(), player.getLocation().getYaw());
	                clicked.teleport(loc);
	                clicked.removePotionEffect(PotionEffectType.SLOW);
	    			clicked.removePotionEffect(PotionEffectType.BLINDNESS);
	                clicked.addPotionEffect(slowness);
	    			clicked.addPotionEffect(blindness);

	                }
 
	            }
 
	        , 0 * 20, 1 * 20);
			Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable(){
 
	            @Override
	            public void run() {
	            	Bukkit.getScheduler().cancelTask(taskid);
	                }
 
	            }
 
	        , 120 * 20);
            }
		}
	}else {
		player.sendMessage("§6§l[GCA] §f§cVous n'avez pas la permission");
		player.getInventory().remove(sw);
 
    }
	}
 
if(item != null && item.getType().equals(Material.FERMENTED_SPIDER_EYE) && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("§b§lCheck")) {
	System.out.println("cc");
	if(player.hasPermission("gca.police")) {
		if(clicked.hasPermission("gca.police")) {
			player.sendMessage("§6§l[GCA] §f§cVous ne pouvez pas regarder les infos d'autres policiers");
		}
		else {
			Inventory info = Bukkit.createInventory(null, 27, "§8§lInfos de " + clicked.getName());
 
			if(clicked.hasPermission("gca.migrant")) {gradepolice = "§7Migrant";       } //§7Migrant
			if(clicked.hasPermission("gca.voyou")) {gradepolice = "§aVoyou";       } //§a§lVoyou
			if(clicked.hasPermission("gca.hll")) {gradepolice = "§5Hors-la-Loi"  ;     } //§5§lHors-la-Loi
			if(clicked.hasPermission("gca.contrebandier")) {gradepolice = "§6Contrebandier";       } //§6§lContrebandier
			if(clicked.hasPermission("gca.braqueur")) {gradepolice = "§1Braqueur";       } //§1§lBraqueur
			if(clicked.hasPermission("gca.gangster")) {gradepolice = "§cGangster";       } //§c§lGangster
			if(clicked.hasPermission("gca.mafieux")) {gradepolice = "§dMafieux";       } //§d§lMafieux
 
			ItemStack rank = new ItemStack(Material.IRON_HELMET, 1);
		    ItemMeta rankM = rank.getItemMeta();
			rankM.setDisplayName("§7Grade: " + gradepolice);
			rank.setItemMeta(rankM);
			info.setItem(1, rank);
			
			info.setItem(19, getItems(Material.BOOK, "§d§lPermis Actuels :", 1));
            if(clicked.hasPermission("gca.armes")) {
                ItemStack arme = new ItemStack(Material.GOLD_SPADE);
                ItemMeta armeM = arme.getItemMeta();
                armeM.setDisplayName("§c§lPermis d'Armes (PdA)");
                armeM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                armeM.setLore(Arrays.asList("","§7Joueur: §c§l" + clicked.getName(),"§7Type: §cPetites Armes", "§7Acheté à: §a§l20 000$","","§8§l§oPossédé",""));
                arme.setItemMeta(armeM);
                info.setItem(21, arme);
            }
			
            if(clicked.hasPermission("gca.voitures")) {
                ItemStack voi = new ItemStack(Material.MINECART);
                ItemMeta voiM = voi.getItemMeta();
                voiM.setDisplayName("§6§lPermis Voiture (PV)");
                voiM.setLore(Arrays.asList("","§7Joueur: §c§l" + clicked.getName(),"§7Type: §6Voitures", "§7Acheté à: §a§l15 000$","","§8§o§lPossédé",""));
                voi.setItemMeta(voiM);
                info.setItem(22, voi);
            }
            ItemStack eye = new ItemStack(Material.EYE_OF_ENDER, 1);
		    ItemMeta eyeM = eye.getItemMeta();
			eyeM.setDisplayName("§dRegarder l'inventaire de " + clicked.getName());
			eye.setItemMeta(eyeM);
			info.setItem(4, eye);
			
			ItemStack jail = new ItemStack(Material.IRON_FENCE, 1);
		    ItemMeta jailM = jail.getItemMeta();
			jailM.setDisplayName("§cMettre en prison " + clicked.getName());
			jail.setItemMeta(jailM);
			info.setItem(6, jail);
			
			
            
            info.setItem(10, getItems(Material.EYE_OF_ENDER, "§dNiveau de recherche",1));
		    int rech = Main.getInstance().data.getRech(clicked.getUniqueId());
		    
			if(rech == 1) {
				info.setItem(12, getItems(Material.NETHER_STAR, "§c§lNiveau 1",1));
			}
			if(rech == 2) {
				info.setItem(12, getItems(Material.NETHER_STAR, "§c§lNiveau 1",1));
				info.setItem(13, getItems(Material.NETHER_STAR, "§c§lNiveau 2",1));
			}
			if(rech == 3) {
				info.setItem(12, getItems(Material.NETHER_STAR, "§c§lNiveau 1",1));
				info.setItem(13, getItems(Material.NETHER_STAR, "§c§lNiveau 2",1));
				info.setItem(14, getItems(Material.NETHER_STAR, "§c§lNiveau 3",1));
			}
			if(rech == 4) {
				info.setItem(12, getItems(Material.NETHER_STAR, "§c§lNiveau 1",1));
				info.setItem(13, getItems(Material.NETHER_STAR, "§c§lNiveau 2",1));
				info.setItem(14, getItems(Material.NETHER_STAR, "§c§lNiveau 3",1));
				info.setItem(15, getItems(Material.NETHER_STAR, "§4§lNiveau 4",1));
			}
			if(rech == 5) {
				info.setItem(12, getItems(Material.NETHER_STAR, "§c§lNiveau 1",1));
				info.setItem(13, getItems(Material.NETHER_STAR, "§c§lNiveau 2",1));
				info.setItem(14, getItems(Material.NETHER_STAR, "§c§lNiveau 3",1));
				info.setItem(15, getItems(Material.NETHER_STAR, "§4§lNiveau 4",1));
				info.setItem(16, getItems(Material.NETHER_STAR, "§4§lNiveau 5",1));
			}
			
            
            player.openInventory(info);
	}
 
}else {
	player.sendMessage("§cVous n'avez pas la permission");
}
} }

}
	}}
