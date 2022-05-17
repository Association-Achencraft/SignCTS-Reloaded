package achen.signcts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Timer;

public class Commands implements CommandExecutor {

    private static CommandSender sender;
    private static Player player;

    @Override
    public boolean onCommand(CommandSender cmd_sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            return false;
        }

        sender = cmd_sender;
        if (sender instanceof Player) {
            player = (Player)sender;
        }

        switch (args[0]) {
            case "create":
                return cmd_create(args);
            case "delete":
                return cmd_delete();
            case "modify":
                return cmd_modify();
            case "info":
                return cmd_info();
            case "update":
                return cmd_update();
            case "reload":
                return cmd_reload();
            default:
                return false;

            //todo: message personnalisé
        }
    }


    private boolean cmd_create(String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande !");
            return false;
        }

        if(args.length < 2 || args.length > 3)
            return false;

        if(Signcts.task.containsKey(player.getUniqueId()))
        {
            Signcts.task.remove(player.getUniqueId());
        }

        CtsTask task = new CtsTask();
        String idsae = args[1];


        //vérif regex idsae
        final Pattern rx1 = Pattern.compile("[a-zA-Z][a-zA-Z][a-zA-Z][a-zA-Z][a-zA-Z]_\\d\\d", Pattern.CASE_INSENSITIVE);
        final Pattern rx2 = Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);
        final Pattern rx3 = Pattern.compile("[0-9]+[a-zA-Z]", Pattern.CASE_INSENSITIVE);
        if(!(rx1.matcher(idsae).matches() || rx2.matcher(idsae).matches() || rx3.matcher(idsae).matches()))
        {
            sender.sendMessage("IDSAE au mauvais format!");
            return false;
        }
        task.idsae = idsae;
        task.userID = player.getUniqueId();
        task.commande = Commande.CREATE;

        if(args.length==3){
            switch (args[2]){
                case "solo":
                    task.mode = Mode.SOLO;
                    break;
                case "double":
                    task.mode = Mode.DOUBLE;
                    break;
                default:
                    return false;
            }
        }
        else
        {
            task.mode = Mode.SOLO;
        }

        Signcts.task.put(player.getUniqueId(),task);
        return true;
    }

    private boolean cmd_delete() {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande !");
        }
        //todo
        return true;
    }

    private boolean cmd_modify() {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande !");
        }
        //todo
        return true;
    }

    private boolean cmd_info() {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seuls les joueurs peuvent utiliser cette commande !");
        }
        //todo
        return true;
    }

    private boolean cmd_update() {
        //todo
        return true;
    }

    private boolean cmd_reload() {
        //todo
        return true;
    }

    private boolean createSoloSign(String idsae) {
        //todo attendre clic sur panneau et ajouter au yml
        return false;
    }

    private boolean createDoubleSign(String idsae) {
        //todo attendre clic sur panneau et ajouter au yml
        return false;
    }

}