package me.fril.regeneration.debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import me.fril.regeneration.common.capability.IRegeneration;

@SuppressWarnings("serial")
class PanelStatus extends JPanel {
	private JLabel lblState;
	private JLabel lblStateVal;
	
	private JLabel lblTicks;
	private JLabel lblTicksVal;
	
	private JLabel lblRegensLeft;
	private JLabel lblRegensLeftVal;
	
	private JLabel lblAnimationProgress;
	private JLabel lblAnimationProgressVal;
	
	
	public PanelStatus() {
		{
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
			gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
			gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0, 0.0, 1.0 };
			gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
			setLayout(gridBagLayout);
		}
		
		lblTicks = new JLabel("Ticks:");
		{
			GridBagConstraints gbc_lblTicks = new GridBagConstraints();
			gbc_lblTicks.anchor = GridBagConstraints.WEST;
			gbc_lblTicks.insets = new Insets(0, 0, 5, 5);
			gbc_lblTicks.gridx = 1;
			gbc_lblTicks.gridy = 1;
			add(lblTicks, gbc_lblTicks);
		}
		
		lblTicksVal = new JLabel("?");
		{
			GridBagConstraints gbc_lblTicksVal = new GridBagConstraints();
			gbc_lblTicksVal.anchor = GridBagConstraints.EAST;
			gbc_lblTicksVal.insets = new Insets(0, 0, 5, 5);
			gbc_lblTicksVal.gridx = 3;
			gbc_lblTicksVal.gridy = 1;
			add(lblTicksVal, gbc_lblTicksVal);
		}
		
		lblRegensLeft = new JLabel("Regenerations:");
		{
			GridBagConstraints gbc_lblRegensLeft = new GridBagConstraints();
			gbc_lblRegensLeft.anchor = GridBagConstraints.WEST;
			gbc_lblRegensLeft.insets = new Insets(0, 0, 5, 5);
			gbc_lblRegensLeft.gridx = 1;
			gbc_lblRegensLeft.gridy = 2;
			add(lblRegensLeft, gbc_lblRegensLeft);
		}
		
		lblRegensLeftVal = new JLabel("?");
		{
			GridBagConstraints gbc_lblRegensLeftVal = new GridBagConstraints();
			gbc_lblRegensLeftVal.anchor = GridBagConstraints.EAST;
			gbc_lblRegensLeftVal.insets = new Insets(0, 0, 5, 5);
			gbc_lblRegensLeftVal.gridx = 3;
			gbc_lblRegensLeftVal.gridy = 2;
			add(lblRegensLeftVal, gbc_lblRegensLeftVal);
		}
		
		lblState = new JLabel("State:");
		{
			GridBagConstraints gbc_lblState = new GridBagConstraints();
			gbc_lblState.anchor = GridBagConstraints.WEST;
			gbc_lblState.insets = new Insets(0, 0, 0, 5);
			gbc_lblState.gridx = 1;
			gbc_lblState.gridy = 3;
			add(lblState, gbc_lblState);
		}
		
		lblStateVal = new JLabel("?");
		{
			GridBagConstraints gbc_lblStateVal = new GridBagConstraints();
			gbc_lblStateVal.anchor = GridBagConstraints.EAST;
			gbc_lblStateVal.insets = new Insets(0, 0, 0, 5);
			gbc_lblStateVal.gridx = 3;
			gbc_lblStateVal.gridy = 3;
			add(lblStateVal, gbc_lblStateVal);
		}
		
		lblAnimationProgress = new JLabel("Animation progress:");
		{
			GridBagConstraints gbc_lblAnimationTicks = new GridBagConstraints();
			gbc_lblAnimationTicks.anchor = GridBagConstraints.WEST;
			gbc_lblAnimationTicks.insets = new Insets(0, 0, 0, 5);
			gbc_lblAnimationTicks.gridx = 1;
			gbc_lblAnimationTicks.gridy = 4;
			add(lblAnimationProgress, gbc_lblAnimationTicks);
		}
		
		lblAnimationProgressVal = new JLabel("?");
		{
			GridBagConstraints gbc_lblAnimationTicksVal = new GridBagConstraints();
			gbc_lblAnimationTicksVal.anchor = GridBagConstraints.EAST;
			gbc_lblAnimationTicksVal.insets = new Insets(0, 0, 0, 5);
			gbc_lblAnimationTicksVal.gridx = 3;
			gbc_lblAnimationTicksVal.gridy = 4;
			add(lblAnimationProgressVal, gbc_lblAnimationTicksVal);
		}
	}
	
	
	
	public void updateState(IRegeneration cap, long currentTick) {
		lblStateVal.setText(cap.getStateManager().getState().toString());
		lblRegensLeftVal.setText(cap.getRegenerationsLeft() + "");
		//lblTicksVal.setText(cap.getStateManager().getCurrentTick() + ""); NOW add scheduled action stuff
		lblAnimationProgressVal.setText(Math.round(cap.getAnimationProgress()*100) + "%");
	}
	
}
