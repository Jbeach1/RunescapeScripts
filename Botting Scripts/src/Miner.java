package scriptTest;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;

import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;

import org.dreambot.api.methods.skills.Skill;

@ScriptManifest(author = "Josh", name = "Miner", version = 1.0, description = "power levels mining based on skill level"
		+ "", category = Category.MINING)
public class Miner extends AbstractScript {
	Area ironRockArea = new Area(2980, 3235, 2982, 3233, 0);
	Area copperRockArea = new Area(2976, 3248, 2980, 3246, 0);
	// Area rimmingtonMineArea = new Area(2970, 3248, 2987, 3230, 0);
	public void onStart() {
		log("script has begun");
	}

	public int onLoop() {
		miningScript();
		return 1000;

	}

	public void miningScript() {
		if (getSkills().getRealLevel(Skill.MINING) < 15) {
			// do copper
			mineCopper();
		}
		if (getSkills().getRealLevel(Skill.MINING) >= 15) {
			// mine iron
			mineIron();
		}

	}

	public void onExit() {
		log("script finsished, final level: " + getSkills().getRealLevel(Skill.MINING));
	}

	public void mineCopper() {
		GameObject copperRock = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Rocks"));

		if (!getInventory().isFull()) {
			if (copperRockArea.contains(getLocalPlayer())) {
				if (!getLocalPlayer().isAnimating()) {
					if (copperRock != null && copperRock.exists()) {
						if(copperRock.interact("Mine") && copperRock.exists()){
								sleep(7000, 8000);
						}
					}
				}
			} else {
				if (getWalking().walk(copperRockArea.getRandomTile())) {
					sleep(Calculations.random(3000, 4000));
				}
			}
		} else {
			if (getInventory().isFull()) {
				dropItLikeItsHotMINING();
			}
		}
	}

	public void mineIron() {
		GameObject ironRock = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Rocks"));

		if (!getInventory().isFull()) {
			if (ironRockArea.contains(getLocalPlayer())) {
				if (!getLocalPlayer().isAnimating()) {
					if (ironRock != null && ironRock.exists()) {
						if(ironRock.interact("Mine") && ironRock.exists()){
								sleep(7000, 8000);
						}
					}	
				}
			} else {
				if (getWalking().walk(ironRockArea.getRandomTile())) {
					sleep(Calculations.random(3000, 4000));
				}
			}
		} else {
			if (getInventory().isFull()) {
				dropItLikeItsHotMINING();
			}
		}
	}

	public void dropItLikeItsHotMINING() {
		if (getInventory().isFull()) {
			getInventory().dropAllExcept((item -> item != null && item.getName().contains("pickaxe")));

		}
	}
}
