package fr.pierronus.gcaplugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mysql.fabric.xmlrpc.base.Array;

import fr.pierronus.gcaplugin.*;

public class SQLGetter {
	private Main plugin;
   private static final SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
   public SQLGetter(Main plugin) {
		this.plugin = plugin;
	}

   public void createTable() {
      try {
         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS joueurs (USERNAME VARCHAR(100),UUID VARCHAR(100),POINTS INT(100),RECH INT(10),DATE_REG VARCHAR(50),KILLS INT(11),MORTS INT(11),IP VARCHAR(255),XP_MINEUR INT(20),XP_BUCHERON INT(20),XP_PECHEUR INT(20),XP_FARMEUR INT(20),PRIMARY KEY (USERNAME))");
         ps.executeUpdate();
      } catch (SQLException var3) {
         var3.printStackTrace();
      }

   }

   public void createPlayer(Player player) {
      try {
         UUID uuid = player.getUniqueId();
         if (!exists(uuid)) {
            PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT OR IGNORE INTO joueurs (USERNAME,UUID,POINTS,RECH,DATE_REG,KILLS,MORTS,IP,XP_MINEUR,XP_BUCHERON,XP_PECHEUR,XP_FARMEUR) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            String ip = String.valueOf(player.getAddress().getHostString());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ps2.setString(1, player.getName());
            ps2.setString(2, uuid.toString());
            ps2.setInt(3, 0);
            ps2.setInt(4, 0);
            ps2.setString(5, sdf3.format(timestamp));
            ps2.setInt(6, 0);
            ps2.setInt(7, 0);
            ps2.setString(8, ip);
            ps2.setInt(9, 0);
            ps2.setInt(10, 0);
            ps2.setInt(11, 0);
            ps2.setInt(12, 0);
            ps2.executeUpdate();
            return;
         }else {
        	 return;
         }
      } catch (SQLException var6) {
         var6.printStackTrace();
      }
   }

   public boolean exists(UUID uuid) {
      try {
         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM joueurs WHERE UUID=?");
         ps.setString(1, uuid.toString());
         ResultSet results = ps.executeQuery();
         if(results.next()) {
				return true;
			}
		 return false;
      } catch (SQLException var4) {
         var4.printStackTrace();
  
      }
      return false;
   }
   
   public int getKills(UUID uuid) {
      try {
         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT KILLS FROM joueurs WHERE UUID=?");
         ps.setString(1, uuid.toString());
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            int kills = rs.getInt("KILLS");
            return kills;
         }
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

      return 0;
   }
   
   public int getPoints(UUID uuid) {
	      try {
	         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT POINTS FROM joueurs WHERE UUID=?");
	         ps.setString(1, uuid.toString());
	         ResultSet rs = ps.executeQuery();
	         if (rs.next()) {
	            int kills = rs.getInt("POINTS");
	            return kills;
	         }
	      } catch (SQLException var5) {
	         var5.printStackTrace();
	      }

	      return 0;
	   }

   public int getMorts(UUID uuid) {
      try {
         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT MORTS FROM joueurs WHERE UUID=?");
         ps.setString(1, uuid.toString());
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
            int morts = rs.getInt("MORTS");
            return morts;
         }
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

      return 0;
   }
   
   public int getRech(UUID uuid) {
	      try {
	         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT RECH FROM joueurs WHERE UUID=?");
	         ps.setString(1, uuid.toString());
	         ResultSet rs = ps.executeQuery();
	         if (rs.next()) {
	            int rech = rs.getInt("RECH");
	            return rech;
	         }
	      } catch (SQLException var5) {
	         var5.printStackTrace();
	      }

	      return 0;
	   }

   public void addKill(UUID uuid) {
      try {
         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET KILLS=? WHERE UUID=?");
         ps.setInt(1, this.getKills(uuid) + 1);
         ps.setString(2, uuid.toString());
         ps.executeUpdate();
      } catch (SQLException var3) {
         var3.printStackTrace();
      }

   }
   public void addPoints(UUID uuid, int points) {
	      try {
	         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET POINTS=? WHERE UUID=?");
	         ps.setInt(1, this.getPoints(uuid) + points);
	         ps.setString(2, uuid.toString());
	         ps.executeUpdate();
	      } catch (SQLException var3) {
	         var3.printStackTrace();
	      }

	   }
   public void addRech(UUID uuid) {
	      try {
	         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET RECH=? WHERE UUID=?");
	         ps.setInt(1, this.getRech(uuid) + 1);
	         ps.setString(2, uuid.toString());
	         ps.executeUpdate();
	      } catch (SQLException var3) {
	         var3.printStackTrace();
	      }

	   }
   public void delRech(UUID uuid) {
	      try {
	         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET RECH=? WHERE UUID=?");
	         ps.setInt(1, this.getRech(uuid) - 1);
	         ps.setString(2, uuid.toString());
	         ps.executeUpdate();
	      } catch (SQLException var3) {
	         var3.printStackTrace();
	      }

	   }
   public void resetRech(UUID uuid) {
	      try {
	         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET RECH=? WHERE UUID=?");
	         ps.setInt(1, 0);
	         ps.setString(2, uuid.toString());
	         ps.executeUpdate();
	      } catch (SQLException var3) {
	         var3.printStackTrace();
	      }

	   }
   
   public void delPoints(UUID uuid, int points) {
	      try {
	         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET POINTS=? WHERE UUID=?");
	         ps.setInt(1, this.getRech(uuid) - points);
	         ps.setString(2, uuid.toString());
	         ps.executeUpdate();
	      } catch (SQLException var3) {
	         var3.printStackTrace();
	      }

	   }

   public void addMorts(UUID uuid) {
      try {
         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET MORTS=? WHERE UUID=?");
         ps.setInt(1, this.getMorts(uuid) + 1);
         ps.setString(2, uuid.toString());
         ps.executeUpdate();
      } catch (SQLException var3) {
         var3.printStackTrace();
      }

   }

   public void setIp(UUID uuid) {
      try {
         Player player = Bukkit.getPlayer(uuid);
         String ip = String.valueOf(player.getAddress().getHostString());
         PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET IP=? WHERE UUID=?");
         ps.setString(1, ip);
         ps.setString(2, uuid.toString());
         ps.executeUpdate();
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

   }
   public int getXpBucheron(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT XP_BUCHERON FROM joueurs WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			int xp = 0;
			if(rs.next()) {
				xp = rs.getInt("XP_BUCHERON");
				return xp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	public int getXpMineur(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT XP_MINEUR FROM joueurs WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			int xp = 0;
			if(rs.next()) {
				xp = rs.getInt("XP_MINEUR");
				return xp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	public int getXpPecheur(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT XP_PECHEUR FROM joueurs WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			int xp = 0;
			if(rs.next()) {
				xp = rs.getInt("XP_PECHEUR");
				return xp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	public int getXpFarmeur(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT XP_FARMEUR FROM joueurs WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			int xp = 0;
			if(rs.next()) {
				xp = rs.getInt("XP_FARMEUR");
				return xp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	
	
	
	public void addXpPecheur(UUID uuid,int points) {
		try {
		PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET XP_PECHEUR=? WHERE UUID=?");
		ps.setInt(1, (getXpPecheur(uuid) + points));
		ps.setString(2, uuid.toString());
		ps.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addXpMineur(UUID uuid,int points) {
		try {
		PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET XP_MINEUR=? WHERE UUID=?");
		ps.setInt(1, (getXpMineur(uuid) + points));
		ps.setString(2, uuid.toString());
		ps.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addXpBucheron(UUID uuid,int points) {
		try {
		PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET XP_BUCHERON=? WHERE UUID=?");
		ps.setInt(1, (getXpBucheron(uuid) + points));
		ps.setString(2, uuid.toString());
		ps.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addXpFarmeur(UUID uuid,int points) {
		try {
		PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE joueurs SET XP_FARMEUR=? WHERE UUID=?");
		ps.setInt(1, (getXpFarmeur(uuid) + points));
		ps.setString(2, uuid.toString());
		ps.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getTenBiggest() {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement(("SELECT USERNAME,RECH FROM joueurs ORDER BY RECH DESC"));
			ArrayList<String> list = new ArrayList<String>();
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("USERNAME"));
				list.add(String.valueOf(rs.getInt("RECH")));
				}
			return list;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}