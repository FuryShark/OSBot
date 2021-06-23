package paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.canvas.paint.Painter;
import org.osbot.rs07.script.MethodProvider;

import furyGlassblower.FuryGlassblower;
import utils.NumberFormat;

public class Paint extends MethodProvider implements Painter {
	
	FuryGlassblower m;
	
	public Paint(PaintTools pt, FuryGlassblower m) {
		this.pt = pt;
		this.m = m;
	}

	private PaintTools pt;

	@Override
	public void onPaint(Graphics2D g) {
		if (pt.shouldShowPaint()) {
			
			int currentLevel = getSkills().getStatic(Skill.CRAFTING);
			
			// ----- Highlights the mouse in a red X ----- \\
			Point mP = getMouse().getPosition();
			g.setColor(Color.RED);
			g.drawLine(mP.x - 5, mP.y + 5, mP.x + 5, mP.y - 5);
			g.drawLine(mP.x + 5, mP.y + 5, mP.x - 5, mP.y - 5);

			g.drawImage(pt.getBackgroundImage(), 2, 298, null);

			// Draw text
			g.setFont(pt.getTitle());
			pt.hoppsoutline(g, "Glassblower", 170, 381, Color.WHITE, Color.BLACK);

			g.setFont(pt.getFont());
			
			pt.hoppsoutline(g, "Status: " + pt.getStatus(), 10, 305, Color.BLACK, Color.CYAN);
			
			g.setColor(Color.BLACK);
			
			g.drawString("Time Running: " + NumberFormat.timeFormatDHMS(pt.getTimeRan()), 30, 405);

			g.drawString("Exp Gained: " + NumberFormat.runescapeFormat(pt.getXpGained()) + " | Xp/hr: " + NumberFormat.runescapeFormat(pt.getXpPerHour()), 30, 425);
			
			g.drawString("Current Level: " + currentLevel + " (+" + pt.getLevelsGained() + ")", 30, 445);

			g.drawString("Time To Next Level: " + pt.getTimeToNextLevel(), 30, 465);
		}
	}

}