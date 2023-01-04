package listeners;

import org.osbot.rs07.input.mouse.BotMouseListener;
import beerGlassCollect.BeerGlassCollect;

import java.awt.event.MouseEvent;

public class MouseListener extends BotMouseListener {

	private BeerGlassCollect m;

	public MouseListener(BeerGlassCollect m) {
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
	}
}