package scriptTest;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest(author = "Josh", name = "Yew woodcutter", version = 1.0, description = "lumby goblins wc", category = Category.WOODCUTTING)
//always extends abstract Script to get default constructor for methods
public class Script1 extends AbstractScript {

//Global variable for banking area	
	Area bankArea = new Area(3160, 3493, 3170, 3486, 0);
	Area treeArea = new Area(3201, 3506, 3225, 3499, 0);

//Override default onStart's default constructor
	@Override
	public void onStart() {
		log("Script Started");
	}

//Override default constructor for onLoop
	@Override
	public int onLoop() {

		GameObject tree = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Yew"));

		if (!getInventory().isFull()) {
			if (treeArea.contains(getLocalPlayer())) {
				if (tree.interact("chop down")) {
					int countLog = getInventory().count("Yew logs");
					sleepUntil(() -> getInventory().count("Yew logs") > countLog, 12000);
				}
			} else {
				if (getWalking().walk(treeArea.getCenter())) {
					sleep(Calculations.random(3000, 4000));

				}
			}

		}

//Banking segment, first check if inventory is full
		if (getInventory().isFull()) { 
			if (bankArea.contains(getLocalPlayer())) { // checks if player is in bank already
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
			} else if (getWalking().walk(bankArea.getCenter())) {
				sleep(Calculations.random(3000, 4000));

			}

		}
		return (int) Math.random() * 100;
	}

	@Override
	public void onExit() {
		log("Script finished");

	}
	/*
	 * @Override public void onPaint(Graphics graphics) {
	 * 
	 * }
	 */
}
