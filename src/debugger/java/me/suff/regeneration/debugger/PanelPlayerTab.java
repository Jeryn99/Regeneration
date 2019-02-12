package me.suff.regeneration.debugger;

import com.mojang.authlib.GameProfile;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.debugger.util.ConditionalDebugChannelProxy;
import me.suff.regeneration.debugger.util.EventQueueDebugChannelProxy;
import me.suff.regeneration.debugger.util.TextPaneLogger;
import me.suff.regeneration.util.RegenState.Transition;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
class PanelPlayerTab extends JPanel {
	
	private final PanelHeader pnlHeader;
	private final PanelStatus pnlStatus;
	private final TextPaneLogger console;
	
	public PanelPlayerTab(GameProfile gp) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0};
		setLayout(gridBagLayout);
		
		pnlHeader = new PanelHeader(gp);
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
		
		JTextPane consoleArea = new JTextPane();
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
	}
	
	public IDebugChannel createChannel() {
		class DebugChannelImpl implements IDebugChannel { // high-tech "anonymous" class
			
			@Override
			public void notifyLoaded() {
				console.println("Player was fully loaded", Color.WHITE, Color.BLACK);
			}
			
			@Override
			public void notifyExecution(Transition action, long atTick) {
				console.println(getPrefix(action) + "EXECUTING " + action + " (after " + atTick + ")", action.color, Color.ORANGE);
			}
			
			@Override
			public void notifySchedule(Transition action, long inTicks) {
				console.println(getPrefix(action) + "SCHEDULED " + action + " in " + inTicks + " (" + (inTicks / 20F) + "s)", action.color, new Color(204, 255, 255));
			}
			
			@Override
			public void notifyCancel(Transition action, long wasInTicks) {
				console.println(getPrefix(action) + "CANCELED " + action + ", was in " + wasInTicks + " (" + (wasInTicks / 20F) + "s)", action.color, Color.LIGHT_GRAY);
				
				if (wasInTicks == 0)
					warn(action, "Cancelling action on tick it was scheduled for");
				else if (wasInTicks < 0)
					warn(action, "Canceled already canceled or completed action");
			}
			
			@Override
			public void warn(Transition action, String msg) {
				console.println(getPrefix(action) + "WARNING: " + msg, action.color, new Color(255, 255, 153));
			}
			
			@Override
			public void warn(String msg) {
				console.println("WARNING: " + msg, Color.BLACK, new Color(255, 255, 153));
			}
			
			@Override
			public void out(Transition action, String msg) {
				console.println(getPrefix(action) + msg, action.color, new Color(255, 255, 153));
			}
			
			@Override
			public void out(String msg) {
				console.println("[OUT]    " + msg, Color.WHITE, Color.DARK_GRAY);
			}
			
			private String getPrefix(Transition action) {
				return "[" + action + "]    ";
			}
			
		}
		
		return new ConditionalDebugChannelProxy(new EventQueueDebugChannelProxy(new DebugChannelImpl()), () -> this.getParent().getParent().getParent().getParent().getParent().isVisible()); // (this -> [a lot of parents due to the tabbed pane] -> main frame).isVisible
	}
	
	public void updateLabels(IRegeneration capability) {
		pnlStatus.updateLabels(capability);
	}
	
}
