package data.scripts.campaign.submarkets;

import com.fs.starfarer.api.Global;

public class SubmarketShared {
    //If we have Nexerelin do its logic for commissions
    public static boolean HAVE_NEXERELIN = Global.getSettings().getModManager().isModEnabled("nexerelin");;

    static final boolean DEBUG = Global.getSettings().getBoolean("mwms_debug");
    static final float WEAPON_MULT = Global.getSettings().getFloat("mwms_weapon_mult");
    static final float FIGHTER_MULT = Global.getSettings().getFloat("mwms_fighter_mult");
    static final float SHIP_MULT = Global.getSettings().getFloat("mwms_ship_mult");
    static final float HULLMODS_MULT = Global.getSettings().getFloat("mwms_hullmods_mult");
    static final boolean MANY_PICK = Global.getSettings().getBoolean("mwms_many_pick");
    static final int WEAPON_PICKS = Global.getSettings().getInt("mwms_weapon_picks");
    static final int FIGHTER_PICKS = Global.getSettings().getInt("mwms_fighter_picks");
    static final int SHIP_PICKS = Global.getSettings().getInt("mwms_ship_picks");
    static final boolean EXTRA_TANKERS = Global.getSettings().getBoolean("mwms_extra_tankers");
}