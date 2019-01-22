package me.fril.regeneration.debugger;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.Pair;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.RegenState.Transition;

@SuppressWarnings("serial")
class PanelStatus extends JPanel {
	
	private JLabel lblStateVal, lblRegensLeftVal, lblScheduledVal;// , lblAnimationProgressVal;
	
	public PanelStatus() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, /* 0.0, */ 0.0, 1.0};
		setLayout(gridBagLayout);
		
		JLabel lblRegensLeft = new JLabel("Regenerations:");
		{
			GridBagConstraints gbc_lblRegensLeft = new GridBagConstraints();
			gbc_lblRegensLeft.anchor = GridBagConstraints.WEST;
			gbc_lblRegensLeft.insets = new Insets(0, 0, 0, 5);
			gbc_lblRegensLeft.gridx = 1;
			gbc_lblRegensLeft.gridy = 1;
			add(lblRegensLeft, gbc_lblRegensLeft);
		}
		
		lblRegensLeftVal = new JLabel("?");
		{
			GridBagConstraints gbc_lblRegensLeftVal = new GridBagConstraints();
			gbc_lblRegensLeftVal.anchor = GridBagConstraints.EAST;
			gbc_lblRegensLeftVal.insets = new Insets(0, 0, 0, 5);
			gbc_lblRegensLeftVal.gridx = 3;
			gbc_lblRegensLeftVal.gridy = 1;
			add(lblRegensLeftVal, gbc_lblRegensLeftVal);
		}
		
		JLabel lblState = new JLabel("State:");
		{
			GridBagConstraints gbc_lblState = new GridBagConstraints();
			gbc_lblState.anchor = GridBagConstraints.WEST;
			gbc_lblState.insets = new Insets(0, 0, 0, 5);
			gbc_lblState.gridx = 1;
			gbc_lblState.gridy = 2;
			add(lblState, gbc_lblState);
		}
		
		lblStateVal = new JLabel("?");
		{
			GridBagConstraints gbc_lblStateVal = new GridBagConstraints();
			gbc_lblStateVal.anchor = GridBagConstraints.EAST;
			gbc_lblStateVal.insets = new Insets(0, 0, 0, 5);
			gbc_lblStateVal.gridx = 3;
			gbc_lblStateVal.gridy = 2;
			add(lblStateVal, gbc_lblStateVal);
		}
		
		/*
		 * JLabel lblAnimationProgress = new JLabel("Animation progress:"); FUTURE possibly add type-specific metadata to debugger?
		 * {
		 * GridBagConstraints gbc_lblAnimationProgress = new GridBagConstraints();
		 * gbc_lblAnimationProgress.anchor = GridBagConstraints.WEST;
		 * gbc_lblAnimationProgress.insets = new Insets(0, 0, 0, 5);
		 * gbc_lblAnimationProgress.gridx = 1;
		 * gbc_lblAnimationProgress.gridy = 3;
		 * add(lblAnimationProgress, gbc_lblAnimationProgress);
		 * }
		 *
		 * lblAnimationProgressVal = new JLabel("?");
		 * {
		 * GridBagConstraints gbc_lblAnimationProgressVal = new GridBagConstraints();
		 * gbc_lblAnimationProgressVal.anchor = GridBagConstraints.EAST;
		 * gbc_lblAnimationProgressVal.insets = new Insets(0, 0, 0, 5);
		 * gbc_lblAnimationProgressVal.gridx = 3;
		 * gbc_lblAnimationProgressVal.gridy = 3;
		 * add(lblAnimationProgressVal, gbc_lblAnimationProgressVal);
		 * }
		 */
		
		JLabel lblScheduled = new JLabel("Scheduled:");
		{
			GridBagConstraints gbc_lblScheduled = new GridBagConstraints();
			gbc_lblScheduled.anchor = GridBagConstraints.WEST;
			gbc_lblScheduled.insets = new Insets(0, 0, 0, 5);
			gbc_lblScheduled.gridx = 1;
			gbc_lblScheduled.gridy = 3;
			add(lblScheduled, gbc_lblScheduled);
		}
		
		lblScheduledVal = new JLabel("nothing");
		{
			GridBagConstraints gbc_lblScheduledVal = new GridBagConstraints();
			gbc_lblScheduledVal.anchor = GridBagConstraints.EAST;
			gbc_lblScheduledVal.insets = new Insets(0, 0, 0, 5);
			gbc_lblScheduledVal.gridx = 3;
			gbc_lblScheduledVal.gridy = 3;
			add(lblScheduledVal, gbc_lblScheduledVal);
		}
	}
	
	public void updateLabels(IRegeneration cap) {
		lblStateVal.setText(cap.getState().toString());
		lblRegensLeftVal.setText(cap.getRegenerationsLeft() + "");
		// lblAnimationProgressVal.setText(Math.round(cap.getAnimationProgress()*100) + "%");
		
		@SuppressWarnings("deprecation")
		Pair<Transition, Long> scheduled = cap.getStateManager().getScheduledEvent();
		lblScheduledVal.setForeground(scheduled == null ? Color.BLACK : scheduled.getLeft().color);
		lblScheduledVal.setText(scheduled == null ? "nothing" : scheduled.getLeft() + " in " + scheduled.getRight() + " ticks (" + (round(scheduled.getRight() / 20F, 1)) + "s)");
	}
	
	private double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
	
}
