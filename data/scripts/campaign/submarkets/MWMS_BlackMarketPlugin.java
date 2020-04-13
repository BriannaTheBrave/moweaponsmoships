package data.scripts.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionDoctrineAPI;
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
                addHullMods(4, hullmods);
            } else {
                if (SubmarketShared.DEBUG) { Global.getLogger(this.getClass()).info("MWMS cannot have HULLMODS Multiplier < 1 so defaults to 0 (no extra hullmods)");}
            }

            if (SubmarketShared.DEBUG) {
                Global.getLogger(this.getClass()).info("Black market WEAPONS: " + weapons + " MWMS");
                Global.getLogger(this.getClass()).info("Black market FIGHTERS: " + fighters + " MWMS");
                Global.getLogger(this.getClass()).info("Generic SubMARKET Ships multiplier: " + ships + " MWMS");
            }

            if (SubmarketShared.MANY_PICK == false) {
                addWeapons(weapons, weapons + 2, 3, factionPicker);
                addFighters(fighters, fighters + 2, 3, factionPicker);
                if (ships > 0) {
                    pickShips(ships);
                } else {
                    if (SubmarketShared.DEBUG) { Global.getLogger(this.getClass()).info("MWMS cannot have SHIPS Multiplier < 1 so skipping extra ships from multiplier");}
                }
            } else {
                //loop through picks, knowing we pick once in super
                for (int x = 1; x < SubmarketShared.WEAPON_PICKS; x++){
                    if (SubmarketShared.DEBUG) {Global.getLogger(this.getClass()).info("MWMS weapon picks: " + x);}
                    addWeapons(weapons, weapons + 2, 3, factionPicker);
                }
                for (int x = 1; x < SubmarketShared.FIGHTER_PICKS; x++){
                    if (SubmarketShared.DEBUG) {Global.getLogger(this.getClass()).info("MWMS fighter picks: " + x);}
                    addFighters(fighters, fighters + 2, 3, factionPicker);
                }
                for (int x = 1; x < SubmarketShared.SHIP_PICKS; x++) {
                    if (SubmarketShared.DEBUG) {Global.getLogger(this.getClass()).info("MWMS ship pick x: " + x);}
                    pickShips(1);
                }
            }
//todo add ships
            getCargo().sort();
        }
        else {
            super.updateCargoPrePlayerInteraction(); //this makes sure the normal commodities get worked out
        }
    }

    //this is straight from source, repeating import, self-production market, and tankers (optional from config)
    private void pickShips(float multiplier) {
        float stability = market.getStabilityValue();
        float sMult = 0.5f + Math.max(0, (1f - stability / 10f)) * 0.5f;
        float pOther = 0.1f;

        FactionDoctrineAPI doctrine = market.getFaction().getDoctrine().clone();
//			FactionDoctrineAPI doctrine = submarket.getFaction().getDoctrine().clone();
//			doctrine.setWarships(3);
//			doctrine.setCarriers(2);
//			doctrine.setPhaseShips(2);

        addShips(market.getFactionId(),
                70f * sMult * multiplier, // combat
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // freighter
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // tanker
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // transport
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // liner
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // utilityPts
                null,
                0f, // qualityMod
                null,
                doctrine);
        FactionDoctrineAPI doctrineOverride = submarket.getFaction().getDoctrine().clone();
        doctrineOverride.setWarships(3);
        doctrineOverride.setPhaseShips(2);
        doctrineOverride.setCarriers(2);
        doctrineOverride.setCombatFreighterProbability(1f);
        doctrineOverride.setShipSize(5);
        addShips(submarket.getFaction().getId(),
                70f * multiplier, // combat
                10f * multiplier, // freighter
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // tanker
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // transport
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // liner
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // utilityPts
                //0.8f,
                Math.min(1f, Misc.getShipQuality(market, market.getFactionId()) + 0.5f),
                0f, // qualityMod
                null,
                doctrineOverride);
        addShips(Factions.INDEPENDENT,
                15f + 15f * sMult * multiplier, // combat
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // freighter
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // tanker
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // transport
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // liner
                itemGenRandom.nextFloat() > pOther ? 0f : 10f * multiplier, // utilityPts
                //0.8f,
                Math.min(1f, Misc.getShipQuality(market, market.getFactionId()) + 0.5f),
                0f, // qualityMod
                null,
                null);
    }
}