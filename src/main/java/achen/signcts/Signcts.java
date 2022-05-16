package achen.signcts;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

import java.util.Timer;
import java.util.TimerTask;

public final class Signcts extends JavaPlugin {

    private static Timer timer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());
        timer = new Timer();

        this.getCommand("cts").setExecutor(new Commands());

        loadConfigs();
        connect();


        //Boucle toutes les 30 secondes

        timer.schedule(new TimerTask() {
            public void run() {
                updateSigns();
            }
        }, 0, 30000);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());
        timer.cancel();
    }


    public void loadConfigs() {

    }

    public void connect() {

    }

    public void updateSigns() {

    }

}
