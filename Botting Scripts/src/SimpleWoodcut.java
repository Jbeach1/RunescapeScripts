package scriptTest;


import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;

/* design of script is for simple power leveling to get to yew trees. 
 * exchange whatever is in line 29 with the tree type and change log drop option. 
 * best to use in spots w/o agro monsters i.e lumby or south of rimmington.
 * 
 */
@ScriptManifest(author = "Josh", name = "Simple woodcutter", version = 1.0, description = "lumby goblins wc", category = Category.WOODCUTTING)
public class SimpleWoodcut extends AbstractScript {

	@Override
	public void onStart() {
		log("Script Started");
	}

	@Override
	public int onLoop() {
		log("Script Running");
		GameObject tree = getGameObjects()
				.closest(gameObject -> gameObject != null && gameObject.getName().equals("Willow"));
			if (!getLocalPlayer().isAnimating()) {
			tree.interact("chop down");
			int countLog = getInventory().count("Willow logs");
			sleepUntil(() -> getInventory().count("Willow logs") > countLog, 12000);
		}
		if (getInventory().isFull()) {
			getInventory().dropAllExcept("Rune axe");
		}
		return (int) Math.random() * 50;
	}

	@Override
	public void onExit() {
		log("Script exited");
	}

}
