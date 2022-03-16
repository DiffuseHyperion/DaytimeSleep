package tk.yjservers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Bed;

import java.util.ArrayList;

public class BedEvent implements Listener {

    DaytimeSleep DaytimeSleep;

    public BedEvent() {
        DaytimeSleep = new DaytimeSleep();
    }

    @EventHandler
    public void onPlayerInteractBed(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();
        Block b = e.getClickedBlock();
        if (a.equals(Action.RIGHT_CLICK_BLOCK)
                && DaytimeSleep.isBed(b)
                && b.getLocation().distanceSquared(p.getLocation()) < 3
                && b.getWorld().getEnvironment().equals(World.Environment.NORMAL)
                && !DaytimeSleep.anyHostileNearby(p)) {

            Bed bed = (Bed) b.getState().getData();
            Location headloc;
            Location tailloc;

            if (bed.isHeadOfBed()) {
                headloc = b.getLocation();
                tailloc = b.getRelative(bed.getFacing().getOppositeFace()).getLocation();
            } else {
                tailloc = b.getLocation();
                headloc = b.getRelative(bed.getFacing()).getLocation();
            }


            // number represents the order of spawning
            ArrayList<Location> locs = new ArrayList<>();
            // adjacent to headloc (0-6)
            locs.add(DaytimeSleep.changeLoc(headloc, 1, 0, -1));
            locs.add(DaytimeSleep.changeLoc(headloc, -1, 0, 0));
            locs.add(DaytimeSleep.changeLoc(headloc, -1, 0, 1));
            locs.add(DaytimeSleep.changeLoc(headloc, 0, 0, -1));
            locs.add(DaytimeSleep.changeLoc(headloc, 1, 0, -1));
            locs.add(DaytimeSleep.changeLoc(headloc, 1, 0, 0));
            locs.add(DaytimeSleep.changeLoc(headloc, 1, 0, 1));

            // adjacent to feetloc (7-9)
            locs.add(DaytimeSleep.changeLoc(tailloc, -1, 0, 1));
            locs.add(DaytimeSleep.changeLoc(tailloc, 0, 0, 1));
            locs.add(DaytimeSleep.changeLoc(tailloc, 1, 0, 1));

            // on bed (10-11)
            locs.add(DaytimeSleep.changeLoc(headloc, 0, 1, 0));
            locs.add(DaytimeSleep.changeLoc(tailloc, 0, 1, 0));

            for (Location loc : locs) {
                Material below = DaytimeSleep.changeLoc(loc, 0, -1, 0).getBlock().getType();
                Material above = DaytimeSleep.changeLoc(loc, 0, 1, 0).getBlock().getType();
                Material origin = loc.getBlock().getType();
                if (locs.indexOf(loc) < 10) {
                    if (below.isSolid() && origin.equals(Material.AIR) && above.equals(Material.AIR)) {
                        p.setBedSpawnLocation(loc);
                        p.sendMessage("Your spawn has been set to " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                        return;
                    }
                } else if (locs.indexOf(loc) == 10){
                    if ((below.isSolid() ||  below.equals(Material.BED_BLOCK)) && origin.equals(Material.AIR) && above.equals(Material.AIR)) {
                        p.setBedSpawnLocation(loc);
                        p.sendMessage("Your spawn has been set to " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                        return;
                    }
                } else {
                    // if all else fails, default to loc 11
                    p.setBedSpawnLocation(loc);
                    p.sendMessage("Your spawn has been set to " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                }
            }
        }
    }
}
