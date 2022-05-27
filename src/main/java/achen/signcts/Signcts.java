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
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.C;


import java.util.*;

public final class Signcts extends JavaPlugin {

    private static Timer timer;
    public static Signcts instance;
    public static List<MySign> signs = new ArrayList<>();
    public static HashMap<String,String> colors = new HashMap<>();
    public static HashMap<UUID,CtsTask> task = new HashMap<>();
    public static HashMap<String,String> config = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + getName());
        timer = new Timer();
        saveDefaultConfig();

        getCommand("cts").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getServer().getPluginManager().registerEvents(new DestroyListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);

        loadConfigs();
        DataSource.connect();
        loadSigns();
        loadColors();



        new BukkitRunnable() {
            @Override
            public void run() {
                for (MySign s : signs) {
                    s.actualise();
                }
            }
        }.runTaskTimer(instance,100, Integer.parseInt(config.get("update-interval"))*20);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DataSource.disconnect();
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());
        timer.cancel();
    }


    public void loadSigns() {


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
                        DataSource.add(idsae);

                        Sign s = (Sign) block.getState();
                        s.setLine(0, "[CTS]");
                        s.setLine(1, idsae);
                        s.setLine(2, mode.toString());
                        s.setLine(3, "");
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

    public void loadColors() {
        int counter = 0;
        ConfigurationSection colorSection = getConfig().getConfigurationSection("colors");

        if (colorSection != null) {

            colors.put("time",colorSection.getString("time"));
            colors.put("waiting",colorSection.getString("waiting"));
            colors.put("unavailable",colorSection.getString("unavailable"));

            ConfigurationSection linesSection = getConfig().getConfigurationSection("colors.lignes");
            if (linesSection != null) {
                for (String ligne : linesSection.getKeys(false)) {
                    colors.put(ligne,linesSection.getString(ligne));
                    counter++;
                }
            }
        }
        getLogger().info(counter + " couleurs chargées.");
    }

    public void loadConfigs() {
        ConfigurationSection configSection = getConfig().getConfigurationSection("variables");
        config.put("redis-server",configSection.getString("redis-server"));
        config.put("redis-port",configSection.getString("redis-port"));
        config.put("redis-password",configSection.getString("redis-password"));
        config.put("update-interval",configSection.getString("update-interval"));
        config.put("timestamp-adjust",configSection.getString("timestamp-adjust"));
    }



}
