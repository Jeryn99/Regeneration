package me.fril.regeneration.debugger;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.mojang.authlib.GameProfile;

public class MainDebugger {
	
	private static final JFrame frame;
	private static final JTabbedPane tabs;
	private static final Map<GameProfile, DebugChannelTab> tabReg = new HashMap<>();
	
	static {
		frame = new JFrame();
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);
		
		tabs = new JTabbedPane();
		contentPane.add(tabs, BorderLayout.CENTER);
		
		
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
	
	private MainDebugger() {}
	
	
	public static IDebugChannel registerPlayer(GameProfile gameProfile) {
		DebugChannelTab tab = new DebugChannelTab(gameProfile);
		
		tabs.add(tab.getName(), tab);
		tabReg.put(gameProfile, tab);
		
		return tab.getChannel();
	}


	public static void unregisterPlayer(GameProfile gameProfile) {
		tabs.remove(tabReg.get(gameProfile));
		tabReg.remove(gameProfile);
	}
	
	
	public static void open() {
		frame.setVisible(true);
	}
	
}
