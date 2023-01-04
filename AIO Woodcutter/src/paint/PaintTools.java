package paint;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.ExperienceTracker;

import aioWoodcutter.AIOWoodcutter;
import utils.NumberFormat;

public class PaintTools {
	
	private ExperienceTracker xpTracker;
	private AIOWoodcutter m;

	public PaintTools(ExperienceTracker xpTracker, AIOWoodcutter m) {
		timeBegan = System.currentTimeMillis();
		this.xpTracker = xpTracker;
		this.m = m;
	}

	private final Font font = new Font("Georgia", 1, 14), title = new Font("Georgia", Font.BOLD, 28);
	private final Rectangle paintArea = new Rectangle(0, 336, 518, 477);
	private final Image bg = getImage("https://i.imgur.com/dQavu1l.png");
	private boolean showPaint = true;
	private long timeBegan;
	private String status;
	
	public void hoppsoutline(Graphics2D g, String text, int x, int y, Color colout, Color coltxt) {
		g.setColor(colout);
		g.drawString(text, x - 1, y);
		g.drawString(text, x + 1, y);
		g.drawString(text, x, y - 1);
		g.drawString(text, x, y + 1);
		g.setColor(coltxt);
		g.drawString(text, x, y);
	}
	
	public void setStatus(String status) {
		if (!status.equals(this.status)) m.log(status);
		this.status = status;
	}

	public Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
		}
		return null;
	}

	public Image getBackgroundImage() {
		return bg;
	}

	public Font getFont() {
		return font;
	}

	public Font getTitle() {
		return title;
	}

	public Rectangle getPaintArea() {
		return paintArea;
	}

	public boolean shouldShowPaint() {
		return showPaint;
	}

	public void setShowPaint(boolean showPaint) {
		this.showPaint = showPaint;
	}

	public String getTimeRan() {
		return NumberFormat.timeFormatDHMS(System.currentTimeMillis() - timeBegan);
	}
	
	public String getXpGained() {
		return NumberFormat.runescapeFormat(xpTracker.getGainedXP(Skill.WOODCUTTING));
	}
		
	public String getXpPerHour() {
		return NumberFormat.runescapeFormat(xpTracker.getGainedXPPerHour(Skill.WOODCUTTING));
	}
	
	public int getLevelsGained() {
		return xpTracker.getGainedLevels(Skill.WOODCUTTING);
	}
	
	public String getStatus() {
		return status;
	}
	
	// fix profit
	public int getProfit() {
		return 0;
	}
	
	public int getProfitHour() {
		return (int) (getProfit() / ((System.currentTimeMillis() - timeBegan) / 3600000.0D));
	}
}