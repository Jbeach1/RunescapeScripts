
package scriptTest;

import java.awt.Graphics;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.methods.container.impl.*;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.magic.Spell;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.container.*;
import org.dreambot.api.methods.magic.Magic;

@ScriptManifest(category = Category.MAGIC, name = "Autoclicker", author = "Wilson", version = 3.0)
public class Planker extends AbstractScript {
	long startTime = System.currentTimeMillis();
	long elapsedTime = System.currentTimeMillis() - startTime;
	long startExp = getSkills().getExperience(Skill.MAGIC);
	
	String task = "no task selected";
	public void onStart(){
		log("Script has begun");	
	}
	
	 public int onLoop(){
		alch();
		 
		return 2000;
	}
	 
	public String alch() {
		 if(getInventory().contains("Nature Rune")) {
			 	getMagic().castSpell(Normal.HIGH_LEVEL_ALCHEMY);
			 	if(getMagic().isSpellSelected()) {
			 		getInventory().interact("Magic longbow", "Cast");
			 		sleep((int)(Math.random()*500));
			 	}
			 } else getTabs().logout();
		 return task;
	}
	
	public void onExit() {
	log("fin~");	
	}
	
	@Override
	public void onPaint(Graphics g) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		long elapsedMinutes = (elapsedTime / 1000) / 60;
		g.drawString("Elapsed time in minutes: " + elapsedMinutes, 50, 190);
		g.drawString("Current Task: " + task, 50, 220);
		g.drawString("Magic level: " + getSkills().getRealLevel(Skill.MAGIC), 50, 250);
		g.drawString("Exp gained: " + (getSkills().getExperience(Skill.MAGIC) - startExp), 50, 280);
		g.drawString("Exp to next level: " + getSkills().getExperienceToLevel(Skill.MAGIC), 50, 300);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}


