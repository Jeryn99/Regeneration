package me.fril.regeneration.debugger;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.debugger.util.EventQueueDebugChannelProxy;
import me.fril.regeneration.debugger.util.TextPaneLogger;
import me.fril.regeneration.util.RegenState.Transition;

@SuppressWarnings("serial")
class PanelPlayer extends JPanel {
	private final IRegeneration capability; //FIXME debugger stops working after death because of outdated capability reference
	
	private final PanelHeader pnlHeader;
	private final PanelStatus pnlStatus;
	
	private final JTextPane consoleArea;
	private final TextPaneLogger console;
	
	public PanelPlayer(IRegeneration cap) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0 };
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
		
		consoleArea = new JTextPane();
		{
			GridBagConstraints gbc_txtConsole = new GridBagConstraints();
			gbc_txtConsole.gridwidth = 2;
			gbc_txtConsole.insets = new Insets(0, 0, 0, 0);
			gbc_txtConsole.fill = GridBagConstraints.BOTH;
			gbc_txtConsole.gridx = 0;
			gbc_txtConsole.gridy = 1;
			add(new JScrollPane(consoleArea), gbc_txtConsole);
		}
		consoleArea.setEditable(false);
		console = new TextPaneLogger(consoleArea);
		
		this.capability = cap;
	}
	
	
	public IDebugChannel getDebugChannel() {
		class DebugChannelImpl implements IDebugChannel { //high-tech "anonymous" class
			
			@Override
			public void notifyLoaded() {
				console.println("Player was fully loaded", Color.WHITE, Color.BLACK);
			}
			
			@Override
			public void notifyExecution(Transition action, long atTick) {
				console.println(getPrefix(action) + "EXECUTING "+action+" (after "+atTick+")", action.color, Color.ORANGE);
			}
			
			
			@Override
			public void notifySchedule(Transition action, long inTicks) {
				console.println(getPrefix(action) + "SCHEDULED "+action+" in "+inTicks+" ("+(inTicks/20F)+"s)", action.color, new Color(204, 255, 255));
			}
			
			@Override
			public void notifyCancel(Transition action, long wasInTicks) {
				console.println(getPrefix(action) + "CANCELED "+action+", was in "+wasInTicks+" ("+(wasInTicks/20F)+"s)", action.color, Color.LIGHT_GRAY);
				
				if (wasInTicks == 0)
					warn(action, "Cancelling action on tick it was scheduled for");
				else if (wasInTicks < 0)
					warn(action, "Canceled already canceled or completed action");
			}
			
			
			@Override
			public void warn(Transition action, String msg) {
				console.println(getPrefix(action) + "WARNING: "+msg, action.color, new Color(255, 255, 153));
			}
			
			
			
			private String getPrefix(Transition action) {
				return "["+action+"]    ";
			}
			
		}
		
		return new EventQueueDebugChannelProxy(new DebugChannelImpl());
	}
	
	
	public void updateState() {
		pnlStatus.updateState(capability);
	}
	
}
