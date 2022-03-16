package tk.yjservers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class DaytimeSleep extends JavaPlugin{
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BedEvent(), this);
    }

    @Override
    public void onDisable() {

    }

    public boolean isBed(Block b) {
        return b.getType().equals(Material.BED_BLOCK);
    }

    public boolean anyHostileNearby(Player p) {
        List<Entity> list = p.getNearbyEntities(4, 2.5, 4);
        ArrayList<EntityType> typelist = new ArrayList<>();
        for (Entity e : list) {
            typelist.add(e.getType());
            getLogger().info("Entities nearby " + p.getName() + ": " + e.getType().getName());
        }
        EntityType[] hostilelist = {EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.PIG_ZOMBIE, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SPIDER, EntityType.WITCH, EntityType.WITHER};
        return !Collections.disjoint(typelist, Arrays.asList(hostilelist));
    }

    // i know there is loc.add and loc.subtract, but they all feel wonky af
    public Location changeLoc(Location target, int x, int y, int z){
        return new Location(target.getWorld(), target.getBlockX() + x, target.getBlockY() + y, target.getBlockZ() + z);
    }
}
