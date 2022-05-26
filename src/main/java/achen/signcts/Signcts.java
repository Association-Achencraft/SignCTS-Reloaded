package achen.signcts;

import achen.signcts.Enums.Mode;
import achen.signcts.Listeners.ClickListener;
import achen.signcts.Listeners.DestroyListener;
import achen.signcts.Listeners.SignListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;


import java.util.*;

public final class Signcts extends JavaPlugin {

    private static Timer timer;
    public static Signcts instance;
    public static List<MySign> signs = new ArrayList<>();
    public static HashMap<UUID,CtsTask> task = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + getName());
        timer = new Timer();

        getCommand("cts").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getServer().getPluginManager().registerEvents(new DestroyListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);

        loadConfigs();

        DataSource.connect();
        DataSource.add("TEST");

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
        ConfigurationSection signsSection = getConfig().getConfigurationSection("signs");

        if (signsSection != null) {
            for (String path : signsSection.getKeys(false)) {
                boolean duplicate = false;

                String world_name = signsSection.getString(path + ".world");
                double x = signsSection.getDouble(path + ".x");
                double y = signsSection.getDouble(path + ".y");
                double z = signsSection.getDouble(path + ".z");
                String idsae = signsSection.getString(path + ".idsae");
                Mode mode = Mode.valueOf(signsSection.getString(path + ".mode"));
                World world = Bukkit.getWorld(world_name);
                Block block = world.getBlockAt((int) x, (int) y, (int) z);

                if(world_name.isEmpty() || idsae.isEmpty())
                {
                    getLogger().warning("Erreur de configuration");
                    duplicate = true;
                }

                if(block.getState() instanceof org.bukkit.block.Sign){


                    for(MySign s : Signcts.signs)
                    {
                        if(s.x == x && s.y == y && s.z == z && s.world == world)
                        {
                            getLogger().warning("Entrée en double pour le panneau aux coordonnées "+x+","+y+","+z+".");
                            duplicate = true;
                        }
                    }



                    if(!duplicate) {
                        signs.add(new MySign(world, x, y, z, idsae, mode));

                        Sign s = (Sign) block.getState();
                        s.setLine(0, "[CTS]");
                        s.setLine(1, idsae);
                        s.setLine(2, mode.toString());
                        s.update();
                        counter++;
                    }
                }
            }
        }
        getLogger().info(counter + " panneaux CTS chargés.");

    }

    public void saveSigns(){

        FileConfiguration config = getConfig();
        config.set("signs", "");


        int counter = 0;
        for (MySign s : signs) {
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
