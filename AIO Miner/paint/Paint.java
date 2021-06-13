package paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.canvas.paint.Painter;
import org.osbot.rs07.script.MethodProvider;

import aioMiner.AIOMiner;
import utils.NumberFormat;

public class Paint extends MethodProvider implements Painter {
	
	AIOMiner m;
	
	public Paint(PaintTools pt, AIOMiner m) {
		this.pt = pt;
		this.m = m;
	}

	private PaintTools pt;

	@Override
	public void onPaint(Graphics2D g) {
		if (pt.shouldShowPaint()) {
			
			for (int i = 0; i < m.getRocks().size(); ++i) {
				RS2Object rock = m.getRocks().get(i);
				Position pos = rock.getPosition();
				if (m.getRockPositions().containsKey(pos)) {
					g.setColor(Color.GREEN);
				} else {
					g.setColor(Color.RED);
				}
				Rectangle box = rock.getModel().getBoundingBox(rock.getGridX(), rock.getGridY(), rock.getZ());
				g.draw(box);
			}
			
			g.setColor(Color.WHITE);
			g.drawString("Bank ores? " + m.shouldBankOres() + " F2 to toggle", 10, 220);
			
			// ----- Highlights the mouse in a red X ----- \\
			Point mP = getMouse().getPosition();
			g.setColor(Color.RED);
			g.drawLine(mP.x - 5, mP.y + 5, mP.x + 5, mP.y - 5);
			g.drawLine(mP.x + 5, mP.y + 5, mP.x - 5, mP.y - 5);

			g.drawImage(pt.getBackgroundImage(), 2, 298, null);

			// Draw text
			g.setFont(pt.getTitle());
			pt.hoppsoutline(g, "AIO Miner", 170, 381, Color.WHITE, Color.BLACK);

			g.setFont(pt.getFont());
			
			pt.hoppsoutline(g, "Status: " + pt.getStatus(), 10, 305, Color.BLACK, Color.CYAN);
			
			g.setColor(Color.BLACK);
			
			g.drawString("Time Running: " + NumberFormat.timeFormatDHMS(pt.getTimeRan()), 30, 405);

			g.drawString("Exp Gained: " + NumberFormat.runescapeFormat(pt.getXpGained()) + " | Xp/hr: " + NumberFormat.runescapeFormat(pt.getXpPerHour()) + " (+" + pt.getLevelsGained() + ")", 30, 425);
			
			g.drawString("", 30, 445);

			g.drawString("", 30, 465);
		}
	}

}