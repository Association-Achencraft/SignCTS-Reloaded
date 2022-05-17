package achen.signcts;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;


import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public final class Signcts extends JavaPlugin {

    private static Timer timer;
    public static HashMap<UUID,CtsTask> task = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + getName());
        timer = new Timer();

        getCommand("cts").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new ClickListener(), this);

        loadConfigs();
        DataSource.connect();


        //Boucle toutes les 30 secondes

        timer.schedule(new TimerTask() {
            public void run() {
                updateSigns();
            }
        }, 0, 30000);
        //todo : fréquence réglable dans la config
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());
        timer.cancel();
    }


    public void loadConfigs() {
        //todo
    }

    public void updateSigns() {
        //todo
    }

}
