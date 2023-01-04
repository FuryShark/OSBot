package paint;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import beerGlassCollect.BeerGlassCollect;

public class PaintTools {
	
	private BeerGlassCollect m;
	private int profitEach;

	public PaintTools(BeerGlassCollect m) {
		timeBegan = System.currentTimeMillis();
		this.m = m;
		profitEach = m.getGrandExchange().getOverallPrice(1919);
	}

	private final Font font = new Font("Georgia", 1, 14), title = new Font("Georgia", Font.BOLD, 28);
	private final Rectangle paintArea = new Rectangle(0, 336, 518, 477);
	private final Image bg = getImage("https://i.imgur.com/dQavu1l.png");
	private boolean showPaint = true;
	private long timeBegan;
	private String status;
	private int glasses;
	
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
		this.status = status;
		m.log(status);
	}
	
	public void addGlassCollected() {
		glasses++;
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

	public long getTimeRan() {
		return (System.currentTimeMillis() - timeBegan);
	}
	
	public String getStatus() {
		return status;
	}
	
	public int getProfit() {
		return glasses * profitEach;
	}
	
	public int getGlasses() {
		return glasses;
	}
	
	public int getGlassesHour() {
		return (int) (glasses / ((System.currentTimeMillis() - timeBegan) / 3600000.0D));
	}
	public int getProfitHour() {
		return (int) (getProfit() / ((System.currentTimeMillis() - timeBegan) / 3600000.0D));
	}
}