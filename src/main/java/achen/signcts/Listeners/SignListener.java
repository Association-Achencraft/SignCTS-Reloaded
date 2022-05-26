package achen.signcts.Listeners;

import achen.signcts.Enums.Mode;
import achen.signcts.MySign;
import achen.signcts.Signcts;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.regex.Pattern;


public class SignListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Location location = event.getBlock().getLocation();
        if(event.getLine(0).equalsIgnoreCase("[CTS]")){
            String idsae = event.getLine(1);
            Mode mode;
            //vérif regex idsae
            final Pattern rx1 = Pattern.compile("[a-zA-Z][a-zA-Z][a-zA-Z][a-zA-Z][a-zA-Z]_\\d\\d", Pattern.CASE_INSENSITIVE);
            final Pattern rx2 = Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);
            final Pattern rx3 = Pattern.compile("[0-9]+[a-zA-Z]", Pattern.CASE_INSENSITIVE);
            if(!(rx1.matcher(idsae).matches() || rx2.matcher(idsae).matches() || rx3.matcher(idsae).matches()))
            {
                event.getPlayer().sendMessage("[CTS] IDSAE au mauvais format!");
                return;
            }

            String sign_mode = event.getLine(2);
            switch (sign_mode){
                case "":
                    mode = Mode.SOLO;
                    break;
                case "SOLO":
                    mode = Mode.SOLO;
                    break;
                case "DOUBLE":
                    mode = Mode.DOUBLE;
                    break;
                default:
                    event.getPlayer().sendMessage("[CTS] Mode invalide.");
                    return;
            }

            Signcts.signs.add(new MySign(location.getWorld(), location.getX(), location.getY(), location.getZ(), idsae,mode));
            Signcts.instance.saveSigns();
            event.getPlayer().sendMessage("[CTS] Panneau CTS créé !");


        }
    }

}


