package listeners;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.input.mouse.BotMouseListener;
import aioWoodcutter.AIOWoodcutter;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class MouseListener extends BotMouseListener {

	private AIOWoodcutter m;
	
	public MouseListener(AIOWoodcutter m) {
		this.m = m;
	}

	@Override
	public void checkMouseEvent(MouseEvent e) {
		if (e.getID() == 501) {
			int i = 0;
			while (i < m.getTrees().size()) {
				RS2Object tree = m.getTrees().get(i);
				Rectangle box = tree.getModel().getBoundingBox(tree.getGridX(), tree.getGridY(), tree.getZ());
				if (box.contains(e.getPoint())) {
					if (m.getTreePositions().containsKey(tree.getPosition())) {
						m.getTreePositions().remove(tree.getPosition());
						break;
					}
					m.getTreePositions().put(tree.getPosition(), tree.getId());
					break;
				} else {
					++i;
				}
			}
			if (!m.getClient().isHumanInputEnabled()) {
				e.consume();
			}
			m.resetTrees();
		}
	}
}