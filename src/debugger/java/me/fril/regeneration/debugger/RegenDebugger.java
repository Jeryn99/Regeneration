package me.fril.regeneration.debugger;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.mojang.authlib.GameProfile;

import me.fril.regeneration.RegenerationMod;

public class RegenDebugger {
	
	private static final JFrame frame;
	private static final JTabbedPane tabs;
	private static final Map<GameProfile, DebugChannelTab> tabReg = new HashMap<>();
	
	static {
		frame = new JFrame("Regeneration v"+RegenerationMod.VERSION+" DEBUGGER");
		
		tabs = new JTabbedPane();
		frame.add(tabs, BorderLayout.CENTER);
		
		String optX = System.getProperty("debuggerX"),
				optY = System.getProperty("debuggerY");
		int dx = optX == null ? 0 : Integer.valueOf(optX),
				dy = optY == null ? 0 : Integer.valueOf(optY);
		frame.setLocationRelativeTo(null);
		frame.setLocation(frame.getX()+dx, frame.getY()+dy);
		
		frame.setAutoRequestFocus(false);
		frame.setSize(450, 300);
		frame.setVisible(true);
	}
	
	private RegenDebugger() {}
	
	
	public static IDebugChannel registerPlayer(GameProfile gameProfile) {
		if (tabReg.containsKey(gameProfile))
			return tabReg.get(gameProfile);
		
		DebugChannelTab tab = new DebugChannelTab(gameProfile);
		
		tabs.addTab(tab.getName(), tab);
		tabReg.put(gameProfile, tab);
		
		return tab;
	}


	public static void unregisterPlayer(GameProfile gameProfile) {
		tabs.remove(tabReg.get(gameProfile));
		tabReg.remove(gameProfile);
	}
	
	
	public static void open() {
		frame.setVisible(true);
	}
	
}
