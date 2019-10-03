package script4;

import java.awt.Point;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest(category = Category.MONEYMAKING, name = "airOrbV2", author = "Wilson", version = 4.0)
public class airOrb extends AbstractScript {
	int glory6 = 11978, glory5 = 11976, glory4 = 1712, glory3 = 1710, glory2 = 1708, glory1 = 1706, glory0 = 1704;
	int airOrb = 573, airOrbUn = 567;
	int cosmic = 564;

	Area bankArea = new Area(3092, 3490, 3094, 3488, 0);
	Area trapdoor = new Area(3093, 3471, 3095, 3469, 0);
	Tile spider = new Tile(3120, 9956, 0);
	Area gate1 = new Area(3101, 9911, 3103, 9907, 0);
	Area gate2 = new Area(3130, 9917, 3133, 9917, 0);
	Area spiders = new Area(3119, 9957, 3122, 9955, 0);
	Area ladder = new Area(3087, 9972, 3068, 9970, 0);
	Area obelisk = new Area(3088, 3570, 3088, 3570, 0);
	Area bandits = new Area(3128, 9931, 3130, 9929, 0);
	Tile ladder1 = new Tile(3089, 9971, 0);

	String state = "start";
	
	public void onStart() {
		log("lets get some orbs");
	}

	public int onLoop() {

		switch (state) {

		case "start":
			getToLocation(bankArea, 4000);
			state = "getItems";
			break;

		case "getItems":
			log("getting items");
			withdrawItems();
			sleep(800, 1200);
			state = "runToTrapdoor";
			break;

		case "runToTrapdoor":
			log("moving to trapdoor");
			getToLocation(trapdoor, 0);
			trapdoor();
			state = "gate1";
			break;

		case "gate1":
			log("heading to gate 1");
			getToLocation(gate1, 0);
			log("gate 1 reached, interacting if needed");
			interactGate();
			state = "gate2";
			break;

		case "gate2":
			log("heading to gate 2");
			getToLocation(gate2, 0);
			log("gate 2 reached, interacting if needed");
			interactGate();
			state = "bandits";
			break;

		case "bandits":
			log("heading to bandits");
			getToLocation(bandits, 0);
			log("bandits reached");
			state = "spiders";
			break;

		case "spiders":
			log("heading to spiders");
			getToLocation(spiders, 0);
			log("spiders");
			state = "ladder";
			break;

		case "ladder":
			log("heading to ladder");
			getToLocation(ladder, 0);
			log("ladder reached, attemtping to interact");
			ladder();
			state = "useOrbs";
			break;

		case "useOrbs":
			log("attemting to cast spell");
			useAirOrbs();
			state = "reset";
			break;

		case "reset":
			log("reset");
			break;
		}
		return 1000;
	}

	

	public void withdrawItems() {
		NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));
		if (banker.interact("Bank")) {
			sleep(900, 1000);
			log("deposit all");
			getBank().depositAllItems();
			sleep(800, 1000);
			log("get cosmics");
			getBank().withdraw(564, 81);
			sleep(800, 1000);
			log("get uncharged orbs");
			getBank().withdraw(567, 27);
			sleep(800, 1000);
		}
		getBank().close();
	}

	public void trapdoor() {
		if (trapdoor.contains(getLocalPlayer())) {
			log("Attempting trapdoor interaction");
			GameObject Trapdoor = getGameObjects()
					.closest(gameObject -> gameObject != null && gameObject.getName().contentEquals("Trapdoor"));
			if (Trapdoor.hasAction("Open")) {
				log("is not open, open then enter");
				Trapdoor.interact("Open");
				sleep(800, 1200);
				Trapdoor.interact("Climb-down");
				log("Successful enter");
			} else if (Trapdoor.hasAction("Climb-down")) {
				Trapdoor.interact("Climb-down");
				log("door was open, we went down");
			}
		}
	}

	public void interactGate() {
		GameObject gate1 = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().contentEquals("Gate"));
		if (gate1.hasAction("Open")) {
			gate1.interact("Open");
		} else if (gate1.hasAction("Close")) {
			return;
		}

	}

	public void ladder() {
		GameObject ladder = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().contentEquals("Ladder"));
		if (ladder.hasAction("Climp-up")) {
			ladder.interact("Climp-up");
		}
		return;
	}

	public void useAirOrbs() {
		GameObject obelisk = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().contentEquals("Obelisk of Air"));
		getMagic().castSpellOn(Normal.CHARGE_AIR_ORB, obelisk);
		sleep(800, 1200);
		getMouse().move(new Point(262, 433));
		sleep(800, 1200);
		getMouse().click();

		while (getInventory().contains("Unpowered orb") && getInventory().contains("Cosmic rune")) {
			sleep(Calculations.random(200, 300));
		}
		return;
	}

	public void teleEdge() {

	}

	public void getToLocation(Area area, int sleepTime) {
		while (!area.contains(getLocalPlayer())) {
			getWalking().walk(area.getCenter());
			 
				
			}
			log("enRoute");
			sleep(sleepTime, sleepTime);
			
	}
	
	
	

	public void onExit() {
		log("level achieved:" + getSkills().getRealLevel(Skill.MAGIC));
	}
	
}
