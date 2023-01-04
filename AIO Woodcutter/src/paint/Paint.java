package paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.canvas.paint.Painter;
import org.osbot.rs07.script.MethodProvider;

import aioWoodcutter.AIOWoodcutter;

public class Paint extends MethodProvider implements Painter {
	
	private AIOWoodcutter m;
	private PaintTools pt;
	private long timeBegan;
	
	public Paint(PaintTools pt, AIOWoodcutter m) {
		this.pt = pt;
		timeBegan = System.currentTimeMillis();
		this.m = m;
	}
	
	public long getTimeRan() {
		return System.currentTimeMillis() - timeBegan;
	}

	@Override
	public void onPaint(Graphics2D g) {
			
			g.setColor(Color.WHITE);
			
			// ----- Highlights the mouse in a red X ----- \\
			Point mP = getMouse().getPosition();
			g.setColor(Color.RED);
			g.drawLine(mP.x - 5, mP.y + 5, mP.x + 5, mP.y - 5);
			g.drawLine(mP.x + 5, mP.y + 5, mP.x - 5, mP.y - 5);

			for (int i = 0; i < m.getTrees().size(); ++i) {
				RS2Object tree = m.getTrees().get(i);
				Position pos = tree.getPosition();
				if (m.getTreePositions().containsKey(pos)) {
					g.setColor(Color.GREEN);
				} else {
					g.setColor(Color.RED);
				}
				Rectangle box = tree.getModel().getBoundingBox(tree.getGridX(), tree.getGridY(), tree.getZ());
				g.draw(box);
			}
			g.setColor(Color.WHITE);
			g.drawString("Xp gained: " + pt.getXpGained() + " | Xp/hr: " +pt.getXpPerHour() + " | Lvls gained: " + pt.getLevelsGained(), 10, 280);
			g.drawString("Time running: " + pt.getTimeRan(), 10, 260);
			g.drawString("Status: " + pt.getStatus(), 10, 240);
	}

}