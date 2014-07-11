package Bammerbom.UltimateCore.API;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import Bammerbom.UltimateCore.UltimateConfiguration;
import Bammerbom.UltimateCore.UltimateFileLoader;

public class UCserver {
	public UCserver(){
	}
	public Location getCustomSpawn(){
		UltimateConfiguration data = new UltimateConfiguration(UltimateFileLoader.DFspawns);
	    if(data.get("spawn") != null){
		 String[] loc = data.getString("spawn").split(",");
	        World w = Bukkit.getWorld(loc[0]);
	        Double x = Double.parseDouble(loc[1]);
	        Double y = Double.parseDouble(loc[2]);
	        Double z = Double.parseDouble(loc[3]);
	        float yaw = Float.parseFloat(loc[4]);
	        float pitch = Float.parseFloat(loc[5]);
	        Location location = new Location(w, x, y, z, yaw, pitch);
	        return location;
	    }else{
	    	return null;
	    }
	    
	}
}
