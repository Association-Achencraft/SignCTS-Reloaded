package achen.signcts;

import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Sign {

        public World world;
        //public BlockFace orientation;
        public double x;
        public double y;
        public double z;
        public String idsae;
        public Mode mode;


        public Sign(World world,double x,double y,double z, String idsae,Mode mode)
        {
                this.world = world;
                this.x = x;
                this.y = y;
                this.z = z;
                this.idsae = idsae;
                this.mode = mode;
        }


}
