package furyGlassblower;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.ExperienceTracker;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import listeners.MouseListener;
import paint.Paint;
import paint.PaintTools;
import utils.NumberFormat;
import utils.Sleep;

@ScriptManifest(author = "Fury Shark", info = "", name = "Fury Glassblower", version = 0.0, logo = "")
public class FuryGlassblower extends Script {

	private ExperienceTracker xpTracker;
	private PaintTools pt;
	private Paint paint;
	private MouseListener mouseListener;


	private final String[] product = { "Beer glass", "Empty candle lantern", "Empty oil lamp", "Oil lantern", "Vial",
			"Empty fishbowl", "Unpowered orb", "Lantern lens", "Empty light orb" };

	@Override
	public void onStart() throws InterruptedException {
		xpTracker = getExperienceTracker();
		xpTracker.start(Skill.CRAFTING);

		pt = new PaintTools(xpTracker, this);
		paint = new Paint(pt, this);
		paint.exchangeContext(getBot());
		getBot().addPainter(paint);

		mouseListener = new MouseListener(this, pt);
		getBot().addMouseListener(mouseListener);
	}

	@Override
	public int onLoop() throws InterruptedException {
		if (getInventory().contains("Glassblowing pipe") && getInventory().contains("Molten glass")) {
			if (getBank().isOpen()) {
				pt.setStatus("Closing bank...");
				getBank().close();
			}
			if (myPlayer().isAnimating()) {
				pt.setStatus("Moving mouse outside screen...");
				if (getMouse().moveOutsideScreen()) {
					getBot().getFocusEventHandler().loseFocus();
				}
				pt.setStatus("Crafting...");
				Sleep.sleepUntil(() -> !getInventory().contains("Molten glass") || getDialogues().inDialogue(), 60000);
				pt.setStatus("AFK Sleep...");
			} else if (getDialogues().inDialogue() && !getDialogues().isPendingContinuation()) {
				pt.setStatus("Selecting option...");
				if (getKeyboard().typeString(getKey(), false)) {
					Sleep.sleepUntil(() -> myPlayer().isAnimating(), 5000);
				}
			} else if (getInventory().isItemSelected() && getInventory().getSelectedItemName().equals("Glassblowing pipe")) {
				pt.setStatus("Using pipe on molten glass...");
				if (getInventory().interact("Use", "Molten glass")) {
					Sleep.sleepUntil(() -> getDialogues().inDialogue() && !getDialogues().isPendingContinuation(), 5000);
				}
			} else if (getInventory().isItemSelected()) {
				pt.setStatus("Deselecting " + getInventory().getSelectedItemName() + "...");
				if (getInventory().deselectItem()) {
					Sleep.sleepUntil(() -> !getInventory().isItemSelected(), 5000);
				}
			} else {
				pt.setStatus("Selecting Glassblowing pipe...");
				if (getInventory().interact("Use", "Glassblowing pipe")) {
				Sleep.sleepUntil(() -> getInventory().isItemSelected(), 5000);
			}
			}
		} else if (getBank().isOpen()) {
			if (getInventory().contains(product)) {
				pt.setStatus("Depositing product...");
				getBank().depositAllExcept("Glassblowing pipe");
			} else if (!getInventory().contains("Glassblowing pipe")) {
				if (getBank().contains("Glassblowing pipe")) {
					pt.setStatus("Withdrawing a Glassblowing pipe...");
					if (getBank().withdraw("Glassblowing pipe", 1)) {
						Sleep.sleepUntil(() -> getInventory().contains("Glassblowing pipe"), 5000);
					}
				} else {
					log("No Glassblowing pipe found. Stopping script");
					stop(false);
				}
			} else if (!getInventory().contains("Molten glass")) {
				if (getBank().contains("Molten glass")) {
					pt.setStatus("Withdrawing all Molten Glass...");
					if (getBank().withdrawAll("Molten glass")) {
						Sleep.sleepUntil(() -> getInventory().contains("Molten glass"), 5000);
					}
				} else {
					log("No Molten glass found. Stopping script");
					stop(false);
				}
			}
		} else {
			pt.setStatus("Opening bank...");
			getBank().open();
		}
		return random(200, 300);
	}

	private String getKey() {
		int lvl = getSkills().getStatic(Skill.CRAFTING);
		return lvl >= 46 ? "6" : lvl >= 42  ? "5" : lvl >= 33  ? "4" : lvl >= 12 ? "3" : lvl >= 4 ? "2" : "1";
	}

	@Override
	public void onExit() {
		getBot().removeMouseListener(mouseListener);
		getBot().removePainter(paint);

		log("");
		log("== Glassblower Results ==");
		log("Time ran: " + NumberFormat.timeFormatDHMS(pt.getTimeRan()));
		log("Exp gained: " + NumberFormat.runescapeFormat(xpTracker.getGainedXP(Skill.CRAFTING)));
		log("Levels gained: " + xpTracker.getGainedLevels(Skill.CRAFTING));
		log("");
	}
}