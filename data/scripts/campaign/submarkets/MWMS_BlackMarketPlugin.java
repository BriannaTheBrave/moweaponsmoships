package data.scripts.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.submarkets.BlackMarketPlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import com.fs.starfarer.campaign.econ.Submarket;
import data.scripts.campaign.submarkets.SubmarketShared;

public class MWMS_BlackMarketPlugin extends BlackMarketPlugin {
    @Override
    public void updateCargoPrePlayerInteraction() {
        if (okToUpdateShipsAndWeapons()) {
            super.updateCargoPrePlayerInteraction(); //must do this first or anything you add isnt real!
            // this was already done in super method, so what we're doing is increasing weapon/fighter counts

            float stability = market.getStabilityValue();

            WeightedRandomPicker<String> factionPicker = new WeightedRandomPicker<String>();
            factionPicker.add(market.getFactionId(), 15f - stability);
            factionPicker.add(Factions.INDEPENDENT, 4f);
            factionPicker.add(submarket.getFaction().getId(), 6f);

            int weapons = 4 + Math.max(0, market.getSize() - 3) + (Misc.isMilitary(market) ? 5 : 0);
            int fighters = 2 + Math.max(0, (market.getSize() - 3) / 2) + (Misc.isMilitary(market) ? 2 : 0);
            int hullmods = 1 + itemGenRandom.nextInt(3);

            if (SubmarketShared.WEAPON_MULT > 1) {
                weapons = Math.round((SubmarketShared.WEAPON_MULT - 1) * weapons);
            }

            if (SubmarketShared.FIGHTER_MULT > 1) {
                fighters = Math.round((SubmarketShared.FIGHTER_MULT - 1) * fighters);
            }

            //hullmods done all together because they are handled differently
            if (SubmarketShared.HULLMODS_MULT > 1) {
                hullmods = Math.round((SubmarketShared.HULLMODS_MULT - 1) * hullmods);
                if (SubmarketShared.DEBUG) { Global.getLogger(this.getClass()).info("Generic SubMARKET Hullmods: " + hullmods + " is this pancake?");}
                addHullMods(4, hullmods);
            }

            if (SubmarketShared.DEBUG) {
                Global.getLogger(this.getClass()).info("Black market WEAPONS: " + weapons + " is this pancake?");
                Global.getLogger(this.getClass()).info("Black market FIGHTERS: " + fighters + " is this pancake?");
            }

            if (SubmarketShared.MANY_PICK == false) {
                addWeapons(weapons, weapons + 2, 3, factionPicker);
                addFighters(fighters, fighters + 2, 3, factionPicker);
            } else {
                //loop through picks, knowing we pick once in super
                for (int x = 1; x < SubmarketShared.WEAPON_PICKS; x++){
                    if (SubmarketShared.DEBUG) {Global.getLogger(this.getClass()).info("flapjack weapon picks: " + x);}
                    addWeapons(weapons, weapons + 2, 3, factionPicker);
                }
                for (int x = 1; x < SubmarketShared.FIGHTER_PICKS; x++){
                    if (SubmarketShared.DEBUG) {Global.getLogger(this.getClass()).info("flapjack fighter picks: " + x);}
                    addFighters(fighters, fighters + 2, 3, factionPicker);
                }
            }
//todo add ships
            getCargo().sort();
        }
        else {
            super.updateCargoPrePlayerInteraction(); //this makes sure the normal commodities get worked out
        }
    }
}