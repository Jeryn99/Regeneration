package me.fril.regeneration.debugger;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.debugger.util.UnloadedPlayerTempChannelProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class RegenDebugger {
	private final Map<EntityPlayer, PanelPlayer> players = new HashMap<>();
	
	private final JFrame frame;
	private final JTabbedPane tabs;
	
	public RegenDebugger() {
		frame = new JFrame("Regeneration v" + RegenerationMod.VERSION + " debugger");
		frame.setSize(600, 600);
		frame.setAutoRequestFocus(false);
		
		tabs = new JTabbedPane();
		frame.add(tabs);
		
		String optX = System.getProperty("debuggerX"),
				optY = System.getProperty("debuggerY");
		int dx = optX == null ? 0 : Integer.valueOf(optX),
				dy = optY == null ? 0 : Integer.valueOf(optY);
		frame.setLocationRelativeTo(null);
		frame.setLocation(frame.getX()+dx, frame.getY()+dy);
		frame.setVisible(true);
	}
	
	public IDebugChannel getChannelFor(EntityPlayer player) {
		return new UnloadedPlayerTempChannelProxy(() -> {
			if (players.containsKey(player))
				return players.get(player).getDebugChannel();
			else
				return null;
		});
	}
	
	public void open() {
		frame.setVisible(true);
	}
	
	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent ev) {
		String name = ev.player.getGameProfile().getName();
		PanelPlayer panel = new PanelPlayer(CapabilityRegeneration.getForPlayer(ev.player));
		
		tabs.addTab(name, panel);
		players.put(ev.player, panel);
	}
	
	@SubscribeEvent
	public void onLogout(PlayerLoggedOutEvent ev) {
		String name = ev.player.getGameProfile().getName();
		
		tabs.removeTabAt(tabs.indexOfTab(name));
		players.remove(ev.player);
	}
	
	public void dispose() {
		frame.dispose();
	}
	
}
