package scriptTest;

import java.awt.Graphics;

//import java.util.concurrent.TimeUnit;
//import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest(author = "Josh", name = "Ultimate script", version = 1.0, description = "power levels basic skills based on skill level"
		+ "", category = Category.FISHING)
public class UltimateScript extends AbstractScript {

	// Global variables
	// Timer
	long startTime = System.currentTimeMillis();
	long elapsedTime = System.currentTimeMillis() - startTime;
	long elapsedSeconds = elapsedTime / 1000;
	long elapsedMinutes = elapsedSeconds / 60;

	String task = "No Task Selected";

	Area ironRockArea = new Area(2980, 3235, 2982, 3233, 0);
	Area copperRockArea = new Area(2976, 3248, 2980, 3246, 0);
	Area grandExchangeBankArea = new Area(3160, 3493, 3170, 3486, 0);
	Area yewTreeArea = new Area(3201, 3506, 3225, 3499, 0);
	Area willowTreeArea = new Area(2963, 3196, 2972, 3191, 0);
	Area oakTreeArea = new Area(3244, 3242, 3264, 3219, 0); // oak and tree location is the same
	Area treeTreeArea = new Area(3244, 3242, 3264, 3219, 0);
	Area troutArea = new Area(3109, 3434, 3109, 3432, 0);
	Area shrimpArea = new Area(3237, 3142, 3245, 3158, 0);

	public void onStart() {
		scriptManagerCheck();
		log("welcome to the ultimate script, this script will train multiple skills, while taking breaks");
		log("be sure to have picaxes, axes and fishing equipment in inventory.");
	}

	public int onLoop() {
		int getRandomTask = getRandomTask();
		log("still running");
	
		if (getRandomTask == 1) {
			scriptManagerCheck();
			goMining();
		}
		if (getRandomTask == 2) {
			scriptManagerCheck();
			goFishing();
		}
		if (getRandomTask == 3) {
			scriptManagerCheck();
			goWoodcutting();
		}

		return 10000;
	}

	public void onExit() {
		log("Woodcutting level:" + getSkills().getRealLevel(Skill.WOODCUTTING));
		log("Mining level:" + getSkills().getRealLevel(Skill.MINING));
		log("Fishing level:" + getSkills().getRealLevel(Skill.FISHING));
		scriptManagerCheck();
	}

	public void goMining() {
		log("we now mine the rocks");
		while (elapsedMinutes < getRandomTime()) {
			if (getSkills().getRealLevel(Skill.MINING) < 99) {

				task = mineCopper();
			}
			if (getSkills().getRealLevel(Skill.MINING) >= 99) {
				// mine iron
				task = mineIron();
			}
		}
	}

	public void goFishing() {
		log("we are fishing now");
		while (elapsedMinutes < getRandomTime()) {
			if (getSkills().getRealLevel(Skill.FISHING) < 20) {
				// get shrimp and anchovies until 20 for trout
				task = fishShrimp();
			}
			if (getSkills().getRealLevel(Skill.FISHING) >= 20) {
				// get trout / salmon
				task = fishTrout();
			}
		}
	}

	public void goWoodcutting() {
		log("we are now cutting some wood");
		while (elapsedMinutes < getRandomTime()) {
			if (getSkills().getRealLevel(Skill.WOODCUTTING) < 15) {
				task = runToLumbridgeTreeTree();
				dropItLikeItsHot();
			}

			if (getSkills().getRealLevel(Skill.WOODCUTTING) >= 15 && getSkills().getRealLevel(Skill.WOODCUTTING) < 30) {
				task = runToLumbridgeOakTree();
				dropItLikeItsHot();
			}

			if (getSkills().getRealLevel(Skill.WOODCUTTING) >= 30 && getSkills().getRealLevel(Skill.WOODCUTTING) < 70) {
				task = runToRimmington();
				dropItLikeItsHot();
			}

			if (getSkills().getRealLevel(Skill.WOODCUTTING) >= 70) {
				task = runToYewTown();
				task = returnToVarrockBank();
			}
		}
	}

	public long getRandomTime() {
		return ((long) Math.random() * 60) + 13;

	}

	public int getRandomTask() {
		return (int) (Math.random() * (3)) + 1;
	}

	public void dropItLikeItsHot() {
		if (getInventory().isFull()) {

			getInventory().dropAllExcept("Small fishing net", "Feather", "Fly fishing rod", "Bronze axe", "Mithril axe",
					"Rune axe", "Bronze pickaxe", "Mithril pickaxe", "Rune pickaxe");

		}
	}

	public String mineIron() {
		GameObject ironRock = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Rocks"));
		if (elapsedMinutes > 240) {
			getTabs().logout();
			if (!getInventory().isFull()) {
				if (ironRockArea.contains(getLocalPlayer())) {
					if (!getLocalPlayer().isAnimating()) {
						if (ironRock != null && ironRock.exists()) {
							if (ironRock.interact("Mine") && ironRock.exists()) {
								sleep(1000, 2000);
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
					dropItLikeItsHot();
				}
			}
		}
		return "Mining Rimmington: iron";
	}

	public String mineCopper() {
		GameObject copperRock = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Rocks"));
		if (elapsedMinutes > 240) {
			getTabs().logout();
		}

		else {
			if (!getInventory().isFull()) {
				if (copperRockArea.contains(getLocalPlayer())) {
					if (!getLocalPlayer().isAnimating()) {
						if (copperRock != null && copperRock.exists()) {
							if (copperRock.interact("Mine") && copperRock.exists()) {
								sleep(3000, 4000);
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
					dropItLikeItsHot();
				}
			}
		}
		return "Mining Rimmington: copper";
	}

	public String returnToVarrockBank() {
		if (getInventory().isFull()) {
			if (grandExchangeBankArea.contains(getLocalPlayer())) { // checks if player is in bank already
				NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));

				if (banker.interact("Bank")) { // verifies banker can interact, then sends to next if
					if (sleepUntil(() -> getBank().isOpen(), 9000)) {
						if (getBank().depositAllExcept(item -> item != null && item.getName().contains("axe")
								&& item != null && item.getName().contains("pickaxe"))) {
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
		return "Banking";
	}

	public String runToYewTown() {
		GameObject treeYew = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Yew"));
		if (elapsedMinutes > 240) {
			getTabs().logout();
		} else {

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
		return "Woodcutting: Varrock Yews";
	}

	public String runToRimmington() {
		GameObject treeWillow = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Willow"));
		if (elapsedMinutes > 240) {
			getTabs().logout();
		} else {
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
		return "Woodcutting: Rimmington Willows";
	}

	public String runToLumbridgeOakTree() {
		GameObject treeOak = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Oak"));
		if (elapsedMinutes > 240) {
			getTabs().logout();
		} else {
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
		return "Woodcutting: Lumbridge Oaks";
	}

	public String runToLumbridgeTreeTree() {
		GameObject treeTree = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Tree"));
		if (elapsedMinutes > 240) {
			getTabs().logout();
		} else {
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
		return "Woodcutting: Lumbridge Tree";
	}

	public String fishTrout() {
		NPC fishingSpot = getNpcs().closest("Rod fishing spot");
		if (elapsedMinutes > 240) {
			getTabs().logout();
		} else {
			if (!getInventory().isFull()) {
				if (troutArea.contains(getLocalPlayer())) {
					if (!getLocalPlayer().isAnimating()) {
						if (fishingSpot != null) {
							fishingSpot.interactForceLeft("Lure");
							sleep(Calculations.random(3000, 5000));
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
		return "Fishing Trout";
	}

	public String fishShrimp() {
		NPC fishingSpot = getNpcs().closest("Fishing spot");
		if (elapsedMinutes > 240) {
			getTabs().logout();
		} else {
			if (!getInventory().isFull()) {
				if (shrimpArea.contains(getLocalPlayer())) {
					if (!getLocalPlayer().isAnimating()) {
						if (fishingSpot != null) {
							fishingSpot.interactForceLeft("Net");
							sleep(Calculations.random(3000, 5000));
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
		return "Fishing Shrimp";
	}

	public void onPaint(Graphics g) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		long elapsedMinutes = (elapsedTime / 1000) / 60;
		g.drawString("Elapsed time in minutes: " + elapsedMinutes, 50, 190);
		g.drawString("Current Task: " + task, 50, 220);
		g.drawString("Woodcutting level:" + getSkills().getRealLevel(Skill.WOODCUTTING), 50, 250);
		g.drawString("Mining level:" + getSkills().getRealLevel(Skill.MINING), 50, 280);
		g.drawString("Fishing level:" + getSkills().getRealLevel(Skill.FISHING), 50, 300);
	}

	public void scriptManagerCheck() {
		if (!getClient().getInstance().getScriptManager().isRunning()) {
			//thread stopping code
			getClient().getInstance().getScriptManager().stop();
			log("Script Manager is not running");

		}
	}
}
