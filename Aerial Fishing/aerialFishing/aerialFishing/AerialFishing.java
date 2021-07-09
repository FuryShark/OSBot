package aerialFishing;

import java.awt.event.MouseEvent;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.ExperienceTracker;
import org.osbot.rs07.event.interaction.MouseMoveProfile;
import org.osbot.rs07.input.mouse.BotMouseListener;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import paint.*;
import utils.*;

@ScriptManifest(author = "Fury Shark", info = "", name = "Aerial Fishing", version = 1.0, logo = "https://i.imgur.com/56TxrYr.png")
public class AerialFishing extends Script {

	private PaintTools pt;
	private Paint paint;
	private ExperienceTracker xpTracker;

	private final String[] fishes = { "Bluegill", "Common tench", "Mottled eel", "Greater siren" };
	private boolean started;
	private int fishXpBefore, caught, birdReady;

	@Override
	public void onStart() throws InterruptedException {
		// Makes the mouse 'teleport' when Use new mouse is enabled.
		MouseMoveProfile profile = new MouseMoveProfile();
		profile.setFlowVariety(MouseMoveProfile.FlowVariety.NONE);
		profile.setSpeedBaseTime(0);
		profile.setFlowSpeedModifier(0);
		profile.setDeviation(0);
		profile.setMinOvershootDistance(0);
		profile.setMinOvershootTime(0);
		profile.setNoise(0);
		profile.setOvershoots(0);
		getBot().setMouseMoveProfile(profile);

		xpTracker = getExperienceTracker();
		xpTracker.start(Skill.FISHING);
		xpTracker.start(Skill.HUNTER);
		xpTracker.start(Skill.COOKING);
		pt = new PaintTools(xpTracker);
		paint = new Paint(pt);
		paint.exchangeContext(getBot());
		getBot().addPainter(paint);

		// Toggles paint being visible when the chatbox (paint area) is clicked
		getBot().addMouseListener(new BotMouseListener() {
			@Override
			public void checkMouseEvent(MouseEvent e) {
				if (e.getID() == MouseEvent.MOUSE_PRESSED) {
					if (getBot().getScriptExecutor().isPaused()) {
						pt.setShowPaint(false);
					} else {
						if (pt.getPaintArea().contains(e.getPoint())) {
							e.consume();
							pt.setShowPaint(!pt.shouldShowPaint());
						}
					}
				}
			}
		});
		pt.setStatus("Loading...");
		fishXpBefore = getSkills().getExperience(Skill.FISHING);
	}

	@Override
	public int onLoop() throws InterruptedException {
		if (!started) {
			if (getEquipment().isWieldingWeapon("Cormorant's glove")) {
				// Storing the ID of bird when its ready to use so we can check for changes when its not ready.
				birdReady = getEquipment().getItemInSlot(EquipmentSlot.WEAPON.slot).getId();
				started = true;
			} else {
				log("need bird");
				stop(false);
			}
		} else if (getInventory().isFull()) {
			Item item = getInventory().getItem(fishes);
			if (item != null) {
				cutFish();
			} else {
				log("Inventory full of something. Stopping script");
				stop(false);
			}
		} else if (getEquipment().getItemInSlot(EquipmentSlot.WEAPON.slot).getId() == birdReady	&& !getInventory().isItemSelected()) {
			//Checks for a visible fishing spot so it doesnt rotate camera or run away
			NPC spot = getNpcs().closest(n -> n.getName().equals("Fishing spot") && n.isVisible());
			if (spot != null) {
				pt.setStatus("Sending the bird to fish...");
				if (spot.interact("Catch")) {
					Sleep.sleepUntil(() -> getEquipment().getItemInSlot(EquipmentSlot.WEAPON.slot).getId() != birdReady	|| !spot.exists(), 5000);
				}
			}
		} else {
			if (getInventory().getItem(fishes) != null) {
				cutFish();
			}
		}
		if (getSkills().getExperience(Skill.FISHING) > fishXpBefore) {
			caught++;
			fishXpBefore = getSkills().getExperience(Skill.FISHING);
		}
		return 100;
	}

	private void cutFish() {
		int xpBefore = getSkills().getExperience(Skill.COOKING);
		if (getInventory().isItemSelected() && getInventory().getSelectedItemName().equals("Knife")) {
			Item item = getInventory().getItem(fishes);
			if (item != null) {
				pt.setStatus("Using Knife on " + item.getName() + "...");
				if (item.interact("Use")) {
					Sleep.sleepUntil(() -> getSkills().getExperience(Skill.COOKING) != xpBefore, 5000);
				}
			}
		} else {
			if (getInventory().isItemSelected()) {
				String selected = getInventory().getSelectedItemName();
				for (String item : fishes) {
					if (item.equals(selected)) {
						pt.setStatus("Using " + selected + " on Knife...");
						if (getInventory().interact("Use", "Knife")) {
							Sleep.sleepUntil(() -> getSkills().getExperience(Skill.COOKING) != xpBefore, 5000);
						}
					}
				}
				if (getInventory().isItemSelected()) {
					pt.setStatus("Deselecting " + getInventory().getSelectedItemName() + "...");
					if (getInventory().deselectItem()) {
						Sleep.sleepUntil(() -> !getInventory().isItemSelected(), 5000);
					}
				}
			} else {
				pt.setStatus("Selecting Knife...");
				getInventory().interact("Use", "Knife");
			}
		}
	}

	@Override
	public void onExit() {
		log("");
		log("Thanks for using Fury Aerial Fisher!");
		log("===== Results =====");
		log("Time ran: " + NumberFormat.timeFormatDHMS(pt.getTimeRan()));
		log("Fish caught " + caught);
		log("+" + xpTracker.getGainedXP(Skill.FISHING) + " fishing xp");
		log("+" + xpTracker.getGainedXP(Skill.HUNTER) + " hunter xp");
		log("+" + xpTracker.getGainedXP(Skill.COOKING) + " cooking xp");
		log("");
	}

}
