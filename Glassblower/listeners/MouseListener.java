package listeners;

import org.osbot.rs07.input.mouse.BotMouseListener;
import furyGlassblower.FuryGlassblower;
import paint.PaintTools;

import java.awt.event.MouseEvent;

public class MouseListener extends BotMouseListener {

	private FuryGlassblower m;
	private PaintTools pt;

	public MouseListener(FuryGlassblower m, PaintTools pt) {
		this.m = m;
		this.pt = pt;
	}

	@Override
	public void checkMouseEvent(MouseEvent e) {
		if (m.getBot().getScriptExecutor().isPaused()) {
			pt.setShowPaint(false);
		} else {
			if (pt.getPaintArea().contains(e.getPoint())) {
				e.consume();
				pt.setShowPaint(!pt.shouldShowPaint());
			}
		}
	}
}