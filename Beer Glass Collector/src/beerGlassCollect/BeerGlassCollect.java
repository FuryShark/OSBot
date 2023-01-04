package beerGlassCollect;

import java.io.IOException;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Message.MessageType;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import listeners.MouseListener;
import paint.Paint;
import paint.PaintTools;
import utils.Sleep;

@ScriptManifest(author = "Fury Shark", info = "", name = "Beer Glass Collect", version = 1.0, logo = "https://i.imgur.com/sDoyS6d.png")
public class BeerGlassCollect extends Script {

	private final Area beerRoom = new Area(3318, 3141, 3324, 3136), bankArea = new Area(3267, 3164, 3269, 3170);
	private CustomMouse customMouse;
	private PaintTools pt;
	private Paint paint;
	private MouseListener mouseListener = new MouseListener(this);
	
	@Override
	public void onStart() throws InterruptedException {
		customMouse = new CustomMouse();
		customMouse.exchangeContext(getBot());
		try {
			customMouse.getCustomMouse(getDirectoryData());
		} catch (IOException e) {
			log(e);
		}
		pt = new PaintTools(this);
		paint = new Paint(pt);
		paint.exchangeContext(getBot());
		getBot().addPainter(paint);
		getBot().addMouseListener(mouseListener);
	} 

	public PaintTools getPt() {
		return pt;
	}

	@Override
	public int onLoop() throws InterruptedException {
		if (!getInventory().isFull()) {
			if (beerRoom.contains(myPlayer())) {
				RS2Object shelves = getObjects().closest("Shelves");
				long amountBefore = getInventory().getAmount("Beer glass");
				pt.setStatus("Searching shelf...");
				if (shelves != null) {
					if (shelves.interact("Search")) {
						Sleep.sleepUntil(() -> getInventory().getAmount("Beer glass") > amountBefore, 10000);
					}
				}
			} else {
				pt.setStatus("Walking to beer room...");
				getWalking().webWalk(beerRoom);
			}
		} else {
			if (getBank().isOpen()) {
				pt.setStatus("Depositing inventory...");
				if (getBank().depositAll()) {
					Sleep.sleepUntil(() -> getInventory().isEmpty(), 5000);
				}
			} else {
				if (Banks.AL_KHARID.contains(myPlayer())) {
					pt.setStatus("Opening bank...");
					getBank().open();
				} else {
					pt.setStatus("Walking to bank...");
					getWalking().webWalk(bankArea);
				}
			}
		}
		return random(200,300);
	}

	public void onMessage(Message message) throws java.lang.InterruptedException {
		if (message.getType() == MessageType.FILTERED) {
			String txt = message.getMessage().toLowerCase();
			if (txt.contains("empty beer glass")) {
				pt.addGlassCollected();
			}
		}
	}

	@Override
	public void onExit() {
		log("");
		log("Thanks for using Beer Glass Collector!");
		log("===== Results =====");
		log("Collected: " + pt.getGlasses());
		log("Profit: " + pt.getProfit());
	}

}