package me.fril.regeneration.debugger;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.debugger.util.UnloadedPlayerTempChannelProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class RegenDebugger {
	private final Map<EntityPlayer, IDebugChannel> channels = new HashMap<>();
	private final Map<EntityPlayer, PanelPlayer> playerTabs = new HashMap<>();
	
	private final JFrame frame;
	private final JTabbedPane tabs;
	
	public RegenDebugger() {
		frame = new JFrame("Regeneration v" + RegenerationMod.VERSION + " debugger");
		frame.setSize(600, 600);
		frame.setAutoRequestFocus(false);
		
		tabs = new JTabbedPane();
		frame.add(tabs);
		
		String optX = System.getProperty("debuggerX"), optY = System.getProperty("debuggerY");
		int dx = optX == null ? 0 : Integer.valueOf(optX), dy = optY == null ? 0 : Integer.valueOf(optY);
		frame.setLocationRelativeTo(null);
		frame.setLocation(frame.getX()+dx, frame.getY()+dy);
		
		frame.setVisible((boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment"));
	}
	
	public IDebugChannel getChannelFor(EntityPlayer player) {
		if (!channels.containsKey(player)) {
			channels.put(player, new UnloadedPlayerTempChannelProxy(() -> {
				if (playerTabs.containsKey(player))
					return playerTabs.get(player).getDebugChannel();
				else
					return null;
			}));
		}
		
		return channels.get(player);
	}
	
	
	
	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent ev) {
		String name = ev.player.getGameProfile().getName();
		PanelPlayer panel = new PanelPlayer(CapabilityRegeneration.getForPlayer(ev.player));
		
		tabs.addTab(name, panel);
		playerTabs.put(ev.player, panel);
		
		getChannelFor(ev.player).notifyLoaded();
	}
	
	@SubscribeEvent
	public void onLogout(PlayerLoggedOutEvent ev) {
		String name = ev.player.getGameProfile().getName();
		
		tabs.removeTabAt(tabs.indexOfTab(name));
		playerTabs.remove(ev.player);
	}
	
	@SubscribeEvent
	public void onTick(LivingUpdateEvent ev) {
		playerTabs.forEach((player, panel)->EventQueue.invokeLater(panel::updateState));
	}
	
	
	
	public void open() {
		frame.setVisible(true);
	}
	
	public void dispose() {
		frame.dispose();
	}
	
}
