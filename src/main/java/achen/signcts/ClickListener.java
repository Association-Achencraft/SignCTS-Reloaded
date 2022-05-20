package achen.signcts;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;


import java.util.ArrayList;
import java.util.List;

public class ClickListener implements Listener {


    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {

        Location location;

        if(event.getHand()!=EquipmentSlot.HAND)
            return;

        if(event.getAction()==Action.LEFT_CLICK_BLOCK || event.getAction()==Action.RIGHT_CLICK_BLOCK)
        {
            location = event.getClickedBlock().getLocation();


        if(Signcts.task.containsKey(event.getPlayer().getUniqueId())) {
            CtsTask task = Signcts.task.get(event.getPlayer().getUniqueId());

            switch (task.commande) {
                case CREATE:
                    click_create(location, task, event.getPlayer());
                    break;
                case DELETE:
                    //todo click_delete(location, task);
                    break;
                case MODIFY:
                    //todo click_modify(location, task);
                    break;
                case INFO:
                    //todo click_info(location, task);
                    break;
                default:
                    return;
            }

        }

        }

    }


    public void click_create(Location location, CtsTask task, Player player){

        if(location.getBlock().getState() instanceof org.bukkit.block.Sign){
            for(Sign s : Signcts.signs)
            {
                if(s.x == location.getX() && s.y == location.getY() && s.z == location.getZ() && s.world == location.getWorld())
                {
                    player.sendMessage("ERROR - This sign is already registered as a sign CTS for IDSAE "+s.idsae+". You can modify or remove it.");
                    return;
                }
            }

            Signcts.signs.add(new Sign(location.getWorld(), location.getX(), location.getY(), location.getZ(), task.idsae,task.mode));
            Signcts.instance.saveSigns();
            //todo ajouter l'IDSAE au REDIS

            player.sendMessage("Panneau CTS créé !");
            Signcts.task.remove(player.getUniqueId());

        }
        else {
            player.sendMessage("ERROR - Please select a sign");
            return;
        }



    }

}