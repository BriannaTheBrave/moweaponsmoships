package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class MoWeaponsMoShipsModPlugin extends BaseModPlugin
{
    @Override
    public void onApplicationLoad() {
        Global.getLogger(MoWeaponsMoShipsModPlugin.class).info("Hooray MoWeaponsMoShips mod plugin in a jar is loaded!");
    }

}