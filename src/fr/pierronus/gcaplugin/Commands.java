package fr.pierronus.gcaplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.SirBlobman.combatlogx.api.ICombatLogX;
import com.SirBlobman.combatlogx.api.listener.*;
import com.SirBlobman.combatlogx.api.utility.ICombatManager;

public class Commands implements CommandExecutor {
	ArrayList<Player> tuto = new ArrayList<Player>();
	
	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	String alert = "§7(§c!§7) ";
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
	    ICombatLogX plugin = getAPI();
	    ICombatManager combatManager = plugin.getCombatManager();
	    return combatManager.isInCombat(player);
	}
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
        if(sender instanceof Player) {
        	
            Player player = (Player) sender;
            if(tuto.contains(player)) {
            	return false;
            }
            if(label.startsWith("minecraft:kill")) {
            	
            }
            if(label.equalsIgnoreCase("appart")) {
            	if(player.hasPermission("gca.admin")) {
            		String nom = args[0];
            		String prix = args[1];
                	Bukkit.dispatchCommand(player, "rg define " + nom);
                	Bukkit.dispatchCommand(player, "rg priority " + nom + " 3");
                	Bukkit.dispatchCommand(player, "as add buy " + nom);
                	Bukkit.dispatchCommand(player, "rg flag " + nom + " use -g non_members deny");
                	Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
						@Override
						public void run() {
							Bukkit.dispatchCommand(player, "as setprice " + prix + " " + nom);
							player.sendMessage("§7(§c!§7) §cAppartement " + nom + " crée");
						}
					}, 10);
                	
            	}
            }
            if(label.equalsIgnoreCase("eeao")) {
            	if(player.hasPermission("gca.admin")) {
            		ItemStack sw = new ItemStack(Material.BLAZE_ROD, 1);
                    ItemMeta swM = sw.getItemMeta();
                    swM.setDisplayName("§e§lEverything Everywhere All at Once");
                    sw.setItemMeta(swM);
                    player.getInventory().addItem(sw);
            	}
            }
            if(label.equalsIgnoreCase("fi")) {
            	ItemStack it = new ItemStack(Material.SADDLE);
            	Item item = player.getLocation().getWorld().dropItem(player.getLocation(), it);
            	item.setPickupDelay(Integer.MAX_VALUE);
            }
            if(label.equalsIgnoreCase("police")) {
            ItemStack sw = new ItemStack(Material.FERMENTED_SPIDER_EYE, 1);
            ItemMeta swM = sw.getItemMeta();
            swM.setDisplayName("§b§lCheck");
            sw.setItemMeta(swM);
            ItemStack loc = new ItemStack(Material.COMPASS);
            ItemMeta locM = loc.getItemMeta();
            locM.setDisplayName("§bLocaliser");
            loc.setItemMeta(locM);
            ItemStack radio = new ItemStack(Material.PRISMARINE_CRYSTALS, 1);
            ItemMeta radioM = radio.getItemMeta();
            radioM.setDisplayName("§c§lRadio");
            radio.setItemMeta(radioM);
            ItemStack aa = new ItemStack(Material.BLAZE_ROD, 1);
            ItemMeta aaM = sw.getItemMeta();
            aaM.setDisplayName("§a§lMenottes");
            aa.setItemMeta(aaM);
            if(player.hasPermission("gca.police")) {
                double y = player.getLocation().getY();
                double x = player.getLocation().getX();
                double z = player.getLocation().getZ();
                if(!(y < 67 || y > 98) && !(x < 72 || x > 92) && !(z < -243 || z > -228) ){
                if(player.getInventory().contains(sw) || player.getInventory().contains(aa) || player.getInventory().contains(Material.GOLD_HOE) || player.getInventory().contains(Material.COMPASS)) {
                    player.getInventory().remove(sw);
                    player.getInventory().remove(aa);
                    player.getInventory().remove(loc);
                    player.getInventory().remove(radio);
                    player.getInventory().remove(Material.GOLD_HOE);
                    player.sendMessage("§b§l[GCA] §cVous n'avez plus vos outils");
                }
                else {
                	int empty;
                	empty = 0;
                	for (int i = 0; i < player.getInventory().getSize(); i++) {
                	    if (player.getInventory().getItem(i) == null) empty = empty + 1;
                	    else if (player.getInventory().getItem(i).getType() == Material.AIR) empty = empty + 1;
                	}
                	if(empty >= 5) {
                		player.getInventory().addItem(sw);
                        player.getInventory().addItem(aa);
                        player.getInventory().addItem(radio);
                        player.getInventory().addItem(loc);
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        Bukkit.dispatchCommand(console, "shot give " + player.getName() + " FAMAS");
                        
                        player.sendMessage("§b§l[GCA] §aVoici vos outils");
                	}else {
                    player.sendMessage(alert+"§cVotre inventaire est plein");
                	}
                }
            }
                else {
                    player.sendMessage("§b§l[GCA] §cVous n'êtes pas au commissariat. Rendez vous là-bas puis refaites la commande");
                }
            }
            else {
                player.sendMessage("§6§l[GCA] §cVous n'avez pas la permission");
        }
            }
            if(label.equalsIgnoreCase("telephone")) {
                if(player.hasPermission("gca.admin")) {
        		ItemStack tel = new ItemStack(Material.SHEARS);
        		ItemMeta telM = tel.getItemMeta();
        		telM.setDisplayName("§b§lTéléphone");
        		tel.setItemMeta(telM);
        	    player.getInventory().addItem(tel);
            }
        }
            if(label.equalsIgnoreCase("points")) {
            	Inventory menu = Bukkit.createInventory(null, 27,"§6Boutique GcaCoins");
            	int points = Main.getInstance().data.getPoints(player.getUniqueId());
            	menu.setItem(22, getItem(Material.EYE_OF_ENDER, (short) 0, "§6§lVos points GCA",Arrays.asList("§7Vous avez §c§l" + String.valueOf(points) + " §7points §6GCA"),1));
            	menu.setItem(13, getItem(Material.REDSTONE_BLOCK, (short) 0, "§4§lBoutique à venir...", Arrays.asList(),1));
            	player.openInventory(menu);
            }
            if(label.equalsIgnoreCase("guide")) {
            	Inventory guide = Bukkit.createInventory(null, 36, "§b§lTéléphone > §f§bGuide");
            	guide.setItem(10, getItem(Material.EMPTY_MAP, (short) 0, "§fComment jouer ?", Arrays.asList("","§aTout autour de la map vous trouvez des coffres",
            			"§aoù vous pourrez trouver des loots tels que"
            			,"§ade l'argent, des munitions, ou encore des armes.",
            			"§aA vous de choisir ce que vous voulez être : policier civilisé ou bien",
            			"§abaron de la drogue redoutable..."), 1));
            	
            	guide.setItem(11, getItem(Material.GOLD_SPADE, (short) 0, "§cComment obtenir les armes ?", Arrays.asList("","§aPour obtenir des armes, vous pouvez chercher",
            			"§adans les coffres autour de la map, ou vous pouvez"
            			,"§aen acheter chez l'armurier ou le dealeur illégal.",
            			"§aVeillez à ne pas vous faire repérer par la police..."), 1));
            	guide.setItem(12, getItem(Material.PRISMARINE_SHARD, (short) 0, "§aComment payer ?", Arrays.asList("","§aVous pourrez trouver un peu partout des banques",
            			"§adans lesquelles vous pourrez retirer de l'argent."
            			,"§aCet argent vous servira pour vous payer ce",
            			"§aque vous voulez, mais attention à ne pas vous",
            			"§afaire tuer!"), 1));
            	guide.setItem(13, getItem(Material.PRISMARINE_CRYSTALS, (short) 0, "§bComment devenir policier ?", Arrays.asList("","§aRegarder discord"), 1));
            	guide.setItem(14, getItem(Material.BOOK, (short) 0, "§6Comment marchent les permis ?", Arrays.asList("","§aIl y a 2 types de permis :","",
            			"§c- Le permis d'armes, celui qui vous servira pour"
            			,"§ctransporter des petites armes vendues en armurerie", "§cen toute légalite.","",
            			"§6- Le permis voiture, qui vous permet de conduire",
            			"§6sans vous faire arrêter."), 1));
            	guide.setItem(15, getItem(Material.SULPHUR, (short) 0, "§aComment avoir de l'argent ?", Arrays.asList("","§aVous pouvez soit chercher dans les coffres,",
            			"§asoit aller dans l'un des 4 endroits métiers, ou faire des","§amétiers illégaux..."
            			,"§ade la map. §cMais méfiez vous, le PVP est actif!"), 1));
            	guide.setItem(16, getItem(Material.DIAMOND, (short)0,"§9§lCliquez pour rejoindre le Discord", Arrays.asList(), 1));
            	guide.setItem(19, getItem(Material.ENDER_PEARL, (short)0,"§eSuggestions ?",Arrays.asList("","§aSi vous avez des suggestions d'infos","§aà ajouter dans le guide, faites-le","§asavoir !"),1));
            	
            	
            	player.openInventory(guide);
            	
            }
            if(label.equalsIgnoreCase("rezghetheth")) {
            	Inventory mineur = Bukkit.createInventory(null, 45, "§4Mineur");
            	mineur.setItem(11, getItem(Material.COAL_ORE,(short)0, "§a§lVendre 1 Minerai de Charbon",Arrays.asList("","§a§lPrix : 2$"),1));
            	mineur.setItem(19, getItem(Material.DIAMOND_PICKAXE, (short) 0, "§bAcheter une pioche", Arrays.asList("","§a§lPrix : 200$"), 1));
            	mineur.setItem(29, getItem(Material.COAL_ORE,(short)0, "§a§lVendre 32 Minerais de Charbon",Arrays.asList("","§a§lPrix : 64$"),32));
            	mineur.setItem(13, getItem(Material.IRON_ORE,(short)0, "§a§lVendre 1 Minerai de Fer",Arrays.asList("","§a§lPrix : 8$"),1));
            	mineur.setItem(31, getItem(Material.IRON_ORE,(short)0, "§a§lVendre 32 Minerais de Fer",Arrays.asList("","§a§lPrix : 256$"),32));
            	mineur.setItem(15, getItem(Material.EMERALD_ORE,(short)0, "§a§lVendre 1 Minerai d'Emeraude",Arrays.asList("","§a§lPrix : 16$"),1));
            	mineur.setItem(33, getItem(Material.EMERALD_ORE,(short)0, "§a§lVendre 32 Minerais d'Emeraude",Arrays.asList("","§a§lPrix : 512$"),32));
            	
            	player.openInventory(mineur);
            	
            }
            if(label.equalsIgnoreCase("sdfsdfsdfre")) {
            	Inventory bucheron = Bukkit.createInventory(null, 45, "§2Bûcheron");
            	bucheron.setItem(11, getItem(Material.LOG_2, (short)0,"§a§lVendre 1 Buche d'acacia",Arrays.asList("","§a§lPrix : 2$"),1));
            	bucheron.setItem(19, getItem(Material.DIAMOND_AXE, (short) 0, "§bAcheter une hache", Arrays.asList("","§a§lPrix : 200$"), 1));
            	bucheron.setItem(29, getItem(Material.LOG_2, (short)0,"§a§lVendre 32 Buche d'acacia",Arrays.asList("","§a§lPrix : 64$"),32));
            	bucheron.setItem(13, getItem(Material.LOG,(short)1, "§a§lVendre 1 Buche de sapin",Arrays.asList("","§a§lPrix : 8$"),1));
            	bucheron.setItem(31, getItem(Material.LOG,(short)1, "§a§lVendre 32 Buche de sapin",Arrays.asList("","§a§lPrix : 256$"),32));
            	bucheron.setItem(15, getItem(Material.LOG,(short)2, "§a§lVendre 1 Buche de bouleau",Arrays.asList("","§a§lPrix : 16$"),1));
            	bucheron.setItem(33, getItem(Material.LOG,(short)2, "§a§lVendre 32 Buche de bouleau",Arrays.asList("","§a§lPrix : 512$"),32));
            	
            	player.openInventory(bucheron);
            }
            if(label.equalsIgnoreCase("sefzgztzytzafe")) {
            	Inventory fermier = Bukkit.createInventory(null, 45, "§aFermier");
            	fermier.setItem(20, getItem(Material.CARROT_ITEM, (short) 0, "§a§lVendre 4 carottes", Arrays.asList("","§a§lPrix : 1$"), 4));
            	fermier.setItem(22, getItem(Material.CARROT_ITEM, (short) 0, "§a§lVendre 32 carottes", Arrays.asList("","§a§lPrix : 8$"), 32));
            	fermier.setItem(24, getItem(Material.CARROT_ITEM, (short) 0, "§a§lVendre 64 carottes", Arrays.asList("","§a§lPrix : 16$"), 64));
            	player.openInventory(fermier);
            }
            if(label.equalsIgnoreCase("zfvzaefzefezaf")) {
            	Inventory pecheur = Bukkit.createInventory(null, 45, "§bPêcheur");
            	pecheur.setItem(11, getItem(Material.RAW_FISH, (short) 0, "§a§lVendre 1 poisson", Arrays.asList("","§a§lPrix: 50$"), 1));
            	pecheur.setItem(19, getItem(Material.FISHING_ROD, (short) 0, "§bAcheter une canne à pêche", Arrays.asList("","§a§lPrix : 200$"), 1));
            	pecheur.setItem(29, getItem(Material.RAW_FISH, (short) 0, "§a§lVendre 32 poissons", Arrays.asList("","§a§lPrix: 1600$"), 32));
            	pecheur.setItem(13, getItem(Material.RAW_FISH, (short) 2, "§a§lVendre 1 poisson tropical", Arrays.asList("","§a§lPrix: 125$"), 1));
            	pecheur.setItem(31, getItem(Material.RAW_FISH, (short) 2, "§a§lVendre 32 poisson tropicaux", Arrays.asList("","§a§lPrix: 4000$"), 32));
            	pecheur.setItem(15, getItem(Material.RAW_FISH, (short) 3, "§a§lVendre 1 poisson-globe", Arrays.asList("","§a§lPrix: 200$"), 1));
            	pecheur.setItem(33, getItem(Material.RAW_FISH, (short) 3, "§a§lVendre 32 poisson-globes", Arrays.asList("","§a§lPrix: 6400$"), 32));
            	player.openInventory(pecheur);

            }
            if(label.equalsIgnoreCase("lbanqueezd")) {
            	if(isInCombat(player)) {
    				player.sendMessage("§cVous êtes en combat");
    				return false;
    			}
        		Inventory banque = Bukkit.createInventory(null, 36, "§aBanque");

        		ItemStack depot10 = new ItemStack(Material.EMERALD);
                ItemMeta depot10M = depot10.getItemMeta();
                depot10M.setDisplayName("§aDéposer 10$");
                depot10M.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                depot10M.setLore(Arrays.asList("","Cliquez pour déposer","1 pièce (10$)"));
                depot10.setItemMeta(depot10M);
                
                ItemStack depot90 = new ItemStack(Material.PRISMARINE_SHARD);
                ItemMeta depot90M = depot90.getItemMeta();
                depot90M.setDisplayName("§aDéposer 90$");
                depot90M.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                depot90M.setLore(Arrays.asList("","Cliquez pour déposer","1 pile de billets (90$)"));
                depot90.setItemMeta(depot90M);
                
                ItemStack depot10k = new ItemStack(Material.SULPHUR);
                ItemMeta depot10kM = depot10k.getItemMeta();
                depot10kM.setDisplayName("§aDéposer 10 000$");
                depot10kM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                depot10kM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                depot10kM.addEnchant(Enchantment.DEPTH_STRIDER, 1, false);
                depot10kM.setLore(Arrays.asList("","Cliquez pour déposer","1 sac de pièces (10 000$)"));
                depot10k.setItemMeta(depot10kM);
                
                
                ItemStack retrait10 = new ItemStack(Material.EMERALD);
                ItemMeta retrait10M = retrait10.getItemMeta();
                retrait10M.setDisplayName("§cRetirer 10$");
                retrait10M.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                retrait10M.setLore(Arrays.asList("","Cliquez pour retirer","1 pièce (10$)"));
                retrait10.setItemMeta(retrait10M);
                
                ItemStack retrait90 = new ItemStack(Material.PRISMARINE_SHARD);
                ItemMeta retrait90M = retrait90.getItemMeta();
                retrait90M.setDisplayName("§cRetirer 90$");
                retrait90M.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                retrait90M.setLore(Arrays.asList("","Cliquez pour retirer","1 pile de billets (90$)"));
                retrait90.setItemMeta(retrait90M);
                
                ItemStack retrait10k = new ItemStack(Material.SULPHUR);
                ItemMeta retrait10kM = retrait10k.getItemMeta();
                retrait10kM.setDisplayName("§cRetirer 10 000$");
                retrait10kM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                retrait10kM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                retrait10kM.addEnchant(Enchantment.DEPTH_STRIDER, 1, false);
                retrait10kM.setLore(Arrays.asList("","Cliquez pour retirer","1 sac de pièces(10 000$)"));
                retrait10k.setItemMeta(retrait10kM);
                
                ItemStack ban = new ItemStack(Material.EMERALD, 1);
    			ItemMeta banM = ban.getItemMeta();
    			banM.setDisplayName("§aBanque");
    			String bal = "§7Vous avez §a§l" + String.valueOf(Main.econ.getBalance(player.getName())) + " §a$";
    			banM.setLore(Arrays.asList(bal));
    			ban.setItemMeta(banM);
                
                banque.setItem(10, depot10);
                banque.setItem(11, depot90);
                banque.setItem(20, depot10k);
                banque.setItem(16, retrait10);
                banque.setItem(15, retrait90);
                banque.setItem(24, retrait10k);
                banque.setItem(31, ban);
                
                player.openInventory(banque);
                
            }
            if(label.equalsIgnoreCase("adpkkdlslp")) {
            	Inventory permis = Bukkit.createInventory(null, 9, "§cAcheter le §c§lpermis d'armes");
            	ItemStack arme = new ItemStack(Material.GOLD_SPADE);
                ItemMeta armeM = arme.getItemMeta();
                armeM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                armeM.setDisplayName("§c§lPermis d'Armes (PdA)");
                if(player.hasPermission("gca.armes")) {
                	armeM.setLore(Arrays.asList("","§7Type: §cPetites Armes","","§a§lVous le possédez déjà"));
                }else {
                	armeM.setLore(Arrays.asList("","§7Type: §cPetites Armes","","§7(§c!§7) §cVous ne le possédez pas","§a§lCliquez pour l'acheter pour §a§l§u20 000$"));
                }
                arme.setItemMeta(armeM);
                permis.setItem(4, arme);
                player.openInventory(permis);
            }
            if(label.equalsIgnoreCase("ffkefeeksk")) {
            	Inventory voiture = Bukkit.createInventory(null, 9, "§6Acheter le §6§lpermis voiture");
            	ItemStack voi = new ItemStack(Material.MINECART);
                ItemMeta voiM = voi.getItemMeta();
                voiM.setDisplayName("§6§lPermis Voiture (PV)");
                voiM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                if(player.hasPermission("gca.voiture")) {
                	voiM.setLore(Arrays.asList("","§7Type: §6Voitures","","§a§lVous le possédez déjà"));
                }else {
                	voiM.setLore(Arrays.asList("","§7Type: §6Voitures","","§7(§c!§7) §cVous ne le possédez pas","§a§lCliquez pour l'acheter pour §a§l§u15 000$"));
                }
                voi.setItemMeta(voiM);
                voiture.setItem(4, voi);
                player.openInventory(voiture);
                
            }
            if(label.equalsIgnoreCase("grade")) {
            	Inventory grade = Bukkit.createInventory(null, 45, "§5Grades");
            	grade.setItem(10, getItem(Material.WHEAT,(short)0, "§a§lVoyou", Arrays.asList("", "§f- kit voyou", "§f- 3 items hdv & enchere","","§aPrix : §735 000$"),1));
            	grade.setItem(13, getItem(Material.GOLD_SPADE,(short)0, "§5§lHors-la-Loi", Arrays.asList("", "§f- kit hll", "§f- 5 items hdv & enchere","§6- Mettre des primes","","§aPrix : §7100 000$"),1));
            	grade.setItem(16, getItem(Material.FEATHER,(short)0, "§6§lContrebandier", Arrays.asList("","§f- kit contrebandier","§f- 7 items hdv & enchere", "§e- Accès a la bourse","§a- 2 propriétés","","§aPrix : §7250 000$"),1));
            	grade.setItem(28, getItem(Material.WOOD_SWORD,(short)0, "§1§lBraqueur", Arrays.asList("", "§f- kit braqueur", "§f- 10 items hdv & enchere","§5- Créer un clan","","§aPrix : §7700 000$"),1));
            	grade.setItem(31, getItem(Material.SUGAR,(short)0, "§c§lGangster", Arrays.asList("", "§f- kit gangster", "§f- 16 items hdv & enchere","§a- 3 propriétés","","§aPrix : §71 500 000$"),1));
            	grade.setItem(34, getItem(Material.SULPHUR, (short)0,"§d§lMafieux", Arrays.asList("", "§f- kit mafieux", "§f- 20 items hdv & enchere","§a- 4 propriétés","","§aPrix : §75 000 000$"),1));
            	
           player.openInventory(grade);
            }
            
            if(label.equalsIgnoreCase("brs")) {
            	if(player.hasPermission("gca.bourse")) {
            	Inventory bourse = Bukkit.createInventory(null, 9, "§6Bourse");
            	bourse.setItem(6, getItem(Material.BOOK, (short)0, "§a§lPortfolio", Arrays.asList("","§aLe portfolio permet de voir","§ales actions que vous possédez"),1));
            	bourse.setItem(4, getItem(Material.EYE_OF_ENDER,(short)0, "§9§lInformations", Arrays.asList("","§aLes actions de ces entreprises","§asont mises a jour en fonction de la vraie bourse.","§cAchetez des actions a prix bas et","§crevendez les à prix haut!"),1));
            	bourse.setItem(2, getItem(Material.EMERALD,(short)0, "§c§lListe des actions", Arrays.asList("","§cLe prix de toutes les actions sont","§ccelles actuelles."),1));
                player.openInventory(bourse);
            	}else {
            	player.sendMessage(alert+"§cVous devez être minimum §6§lContrebandier");
            }
            }
            if(label.equalsIgnoreCase("gilet")) {
            	if(player.hasPermission("gca.admin")) {
            		ItemStack stop = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
    				ItemMeta stopM = stop.getItemMeta();
    				stopM.setDisplayName("§4§lGilet Kamikaze");
    				stop.setItemMeta(stopM);
    				if(!(player.getInventory().firstEmpty() == -1)) {
    					player.getInventory().addItem(stop);
    				}
    				
            	}
            }
            if(label.equalsIgnoreCase("tuto")) {
            if(player.hasPermission("gca.tutoriel")) {
				
    			if(isInCombat(player)) {
    				player.sendMessage("§cVous êtes en combat");
    				return false;
    			}
    			if(player.getGameMode() == GameMode.SPECTATOR) {
    				player.sendMessage("§7(§c!§7) §f§cVous êtes déja en tutoriel...");
    				
    			}
    			else {
    			Location one = new Location(player.getWorld(), -55, 70, -192, -62f, 23f); //banque 1
    			Location two = new Location(player.getWorld(), 5, 71, -235, 148f, 25.5f); //Armurier 1
    			Location five = new Location(player.getWorld(), 73, 71, -233, -140f, 14f); //commissariat 1s
    			Location six = new Location(player.getWorld(), 97, 70, -161, 0f, 22.6f); //casino 1 
    			Location seven = new Location(player.getWorld(), -64, 70, -154, -130.5f, 19.4f); //concesso 1
    			Location eight = new Location(player.getWorld(), -105, 64, -141, 90.3f, 5.8f); //tel 1
    			Location spawn = new Location(player.getWorld(), 132.5, 69, -169.500, 0f, 0f); //spawn
    			player.playSound(one, Sound.RECORD_WARD, 50000, 1); 
    				
    			int wait = 257; //(20 * 12.5)
    			tuto.add(player);
    			player.setGameMode(GameMode.SPECTATOR);
    			player.setFlySpeed(0);
    			player.sendMessage(StringUtils.repeat(" \n", 100));
    			player.sendMessage("                        §7§lBienvenue sur §6§lGCA\n \n");
    			player.sendMessage("                §cLe serveur où §a§l§ntout est possible");

    			
    			Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
    	        	@Override
    	        	public void run() {
    	        		player.teleport(one);
    	        		player.sendMessage(StringUtils.repeat(" \n", 100));
    	    			player.sendMessage("                §a§lIci, c'est la Banque de Greenfield!\n \n   §aIci, vous pourrez retirer de l'argent ou en déposer\n    §aen toute sécurité.\n \n    §b§lIl y a plusieurs banque à travers toute la map! ");
    	    			Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
    	    				@Override
    	    				public void run() {
    	    	        		player.teleport(two);
    	    					player.sendMessage(StringUtils.repeat(" \n", 100));
    	    					player.sendMessage("                        §a§lLui, c'est §c§ll'Armurier\n \n    §7C'est lui qui s'occupe de vendre des armes §7§nlégales§f§7 , des\n   §7gilets pare-balles, permis d'armes, protections corporelles\n \n   §7§lPour les plus grosses armes, s'adresser au \n   §0dealeur d'armes illégales");
    	    					
    	    					Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
    	    										@Override
    	    										public void run() {
    	    							        		player.teleport(five);
    	    											player.sendMessage(StringUtils.repeat(" \n", 100));
    	    											player.sendMessage("                            §a§lIci, c'est le §1Commissariat\n \n    §7Les policier se retrouvent ici pour accéder\n    au classement des personnes les plus recherchés§c\n    §cet aussi avoir leurs outils!");
    	    											Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
    	    	    										@Override
    	    	    										public void run() {
    	    	    							        		player.teleport(six);
    	    	    											player.sendMessage(StringUtils.repeat(" \n", 100));
    	    	    											player.sendMessage("                     §a§lIci, c'est le §d§lCasino\n \n    §dLà vous pouvez acheter des clés à lootbox\n    §dpour peut-être avoir une chance de gagner + de ce\n    §dque vous avez dépensé. \n ");
    	    	    											Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
    	    	    	    										@Override
    	    	    	    										public void run() {
    	    	    	    							        		player.teleport(seven);
    	    	    	    											player.sendMessage(StringUtils.repeat(" \n", 100));
    	    	    	    											player.sendMessage("                  §a§lIci, c'est le §9§lConcessionnaire\n \n    §9Dans cet endroit, vous pouvez acheter des motos\n    §9et des voitures pour rouler dans la ville. Veillez\n    §9toutefois à posséder un permis Voiture :) \n ");
    	    	    	    											Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
    	    	    	    	    										@Override
    	    	    	    	    										public void run() {
    	    	    	    	    							        		player.teleport(eight);
    	    	    	    	    											player.sendMessage(StringUtils.repeat(" \n", 100));
    	    	    	    	    											player.sendMessage("             §a§lEt enfin, cet item est le §b§lTéléphone\n \n    §bVous aurez l'occasion de le découvrir en jouant\n    §bjuste après le tutoriel. Vous pourrez y trouver\n    §bplusieurs apps bien utiles ^^\n ");
    	    	    	    	    											Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
    	    	    	    	    	    										@Override
    	    	    	    	    	    										public void run() {
    	    	    	    	    	    											player.setFlySpeed(1);
    	    	    	    	    	    											player.setGameMode(GameMode.SURVIVAL);
    	    	    	    	    	    											player.removePotionEffect(PotionEffectType.SPEED);
    	    	    	    	    	    											player.sendMessage(StringUtils.repeat(" \n", 100));
    	    	    	    	    	    											player.sendMessage("                         §6§lMerci, et bon jeu ! \n \n \n \n \n");
    	    	    	    	    	    											player.teleport(spawn);
    	    	    	    	    	    											ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    	    	    	    	    	    											String welcome = "{\"text\":\"Bienvenue sur GCA\",\"color\":\"gold\",\"bold\":\"true\"}";
    	    	    	    	    	    											String command = "title " + player.getName() + " title " + welcome;
    	    	    	    	    	    											Bukkit.dispatchCommand(console, command);
    	    	    	    	    	    										}
    	    	    	    	    	    									}, wait);
    	    	    	    	    											
    	    	    										}
    	    	    									}, wait);
    	    											
    	    										}
    	    									}, wait);
    	    								}
    	    							}, wait);
    	    						}
    	    					}, wait);
    	  				}
    	    			}, wait);
    	        		
    	        		
    	        	}
    	        }, 20 * 5);
    			tuto.remove(player);
    		}}
            else {
    			player.sendMessage("§7(§c!§7) §f§cVous n'avez pas la permission.");
    		}
    		if(!(sender instanceof Player)) {
    			sender.sendMessage("§cLa commande peut etre effectuee seulement a partir d'un joueur");
	}
    		}
        }
    		
        
        return false;
    }
	public ItemStack getItem(Material material, Short bte, String customName, List<String> lore, int amount) {
		ItemStack it = new ItemStack(material, amount, bte);
		ItemMeta itM = it.getItemMeta();
		if(customName != null) itM.setDisplayName(customName);
		if(lore != null) itM.setLore(lore);
		it.setItemMeta(itM);
 
		return it;
	}
	public ItemStack get(Material material, String customName, List<String> lore, int amount) {
		ItemStack it = new ItemStack(material, amount);
		ItemMeta itM = it.getItemMeta();
		if(customName != null) itM.setDisplayName(customName);
		if(lore != null) itM.setLore(lore);
		it.setItemMeta(itM);
 
		return it;
	}
	
	public ItemStack tenkem(int amt) {
		ItemStack emerald = new ItemStack(Material.SULPHUR, amt);
	    ItemMeta emeraldM = emerald.getItemMeta();
	    emeraldM.setDisplayName("§210 000$");
	    emeraldM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    emeraldM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    emeraldM.addEnchant(Enchantment.DEPTH_STRIDER, 1, false);
	    emerald.setItemMeta(emeraldM);
	    
	    return emerald;
	}
	
	public ItemStack tenem(int amt) {
		ItemStack emerald = new ItemStack(Material.EMERALD, amt);
	    ItemMeta emeraldM = emerald.getItemMeta();
	    emeraldM.setDisplayName("§a10$");
	    emeraldM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    emerald.setItemMeta(emeraldM);
	    
	    return emerald;
	}
	
	public ItemStack nintyem(int amt) {
		ItemStack emerald = new ItemStack(Material.PRISMARINE_SHARD, amt);
	    ItemMeta emeraldM = emerald.getItemMeta();
	    emeraldM.setDisplayName("§a90$");
	    emeraldM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    emerald.setItemMeta(emeraldM);
	    
	    return emerald;
	}
	
	
}
