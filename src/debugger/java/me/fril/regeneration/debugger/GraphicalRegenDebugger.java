package me.fril.regeneration.debugger;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.mojang.authlib.GameProfile;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.debugger.util.UnloadedPlayerBufferChannel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class GraphicalRegenDebugger implements IRegenDebugger {
	private final Map<GameProfile, IDebugChannel> channelz = new HashMap<>();
	private final Map<GameProfile, PanelPlayerTab> playerTabz = new HashMap<>();
	
	private final Map<EntityPlayer, UnloadedPlayerBufferChannel> channelBuffer = new WeakHashMap<>();
	
	private final JFrame frame;
	private final JTabbedPane tabs;
	
	public GraphicalRegenDebugger() {
		frame = new JFrame("Regeneration v" + RegenerationMod.VERSION + " debugger");
		frame.setSize(600, 600);
		frame.setAutoRequestFocus(false);
		
		tabs = new JTabbedPane();
		frame.add(tabs);
		
		String optX = System.getProperty("debuggerX"), optY = System.getProperty("debuggerY");
		int dx = optX == null ? 0 : Integer.valueOf(optX), dy = optY == null ? 0 : Integer.valueOf(optY);
		frame.setLocationRelativeTo(null);
		frame.setLocation(frame.getX() + dx, frame.getY() + dy);
		
		frame.setVisible((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"));
	}
	
	/**
	 * <B>NEVER EVER SAVE THE RESULT OF THIS IN A REFERENCE!</B>
	 */
	@Override
	public IDebugChannel getChannelFor(EntityPlayer player) {
		if (player.getGameProfile() != null && channelz.containsKey(player.getGameProfile())) {
			return channelz.get(player.getGameProfile());
		} else {
			channelBuffer.putIfAbsent(player, new UnloadedPlayerBufferChannel());
			return channelBuffer.get(player);
		}
	}
	
	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent ev) {
		GameProfile gp = ev.player.getGameProfile();
		PanelPlayerTab panel = new PanelPlayerTab(gp);
		
		tabs.addTab(gp.getName(), panel);
		playerTabz.put(gp, panel);
		
		IDebugChannel ch = panel.createChannel();
		channelz.put(gp, ch);
		if (channelBuffer.containsKey(ev.player)) {
			channelBuffer.get(ev.player).flush(ch);
			channelBuffer.remove(ev.player);
		}
		ch.notifyLoaded();
	}
	
	@SubscribeEvent
	public void onLogout(PlayerLoggedOutEvent ev) {
		GameProfile gp = ev.player.getGameProfile();
		tabs.removeTabAt(tabs.indexOfTab(gp.getName()));
		playerTabz.remove(gp);
	}
	
	@SubscribeEvent
	public void onTick(LivingUpdateEvent ev) {
		if (ev.getEntity().world.isRemote)
			return;
		
		if (ev.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ev.getEntityLiving();
			EventQueue.invokeLater(()->playerTabz.get(player.getGameProfile()).updateLabels(CapabilityRegeneration.getForPlayer(player)));
		}
	}
	
	@Override
	public void open() {
		frame.setVisible(true);
	}
	
	@Override
	public void dispose() {
		frame.dispose();
	}
	
}
