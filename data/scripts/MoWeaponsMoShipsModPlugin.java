package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;

public class MoWeaponsMoShipsModPlugin extends BaseModPlugin
{
    private static void initMoWeaponsMoShips() {
        Global.getLogger(MoWeaponsMoShipsModPlugin.class).info("Hooray MoWeaponsMoShips mod plugin in a jar is loaded!");
        //todo
        //If we have Nexerelin and random worlds enabled, don't spawn our manual systems
        //boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
//        if (!haveNexerelin || SectorManager.getManager().isCorvusMode()){
//            initRedLegion();
//            //new tahlan_Rubicon().generate(sector); //logic from tahlan
//        }

//      todo perform check van vs nex
//        if (!haveNexerelin) {
//            //Legio Infernalis relations
//            RedLegionFactionRelations.initFactionRelationships(sector);
//        }
    }
}