package scriptTest;

import java.awt.Graphics;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.skills.Skill;

@ScriptManifest(category = Category.MAGIC, name = "comeplex Autoclicker", author = "Wilson", version = 4.0)
public class AutoAlch extends AbstractScript {

	String task = "no task selected";
	int natureQuantity, itemQuantity, lawQuantity, airQuantity = 0;
	int alchItemID = 860; // change this to match ID for item you're alching
	String itemName = "Magic longbow"; // change this to match name for item you're alching
	int airruneCount;
	String state;

	public void onStart() {
		log("Script has begun");
		state = "startState";
	}

	public int onLoop() {
		switch (state) {
		case "startState":
			if (canAlch() && canTele()) {
				state = "alchANDcammy";
				break;
			} else if (canAlch() && !canTele()) {
				state = "alch";
				break;
			} else if (canTele() && !canAlch()) {
				state = "cammy";
				break;
			} else if (!canTele() && !canAlch()) {
				break;
			} else {
				log("error likely reached, unreachable state, will soon reach 'default case' and logout");
				break;
			}

		case "alch":
			alcher();
			state = "startState";
			break;
		case "alchANDcammy":
			alcher();
			cammy();
			state = "startState";
			break;
		case "cammy":
			cammy();
			state = "startState";
			break;
		case "logout":
			logout();
			break;
		default:
			log("real default case reached");
			log("Unable to perform either high alchemy or camelot teleport based on insuffucient runes/items/level requirements");
			logout();
		}

		return Calculations.random(1000, 1500);

	}

	public void alcher() {
		task = "Alching";
		if (getInventory().contains("Nature rune")) {
			natureQuantity = getInventory().count(561);
			getMagic().castSpell(Normal.HIGH_LEVEL_ALCHEMY);
			if (getMagic().isSpellSelected()) {
				getInventory().interact("Magic longbow", "Cast");
				itemQuantity = getInventory().count(alchItemID);
				sleep(Calculations.random(1000, 1500));
			} else {
				state = "startState";
			}
		} else {
			state = "startState";
		}

		task = "alching";
	}

	public void cammy() {
		lawQuantity = getInventory().count("Law rune");
		airQuantity = getInventory().count("Air rune");
		task = "Teleporting Camelot";
		if (getInventory().contains("Law rune") && getInventory().contains("Air rune")) {
			getMagic().castSpell(Normal.CAMELOT_TELEPORT);
		} else {
			state = "startState";
		}
		task = "teleporting";
	}

	public void logout() {
		getTabs().logout();
	}

	public boolean canAlch() {
		if (getSkills().getRealLevel(Skill.MAGIC) >= 55) {
			if (getInventory().contains("Nature rune") && getInventory().contains(itemName)) {
			} else
				return false;

		} else
			return false;
		return true;
	}

	public boolean canTele() {
		if (getSkills().getRealLevel(Skill.MAGIC) >= 45) {
			if (getInventory().contains("Law rune") && getInventory().contains("Air rune")) {
			} else
				return false;

		} else
			return false;
		return true;
	}

	public int potientialExp() {
		int a, b;
		if (itemQuantity < natureQuantity) {
			a = itemQuantity * 65;
		} else {
			a = natureQuantity * 65;
		}
		if (airQuantity / 5 < lawQuantity) {
			b = airQuantity * 55;
		} else {
			b = lawQuantity * 55;
		}
		return a + b;
	}

	public void onExit() {
		log("fin~");
	}

	public void onPaint(Graphics g) {
		g.drawString("Possible Exp in inventory: " + potientialExp(), 50, 280);
		g.drawString("Current Task: " + task, 50, 220);
		g.drawString("Magic level: " + getSkills().getRealLevel(Skill.MAGIC), 50, 250);
		g.drawString("Exp to next level: " + getSkills().getExperienceToLevel(Skill.MAGIC), 50, 300);

	}

}




