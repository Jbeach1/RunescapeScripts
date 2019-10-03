package script4;

import java.awt.Point;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest(author = "Josh", name = "Tanner", version = 1.0, description = "tan da hide"
		+ "", category = Category.MONEYMAKING)
public class tanner extends AbstractScript {

	Area bankArea = new Area(3269, 3170, 3271, 3165, 0);
	Area tanArea = new Area(3279, 3193, 3281, 3191, 0);
	Tile tanAreaT = new Tile(3280, 3191, 0);
	String state = "start";

	public void onStart() {
		log("tanner has begun");

	}

	public int onLoop() {

		switch (state) {
		case "start":
			if (bankArea.contains(getLocalPlayer())) {
				bank();
				state = "tan";
			} else {
				getWalking().walk(bankArea.getRandomTile());
			}
			break;

		case "tan":
			if (tanArea.contains(getLocalPlayer())) {
				tan();
				state = "return";
			} else {
				getWalking().walk(tanArea.getRandomTile());
			}
			break;

		case "return":

			if (!bankArea.contains(getLocalPlayer())) {
				getWalking().walk(bankArea.getCenter());
			} else {
				state = "start";
			}
			break;
		}

		return 1000;

	}

	public void tan() {
		if (getLocalPlayer().distance(tanAreaT) < 5) {
				NPC tanner = getNpcs().closest(npc -> npc != null && npc.hasAction("Trade"));
				if (tanner.interact("Trade")) {
					sleep(6000, 8000);
					getMouse().move(new Point(((int) (Math.random() * 28) + 184), (int) (Math.random() * 50) + 209));
					sleep(1500, 2000);
					if(getClient().getMenu().isMenuVisible()) {
						getClient().getMenu().clickAction("All");
					}
					sleep(800, 1200);
				}
			
		}
	}

	public void bank() {
		NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));
		if (banker.interact("Bank")) {
			if (sleepUntil(() -> getBank().isOpen(), 9000)) {
				if (getBank().depositAllExcept(item -> item != null && item.getName().contains("Coins"))) {
					getBank().withdraw(1751, 27);
					if (sleepUntil(() -> !getInventory().isFull(), 9000)) {
						if (getBank().close()) {
						}
					}
				}

			}

		}
	}

	public void reset() {

	}

	public void onExit() {

	}

}