package Listeners;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.input.mouse.BotMouseListener;
import org.osbot.rs07.script.Script;

import aioMiner.AIOMiner;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MouseListener extends BotMouseListener {

	private AIOMiner m;

	public MouseListener(AIOMiner m) {
		this.m = m;
	}

	@Override
	public void checkMouseEvent(MouseEvent e) {
		if (m.getBot().getScriptExecutor().isPaused()) {
			m.getPt().setShowPaint(false);
		} else {
			if (m.getPt().getPaintArea().contains(e.getPoint())) {
				e.consume();
				m.getPt().setShowPaint(!m.getPt().shouldShowPaint());
			}
		}
		if (!e.isConsumed()) {
			if (e.getID() == 501) {
				for (int i = 0; i < m.getRocks().size(); ++i) {
					RS2Object rock = m.getRocks().get(i);
					Position pos = rock.getPosition();
					int id = rock.getId();
					if (pos.getPolygon(m.getBot()).contains(e.getPoint())) {
						if (m.getRockPositions().containsKey(pos)) {
							m.getRockPositions().remove(pos);
						} else {
							m.getRockPositions().put(pos, id);
						}
					}
				}
				if (!m.getClient().isHumanInputEnabled()) {
					e.consume();
				}
				m.getNewRocks();
			}
		}
	}
}