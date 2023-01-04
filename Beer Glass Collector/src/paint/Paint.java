package paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import org.osbot.rs07.canvas.paint.Painter;
import org.osbot.rs07.script.MethodProvider;

import utils.NumberFormat;

public class Paint extends MethodProvider implements Painter {
	
	public Paint(PaintTools pt) {
		this.pt = pt;
	}

	private PaintTools pt;

	@Override
	public void onPaint(Graphics2D g) {
		if (pt.shouldShowPaint()) {
			
			g.setColor(Color.WHITE);
			
			// ----- Highlights the mouse in a red X ----- \\
			Point mP = getMouse().getPosition();
			g.setColor(Color.RED);
			g.drawLine(mP.x - 5, mP.y + 5, mP.x + 5, mP.y - 5);
			g.drawLine(mP.x + 5, mP.y + 5, mP.x - 5, mP.y - 5);

			g.drawImage(pt.getBackgroundImage(), 2, 298, null);

			// Draw text
			g.setFont(pt.getTitle());
			pt.hoppsoutline(g, "Beer Glass Collector", 120, 381, Color.WHITE, Color.BLACK);

			g.setFont(pt.getFont());
			
			pt.hoppsoutline(g, "Status: " + pt.getStatus(), 10, 305, Color.BLACK, Color.CYAN);
			
			g.setColor(Color.BLACK);
			
			g.drawString("Time Running: " + NumberFormat.timeFormatDHMS(pt.getTimeRan()), 30, 405);

			g.drawString("Glasses collected: " + NumberFormat.runescapeFormat(pt.getGlasses()) + " | per hour: " + NumberFormat.runescapeFormat(pt.getGlassesHour()), 30, 425);
			
			g.drawString("Profit: " + NumberFormat.runescapeFormat(pt.getProfit()) + " | Gp/hr: " + NumberFormat.runescapeFormat(pt.getProfitHour()), 30, 445);

			g.drawString("", 30, 465);
		}
	}

}