package script4;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest(author = "Josh", name = "Fisher", version = 2.0, description = "power levels fishing based on skill level"
		+ "", category = Category.FISHING)
public class fisher extends AbstractScript {
	Area shrimpArea = new Area(3237, 3142, 3245, 3158, 0);
	Area troutArea = new Area(3098, 3435, 3112, 3424, 0);

	public void onStart() {
		log("fisher has begun");
	}

	public int onLoop() {
		log("fisher is running");
		fishingScript();
		return 2000;
	}

	public void onExit() {
		log("level achieved:" + getSkills().getRealLevel(Skill.FISHING));
	}

	public void fishingScript() {
		log("made it to if statements");
		if (getSkills().getRealLevel(Skill.FISHING) < 20) {
			// get shrimp and anchovies until 20 for trout
			fishShrimp();
		}
		if (getSkills().getRealLevel(Skill.FISHING) >= 20) {
			// get trout / salmon
			fishTrout();
		}

	}

	public void fishShrimp() {
		NPC fishingSpot = getNpcs().closest("Fishing spot");
		if (!getInventory().isFull()) {
			if (shrimpArea.contains(getLocalPlayer())) {
				if (!getLocalPlayer().isAnimating()) {
					if (fishingSpot != null) {
						fishingSpot.interactForceLeft("Net");
					}

				}
			} else {
				if (getWalking().walk(shrimpArea.getCenter())) {
					sleep(Calculations.random(3000, 4000));
				}
			}
		} else {
			if (getInventory().isFull()) {
				dropItLikeItsHot();
			}
		}
	}

	public void fishTrout() {
		NPC fishingSpot = getNpcs().closest("Rod fishing spot");
		if (!getInventory().isFull()) {
			if (troutArea.contains(getLocalPlayer())) {
				if (!getLocalPlayer().isAnimating()) {
					if (fishingSpot != null) {
						fishingSpot.interactForceLeft("Lure");
					}

				}
			} else {
				if (getWalking().walk(troutArea.getCenter())) {
					sleep(Calculations.random(3000, 4000));
				}
			}
		} else {
			if (getInventory().isFull()) {
				dropItLikeItsHot();
			}
		}
	}

	public void dropItLikeItsHot() {
		if (getInventory().isFull()) {
			getInventory().dropAllExcept("Small fishing net", "Bronze axe", "Mithril axe", "Fly fishing rod", "Feather",
					"Rune axe", "Mithril pickaxe", "Rune pickaxe");

		}
	}
}