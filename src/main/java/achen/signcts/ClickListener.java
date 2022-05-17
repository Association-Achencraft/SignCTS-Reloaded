package achen.signcts;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ClickListener implements Listener {

    private static Location location;

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {

        if(event.getHand()!=EquipmentSlot.HAND)
            return;

        if(event.getAction()==Action.LEFT_CLICK_BLOCK || event.getAction()==Action.RIGHT_CLICK_BLOCK)
        {
            location = event.getClickedBlock().getLocation();
        }

        if(Signcts.task.containsKey(event.getPlayer().getUniqueId())) {
            CtsTask val = Signcts.task.get(event.getPlayer().getUniqueId());
            event.getPlayer().sendMessage(val.idsae);
            Signcts.task.remove(event.getPlayer().getUniqueId());
        }

    }

    public Location getLastBlockLocation() {
        return location;
    }


}