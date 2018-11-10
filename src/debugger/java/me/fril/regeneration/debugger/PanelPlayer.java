package me.fril.regeneration.debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.debugger.util.EventQueueDebugChannelProxy;
import me.fril.regeneration.debugger.util.TextAreaOutputStream;

@SuppressWarnings("serial")
class PanelPlayer extends JPanel {
	//private final IRegeneration capability; //FIXME debugger stops working after death because of outdated capability reference
	
	private final PanelHeader pnlHeader;
	private final PanelStatus pnlStatus;
	private final PanelScheduleStatus pnlSchedule;
	
	private final JTextArea consoleArea;
	private final PrintStream console;
	
	public PanelPlayer(IRegeneration cap) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0 };
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
			gbc_pnlStatus.insets = new Insets(0, 0, 0, 25);
			gbc_pnlStatus.fill = GridBagConstraints.HORIZONTAL;
			gbc_pnlStatus.gridx = 1;
			gbc_pnlStatus.gridy = 0;
			add(pnlStatus, gbc_pnlStatus);
		}
		
		pnlSchedule = new PanelScheduleStatus();
		{
			GridBagConstraints gbc_pnlSchedule = new GridBagConstraints();
			gbc_pnlSchedule.gridwidth = 2;
			gbc_pnlSchedule.insets = new Insets(10, 10, 10, 10);
			gbc_pnlSchedule.fill = GridBagConstraints.BOTH;
			gbc_pnlSchedule.gridx = 0;
			gbc_pnlSchedule.gridy = 1;
			add(pnlSchedule, gbc_pnlSchedule);
		}
		
		consoleArea = new JTextArea();
		{
			GridBagConstraints gbc_txtConsole = new GridBagConstraints();
			gbc_txtConsole.gridwidth = 2;
			gbc_txtConsole.insets = new Insets(0, 0, 0, 0);
			gbc_txtConsole.fill = GridBagConstraints.BOTH;
			gbc_txtConsole.gridx = 0;
			gbc_txtConsole.gridy = 2;
			add(new JScrollPane(consoleArea), gbc_txtConsole);
		}
		consoleArea.setEditable(false);
		console = new PrintStream(new TextAreaOutputStream(consoleArea));
		
		//this.capability = cap;
	}
	
	
	public IDebugChannel getDebugChannel() {
		class DebugChannelImpl implements IDebugChannel { //high-tech "anonymous" class
			private long currentTick;
			
			/*@Override
			public void update(long currentTick) { NOW update GUI
				this.currentTick = currentTick;
				pnlStatus.updateState(capability, currentTick);
				pnlSchedule.updateState(capability.getStateManager().getScheduler());
			}*/
			
			@Override
			public void notifyExecution(String identifier, long tick) {
				console.println(tickString(identifier) + "EXECUTING at "+tick);
				
				if (tick != currentTick)
					console.println(tickString(identifier) + "WARNING: reported tick "+tick+" does not match recording tick "+currentTick);
			}
			
			
			@Override
			public void notifySchedule(String identifier, long inTicks, long scheduledTick) {
				console.println(tickString(identifier) + "SCHEDULED in "+inTicks+" ("+(inTicks/20F)+"s) at "+scheduledTick);
				
				if (scheduledTick - inTicks != currentTick)
					console.println(tickString(identifier) + "WARNING: inTicks & shceduledTick don't add up with the current recording tick ("+scheduledTick+"-"+inTicks+" != "+currentTick+")");
			}
			
			
			@Override
			public void notifyScheduleBlank(String identifier) {
				console.println(tickString(identifier) + "SCHEDULED BLANK");
			}
			
			
			@Override
			public void notifyCancel(String identifier, long inTicks, long scheduledTick) {
				console.println(tickString(identifier) + "CANCELED, was in "+inTicks+" ("+(inTicks/20F)+"s) at "+scheduledTick);
				
				if (inTicks == 0)
					warn(identifier, "Cancelling action on tick it was scheduled for");
				else if (inTicks < 0)
					warn(identifier, "Canceled already canceled or completed action");
				
				if (scheduledTick - inTicks != currentTick)
					warn(identifier, "inTicks & shceduledTick don't add up with the current recording tick ("+scheduledTick+"-"+inTicks+" != "+currentTick+")");
			}
			
			
			@Override
			public void warn(String identifier, String msg) {
				console.println(tickString(identifier) + "WARNING: "+msg);
			}
			
			
			private long previousMessageTick = -1;
			
			private String tickString(String identifier) {
				String nl = "";
				if (previousMessageTick != currentTick) {
					nl = previousMessageTick >= 0 ? "\n" : "";
					previousMessageTick = currentTick;
				}
				
				return nl + "["+identifier+":"+currentTick+"] ";
			}
			
		}
		
		return new EventQueueDebugChannelProxy(new DebugChannelImpl());
	}
	
}
