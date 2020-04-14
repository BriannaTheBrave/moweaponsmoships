package data.scripts.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.submarkets.MilitarySubmarketPlugin;
import com.fs.starfarer.api.impl.campaign.submarkets.MilitarySubmarketPlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.econ.Submarket;
import data.scripts.campaign.submarkets.SubmarketShared;

import static data.scripts.campaign.submarkets.SubmarketShared.HAVE_NEXERELIN;
import exerelin.campaign.AllianceManager;
import exerelin.campaign.PlayerFactionStore;
import exerelin.utilities.ExerelinUtilsFaction;

public class MWMS_MilitarySubmarketPlugin extends MilitarySubmarketPlugin {
    @Override
    public void updateCargoPrePlayerInteraction() {
        if (okToUpdateShipsAndWeapons()) {
            super.updateCargoPrePlayerInteraction(); //must do this first or anything you add isnt real!
            // this was already done in super method, so what we're doing is increasing weapon/fighter counts

            int weapons = 4 + Math.max(0, market.getSize() - 3) * 2;
            int fighters = 2 + Math.max(0, market.getSize() - 3);
            int hullmods = 2 + itemGenRandom.nextInt(4);
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
                Global.getLogger(this.getClass()).info("Military SubMARKET WEAPONS: " + weapons + " MWMS");
                Global.getLogger(this.getClass()).info("Military SubMARKET FIGHTERS: " + fighters + " MWMS");
                Global.getLogger(this.getClass()).info("Generic SubMARKET Ships multiplier: " + ships + " MWMS");
            }

            if (SubmarketShared.MANY_PICK == false) {
                addWeapons(weapons, weapons + 2, 3, submarket.getFaction().getId());
                addFighters(fighters, fighters + 2, 3, market.getFactionId());
                if (ships > 0) {
                    pickShips(ships);
                } else {
                    if (SubmarketShared.DEBUG) { Global.getLogger(this.getClass()).info("MWMS cannot have SHIPS Multiplier < 1 so skipping extra ships from multiplier");}
                }
            } else {
                //loop through picks, knowing we pick once in super
                for (int x = 1; x < SubmarketShared.WEAPON_PICKS; x++){
                    if (SubmarketShared.DEBUG) {Global.getLogger(this.getClass()).info("MWMS weapon picks: " + x);}
                    addWeapons(weapons, weapons + 2, 3, submarket.getFaction().getId());
                }
                for (int x = 1; x < SubmarketShared.FIGHTER_PICKS; x++){
                    if (SubmarketShared.DEBUG) {Global.getLogger(this.getClass()).info("MWMS fighter picks: " + x);}
                    addFighters(fighters, fighters + 2, 3, market.getFactionId());
                }
                for (int x = 1; x < SubmarketShared.SHIP_PICKS; x++) {
                    if (SubmarketShared.DEBUG) {Global.getLogger(this.getClass()).info("MWMS ship pick x: " + x);}
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
        float stability = market.getStabilityValue();
        float sMult = Math.max(0.1f, stability / 10f);
        addShips(submarket.getFaction().getId(),
                200f * sMult * multiplier, // combat
                15f * multiplier, // freighter
                10f * multiplier, // tanker
                20f * multiplier, // transport
                10f * multiplier, // liner
                10f * multiplier, // utilityPts
                null, // qualityOverride
                0f, // qualityMod
                null,
                null);
    }

    //used with permission from Histidine to ensure compatibility
    @Override
    protected boolean hasCommission() {
        if (HAVE_NEXERELIN) {
            String commissionFaction = ExerelinUtilsFaction.getCommissionFactionId();
            if (commissionFaction != null && AllianceManager.areFactionsAllied(commissionFaction, submarket.getFaction().getId())) {
                return true;
            }
            if (AllianceManager.areFactionsAllied(PlayerFactionStore.getPlayerFactionId(), submarket.getFaction().getId())) {
                return true;
            }
            return submarket.getFaction().getId().equals(commissionFaction);
        } else {
            return super.hasCommission();
        }
    }
}