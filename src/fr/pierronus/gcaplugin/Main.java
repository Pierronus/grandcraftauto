package fr.pierronus.gcaplugin;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.shampaggon.crackshot.*;
import net.milkbowl.vault.economy.Economy;


public class Main extends JavaPlugin implements Listener{
	private static Main instance;

	
	public static Main getInstance() {
		return instance;
	}
	public static Economy econ;
	   public MySQL SQL;
	   public SQLGetter data;
	@Override
	public void onEnable() {
		
		this.SQL = new MySQL();
		this.data = new SQLGetter(this);

		      try {
		         this.SQL.connect();
		      } catch (SQLException | ClassNotFoundException var2) {
		         var2.printStackTrace();
		         Bukkit.getLogger().info("BDD non connectee");
		      }

		      if (this.SQL.isConnected()) {
		         Bukkit.getLogger().info("BDD connectee");
		         this.data.createTable();
		      }
		System.out.println("Le plugin GCA s'est bien activé");
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getServer().getPluginManager().registerEvents(new RegenBlockListener(), this);
		instance = this;
		
		
		String sArray[] = new String[] { "police","eeao","guide","points", "telephone","appart" ,"lbanqueezd", "gilet", "grade", "tuto", "adpkkdlslp", "ffkefeeksk","brs","fi","sdfsdfsdfre","sefzgztzytzafe","zfvzaefzefezaf","rezghetheth"};
		
		for(String command: sArray) {
			getCommand(command).setExecutor(new Commands());;
		}
		
		if(!setupEconomy()) {
			this.getLogger().severe("Y'a pas le plugin Vault");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
	}
	
	private boolean setupEconomy() {
		if(Bukkit.getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
		
	}
    
	
	@Override
	public void onDisable() {
		System.out.println("Le plugin GCA s'est bien désactivé");
		this.SQL.disconnect();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		data.createPlayer(player);
	}

}
