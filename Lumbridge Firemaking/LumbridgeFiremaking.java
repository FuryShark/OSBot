package lumbridgeFiremaking;

import java.awt.Graphics2D;

import org.osbot.rs07.api.Client.GameState;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.ExperienceTracker;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;


@ScriptManifest(author = "Fury Shark", info = "", name = "Lumb FM", version = 0.0, logo = "")
public class LumbridgeFiremaking extends Script {

	private long lastAnimationTime;
	private ExperienceTracker xpTracker;
	private long timeBegan;

	@Override
	public void onStart() throws InterruptedException {
		xpTracker = getExperienceTracker();
		xpTracker.start(Skill.FIREMAKING);
		timeBegan = System.currentTimeMillis();
	}

	@Override
	public int onLoop() throws InterruptedException {

		GroundItem logs = getGroundItems().closest(i -> i.getName().equals("Logs") && !fireAtPosition(i.getPosition()));
		if (logs != null) {
			lastAnimationTime = System.currentTimeMillis();
			if (logs.interact("Light")) {
				Sleep.sleepUntil(() -> !logs.exists() || lastAnimationOver(5000), 30000);
			}
		} else {
			int currentWorld = getWorlds().getCurrentWorld();
			if (getWorlds().isMembersWorld()) {
				if (getWorlds().hopToP2PWorld()) {
					Sleep.sleepUntil(() -> getClient().getGameState() == GameState.LOGGED_IN && getWorlds().getCurrentWorld() != currentWorld, 50000);
				}
			} else {
				if (getWorlds().hopToF2PWorld()) {
					Sleep.sleepUntil(() -> getClient().getGameState() == GameState.LOGGED_IN && getWorlds().getCurrentWorld() != currentWorld, 50000);
				}
			}
		}

		return random(200, 300);
	}

	private boolean lastAnimationOver(int i) {
		if (myPlayer().isAnimating()) {
			lastAnimationTime = System.currentTimeMillis();
		}
		return System.currentTimeMillis() - lastAnimationTime > i;
	}

	private boolean fireAtPosition(Position position) {
		return getObjects().closest(o -> o.getName().equals("Fire") && o.getPosition().equals(position)) != null;
	}

	public void onMessage(Message message) throws java.lang.InterruptedException {

	}

	@Override
	public void onExit() {

	}

	@Override
	public void onPaint(Graphics2D g) {
		long timeRan = System.currentTimeMillis() - timeBegan;
		g.drawString("Time ran: " + NumberFormat.timeFormatDHMS(timeRan), 15, 290);
		g.drawString("Exp gained: " + NumberFormat.runescapeFormat(xpTracker.getGainedXP(Skill.FIREMAKING))
				+ " | per hour: " + NumberFormat.runescapeFormat(xpTracker.getGainedXPPerHour(Skill.FIREMAKING)), 15,
				310);
		g.drawString("Current level: " + getSkills().getStatic(Skill.FIREMAKING) + " (+" + xpTracker.getGainedLevels(Skill.FIREMAKING) + ")", 15, 330);
	}

}
