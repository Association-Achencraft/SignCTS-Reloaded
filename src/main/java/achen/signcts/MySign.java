package achen.signcts;

import achen.signcts.Enums.Mode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import org.bukkit.block.data.Rotatable;
import org.json.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class MySign {

        public World world;
        public double x;
        public double y;
        public double z;
        public String idsae;
        public Mode mode;

        private HashMap<Character,Integer> letters = new HashMap<>();


        public MySign(World world,double x,double y,double z, String idsae,Mode mode)
        {
                this.world = world;
                this.x = x;
                this.y = y;
                this.z = z;
                this.idsae = idsae;
                this.mode = mode;

                letters.put('i',1);
                letters.put('l',2);
                letters.put('k',4);
                letters.put('t',3);
                letters.put('f',4);
                letters.put('I',3);
                letters.put(' ',3);
        }

        public void actualise()
        {
                Block block = world.getBlockAt((int) x, (int) y, (int) z);
                if(block.getState() instanceof org.bukkit.block.Sign) {
                        Sign panneau1 = (Sign) block.getState();
                        panneau1.setLine(0,  ChatColor.translateAlternateColorCodes('&', Signcts.colors.get("waiting"))+"Temps d'attente");
                        panneau1.setLine(1, "");
                        panneau1.setLine(2, "");
                        panneau1.setLine(3, "");
                        String data = DataSource.get(this.idsae);


                        Timestamp ts_now = new Timestamp(System.currentTimeMillis());
                        ts_now.setTime(ts_now.getTime() + (1000L * 3600 * Integer.parseInt(Signcts.config.get("timestamp-adjust"))));
                        Integer cnt = 1;


                        if (mode == Mode.SOLO) {

                                if(data.equalsIgnoreCase("NO DATA") || data.equalsIgnoreCase("vide")) {
                                        panneau1.setLine(2, ChatColor.translateAlternateColorCodes('&', Signcts.colors.get("unavailable")) + "Indisponible");
                                        panneau1.update();
                                }else {

                                        JSONObject JsonData = new JSONObject(data);
                                        JSONArray passages = JsonData.getJSONArray("passages");

                                        for (int i = 1; i < passages.length()+1 && cnt < 4; i++) {
                                                String ligne = passages.getJSONObject(i - 1).getString("ligne");
                                                String textLine = "";
                                                textLine = "Ligne " + ChatColor.translateAlternateColorCodes('&', Signcts.colors.get(ligne)) + ligne;
                                                textLine = textLine + ChatColor.BLACK;
                                                switch (ligne.length()) {
                                                        case 1:
                                                                textLine = textLine + "  ";
                                                        case 2:
                                                                textLine = textLine + " ";
                                                        case 3:
                                                                break;
                                                }

                                                String vehicleTimestamp = passages.getJSONObject(i - 1).getString("heure");
                                                DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                                                LocalDateTime localDateTime = LocalDateTime.from(formatDateTime.parse(vehicleTimestamp));
                                                Timestamp ts = Timestamp.valueOf(localDateTime);

                                                //partie heure
                                                if (ts.getTime() > ts_now.getTime()) {
                                                        long diff = ts.getTime() - ts_now.getTime();
                                                        long diffMinutes = diff / (60 * 1000);
                                                        String attente = String.valueOf(diffMinutes);
                                                        switch (attente.length()) {
                                                                case 1:
                                                                        textLine = textLine + " ";
                                                                case 2:
                                                                        textLine = textLine + "  ";
                                                                case 3:
                                                                        textLine = textLine + " ";
                                                                        break;
                                                        }

                                                        textLine = textLine + attente + " min";

                                                        panneau1.setLine(cnt, textLine);
                                                        cnt++;
                                                }
                                        }
                                        panneau1.update();
                                }

                        } else {
                                Block second_panneau;
                                Block block_south = block.getRelative(BlockFace.SOUTH);
                                Block block_north = block.getRelative(BlockFace.NORTH);
                                Block block_east = block.getRelative(BlockFace.EAST);
                                Block block_west = block.getRelative(BlockFace.WEST);


                                if (block_south.getState() instanceof org.bukkit.block.Sign) {
                                        second_panneau = block_south;
                                } else {
                                        if (block_north.getState() instanceof org.bukkit.block.Sign) {
                                                second_panneau = block_north;
                                        } else {
                                                if (block_east.getState() instanceof org.bukkit.block.Sign) {
                                                        second_panneau = block_east;
                                                } else {
                                                        if (block_west.getState() instanceof org.bukkit.block.Sign) {
                                                                second_panneau = block_west;
                                                        } else {
                                                                return;
                                                        }
                                                }
                                        }

                                }


                                if (second_panneau.getState() instanceof org.bukkit.block.Sign) {
                                        Sign panneau2 = (Sign) second_panneau.getState();
                                        String textLine1 = "";
                                        String textLine2 = "";
                                        String heure = new SimpleDateFormat("HH:mm").format(ts_now);
                                        panneau2.setLine(0, "                "+ChatColor.translateAlternateColorCodes('&', Signcts.colors.get("waiting"))+heure);
                                        panneau2.setLine(1, "");
                                        panneau2.setLine(2, "");
                                        panneau2.setLine(3, "");

                                        if(data.equalsIgnoreCase("NO DATA") || data.equalsIgnoreCase("vide")) {
                                                panneau1.setLine(2, ChatColor.translateAlternateColorCodes('&', Signcts.colors.get("unavailable")) + "Indisponible");
                                                panneau1.update();
                                                panneau2.update();
                                        }else {

                                                JSONObject JsonData = new JSONObject(data);
                                                JSONArray passages = JsonData.getJSONArray("passages");

                                                for (int i = 1; i < passages.length()+1 && cnt < 4; i++) {
                                                        String ligne = passages.getJSONObject(i - 1).getString("ligne");

                                                        if (ligne.length() == 1) {
                                                                textLine1 = " ";
                                                        }
                                                        textLine1 = ChatColor.translateAlternateColorCodes('&', Signcts.colors.get(ligne)) + ligne;
                                                        switch (ligne.length()) {
                                                                case 1:
                                                                        textLine1 = textLine1 + " ";
                                                                case 2:
                                                                        textLine1 = textLine1 + " ";
                                                                case 3:
                                                                        textLine1 = textLine1 + " ";
                                                                        break;
                                                        }
                                                        textLine1 = textLine1 + ChatColor.BLACK;

                                                        String dest = passages.getJSONObject(i - 1).getString("destination");
                                                        int pixel_lgr = 0;
                                                        int lgr = 0;
                                                        int lgr_mot = 0;
                                                        String out = "";
                                                        char[] destab = dest.toCharArray();
                                                        while (pixel_lgr < 65) {
                                                                if (lgr < dest.length()){
                                                                        if (letters.containsKey(destab[lgr])) {
                                                                                pixel_lgr = pixel_lgr + letters.get(destab[lgr]);
                                                                        } else {
                                                                                pixel_lgr += 5;
                                                                        }
                                                                        out = out + destab[lgr];
                                                                        lgr_mot++;
                                                                } else {
                                                                        out = out + " ";
                                                                        pixel_lgr += 3;
                                                                }
                                                                pixel_lgr++;
                                                                lgr++;

                                                        }


                                                        textLine1 = textLine1 + out;


                                                        String dest2 = dest.substring(lgr_mot);


                                                        //panneau 2
                                                        pixel_lgr = 0;
                                                        lgr_mot = 0;
                                                        lgr = 0;
                                                        out = "";
                                                        char[] dest2tab = dest2.toCharArray();
                                                        while (pixel_lgr < 46) {
                                                                if (lgr < dest2.length()) {
                                                                        if (letters.containsKey(dest2tab[lgr])) {
                                                                                pixel_lgr = pixel_lgr + letters.get(dest2tab[lgr]);
                                                                        } else {
                                                                                pixel_lgr += 5;
                                                                        }
                                                                        out = out + dest2tab[lgr];
                                                                        lgr_mot++;
                                                                } else {
                                                                        out = out + " ";
                                                                        pixel_lgr += 3;
                                                                }
                                                                pixel_lgr++;
                                                                lgr++;
                                                        }

                                                        if(pixel_lgr >= 46 && lgr_mot == out.length())
                                                        {
                                                                out = out.substring(0,out.length()-2) + ".  ";
                                                        }


                                                        textLine2 = out;



                                                        String vehicleTimestamp = passages.getJSONObject(i - 1).getString("heure");
                                                        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                                                        LocalDateTime localDateTime = LocalDateTime.from(formatDateTime.parse(vehicleTimestamp));
                                                        Timestamp ts = Timestamp.valueOf(localDateTime);

                                                        if (ts.getTime() > ts_now.getTime()) {
                                                                long diff = ts.getTime() - ts_now.getTime();
                                                                long diffMinutes = diff / (60 * 1000);
                                                                String attente = String.valueOf(diffMinutes);
                                                                switch (attente.length()) {
                                                                        case 1:
                                                                                textLine2 = textLine2 + " ";
                                                                        case 2:
                                                                                textLine2 = textLine2 + "  ";
                                                                        case 3:
                                                                                textLine2 = textLine2 + " ";
                                                                                break;
                                                                }

                                                                textLine2 = textLine2 + attente + " min";

                                                                panneau1.setLine(cnt, textLine1);
                                                                panneau2.setLine(cnt, textLine2);
                                                                cnt++;

                                                        }
                                                }
                                                panneau1.update();
                                                panneau2.update();
                                        }
                                }
                        }
                        if (cnt == 1) {
                                panneau1.setLine(2, ChatColor.translateAlternateColorCodes('&', Signcts.colors.get("unavailable")) + "Indisponible");
                                panneau1.update();
                        }


                }
        }
}
