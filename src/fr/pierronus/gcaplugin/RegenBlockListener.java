package fr.pierronus.gcaplugin;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;

public class RegenBlockListener implements Listener{
	
	@EventHandler
	public void onMine(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
		int xpmineur = Main.getInstance().data.getXpMineur(player.getUniqueId());
		int lvlmineur;
		lvlmineur = 0;
		
		if(xpmineur < 10000) {
			lvlmineur = 0;
		}
		if(xpmineur < 20000 && xpmineur >= 10000) {
			lvlmineur = 1;
		}
		if(xpmineur < 35000 && xpmineur >= 20000) {
			lvlmineur = 2;
		}
		if(xpmineur < 55000 && xpmineur >= 35000) {
			lvlmineur = 3;
		}
		if(xpmineur < 80000 && xpmineur >= 55000) {
			lvlmineur = 4;
		}
		if(xpmineur < 115000 && xpmineur >= 80000) {
			lvlmineur = 5;
		}
		if(xpmineur < 150000 && xpmineur >= 115000) {
			lvlmineur = 6;
		}
		if(xpmineur < 200000 && xpmineur >= 150000) {
			lvlmineur = 7;
		}
		if(xpmineur >= 200000) {
			lvlmineur = 8;
		}
		Block block = event.getBlock();
		int y = block.getY();
		int x = block.getX();
		int z = block.getZ();
		
		if(!(y < 64 || y > 73) && !(x < -176 || x > -130) && !(z < 7 || z > 31)) {
			if(block.getType() == Material.STONE) {
			Random rand = new Random();
    		int rdmint = rand.nextInt(100);
    		
    		//1 : 5 || 2: 10 || 3: 25 || 4: 40 || 5: 55 | 6: 70 || 7 : 85 || 8: 100
    		if(rdmint >= 0 && rdmint <= 75) {
    			int rdminttwo = rand.nextInt(100);
    			if(lvlmineur == 1) {
    				if(rdminttwo <= 5) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL_ORE, 1));
    				}
    			}
    			if(lvlmineur == 2) {
    				if(rdminttwo <= 10) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL_ORE, 1));
    				}
    			}
    			if(lvlmineur == 3) {
    				if(rdminttwo <= 25) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL_ORE, 1));
    				}
    			}
    			if(lvlmineur == 4) {
    				if(rdminttwo <= 40) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL_ORE, 1));
    				}
    			}
    			if(lvlmineur == 5) {
    				if(rdminttwo <= 55) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL_ORE, 1));
    				}
    			}
    			if(lvlmineur == 6) {
    				if(rdminttwo <= 70) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL_ORE, 1));
    				}
    			}
    			if(lvlmineur == 7) {
    				if(rdminttwo <= 85) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL_ORE, 1));
    				}
    			}
    			if(lvlmineur == 8) {
    				block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL_ORE, 1));
    			}
    			
    			Main.getInstance().data.addXpMineur(player.getUniqueId(), 10);
    			
    			//coal
    			block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL_ORE, 1));
    			block.getDrops().clear();
				String hello = "{\"text\":\"+1 Charbon (+10xp)\",\"color\":\"gray\",\"bold\":\"true\"}";
				this.send(player, hello);
    		}
    		// PUTAIN DE BUG DE MERDE
    		if(rdmint >= 76 && rdmint <= 95) {
    			int rdminttwo = rand.nextInt(100);
    			if(lvlmineur == 1) {
    				if(rdminttwo <= 5) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_ORE, 1));
    				}
    			}
    			if(lvlmineur == 2) {
    				if(rdminttwo <= 10) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_ORE, 1));
    				}
    			}
    			if(lvlmineur == 3) {
    				if(rdminttwo <= 25) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_ORE, 1));
    				}
    			}
    			if(lvlmineur == 4) {
    				if(rdminttwo <= 40) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_ORE, 1));
    				}
    			}
    			if(lvlmineur == 5) {
    				if(rdminttwo <= 55) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_ORE, 1));
    				}
    			}
    			if(lvlmineur == 6) {
    				if(rdminttwo <= 70) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_ORE, 1));
    				}
    			}
    			if(lvlmineur == 7) {
    				if(rdminttwo <= 85) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_ORE, 1));
    				}
    			}
    			if(lvlmineur == 8) {
    				block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_ORE, 1));
    			}
    			Main.getInstance().data.addXpMineur(player.getUniqueId(), 25);
    			block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_ORE, 1));
    			block.getDrops().clear();
				String hello = "{\"text\":\"+1 Fer (+25xp)\",\"color\":\"gold\",\"bold\":\"true\"}";
				this.send(player, hello);
    		}
			if(rdmint >= 96 && rdmint <= 100) {
				int rdminttwo = rand.nextInt(100);
    			if(lvlmineur == 1) {
    				if(rdminttwo <= 5) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD_ORE, 1));
    				}
    			}
    			if(lvlmineur == 2) {
    				if(rdminttwo <= 10) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD_ORE, 1));
    				}
    			}
    			if(lvlmineur == 3) {
    				if(rdminttwo <= 25) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD_ORE, 1));
    				}
    			}
    			if(lvlmineur == 4) {
    				if(rdminttwo <= 40) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD_ORE, 1));
    				}
    			}
    			if(lvlmineur == 5) {
    				if(rdminttwo <= 55) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD_ORE, 1));
    				}
    			}
    			if(lvlmineur == 6) {
    				if(rdminttwo <= 70) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD_ORE, 1));
    				}
    			}
    			if(lvlmineur == 7) {
    				if(rdminttwo <= 85) {
    					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD_ORE, 1));
    				}
    			}
    			if(lvlmineur == 8) {
    				block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD_ORE, 1));
    			}
				Main.getInstance().data.addXpMineur(player.getUniqueId(), 50);
				int rdmint2 = rand.nextInt(100);
				if(rdmint2 >= 85) {
					Main.getInstance().data.addPoints(player.getUniqueId(), 10);			
					player.sendMessage("§7(§c!§7) §7Vous avez gagné §c10 §6GcaCoins §7en minant!");
				}
				block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD_ORE, 1));
				block.getDrops().clear();
				String hello = "{\"text\":\"+1 Emeraude (+50xp)\",\"color\":\"green\",\"bold\":\"true\"}";
				this.send(player, hello);
			}
			
		
			
			
	        Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
	        	
	        
				@Override
	        	public void run() {
	        		block.setType(Material.STONE);
	        	}
	        }, 20 * 60);

		}
			if(!(block.getType() == Material.STONE)) {
				event.setCancelled(true);
				block.getDrops().clear();
			}
			
				
			
			
		}
		
}
	
	@EventHandler
	public void fishBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		int y = block.getY();
		int x = block.getX();
		int z = block.getZ();
		if(!(y < 58 || y > 73) && !(x < -336 || x > -312) && !(z < -1 || z > 22) ){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onWood(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
		int xpbucheron = Main.getInstance().data.getXpBucheron(player.getUniqueId());
		int lvlbucheron;
		lvlbucheron = 0;
		
		
		if(xpbucheron < 10000) {
			lvlbucheron = 0;
		}
		if(xpbucheron < 20000 && xpbucheron >= 10000) {
			lvlbucheron = 1;
		}
		if(xpbucheron < 35000 && xpbucheron >= 20000) {
			lvlbucheron = 2;
		}
		if(xpbucheron < 55000 && xpbucheron >= 35000) {
			lvlbucheron = 3;
		}
		if(xpbucheron < 80000 && xpbucheron >= 55000) {
			lvlbucheron = 4;
		}
		if(xpbucheron < 115000 && xpbucheron >= 80000) {
			lvlbucheron = 5;
		}
		if(xpbucheron < 150000 && xpbucheron >= 115000) {
			lvlbucheron = 6;
		}
		if(xpbucheron < 200000 && xpbucheron >= 150000) {
			lvlbucheron = 7;
		}
		if(xpbucheron >= 20000) {
			lvlbucheron = 8;
		}
		Block block = event.getBlock();
		int y = block.getY();
		int x = block.getX();
		int z = block.getZ();
		if(!(y < 67 || y > 86) && !(x < -329 || x > -198) && !(z < -107 || z > -72) ){
			if(block.getType() == Material.LOG) {
			Random rand = new Random();
    		int rdmint = rand.nextInt(100);
    		int rdminttwo = rand.nextInt(100);
    		if(rdmint >= 0 && rdmint <= 75) {
    			if(lvlbucheron == 1) {
    				if(rdminttwo <= 5) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG_2, (short) 0, 1));
    				}
    			}
    			if(lvlbucheron == 2) {
    				if(rdminttwo <= 10) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG_2, (short) 0, 1));
    				}
    			}
    			if(lvlbucheron == 3) {
    				if(rdminttwo <= 25) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG_2, (short) 0, 1));
    				}
    			}
    			if(lvlbucheron == 4) {
    				if(rdminttwo <= 40) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG_2, (short) 0, 1));
    				}
    			}
    			if(lvlbucheron == 5) {
    				if(rdminttwo <= 55) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG_2, (short) 0, 1));
    				}
    			}
    			if(lvlbucheron == 6) {
    				if(rdminttwo <= 70) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG_2, (short) 0, 1));
    				}
    			}
    			if(lvlbucheron == 7) {
    				if(rdminttwo <= 85) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG_2, (short) 0, 1));
    				}
    			}
    			if(lvlbucheron == 8) {
    				block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG_2, (short) 0, 1));
    			}
    			Main.getInstance().data.addXpBucheron(player.getUniqueId(), 10);
    			//coal
    			block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG_2, (short) 0, 1));
    			block.getDrops().clear();
				String hello = "{\"text\":\"+1 Acacia (+10xp)\",\"color\":\"gold\",\"bold\":\"true\"}";
				this.send(player, hello);
    		}
    		// PUTAIN DE BUG DE MERDE
    		if(rdmint >= 76 && rdmint <= 95) {
    			if(lvlbucheron == 1) {
    				if(rdminttwo <= 5) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 1, 1));
    				}
    			}
    			if(lvlbucheron == 2) {
    				if(rdminttwo <= 10) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 1, 1));
    				}
    			}
    			if(lvlbucheron == 3) {
    				if(rdminttwo <= 25) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 1, 1));
    				}
    			}
    			if(lvlbucheron == 4) {
    				if(rdminttwo <= 40) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 1, 1));
    				}
    			}
    			if(lvlbucheron == 5) {
    				if(rdminttwo <= 55) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 1, 1));
    				}
    			}
    			if(lvlbucheron == 6) {
    				if(rdminttwo <= 70) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 1, 1));
    				}
    			}
    			if(lvlbucheron == 7) {
    				if(rdminttwo <= 85) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 1, 1));
    				}
    			}
    			if(lvlbucheron == 8) {
    				block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 1, 1));
    			}
    			Main.getInstance().data.addXpBucheron(player.getUniqueId(), 25);
    			//iron
    			block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 1, 1));
    			block.getDrops().clear();
				String hello = "{\"text\":\"+1 Sapin (+25xp)\",\"color\":\"dark_gray\",\"bold\":\"true\"}";
				this.send(player, hello);
    		}
			if(rdmint >= 96 && rdmint <= 100) {
				if(lvlbucheron == 1) {
    				if(rdminttwo <= 5) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 2, 1));
    				}
    			}
    			if(lvlbucheron == 2) {
    				if(rdminttwo <= 10) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 2, 1));
    				}
    			}
    			if(lvlbucheron == 3) {
    				if(rdminttwo <= 25) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 2, 1)); 
    				}
    			}
    			if(lvlbucheron == 4) {
    				if(rdminttwo <= 40) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 2, 1));
    				}
    			}
    			if(lvlbucheron == 5) {
    				if(rdminttwo <= 55) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 2, 1));
    				}
    			}
    			if(lvlbucheron == 6) {
    				if(rdminttwo <= 70) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 2, 1));
    				}
    			}
    			if(lvlbucheron == 7) {
    				if(rdminttwo <= 85) {
    					block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 2, 1));
    				}
    			}
    			if(lvlbucheron == 8) {
    				block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 2, 1));
    			}
				Main.getInstance().data.addXpBucheron(player.getUniqueId(), 50);
				int rdmint2 = rand.nextInt(100);
				if(rdmint2 >= 85) {
					Main.getInstance().data.addPoints(player.getUniqueId(), 10);			
					player.sendMessage("§7(§c!§7) §7Vous avez gagné §c10 §6GcaCoins §7en bûchant!");
				}
				block.getWorld().dropItem(block.getLocation(), getItemstack(Material.LOG, (short) 2, 1));
				block.getDrops().clear();
				String hello = "{\"text\":\"+1 Bouleau (+50xp)\",\"color\":\"white\",\"bold\":\"true\"}";
				this.send(player, hello);
			}
			
			
			
	        Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
	        	
	        
	        	@Override
	        	public void run() {
	        		block.setType(Material.LOG);
	        	}
	        }, 20 * 60);
	        

		
			
		}
			if(!(block.getType() == Material.LOG)) {
				event.setCancelled(true);
				block.getDrops().clear();
				
				
			}
		}
}
	
	@EventHandler
	public void onFarm(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
int xpfarmeur = Main.getInstance().data.getXpPecheur(player.getUniqueId());
		

		int lvlfarmeur;
		lvlfarmeur = 0;
		if(xpfarmeur < 10000) {
			lvlfarmeur = 0;
		}
		if(xpfarmeur < 20000 && xpfarmeur >= 10000) {
			lvlfarmeur = 1;
		}
		if(xpfarmeur < 35000 && xpfarmeur >= 20000) {
			lvlfarmeur = 2;
		}
		if(xpfarmeur < 55000 && xpfarmeur >= 35000) {
			lvlfarmeur = 3;
		}
		if(xpfarmeur < 80000 && xpfarmeur >= 55000) {
			lvlfarmeur = 4;
		}
		if(xpfarmeur < 115000 && xpfarmeur >= 80000) {
			lvlfarmeur = 5;
		}
		if(xpfarmeur < 150000 && xpfarmeur >= 115000) {
			lvlfarmeur = 6;
		}
		if(xpfarmeur < 200000 && xpfarmeur >= 150000) {
			lvlfarmeur = 7;
		}
		if(xpfarmeur >= 20000) {
			lvlfarmeur = 8;
		}
		Block block = event.getBlock();
		int y = block.getY();
		int x = block.getX();
		int z = block.getZ();
		if(!(y < 66 || y > 70) && !(x < -93 || x > -55) && !(z < 250 || z > 289) ){
			if(block.getType() == Material.CARROT) {
			String hello;
			int amt= 0;
			Random rand = new Random();
			int rdminttwo = rand.nextInt(100);
			if(lvlfarmeur == 1) {
				if(rdminttwo <= 5) {
					amt = amt+1;
				}
			}
			if(lvlfarmeur == 2) {
				if(rdminttwo <= 10) {
					amt = amt+1;
				}
			}
			if(lvlfarmeur == 3) {
				if(rdminttwo <= 25) {
					amt = amt+1;
				}
			}
			if(lvlfarmeur == 4) {
				if(rdminttwo <= 40) {
					amt = amt+1;
				}
			}
			if(lvlfarmeur == 5) {
				if(rdminttwo <= 55) {
					amt = amt+1;
				}
			}
			if(lvlfarmeur == 6) {
				if(rdminttwo <= 70) {
					amt = amt+1;
				}
			}
			if(lvlfarmeur == 7) {
				if(rdminttwo <= 85) {
					amt = amt+1;
				}
			}
			if(lvlfarmeur == 8) {
				amt = amt+1;
			}
			Main.getInstance().data.addXpFarmeur(player.getUniqueId(), 25);
			block.getDrops().clear();
			if(amt >= 1) {
				hello= "{\"text\":\"+1 Carotte (+1 bonus)\",\"color\":\"gold\",\"bold\":\"true\"}";
			}else {
				hello = "{\"text\":\"+1 Carotte\",\"color\":\"gold\",\"bold\":\"true\"}";
			}
			block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.CARROT_ITEM, amt+1));
			this.send(player, hello);
			
			
	        Bukkit.getScheduler().runTaskLater(Bukkit.getServer().getPluginManager().getPlugin("GCAPlugin"), new Runnable() {
	        	
	        
	        	@Override
	        	public void run() {
	        		block.setType(Material.CARROT);
	        	}
	        }, 20 * 30);

		}
			if(!(block.getType() == Material.CARROT)) {
				event.setCancelled(true);
				block.getDrops().clear();
			}
			
		}
}
	@EventHandler
	public void onFish(PlayerFishEvent event) {
		//1 : 5 || 2: 10 || 3: 20 || 4: 30 || 5: 40 | 6: 55 || 7 : 70 || 8: 90
		Player player = event.getPlayer();
        int xppecheur = Main.getInstance().data.getXpPecheur(player.getUniqueId());
		

		int lvlpecheur;
		lvlpecheur = 0;
		if(xppecheur < 10000) {
			lvlpecheur = 0;
		}
		if(xppecheur < 20000 && xppecheur >= 10000) {
			lvlpecheur = 1;
		}
		if(xppecheur < 35000 && xppecheur >= 20000) {
			lvlpecheur = 2;
		}
		if(xppecheur < 55000 && xppecheur >= 35000) {
			lvlpecheur = 3;
		}
		if(xppecheur < 80000 && xppecheur >= 55000) {
			lvlpecheur = 4;
		}
		if(xppecheur < 115000 && xppecheur >= 80000) {
			lvlpecheur = 5;
		}
		if(xppecheur < 150000 && xppecheur >= 115000) {
			lvlpecheur = 6;
		}
		if(xppecheur < 200000 && xppecheur >= 150000) {
			lvlpecheur = 7;
		}
		if(xppecheur >= 20000) {
			lvlpecheur = 8;
		}
		double y = player.getLocation().getY();
		double x = player.getLocation().getX();
		double z = player.getLocation().getZ();
		if(!(y < 63 || y > 71) && !(x < 392 || x > 399) && !(z < -152 || z > -123) ){
			int amt= 0;
			Random rand = new Random();
			int rdminttwo = rand.nextInt(100);
    		int rdmint = rand.nextInt(100);
    		if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
    		
    		if(rdmint >= 0 && rdmint <= 75) {
    			Item item = (Item) event.getCaught();
    			if(lvlpecheur == 1) {
    				if(rdminttwo <= 5) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 2) {
    				if(rdminttwo <= 10) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 3) {
    				if(rdminttwo <= 25) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 4) {
    				if(rdminttwo <= 40) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 5) {
    				if(rdminttwo <= 55) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 6) {
    				if(rdminttwo <= 70) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 7) {
    				if(rdminttwo <= 85) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 8) {
    				amt = amt+1;
    			}
    			Main.getInstance().data.addXpPecheur(player.getUniqueId(), 25);
        	    item.setItemStack(getItemstack(Material.RAW_FISH, (short) 0, 1+amt));
    			String hello;
    			if(amt >= 1) {
    				hello = "{\"text\":\"+1 Poisson (+1 bonus) (+25xp)\",\"color\":\"red\",\"bold\":\"true\"}";
    			}else {
    				hello = "{\"text\":\"+1 Poisson (+25xp)\",\"color\":\"red\",\"bold\":\"true\"}";
    			}
    			this.send(player, hello);
    		}
    		// PUTAIN DE BUG DE MERDE
    		if(rdmint >= 76 && rdmint <= 95) {
    			Item item = (Item) event.getCaught();
    			if(lvlpecheur == 1) {
    				if(rdminttwo <= 5) {
    					amt=amt+1;
    				}
    			}
    			if(lvlpecheur == 2) {
    				if(rdminttwo <= 10) {
    					amt=amt+1;
    				}
    			}
    			if(lvlpecheur == 3) {
    				if(rdminttwo <= 25) {
    					amt=amt+1;
    				}
    			}
    			if(lvlpecheur == 4) {
    				if(rdminttwo <= 40) {
    					amt=amt+1;
    				}
    			}
    			if(lvlpecheur == 5) {
    				if(rdminttwo <= 55) {
    					amt=amt+1;
    				}
    			}
    			if(lvlpecheur == 6) {
    				if(rdminttwo <= 70) {
    					amt=amt+1;
    				}
    			}
    			if(lvlpecheur == 7) {
    				if(rdminttwo <= 85) {
    					amt=amt+1;
    				}
    			}
    			if(lvlpecheur == 8) {
    				amt=amt+1;
    			}
    			//75 xp
    			Main.getInstance().data.addXpPecheur(player.getUniqueId(), 75);
        	    item.setItemStack(getItemstack(Material.RAW_FISH, (short) 2, 1+amt));
    			ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    			String hello;
    			if(amt >= 1) {
    				hello = "{\"text\":\"+1 Poisson Clown (+1 bonus) (+75xp)\",\"color\":\"gold\",\"bold\":\"true\"}";
    			}else {
    				hello = "{\"text\":\"+1 Poisson Clown (+75xp)\",\"color\":\"gold\",\"bold\":\"true\"}";
    			}
    			this.send(player, hello);
    		}
			if(rdmint >= 96 && rdmint <= 100) {
				Item item = (Item) event.getCaught();
    			if(lvlpecheur == 1) {
    				if(rdminttwo <= 5) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 2) {
    				if(rdminttwo <= 10) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 3) {
    				if(rdminttwo <= 25) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 4) {
    				if(rdminttwo <= 40) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 5) {
    				if(rdminttwo <= 55) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 6) {
    				if(rdminttwo <= 70) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 7) {
    				if(rdminttwo <= 85) {
    					amt = amt+1;
    				}
    			}
    			if(lvlpecheur == 8) {
    				amt = amt+1;
    			}
				Main.getInstance().data.addXpPecheur(player.getUniqueId(), 150);
				
				int rdmint2 = rand.nextInt(100);
				if(rdmint2 >= 66) {
					Main.getInstance().data.addPoints(player.getUniqueId(), 10);			
					player.sendMessage("§7(§c!§7) §7Vous avez gagné §c10 §6GcaCoins §7en pêchant!");
				}
        	    item.setItemStack(getItemstack(Material.RAW_FISH, (short) 3, 1+amt));
				ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
				String hello;
				if(amt>= 1) {
					hello = "{\"text\":\"+1 Poisson Globe (+1 bonus) (+150xp)\",\"color\":\"yellow\",\"bold\":\"true\"}";
				}else {
					hello = "{\"text\":\"+1 Poisson Globe (+150xp)\",\"color\":\"yellow\",\"bold\":\"true\"}";
				}
				this.send(player, hello);
				
			}

		
		}else {
			event.setCancelled(true);
		}
}
	public ItemStack getItemstack(Material material,Short bte, int count) {
		ItemStack it = new ItemStack(material, count, bte);
		
 
		return it;
	}
	
	public static void send(Player p, String msg) {
        IChatBaseComponent icbc = ChatSerializer.a(msg);
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte)2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(bar);
    }
	
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)  {
        // Physical means jump on it
        if (event.getAction() == Action.PHYSICAL) {
            Block block = event.getClickedBlock();
            int y = block.getY();
    		int x = block.getX();
    		int z = block.getZ();
            if (block == null) return;
            // If the block is farmland (soil)
            if (block.getType() == Material.SOIL) {
            	if(!(y < 66 || y > 70) && !(x < -93 || x > -55) && !(z < 250 || z > 289) ){
                // Deny event and set the block
                event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                event.setCancelled(true);
            }
            }
        }
    }
}

