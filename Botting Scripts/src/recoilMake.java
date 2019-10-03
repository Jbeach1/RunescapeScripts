package script4;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest(author = "Josh", name = "Recoil", version = 3.0, description = "makes recoils"
		+ "", category = Category.MAGIC)

public class recoilMake extends AbstractScript {
	int magic = 0;
	String state = "start";
	public void onStart() {
		log("recoil has begun");

	}

	public int onLoop() {
		
		switch(state) {
		case "start":
			bank();
			state = "cast";
			break;
			
		case "cast":
			magic = getSkills().getRealLevel(Skill.MAGIC);
			if (getInventory().contains("Cosmic rune") && (getInventory().contains("Sapphire ring"))) {
				castRecoil();
			}
			
			break;
		}
		
		return 2000;

	}

	public void castRecoil() {
		getMagic().castSpell(Normal.LEVEL_1_ENCHANT);
		getInventory().interact("Sapphire ring", "Cast");

		while (getInventory().contains("Sapphire ring") && getInventory().contains("Cosmic rune")) {
			if (magic < getSkills().getRealLevel(Skill.MAGIC)) {
				getDialogues().spaceToContinue();
				state = "start";
				break;
			}
			state = "start";
			sleep(Calculations.random(200, 300));
		}
		
	}

	public void bank() {
		NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));
		if (banker.interact("Bank")) {
			if (sleepUntil(() -> getBank().isOpen(), 9000)) {
				if (getBank().depositAllExcept(item -> item != null && item.getName().contains("Cosmic rune"))) {
					getBank().withdraw(1637, 27);
					if (sleepUntil(() -> !getInventory().isFull(), 9000)) {
						if (getBank().close()) {
						}
					}
				}

			}

		}
	}

	public void onExit() {
		log("level achieved:" + getSkills().getRealLevel(Skill.MAGIC));
	}

}