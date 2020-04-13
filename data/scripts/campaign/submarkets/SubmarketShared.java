package data.scripts.campaign.submarkets;

public class SubmarketShared {
    static final boolean DEBUG = true; //false default
    static final float WEAPON_MULT = 2; // todo load from config
    static final float FIGHTER_MULT = 2; //todo load from config
    static final float SHIP_MULT = 2; //todo load from config //this not only means more ships but bigger, stronger ships too
    static final float HULLMODS_MULT = 2; //todo include from config and default to 1
    static final boolean MANY_PICK = false; //false default?
    static final int WEAPON_PICKS = 2;
    static final int FIGHTER_PICKS = 2;
    static final int SHIP_PICKS = 2;
    static final boolean EXTRA_TANKERS = true; //default to false

    //todo check on load for anything over say 1000 and throw warning, hard cap to 10k? especially for picks lol

    //todo utility to load this from a function
}