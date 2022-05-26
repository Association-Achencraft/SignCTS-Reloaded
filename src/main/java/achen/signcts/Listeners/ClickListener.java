package achen.signcts.Listeners;

import achen.signcts.CtsTask;
import achen.signcts.DataSource;
import achen.signcts.MySign;
import achen.signcts.Signcts;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ClickListener implements Listener {


    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {

        Location location;
        if(event.getHand()!=EquipmentSlot.HAND)
            return;
        if(event.getAction()==Action.LEFT_CLICK_BLOCK || event.getAction()==Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                location = event.getClickedBlock().getLocation();
                if (Signcts.task.containsKey(event.getPlayer().getUniqueId())) {
                    CtsTask task = Signcts.task.get(event.getPlayer().getUniqueId());
                    switch (task.commande) {
                        case CREATE:
                            click_create(location, task, event.getPlayer());
                            break;
                        case DELETE:
                            click_delete(location, event.getPlayer());
                            break;
                        case MODIFY:
                            click_modify(location, task, event.getPlayer());
                            break;
                        case INFO:
                            click_info(location, event.getPlayer());
                            break;
                        default:
                    }
                }
            }
        }
    }


    public void click_create(Location location, CtsTask task, Player player){

        if(location.getBlock().getState() instanceof org.bukkit.block.Sign){
            for(MySign s : Signcts.signs)
            {
                if(s.x == location.getX() && s.y == location.getY() && s.z == location.getZ() && s.world == location.getWorld())
                {
                    player.sendMessage("[CTS] ERROR - This sign is already registered as a sign CTS for IDSAE "+s.idsae+". You can modify or remove it.");
                    return;
                }
            }

            Signcts.signs.add(new MySign(location.getWorld(), location.getX(), location.getY(), location.getZ(), task.idsae,task.mode));
            Signcts.instance.saveSigns();
            DataSource.add(task.idsae);
            Sign panneau = (Sign)location.getBlock().getState();
            panneau.setLine(0,"[CTS]");
            panneau.setLine(1,task.idsae);
            panneau.setLine(2,task.mode.toString());
            panneau.update();

            player.sendMessage("[CTS] Panneau CTS créé !");
            Signcts.task.remove(player.getUniqueId());
        }
        else {
            player.sendMessage("[CTS] ERROR - Please select a sign");
        }
    }


    public void click_modify(Location location, CtsTask task, Player player){

        if(location.getBlock().getState() instanceof org.bukkit.block.Sign){

            String old_idsae = "";
            boolean found = false;
            for(MySign s : Signcts.signs)
            {
                if(s.x == location.getX() && s.y == location.getY() && s.z == location.getZ() && s.world == location.getWorld())
                {
                    found = true;
                    old_idsae = s.idsae;
                    s.idsae = task.idsae;
                    s.mode = task.mode;
                }
            }

            if(!found)
            {
                player.sendMessage("[CTS] Ce panneau n'est pas un panneau CTS.");
                return;
            }

            Signcts.instance.saveSigns();
            DataSource.remove(old_idsae);
            DataSource.add(task.idsae);
            Sign panneau = (Sign)location.getBlock().getState();
            panneau.setLine(0,"[CTS]");
            panneau.setLine(1,task.idsae);
            panneau.setLine(2,task.mode.toString());
            panneau.update();

            player.sendMessage("[CTS] Panneau CTS modifié !");
            Signcts.task.remove(player.getUniqueId());

        }
        else {
            player.sendMessage("[CTS] ERROR - Please select a CTS sign");
        }
    }

    public void click_delete(Location location, Player player){

        if(location.getBlock().getState() instanceof org.bukkit.block.Sign){

            for(MySign s : Signcts.signs)
            {
                if(s.x == location.getX() && s.y == location.getY() && s.z == location.getZ() && s.world == location.getWorld())
                {
                    String idsae = s.idsae;
                    Signcts.signs.remove(s);
                    Signcts.instance.saveSigns();
                    DataSource.remove(idsae);

                    Sign panneau = (Sign)location.getBlock().getState();
                    panneau.setLine(0,"[CTS]");
                    panneau.setLine(1,"supprimé");
                    panneau.setLine(2,"");
                    panneau.setLine(3,"");
                    panneau.update();

                    player.sendMessage("[CTS] Panneau CTS supprimé !");
                    Signcts.task.remove(player.getUniqueId());
                    return;
                }
            }
            player.sendMessage("[CTS] Ce panneau n'est pas un panneau CTS.");
        }
        else {
            player.sendMessage("[CTS] ERROR - Please select a CTS sign");
        }
    }

    public void click_info(Location location, Player player){

        if(location.getBlock().getState() instanceof org.bukkit.block.Sign){

            boolean found = false;
            for(MySign s : Signcts.signs)
            {
                if(s.x == location.getX() && s.y == location.getY() && s.z == location.getZ() && s.world == location.getWorld())
                {
                    found = true;
                    player.sendMessage("[CTS] - IDSAE: "+s.idsae+",MODE:"+s.mode);
                }
            }
            if(!found)
            {
                player.sendMessage("[CTS] Ce panneau n'est pas un panneau CTS.");
                return;
            }

            Signcts.task.remove(player.getUniqueId());

        }
        else {
            player.sendMessage("[CTS] ERROR - Merci de choisir un panneau CTS");
        }
    }
}