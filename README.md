# MoWeaponsMoShips
This is a mod for StarSector
https://fractalsoftworks.com/

Check out my mod on the forums:
https://fractalsoftworks.com/forum/index.php?topic=

Overview:

This is a mod to allow markets to spawn more weapons, fighter LPCs, and Ships.
Everything is configurable to suit your own needs, and each can be adjusted separately.
This mods is a little risky - it is overwriting all "submarkets" with a custom one so I can do this. If a future change to Star Sector should require a major change, it may require doing Find and Replace operations on existing save files to upgrade. You have been warned.
There is a Vanilla (van) and Nexerelin (Nex) version of this mod. This is because Nexerelin overrides the submarkets as well and the mod cannot determine if the mod is active before submarkets are merged in the Star Sector startup process. Please pick the right version.

Features (To Be):

Weapons and Fighter LPCs
Ships
Static reduction, Static Add
Manipulation of specific picker categories
Expansion of Black Market

Dependencies:
None at this time.

Limitations:

This mod has to overwrite submarket definitions to work. This was done to avoid needing to overwrite the core submarket logic on the base game.
This means any mod that adds its own market would need to be included in this mod so it can be overwritten.
Other modders can contact me at their lesiure to include their submarkets or check in their own custom code for this mod and use its static variables to perform the necessary logic.

This mod also does not perform "perfect math" because of how weapons, ships, and such are "picked" by the game logic. For example a more common item counts as a pick but may have many more items in its stack than say, a rarer item.
Also note that you may not always see more variety - this is because the picker can pick the same weapon multiple times (by chance) and you just end up with more of them in the stack on the market.
Also note that even if your multiplier is low (say 1.1) you will still see, by random chance, that small markets may have 2x the normal amount of weapons/ships because of the randomness involved in the selection process.

Details:

So what is this mod doing, why didn't some expert modder do it earlier (IE why didn't this exist before) and how are weapons and ships generated?

How to uninstall this mod and continue an existing save:

SOON