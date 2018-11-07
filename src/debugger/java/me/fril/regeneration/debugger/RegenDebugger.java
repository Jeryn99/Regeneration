package me.fril.regeneration.debugger;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class RegenDebugger {
	private final Map<String, PanelPlayer> players = new HashMap<>();
	
	private final JFrame frame;
	private final JTabbedPane tabs;
	
	public RegenDebugger() {
		frame = new JFrame("Regeneration v"+RegenerationMod.VERSION+" debugger");
		frame.setSize(600, 600);
		
		tabs = new JTabbedPane();
		frame.add(tabs);
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public IDebugChannel getChannelFor(EntityPlayer player) {
		return players.get(player.getGameProfile().getName()).getDebugChannel();
	}
	
	public void open() {
		frame.setVisible(true);
	}
	
	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent ev) {
		String name = ev.player.getGameProfile().getName();
		PanelPlayer panel = new PanelPlayer(CapabilityRegeneration.getForPlayer(ev.player));
		
		tabs.addTab(name, panel);
		players.put(name, panel);
	}
	
	@SubscribeEvent
	public void onLogout(PlayerLoggedOutEvent ev) {
		String name = ev.player.getGameProfile().getName();
		
		tabs.removeTabAt(tabs.indexOfTab(name));
		players.remove(name);
	}
	
	
	public void dispose() {
		frame.dispose();
	}
	
}
