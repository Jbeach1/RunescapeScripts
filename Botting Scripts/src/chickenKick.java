package script4;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;

@ScriptManifest(author = "Josh", name = "chicken kicker", version = 1.0, description = "kicks chicks"
		+ "", category = Category.COMBAT)

public class chickenKick extends AbstractScript {
	Area chickenCoop = new Area(3225, 3300, 3236, 3292, 0);
	String state = "start";

	public void onStart() {
		log("time to kick fowl");
	}

	public int onLoop() {
		NPC chicken = getNpcs().closest("Chicken");
		if (chickenCoop.contains(getLocalPlayer())) {
			if (chicken != null) {
				if (chicken.interact("Attack")) {
					while(getLocalPlayer().isInCombat()) {
						sleep(3000);
					} if (!getLocalPlayer().isInCombat()) {
						return 3000;//loot();
					}
					
				}
			}
		}

		return 2000;
	}

	public boolean loot() {
		GroundItem feather = getGroundItems().closest("Feather");
		if (feather != null && chickenCoop.contains(feather.getTile())) {
			if(getLocalPlayer().isInCombat()) {
				return true;
			}
			feather.interact("Take");
		}
		return true;
	}

	public boolean fight() {
		return true;
	}

	public void onExit() {
		log("level achieved:" + getSkills().getRealLevel(Skill.STRENGTH));
	}

}