package aioMiner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.osbot.rs07.api.filter.ContainsNameFilter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.ExperienceTracker;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import listeners.*;
import utils.*;
import paint.*;

@ScriptManifest(author = "Fury Shark", info = "", name = "Fury AIO Miner", version = 0.0, logo = "https://i.imgur.com/Oa4mAAS.png")
public class AIOMiner extends Script {

	private ExperienceTracker xpTracker;
	private PaintTools pt;
	private Paint paint;
	private CustomMouse customMouse;
	
	private int minEnergy = 30, maxEnergy = 60, nextEnergy;
	private boolean bankOres;

	private HashMap<Position, Integer> rockPositions = new HashMap<Position, Integer>();
	private List<RS2Object> rocks;

	private final String[] ores = new String[] { "Clay", "Rune essence", "Copper ore", "Tin ore", "Blurite ore",
			"Iron ore", "Silver ore", "Pure essence", "Coal", "Gold ore", "Mithril ore",
			"Adamantite ore", "Runite ore", "Amethyst", "Uncut sapphire", "Uncut emerald", "Uncut ruby", "Uncut diamond", "Soda ash" },
			pickaxes = new String[] { "Bronze pickaxe", "Iron pickaxe", "Steel pickaxe", "Black pickaxe",
					"Mithril pickaxe", "Adamant pickaxe", "Rune pickaxe", "Dragon pickaxe" };

	private final Area[] bankAreas = new Area[] { Banks.AL_KHARID, Banks.ARCEUUS_HOUSE, Banks.ARDOUGNE_NORTH,
			Banks.ARDOUGNE_SOUTH, Banks.CAMELOT, Banks.CANIFIS, Banks.CASTLE_WARS, Banks.CATHERBY, Banks.DRAYNOR,
			Banks.DUEL_ARENA, Banks.EDGEVILLE, Banks.FALADOR_EAST, Banks.FALADOR_WEST, Banks.GNOME_STRONGHOLD,
			Banks.GRAND_EXCHANGE, Banks.HOSIDIUS_HOUSE, Banks.LOVAKENGJ_HOUSE, Banks.LOVAKITE_MINE,
			Banks.LUMBRIDGE_UPPER, Banks.PEST_CONTROL, Banks.PISCARILIUS_HOUSE, Banks.SHAYZIEN_HOUSE, Banks.TZHAAR,
			Banks.VARROCK_EAST, Banks.VARROCK_WEST, Banks.YANILLE };
	private Area surroundingArea, miningArea;

	private final ContainsNameFilter<Item> pickFilter = new ContainsNameFilter<Item>(pickaxes), 
			oreFilter = new ContainsNameFilter<Item>(ores);
	private KeyListener keyListener = new KeyListener(this);
	private MouseListener mouseListener = new MouseListener(this);
	private Position nextPos;



	public void onStart() throws InterruptedException {
		customMouse = new CustomMouse();
		customMouse.exchangeContext(getBot());
		try {
			customMouse.getCustomMouse(getDirectoryData());
		} catch (IOException e) {
			log(e);
		}
		xpTracker = getExperienceTracker();
		xpTracker.start(Skill.MINING);
		getBot().setLoadingStage("Test");
		pt = new PaintTools(xpTracker, this);
		paint = new Paint(pt, this);
		paint.exchangeContext(getBot());
		getBot().addPainter(paint);
		pt.setStatus("Select a rock highlighted in red to mine it!");

		getBot().addMouseListener(mouseListener);
		getBot().addKeyListener(keyListener);

		surroundingArea = myPlayer().getArea(14);
		miningArea = myPlayer().getArea(3);
		rocks = getNewRocks();
		nextEnergy = random(minEnergy, maxEnergy);


	}

	public int onLoop() throws InterruptedException {
		try {
			if (getInventory().isFull()) {
				if (bankOres) {
					if (inBank()) {
						if (getBank().isOpen()) {
							pt.setStatus("Depositing inventory...");
							getBank().depositAllExcept(pickFilter);
						} else {
							pt.setStatus("Opening bank...");
							getBank().open();
						}
					} else {
						pt.setStatus("Walking to closest bank...");
						getWalking().webWalk(bankAreas);
					}
				} else {
					pt.setStatus("Dropping all ores...");
					getInventory().dropAll(oreFilter);
				}
			} else if (surroundingArea.contains(myPlayer())) {
				if (!getSettings().isRunning() && getSettings().getRunEnergy() > nextEnergy) {
					pt.setStatus("Toggling run energy...");
					if (getSettings().setRunning(true)) {
						nextEnergy = random(minEnergy, maxEnergy);
						log("Next energy threshold: " + nextEnergy);
						Sleep.sleepUntil(() -> getSettings().isRunning(), 5000);
					}
				} else {
					RS2Object rock = getObjects().closest(o -> rockPositions.containsKey(o.getPosition()) && o.getId() == rockPositions.get(o.getPosition()) && nextPos(o));
					if (rock != null) {
						Position oldPos = rock.getPosition();
						pt.setStatus("Mining...");
						if (rock.interact("Mine")) {
							Sleep.sleepUntil(() -> myPlayer().isAnimating() || !rock.exists(), 5000);
							if (myPlayer().isAnimating()) {
								if (getInventory().getEmptySlots() > 1) {
									RS2Object next = getObjects().closest(o -> rockPositions.containsKey(o.getPosition()) && o.getId() == rockPositions.get(o.getPosition()) && !o.getPosition().equals(oldPos));
									if (next != null) {
										nextPos = next.getPosition();
										next.hover();
									}
								}
								Sleep.sleepUntil(() -> !rock.exists() || getInventory().isFull(), 50000);
							}
						}
					} else {
						nextPos = null;
					}
				}
			} else {
				pt.setStatus("Walking to mining area...");
				getWalking().webWalk(miningArea.getRandomPosition());
				if (surroundingArea.contains(myPlayer())) {
					rocks = getNewRocks();
				}
			}
		} catch (Exception e) {
			log(e);
		}
		return random(200, 300);
	}

	private boolean nextPos(RS2Object o) {
		if (nextPos != null) {
			return o.getPosition().equals(nextPos);
		}
		return true;
	}

	public void onExit() {
		getBot().removeKeyListener(keyListener);
		getBot().removeMouseListener(mouseListener);
		getBot().removePainter(paint);

		log("");
		log("== AIO Miner Results ==");
		log("Time ran: " + NumberFormat.timeFormatDHMS(pt.getTimeRan()));
		log("Exp gained: " + NumberFormat.runescapeFormat(xpTracker.getGainedXP(Skill.MINING)));
		log("Levels gained: " + xpTracker.getGainedLevels(Skill.MINING));
		log("");
	}

	public PaintTools getPt() {
		return pt;
	}

	public List<RS2Object> getNewRocks() {
		return getObjects().getAll().stream().filter(o -> surroundingArea.contains(o) && (o.getName().equals("Rocks") || o.getName().equals("Ash pile")) && o.isVisible()).collect(Collectors.toList());
	}

	public List<RS2Object> getRocks() {
		return rocks;
	}

	public HashMap<Position, Integer> getRockPositions() {
		return rockPositions;
	}

	private boolean inBank() {
		for (int i = 0; i < bankAreas.length; ++i) {
			if (bankAreas[i].contains(myPlayer())) {
				return true;
			}
		}
		return false;
	}

	public void toggleBankOres() {
		bankOres = !bankOres;		
	}

	public boolean shouldBankOres() {
		return bankOres;
	}

}
