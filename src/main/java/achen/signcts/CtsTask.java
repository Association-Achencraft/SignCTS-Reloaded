package achen.signcts;

import java.util.UUID;

enum Commande {
    CREATE,
    DELETE,
    MODIFY,
    INFO
}

enum Mode {
    SOLO,
    DOUBLE
}

public class CtsTask {


    public static UUID userID;
    public static String idsae;
    public static Mode mode;
    public static Commande commande;



}
