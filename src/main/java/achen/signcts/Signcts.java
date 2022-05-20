package achen.signcts;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;


import java.util.*;

public final class Signcts extends JavaPlugin {

    private static Timer timer;
    public static Signcts instance;
    public static FileConfiguration config;
    public static List<Sign> signs = new ArrayList<>();
    public static HashMap<UUID,CtsTask> task = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        config = getConfig();
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + getName());
        timer = new Timer();

        getCommand("cts").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new ClickListener(), this);

        saveDefaultConfig();
        loadConfigs();

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

        int counter = 0;
        ConfigurationSection signsSection = config.getConfigurationSection("signs");

        if (signsSection != null) {
            for (String path : signsSection.getKeys(false)) {
                String world_name = signsSection.getString(path + ".world");
                double x = signsSection.getDouble(path + ".x");
                double y = signsSection.getDouble(path + ".y");
                double z = signsSection.getDouble(path + ".z");
                String idsae = signsSection.getString(path + ".idsae");
                Mode mode = Mode.valueOf(signsSection.getString(path + ".mode"));
                World world = Bukkit.getWorld(world_name);
                Block block = world.getBlockAt((int) x, (int) y, (int) z);
                //BlockFace orientation = block.getFace(block);

                if(block.getState() instanceof org.bukkit.block.Sign){
                    signs.add(new Sign(world,x,y,z,idsae,mode));
                }
                counter++;
            }
        }
        System.out.println(counter + " signs loaded.");
    }

    public void saveSigns(){

        config.set("signs", "");

        int counter = 0;
        for (Sign s : signs) {
            config.set("signs."+counter+".world",s.world.getName());
            config.set("signs."+counter+".x",s.x);
            config.set("signs."+counter+".y",s.y);
            config.set("signs."+counter+".z",s.z);
            config.set("signs."+counter+".idsae",s.idsae);
            config.set("signs."+counter+".mode",s.mode.name());
            counter++;
        }

        saveConfig();
    }
    public void updateSigns() {
        //todo
    }


}
