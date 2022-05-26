package achen.signcts;

import achen.signcts.Enums.Mode;
import org.bukkit.World;

public class MySign {

        public World world;
        //public BlockFace orientation;
        public double x;
        public double y;
        public double z;
        public String idsae;
        public Mode mode;


        public MySign(World world,double x,double y,double z, String idsae,Mode mode)
        {
                this.world = world;
                this.x = x;
                this.y = y;
                this.z = z;
                this.idsae = idsae;
                this.mode = mode;
        }


}
