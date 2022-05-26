package achen.signcts;

import achen.signcts.Enums.Commande;
import achen.signcts.Enums.Mode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.regex.Pattern;

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
                return cmd_create(args, Commande.CREATE);
            case "delete":
                return cmd_delete();
            case "modify":
                return cmd_create(args, Commande.MODIFY);
            case "info":
                return cmd_info();
            case "cancel":
                return cmd_cancel();
            case "update":
                return cmd_update();
            case "reload":
                return cmd_reload();
            default:
                return false;

            //todo: message personnalisé
        }
    }


    private boolean cmd_create(String[] args,Commande cmd) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[CTS] Seuls les joueurs peuvent utiliser cette commande !");
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


        final Pattern rx1 = Pattern.compile("[a-zA-Z][a-zA-Z][a-zA-Z][a-zA-Z][a-zA-Z]_\\d\\d", Pattern.CASE_INSENSITIVE);
        final Pattern rx2 = Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);
        final Pattern rx3 = Pattern.compile("[0-9]+[a-zA-Z]", Pattern.CASE_INSENSITIVE);
        if(!(rx1.matcher(idsae).matches() || rx2.matcher(idsae).matches() || rx3.matcher(idsae).matches()))
        {
            sender.sendMessage("[CTS] IDSAE au mauvais format!");
            return false;
        }
        task.idsae = idsae;
        task.userID = player.getUniqueId();
        task.commande = cmd;

        if(args.length==3){
            switch (args[2]){
                case "solo":
                case "SOLO":
                    task.mode = Mode.SOLO;
                    break;
                case "double":
                case "DOUBLE":
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
        player.sendMessage("[CTS] Cliquez sur un panneau à créer. Annulez avec /cts cancel");
        return true;
    }

    private boolean cmd_delete() {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[CTS] Seuls les joueurs peuvent utiliser cette commande !");
        }
        if(Signcts.task.containsKey(player.getUniqueId()))
        {
            Signcts.task.remove(player.getUniqueId());
        }
        CtsTask task = new CtsTask();
        task.userID = player.getUniqueId();
        task.commande = Commande.DELETE;
        Signcts.task.put(player.getUniqueId(),task);
        player.sendMessage("[CTS] Cliquez sur un panneau CTS à supprimer. Annulez avec /cts cancel");
        return true;
    }


    private boolean cmd_info() {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[CTS] Seuls les joueurs peuvent utiliser cette commande !");
        }
        if(Signcts.task.containsKey(player.getUniqueId()))
        {
            Signcts.task.remove(player.getUniqueId());
        }
        CtsTask task = new CtsTask();
        task.userID = player.getUniqueId();
        task.commande = Commande.INFO;
        Signcts.task.put(player.getUniqueId(),task);
        player.sendMessage("[CTS] Cliquez sur un panneau CTS. Annulez avec /cts cancel");
        return true;
    }

    private boolean cmd_cancel() {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[CTS] Seuls les joueurs peuvent utiliser cette commande !");
        }
        if(Signcts.task.containsKey(player.getUniqueId())) {
            Signcts.task.remove(player.getUniqueId());
        }
        sender.sendMessage("[CTS] Action abandonnée.");
        return true;
    }

    private boolean cmd_update() {

        //Signcts.instance.updateSigns();
        //todo
        return true;
    }

    private boolean cmd_reload() {

        Signcts.signs.clear();
        Signcts.instance.loadSigns();
        Signcts.instance.saveSigns();
        sender.sendMessage("[CTS] Reload effectué.");
        return true;
    }
}