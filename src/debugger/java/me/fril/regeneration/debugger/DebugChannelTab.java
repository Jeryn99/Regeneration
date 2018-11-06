package me.fril.regeneration.debugger;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.PrintStream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import com.mojang.authlib.GameProfile;

import me.fril.regeneration.debugger.util.TextAreaOutputStream;
import me.fril.regeneration.util.TimerChannel;

@SuppressWarnings("serial")
public class DebugChannelTab extends JPanel implements IDebugChannel {
	
	private final String name;
	private final PrintStream console;
	private final JPanel topPanel;
	
	private final JLabel lblTick;
	private long currentRecordingTick;
	
	public DebugChannelTab(GameProfile gameProfile) {
		name = gameProfile.getName();
		
		setLayout(new GridLayout(1, 1));
		
		JTextArea textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setDisabledTextColor(Color.BLACK);
		console = new PrintStream(new TextAreaOutputStream(textArea));
		
		
		
		topPanel = new JPanel();
		{
			lblTick = new JLabel("Current tick: ");
			topPanel.add(lblTick);
		}
		
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(textArea);
		splitPane.setLeftComponent(topPanel);
		splitPane.setDividerLocation(.75D);
		add(splitPane);
	}
	
	
	@Override
	public String getName() {
		return name == null ? "Unknown (logging in...)" : name;
	}
	
	@Override
	public PrintStream getConsole() {
		return console;
	}
	
	

	@Override
	public void updateCurrentTick(long tick) {
		EventQueue.invokeLater(()->{
			currentRecordingTick = tick;
			lblTick.setText("Current tick: "+tick);
			
			if (name == null && capability.getPlayer().getGameProfile() != null) {
				name = capability.getPlayer().getGameProfile().getName();
				
				JTabbedPane tabs = (JTabbedPane)getParent();
				tabs.setTitleAt(tabs.getSelectedIndex(), name);
			}
			
			for (Entry<TimerChannel, ScheduledTask> en : capability.getStateManager().getScheduler().getSchedule().entrySet()) {
				timerLabels.get(en.getKey()).setText(en.getKey() + ": " + en.getValue());
			}
		});
	}
	
	@Override
	public void notifyExecution(TimerChannel channel, long tick) { //TODO EventQueue proxy implementation?
		EventQueue.invokeLater(()->{
			console.println("EXECUTING "+channel+" at "+tick);
			
			if (tick != currentRecordingTick)
				console.println("WARNING: reported tick "+tick+" does not match recording tick "+currentRecordingTick);
		});
	}


	@Override
	public void notifySchedule(TimerChannel channel, long inTicks, long scheduledTick) {
		EventQueue.invokeLater(()->{
			console.println("SCHEDULED "+channel+" in "+inTicks+" ("+(inTicks/20F)+"s) at "+scheduledTick);
			
			if (scheduledTick - inTicks != currentRecordingTick)
				console.println("WARNING: inTicks & shceduledTick don't add up with the current recording tick ("+scheduledTick+"-"+inTicks+" != "+currentRecordingTick+")");
		});
	}
	
	
	@Override
	public void notifyScheduleBlank(TimerChannel channel) {
		EventQueue.invokeLater(()->{
			console.println("SCHEDULED BLANK ON "+channel);
		});
	}


	@Override
	public void notifyCancel(TimerChannel channel, long inTicks, long scheduledTick) {
		EventQueue.invokeLater(()->{
			console.println("CANCELED "+channel+", was in "+inTicks+" ("+(inTicks/20F)+"s) at "+scheduledTick);
			
			if (scheduledTick - inTicks != currentRecordingTick)
				console.println("WARNING: inTicks & shceduledTick don't add up with the current recording tick ("+scheduledTick+"-"+inTicks+" != "+currentRecordingTick+")");
		});
	}


	@Override
	public void warn(String msg) {
		EventQueue.invokeLater(()->{
			console.println("WARNING: "+msg);
		});
	}
	
}
