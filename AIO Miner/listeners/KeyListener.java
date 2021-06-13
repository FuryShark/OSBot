package listeners;

import java.awt.event.KeyEvent;

import org.osbot.rs07.input.keyboard.BotKeyListener;

import aioMiner.AIOMiner;

public class KeyListener extends BotKeyListener {
	
	private AIOMiner m;

	public KeyListener(AIOMiner m) {
		this.m = m;
	}

	@Override
	public void checkKeyEvent(KeyEvent e) {
		if (e.getID() == 401 && e.getKeyCode() == 113) {
			e.consume();
			m.toggleBankOres();
		}
	}
	
	

}
