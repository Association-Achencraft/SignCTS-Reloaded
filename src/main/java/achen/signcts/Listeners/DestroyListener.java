package achen.signcts.Listeners;

import achen.signcts.DataSource;
import achen.signcts.MySign;
import achen.signcts.Signcts;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class DestroyListener implements Listener {

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();

        if(location.getBlock().getState() instanceof org.bukkit.block.Sign){

            for(MySign s : Signcts.signs)
            {
                if(s.x == location.getX() && s.y == location.getY() && s.z == location.getZ() && s.world == location.getWorld())
                {
                    String idsae = s.idsae;
                    Signcts.signs.remove(s);
                    Signcts.instance.saveSigns();

                    DataSource.remove(idsae);

                    event.getPlayer().sendMessage("[CTS] Panneau CTS supprimé !");
                    return;
                }
            }

        }

    }

}
