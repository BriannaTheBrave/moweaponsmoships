package data.scripts.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.submarkets.OpenMarketPlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.econ.Submarket;
import data.scripts.campaign.submarkets.SubmarketShared;

public class MWMS_OpenMarketPlugin extends OpenMarketPlugin {
    @Override
    public void updateCargoPrePlayerInteraction() {
        if (okToUpdateShipsAndWeapons()) {
            super.updateCargoPrePlayerInteraction(); //must do this first or anything you add isnt real!
            // this was already done in super method, so what we're doing is increasing weapon/fighter counts

            int weapons = 2 + Math.max(0, market.getSize() - 3) + (Misc.isMilitary(market) ? 5 : 0);
            int fighters = 1 + Math.max(0, (market.getSize() - 3) / 2) + (Misc.isMilitary(market) ? 2 : 0);

            if (SubmarketShared.WEAPON_MULT > 1) {
                weapons = Math.round((SubmarketShared.WEAPON_MULT - 1) * weapons);
            }

            if (SubmarketShared.FIGHTER_MULT > 1) {
                fighters = Math.round((SubmarketShared.FIGHTER_MULT - 1) * fighters);
            }

            addWeapons(weapons, weapons + 2, 0, market.getFactionId());
            addFighters(fighters, fighters + 2, 0, market.getFactionId());

            Global.getLogger(this.getClass()).info("Generic SubMARKET WEAPONS: " + weapons + " is this pancake?");
            Global.getLogger(this.getClass()).info("Generic SubMARKET FIGHTERS: " + fighters + " is this pancake?");
//            for (int x = 1; x < submarketShared.getMinWepMult(); x++){
//                Global.getLogger(this.getClass()).info("flapjack " + x);
//                addWeapons(weapons, weapons, 1, market.getFactionId());
//                addFighters(fighters, fighters, 1, market.getFactionId());
//            }
            getCargo().sort();
        }
        else {
            super.updateCargoPrePlayerInteraction(); //this makes sure the normal commodities get worked out
        }
    }
}