package me.fril.regeneration.debugger;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import me.fril.regeneration.util.Scheduler;
import me.fril.regeneration.util.Scheduler.ScheduledTask;
import me.fril.regeneration.util.TimerChannel;

@SuppressWarnings("serial")
class PanelScheduleStatus extends JPanel {
	
	private Map<TimerChannel, JLabel[]> labelRefs = new HashMap<>();
	
	public PanelScheduleStatus() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);
		
		for (int i = 0; i < TimerChannel.values().length; i++) {
			TimerChannel tc = TimerChannel.values()[i];
			
			JLabel lbl = new JLabel("<html><center>" + tc.toString()/*.replace("_", "<br>")*/ + "</center></html>");
			{
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets = new Insets(0, 0, 5, 5);
				gbc.gridx = i + 1;
				gbc.gridy = 0;
				add(lbl, gbc);
			}
			
			JLabel[] valLabels = new JLabel[3];
			for (int j = 0; j < 3; j++) {
				valLabels[j] = new JLabel("?");
				{
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.insets = new Insets(0, 0, 5, 5);
					gbc.gridx = i + 1;
					gbc.gridy = j + 1;
					add(valLabels[j], gbc);
				}
			}
			
			labelRefs.put(tc, valLabels);
		}
		
		
		JLabel lblScheduledTick = new JLabel("Scheduled tick");
		{
			GridBagConstraints gbc_lblScheduledTick = new GridBagConstraints();
			gbc_lblScheduledTick.insets = new Insets(0, 0, 5, 5);
			gbc_lblScheduledTick.gridx = 0;
			gbc_lblScheduledTick.gridy = 1;
			add(lblScheduledTick, gbc_lblScheduledTick);
		}
		
		JLabel lblTicksleft = new JLabel("Ticks left");
		{
			GridBagConstraints gbc_lblTicksleft = new GridBagConstraints();
			gbc_lblTicksleft.insets = new Insets(0, 0, 5, 5);
			gbc_lblTicksleft.gridx = 0;
			gbc_lblTicksleft.gridy = 2;
			add(lblTicksleft, gbc_lblTicksleft);
		}
		
		JLabel lblCanceled = new JLabel("Canceled?");
		{
			GridBagConstraints gbc_lblCanceled = new GridBagConstraints();
			gbc_lblCanceled.insets = new Insets(0, 0, 0, 5);
			gbc_lblCanceled.gridx = 0;
			gbc_lblCanceled.gridy = 3;
			add(lblCanceled, gbc_lblCanceled);
		}
		
	}
	
	public void updateState(Scheduler scheduler) {
		for (TimerChannel tc : TimerChannel.values()) {
			ScheduledTask task = scheduler.getSchedule().get(tc);
			JLabel[] valLabels = labelRefs.get(tc);
			
			valLabels[0].setText(task == null ? "?" : task.scheduledTick()+"");
			valLabels[1].setText(task == null ? "?" : task.ticksLeft()+"");
			valLabels[2].setText(task == null ? "?" : task.isCancelled()+"");
		}
	}
	
}
