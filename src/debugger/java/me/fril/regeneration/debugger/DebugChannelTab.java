package me.fril.regeneration.debugger;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mojang.authlib.GameProfile;

@SuppressWarnings("serial")
public class DebugChannelTab extends JPanel {
	
	private final GameProfile user;
	
	public DebugChannelTab(GameProfile gameProfile) {
		user = gameProfile;
		add(new JLabel(user.getName()));
	}
	
	@Override
	public String getName() {
		return user.getName();
	}

	public IDebugChannel getChannel() {
		return new IDebugChannel() {
			
		};
	}
	
}
