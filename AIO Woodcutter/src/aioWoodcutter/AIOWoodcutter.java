package aioWoodcutter;

import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import org.osbot.rs07.api.model.GroundItem;
import utils.Sleep;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import java.util.List;
import org.osbot.rs07.api.map.Position;

import java.io.IOException;
import java.util.HashMap;
import org.osbot.rs07.api.util.ExperienceTracker;

import utils.CLI;
import org.osbot.rs07.script.ScriptManifest;

import listeners.MouseListener;
import paint.Paint;
import paint.PaintTools;

import org.osbot.rs07.script.Script;

import static data.Constants.*;

@ScriptManifest(author = "Fury Shark", info = "", name = "Fury AIO Woodcutter", version = 2.0, logo = "https://i.imgur.com/5xUau6u.png")
public class AIOWoodcutter extends Script {
	
	private CLI cli = new CLI();
	private AIOWoodcutterGUI gui;
	private ExperienceTracker xpTracker;
	private CustomMouse customMouse;
	private MouseListener mouseListener = new MouseListener(this);
	private PaintTools pt;
	private Paint paint;
	private HashMap<Position, Integer> treePositions = new HashMap<Position, Integer>();
	private HashMap<String, String> parameters;
	private List<RS2Object> trees;
	
	private boolean bankLogs, lootNests, afkMode;
	private long lastAnimation;
	private int minSleep, maxSleep;
	private Area surroundingArea, cuttingArea;
	private int nextRunThreshold;
	

	@Override
	public void onStart() throws InterruptedException {
		customMouse = new CustomMouse();
		customMouse.exchangeContext(getBot());
		try {
			customMouse.getCustomMouse(getDirectoryData());
		} catch (IOException e) {
			log(e);
		}
		xpTracker = getExperienceTracker();
		xpTracker.start(Skill.WOODCUTTING);
		pt = new PaintTools(xpTracker, this);
		paint = new Paint(pt, this);
		paint.exchangeContext(getBot());
		getBot().addPainter(paint);
		getBot().addMouseListener(mouseListener);
		log("Script Version: " + getVersion());
		if (getParameters() != null) {
			cli.compileParameters(getParameters());
			if (parameters.containsKey("BANK_LOGS") && parameters.get("BANK_LOGS").equals("true")) {
				bankLogs = true;
			}
			if (parameters.containsKey("LOOT_NESTS") && parameters.get("LOOT_NESTS").equals("true")) {
				lootNests = true;
			}
			if (parameters.containsKey("AFK_MODE") && parameters.get("AFK_MODE").equals("true")) {
				afkMode = true;
				if (parameters.containsKey("MIN_SLEEP") && !parameters.get("MIN_SLEEP").equals("")) {
					minSleep = Integer.parseInt(parameters.get("MIN_SLEEP"));
				}
				if (parameters.containsKey("MAX_SLEEP") && !parameters.get("MAX_SLEEP").equals("")) {
					maxSleep = Integer.parseInt(parameters.get("MAX_SLEEP"));
				}
			}
			logValues();
		}		
		surroundingArea = myPlayer().getArea(10);
		cuttingArea = myPlayer().getArea(3);
		resetTrees();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui = new AIOWoodcutterGUI();
				gui.setVisible(true);
			}
		});
	}

	private void logValues() {
		log("Bank Logs: " + bankLogs);
		log("Loot Nests: " + lootNests);
		log("Afk Mode: " + afkMode);
		if (afkMode) {
			log("Min Sleep: " + minSleep);
			log("Max Sleep: " + maxSleep);
		}
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		if (gui != null && gui.shouldLoadValues()) {
			bankLogs = gui.shouldBankLogs();
			lootNests = gui.shouldLootNests();
			afkMode = gui.isAfkMode();
			minSleep = gui.getMinSleep();
			maxSleep = gui.getMaxSleep();
			logValues();
			gui.setLoadValues(false);
		} else if (getInventory().isFull()) {
			if (bankLogs) {
				if (inBank()) {
					if (getBank().isOpen()) {
						pt.setStatus("Depositing inventory...");
						getBank().depositAllExcept(AXES);
					} else {
						pt.setStatus("Opening bank...");
						getBank().open();
					}
				} else {
					pt.setStatus("Walking to closest bank...");
					getWalking().webWalk(BANK_AREAS);
				}
			} else {
				pt.setStatus("Dropping all logs...");
				getInventory().dropAll(LOGS);
			}
		} else if (surroundingArea.contains(myPlayer())) {
			if (!getSettings().isRunning() && getSettings().getRunEnergy() > nextRunThreshold) {
				pt.setStatus("Toggling run energy...");
				if (getSettings().setRunning(true)) {
					nextRunThreshold = random(MIN_RUN, MAX_RUN);
					Sleep.sleepUntil(() -> getSettings().isRunning(), 5000);
				}
			} else {
				GroundItem nest = getGroundItems().closest(o -> o.getName().toLowerCase().contains("nest"));
				if (lootNests && nest != null) {
					pt.setStatus("Looting " + nest.getName() + "...");
					if (nest.interact("Take")) {
						Sleep.sleepUntil(() -> !nest.exists(), 7500);
					}
				} else if (!treePositions.isEmpty() && getEquipment().isWieldingWeapon("Dragon axe") && getCombat().getSpecialPercentage() == 100) {
					pt.setStatus("Toggling special attack...");
					getCombat().toggleSpecialAttack(true);
					Sleep.sleepUntil(() -> getCombat().getSpecialPercentage() < 100, 5000);
				} else {
					RS2Object tree = getObjects().closest(o -> treePositions.containsKey(o.getPosition()) && o.getId() == treePositions.get(o.getPosition()));
					if (tree != null) {
						pt.setStatus("Chopping down " + tree.getName() + "...");
						if (tree.interact("Chop down")) {
							int levelBefore = getSkills().getStatic(Skill.WOODCUTTING);
							Sleep.sleepUntil(() -> myPlayer().isAnimating() || !treeExists(tree) || nestIsVisible(), 5000);
							if (myPlayer().isAnimating()) {
								lastAnimation = System.currentTimeMillis();
								if (afkMode) {
									getMouse().moveOutsideScreen();
									getBot().getFocusEventHandler().loseFocus();
								}
								Sleep.sleepUntil(() -> !treeExists(tree) || inventoryFull() || levelAbove(levelBefore) || nestIsVisible() || lastAnimationOver(5000), 180000);
								if (afkMode) {
									int randomSleep = random(minSleep, maxSleep);
									sleep(randomSleep);
								}
							}
						}
					} else if (treePositions.isEmpty()) {
						pt.setStatus("Click a tree to select it!");
					} else {
						pt.setStatus("Waiting...");
					}
				}
			}
		} else {
			pt.setStatus("Walking to cutting area...");
			getWalking().webWalk(cuttingArea.getRandomPosition());
			if (surroundingArea.contains(myPlayer())) {
				resetTrees();
			}
		}
		return random(200, 300);
	}

	private boolean inventoryFull() {
		if (getInventory().isFull()) {
			log("Inventory is full");
			return true;
		}
		return false;
	}

	private boolean levelAbove(int levelBefore) {
		if (getSkills().getStatic(Skill.WOODCUTTING) > levelBefore) {
			log("Levelled up. " + getSkills().getStatic(Skill.WOODCUTTING) + " woodcutting achieved.");
			return true;
		}
		return false;
	}

	private boolean treeExists(RS2Object tree) {
		if (tree.exists()) {
			return true;
		} else {
			log("Tree is dead");
			return false;
		}
	}

	private boolean lastAnimationOver(int time) {
		if (myPlayer().isAnimating()) {
			lastAnimation = System.currentTimeMillis();
			return false;
		} else {
			return (System.currentTimeMillis() - time) > lastAnimation;
		}
	}

	private boolean nestIsVisible() {
		if (lootNests) {
			if (getGroundItems().closest(o -> o.getName().toLowerCase().contains("nest")) != null) {
				log("Nest found");
				return true;
			}
		}
		return false;
	}

	public void resetTrees() {
		trees = getObjects().getAll().stream().filter(o -> surroundingArea.contains(o) && o.hasAction("Chop down") && o.isVisible()).collect(Collectors.toList());
	}
	
	public List<RS2Object> getTrees() {
		return trees;
	}
	
	public HashMap<Position, Integer> getTreePositions() {
		return treePositions;
	}

	private boolean inBank() {
		for (int i = 0; i < BANK_AREAS.length; ++i) {
			if (BANK_AREAS[i].contains(myPlayer())) {
				return true;
			}
		}
		return false;
	}


	@Override
	public void onExit() {
		getBot().removeMouseListener(mouseListener);
		if (gui != null) {
			gui.dispose();
		}
		log("");
		log("== AIO Woodcutter Results ==");
		log("Time ran: " + pt.getTimeRan());
		log("Exp gained: " + pt.getXpGained());
		log("Levels gained: " + xpTracker.getGainedLevels(Skill.WOODCUTTING));
		log("");
	}

	/*public void onMessage(final Message message) throws InterruptedException {
		
	} */

}
