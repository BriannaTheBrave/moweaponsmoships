package data.scripts.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
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
            int hullmods = 1 + itemGenRandom.nextInt(3);
            float ships = SubmarketShared.SHIP_MULT - 1;

            if (SubmarketShared.WEAPON_MULT > 1) {
                weapons = Math.round((SubmarketShared.WEAPON_MULT - 1) * weapons);
            } else {
                if (SubmarketShared.DEBUG) { Global.getLogger(this.getClass()).info("MWMS cannot have Weapon Multiplier < 1 so defaults to 0 (chance for small number extra picks)");}
                weapons = 0;
            }

            if (SubmarketShared.FIGHTER_MULT > 1) {
                fighters = Math.round((SubmarketShared.FIGHTER_MULT - 1) * fighters);
            } else {
                if (SubmarketShared.DEBUG) { Global.getLogger(this.getClass()).info("MWMS cannot have Fighter Multiplier < 1 so defaults to 0 (chance for small number extra picks)");}
                fighters = 0;
            }

            //hullmods done all together because they are handled differently
            if (SubmarketShared.HULLMODS_MULT > 1) {
                hullmods = Math.round((SubmarketShared.HULLMODS_MULT - 1) * hullmods);
                if (SubmarketShared.DEBUG) { Global.getLogger(this.getClass()).info("Generic SubMARKET Hullmods: " + hullmods + " MWMS");}
                addHullMods(1, hullmods);
            } else {
                if (SubmarketShared.DEBUG) { Global.getLogger(this.getClass()).info("MWMS cannot have HULLMODS Multiplier < 1 so defaults to 0 (no extra hullmods)");}
            }

            if (SubmarketShared.DEBUG) {
                Global.getLogger(this.getClass()).info("Generic SubMARKET WEAPONS: " + weapons + " MWMS");
                Global.getLogger(this.getClass()).info("Generic SubMARKET FIGHTERS: " + fighters + " MWMS");
                Global.getLogger(this.getClass()).info("Generic SubMARKET Ships multiplier: " + ships + " MWMS");
            }

            if (SubmarketShared.MANY_PICK == false) {
                addWeapons(weapons, weapons + 2, 0, market.getFactionId());
                addFighters(fighters, fighters + 2, 0, market.getFactionId());
                if (ships > 0) {
                    pickShips(ships);
                } else {
                    if (SubmarketShared.DEBUG) { Global.getLogger(this.getClass()).info("MWMS cannot have SHIPS Multiplier < 1 so skipping extra ships from multiplier");}
                }
            } else {
                //loop through picks, knowing we pick once in super
                for (int x = 1; x < SubmarketShared.WEAPON_PICKS; x++){
                    if (SubmarketShared.DEBUG) { Global.getLogger(this.getClass()).info("MWMS weapon pick x: " + x);}
                    addWeapons(weapons, weapons + 2, 0, market.getFactionId());
                }
                for (int x = 1; x < SubmarketShared.FIGHTER_PICKS; x++){
                    if (SubmarketShared.DEBUG) {Global.getLogger(this.getClass()).info("flapjack fighter pick x: " + x);}
                    addFighters(fighters, fighters + 2, 0, market.getFactionId());
                }
                for (int x = 1; x < SubmarketShared.SHIP_PICKS; x++) {
                    if (SubmarketShared.DEBUG) {Global.getLogger(this.getClass()).info("flapjack ship pick x: " + x);}
                    pickShips(1);
                }
            }
            getCargo().sort();
        }
        else {
            super.updateCargoPrePlayerInteraction(); //this makes sure the normal commodities get worked out
        }
    }

    //this is straight from source, repeating import, self-production market, and tankers (optional from config)
    private void pickShips(float multiplier) {
        addShips(market.getFactionId(),
                20f * multiplier, // combat
                20f * multiplier, // freighter
                0f, // tanker
                10f * multiplier, // transport
                10f * multiplier, // liner
                5f * multiplier, // utilityPts
                null, // qualityOverride
                0f, // qualityMod
                FactionAPI.ShipPickMode.IMPORTED,
                null);

        addShips(market.getFactionId(),
                30f * multiplier, // combat
                0f, // freighter
                0f, // tanker
                0f, // transport
                0f, // liner
                0f, // utilityPts
                null, // qualityOverride
                -0.5f, // qualityMod
                null,
                null);

        //check if allowing extra tankers or not
        if (SubmarketShared.EXTRA_TANKERS) {
            float tankers = 20f;
            CommodityOnMarketAPI com = market.getCommodityData(Commodities.FUEL);
            tankers += com.getMaxSupply() * 3f;
            tankers = tankers * multiplier; // apply multiplier
            if (tankers > 40) tankers = 40;
            addShips(market.getFactionId(),
                    0f, // combat
                    0f, // freighter
                    tankers, // tanker
                    0, // transport
                    0f, // liner
                    0f, // utilityPts
                    null, // qualityOverride
                    0f, // qualityMod
                    FactionAPI.ShipPickMode.PRIORITY_THEN_ALL,
                    null);
        }
    }
}