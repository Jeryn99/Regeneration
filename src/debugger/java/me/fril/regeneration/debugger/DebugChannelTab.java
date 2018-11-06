package me.fril.regeneration.debugger;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.debugger.util.TextAreaOutputStream;
import me.fril.regeneration.util.Scheduler.ScheduledTask;
import me.fril.regeneration.util.TimerChannel;

@SuppressWarnings("serial")
public class DebugChannelTab extends JPanel implements IDebugChannel {
	
	private final IRegeneration capability;
	private String name;
	
	private final PrintStream console;
	//private final JPanel pnlTop, pnlTimers;
	
	private final JLabel lblTick;
	private final Map<TimerChannel, JLabel> timerLabels = new HashMap<>();
	private long currentRecordingTick;
	
	public DebugChannelTab(IRegeneration cap) {
		this.capability = cap;
		
		setLayout(new GridLayout(1, 1));
		
		JTextArea textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setDisabledTextColor(Color.BLACK);
		console = new PrintStream(new TextAreaOutputStream(textArea));
		
		
		
		JPanel pnlTop = new JPanel();
		{
			lblTick = new JLabel("Current tick: ", SwingConstants.CENTER);
			
			JPanel pnlTimers = new JPanel();
			for (TimerChannel tc : TimerChannel.values()) {
				JLabel lblTitle = new JLabel(tc.toString());
				pnlTimers.add(lblTitle);
				
				JLabel lblValue = new JLabel("undefined");
				pnlTimers.add(lblValue);
				timerLabels.put(tc, lblValue);
			}
			pnlTimers.setLayout(new GridLayout(TimerChannel.values().length, 2));
			
			pnlTop.add(lblTick);
			pnlTop.add(pnlTimers);
			pnlTop.setLayout(new GridLayout(2, 1));
		}
		
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(new JScrollPane(textArea));
		splitPane.setLeftComponent(pnlTop);
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
			
			for (TimerChannel tc : TimerChannel.values()) {
				ScheduledTask task = capability.getStateManager().getScheduler().getSchedule().get(tc);
				JLabel label = timerLabels.get(tc);
				
				if (task == null)
					label.setText("undefined");
				else
					label.setText(task.toStatusString());
			}
		});
	}
	
	@Override
	public void notifyExecution(TimerChannel channel, long tick) { //TODO EventQueue proxy implementation?
		EventQueue.invokeLater(()->{
			console.println(tickString() + "EXECUTING "+channel+" at "+tick);
			
			if (tick != currentRecordingTick)
				console.println(tickString() + "WARNING: reported tick "+tick+" does not match recording tick "+currentRecordingTick);
		});
	}


	@Override
	public void notifySchedule(TimerChannel channel, long inTicks, long scheduledTick) {
		EventQueue.invokeLater(()->{
			console.println(tickString() + "SCHEDULED "+channel+" in "+inTicks+" ("+(inTicks/20F)+"s) at "+scheduledTick);
			
			if (scheduledTick - inTicks != currentRecordingTick)
				console.println(tickString() + "WARNING: inTicks & shceduledTick don't add up with the current recording tick ("+scheduledTick+"-"+inTicks+" != "+currentRecordingTick+")");
		});
	}
	
	
	@Override
	public void notifyScheduleBlank(TimerChannel channel) {
		EventQueue.invokeLater(()->{
			console.println(tickString() + "SCHEDULED BLANK ON "+channel);
		});
	}


	@Override
	public void notifyCancel(TimerChannel channel, long inTicks, long scheduledTick) {
		EventQueue.invokeLater(()->{
			console.println(tickString() + "CANCELED "+channel+", was in "+inTicks+" ("+(inTicks/20F)+"s) at "+scheduledTick);
			
			if (scheduledTick - inTicks != currentRecordingTick)
				console.println(tickString() + "WARNING: inTicks & shceduledTick don't add up with the current recording tick ("+scheduledTick+"-"+inTicks+" != "+currentRecordingTick+")");
		});
	}


	@Override
	public void warn(String msg) {
		EventQueue.invokeLater(()->{
			console.println(tickString() + "WARNING: "+msg);
		});
	}
	
	
	private long previousMessageTick = 0;
	
	private String tickString() {
		String nl = "";
		if (previousMessageTick != currentRecordingTick) {
			nl = "\n";
			previousMessageTick = currentRecordingTick;
		}
		
		return nl + "["+currentRecordingTick+"] ";
	}
	
}
