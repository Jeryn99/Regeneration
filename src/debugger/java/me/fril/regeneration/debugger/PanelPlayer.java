package me.fril.regeneration.debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.debugger.util.EventQueueDebugChannelProxy;
import me.fril.regeneration.debugger.util.TextAreaOutputStream;
import me.fril.regeneration.util.TimerChannel;

@SuppressWarnings("serial")
public class PanelPlayer extends JPanel {
	private final PanelHeader pnlHeader;
	private final PanelStatus pnlStatus;
	private final PanelScheduleStatus pnlSchedule;
	
	private final JTextArea consoleArea;
	private final PrintStream console;
	
	public PanelPlayer(IRegeneration cap) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0 };
		setLayout(gridBagLayout);
		
		pnlHeader = new PanelHeader(cap.getPlayer().getGameProfile());
		{
			GridBagConstraints gbc_pnlHeader = new GridBagConstraints();
			gbc_pnlHeader.insets = new Insets(10, 10, 10, 10);
			gbc_pnlHeader.fill = GridBagConstraints.BOTH;
			gbc_pnlHeader.gridx = 0;
			gbc_pnlHeader.gridy = 0;
			add(pnlHeader, gbc_pnlHeader);
		}
		
		pnlStatus = new PanelStatus();
		{
			GridBagConstraints gbc_pnlStatus = new GridBagConstraints();
			gbc_pnlStatus.gridwidth = 2;
			gbc_pnlStatus.insets = new Insets(10, 0, 5, 5);
			gbc_pnlStatus.fill = GridBagConstraints.HORIZONTAL;
			gbc_pnlStatus.gridx = 1;
			gbc_pnlStatus.gridy = 0;
			add(pnlStatus, gbc_pnlStatus);
		}
		
		pnlSchedule = new PanelScheduleStatus();
		{
			GridBagConstraints gbc_pnlSchedule = new GridBagConstraints();
			gbc_pnlSchedule.gridwidth = 3;
			gbc_pnlSchedule.insets = new Insets(10, 10, 10, 10);
			gbc_pnlSchedule.fill = GridBagConstraints.BOTH;
			gbc_pnlSchedule.gridx = 0;
			gbc_pnlSchedule.gridy = 1;
			add(pnlSchedule, gbc_pnlSchedule);
		}
		
		consoleArea = new JTextArea();
		{
			GridBagConstraints gbc_txtConsole = new GridBagConstraints();
			gbc_txtConsole.gridwidth = 3;
			gbc_txtConsole.insets = new Insets(0, 0, 0, 0);
			gbc_txtConsole.fill = GridBagConstraints.BOTH;
			gbc_txtConsole.gridx = 0;
			gbc_txtConsole.gridy = 2;
			add(consoleArea, gbc_txtConsole);
		}
		console = new PrintStream(new TextAreaOutputStream(consoleArea));
		
		//TODO start scheduled status updater (with cap reference)
	}
	
	
	public IDebugChannel getDebugChannel() {
		class DebugChannelImpl implements IDebugChannel { //high-tech "anonymous" class
			private long currentRecordingTick;
			
			@Override
			public void notifyExecution(TimerChannel channel, long tick) {
				console.println(tickString() + "EXECUTING "+channel+" at "+tick);
				
				if (tick != currentRecordingTick)
					console.println(tickString() + "WARNING: reported tick "+tick+" does not match recording tick "+currentRecordingTick);
			}
		
		
			@Override
			public void notifySchedule(TimerChannel channel, long inTicks, long scheduledTick) {
				console.println(tickString() + "SCHEDULED "+channel+" in "+inTicks+" ("+(inTicks/20F)+"s) at "+scheduledTick);
				
				if (scheduledTick - inTicks != currentRecordingTick)
					console.println(tickString() + "WARNING: inTicks & shceduledTick don't add up with the current recording tick ("+scheduledTick+"-"+inTicks+" != "+currentRecordingTick+")");
			}
			
			
			@Override
			public void notifyScheduleBlank(TimerChannel channel) {
				console.println(tickString() + "SCHEDULED BLANK ON "+channel);
			}
		
		
			@Override
			public void notifyCancel(TimerChannel channel, long inTicks, long scheduledTick) {
				console.println(tickString() + "CANCELED "+channel+", was in "+inTicks+" ("+(inTicks/20F)+"s) at "+scheduledTick);
				
				if (inTicks <= 0)
					warn(channel+" wasn't scheduled (either already canceled or completed)");
				
				if (scheduledTick - inTicks != currentRecordingTick)
					warn("inTicks & shceduledTick don't add up with the current recording tick ("+scheduledTick+"-"+inTicks+" != "+currentRecordingTick+")");
			}
			
			
			@Override
			public void warn(String msg) {
				console.println(tickString() + "WARNING: "+msg);
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
		
		return new EventQueueDebugChannelProxy(new DebugChannelImpl());
	}
	
	
}
