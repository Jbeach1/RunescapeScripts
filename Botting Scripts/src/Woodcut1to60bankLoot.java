package scriptTest;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.methods.skills.Skill;

@ScriptManifest(author = "Josh", name = "1-60 woodcutter", version = 2.0, description = "power levels up to 60 then goes to yews"
		+ "", category = Category.WOODCUTTING)
public class Woodcut1to60bankLoot extends AbstractScript {

//Global variables: tree spot locations
	Area grandExchangeBankArea = new Area(3160, 3493, 3170, 3486, 0);
	Area yewTreeArea = new Area(3201, 3506, 3225, 3499, 0);
	Area willowTreeArea = new Area(2963, 3196, 2972, 3191, 0);
	Area oakTreeArea = new Area(3244, 3242, 3264, 3219, 0); // oak and tree location is the same
	Area treeTreeArea = new Area(3244, 3242, 3264, 3219, 0);

	@Override
	public void onStart() {
		log("Script 1-60 woodcut has begun, include any axes in inventory that are planned to be used through leveling");
		log("Script will run player to each location");
	}

	@Override
	public int onLoop() {
		log("level achieved"+ getSkills().getRealLevel(Skill.WOODCUTTING));
		if (getSkills().getRealLevel(Skill.WOODCUTTING) < 15) {
			runToLumbridgeTreeTree();
			dropItLikeItsHot();
		}

		if (getSkills().getRealLevel(Skill.WOODCUTTING) >= 15 && getSkills().getRealLevel(Skill.WOODCUTTING) < 30) {
			runToLumbridgeOakTree();
			dropItLikeItsHot();
		}
	
		if (getSkills().getRealLevel(Skill.WOODCUTTING) >= 30 && getSkills().getRealLevel(Skill.WOODCUTTING) < 70) {
			runToRimmington();
			dropItLikeItsHot();
		}
	
		if (getSkills().getRealLevel(Skill.WOODCUTTING) >= 70) {
			runToYewTown();
			returnToVarrockBank();
		}
		return (int) Math.random() * 5000;
	}

	@Override
	public void onExit() {
		log("happy botting! you should be banned soon");
		log("level achieved"+ getSkills().getRealLevel(Skill.WOODCUTTING));
	}

	public void returnToVarrockBank() {
		if (getInventory().isFull()) {
			if (grandExchangeBankArea.contains(getLocalPlayer())) { // checks if player is in bank already
				NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));

				if (banker.interact("Bank")) { // verifies banker can interact, then sends to next if
					if (sleepUntil(() -> getBank().isOpen(), 9000)) {
						if (getBank().depositAllExcept(item -> item != null && item.getName().contains("axe"))) {
							if (sleepUntil(() -> !getInventory().isFull(), 9000)) {
								if (getBank().close()) {
								}
							}
						}

					}

				}
			} else if (getWalking().walk(grandExchangeBankArea.getCenter())) {
				sleep(Calculations.random(3000, 4000));

			}

		}
	}

	public void runToYewTown() {
		GameObject treeYew = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Yew"));

		if (!getInventory().isFull()) {
			if (yewTreeArea.contains(getLocalPlayer())) {
				if (!getLocalPlayer().isAnimating()) {
					treeYew.interact("chop down");
					int countLog = getInventory().count("Yew logs");
					sleepUntil(() -> getInventory().count("Yew logs") > countLog, 12000);
				}
			} else {
				if (getWalking().walk(yewTreeArea.getCenter())) {
					sleep(Calculations.random(3000, 4000));

				}
			}

		}
	}

	public void runToRimmington() {
		GameObject treeWillow = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Willow"));

		if (!getInventory().isFull()) {
			if (willowTreeArea.contains(getLocalPlayer())) {
				if (!getLocalPlayer().isAnimating()) {
					treeWillow.interact("chop down");
					int countLog = getInventory().count("Willow logs");
					sleepUntil(() -> getInventory().count("Willow logs") > countLog, 30000);
				}
			} else {
				if (getWalking().walk(willowTreeArea.getCenter())) {
					sleep(Calculations.random(3000, 4000));
				}
			}
		}
	}

	public void runToLumbridgeOakTree() {
		GameObject treeOak = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Oak"));

		if (!getInventory().isFull()) {
			if (oakTreeArea.contains(getLocalPlayer())) {
				if (!getLocalPlayer().isAnimating()) {
					treeOak.interact("chop down");
					int countLog = getInventory().count("Oak logs");
					sleepUntil(() -> getInventory().count("Oak logs") > countLog, 30000);
				}
			} else {
				if (getWalking().walk(oakTreeArea.getCenter())) {
					sleep(Calculations.random(3000, 4000));
				}
			}
		}
	}

	public void runToLumbridgeTreeTree() {
		GameObject treeTree = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Tree"));
		if (!getInventory().isFull()) {
			if (treeTreeArea.contains(getLocalPlayer())) {
				if (!getLocalPlayer().isAnimating()) {
					treeTree.interact("chop down");
					int countLog = getInventory().count("Logs");
					sleepUntil(() -> getInventory().count("Logs") > countLog, 30000);
				}
			} else {
				if (getWalking().walk(treeTreeArea.getCenter())) {
					sleep(Calculations.random(3000, 4000));
				}
			}
		}
	}

	public void dropItLikeItsHot() {
		if (getInventory().isFull()) {
			getInventory().dropAllExcept(item -> item != null && item.getName().contains("Mithril axe"));
					
		}
	}
}
