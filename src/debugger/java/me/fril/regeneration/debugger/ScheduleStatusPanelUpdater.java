package me.fril.regeneration.debugger;

import java.awt.EventQueue;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;

import me.fril.regeneration.util.Scheduler;
import me.fril.regeneration.util.Scheduler.ScheduledTask;
import me.fril.regeneration.util.TimerChannel;

public class ScheduleStatusPanelUpdater extends Thread { //TODO update the whole debugger UI
	
	private Scheduler scheduler;
	private Map<TimerChannel, JLabel[]> labelRefs;

	public ScheduleStatusPanelUpdater(Scheduler scheduler, Map<TimerChannel, JLabel[]> labelRefs) {
		setDaemon(true);
		this.scheduler = scheduler;
		this.labelRefs = labelRefs;
	}
	
	@Override
	public void run() {
		
		while (true) {
			for (Entry<TimerChannel, ScheduledTask> en : scheduler.getSchedule().entrySet()) {
				JLabel[] valLabels = labelRefs.get(en.getKey());
				EventQueue.invokeLater(()->{
					valLabels[0].setText(en.getValue().scheduledTick()+"");
					valLabels[1].setText(en.getValue().ticksLeft()+"");
					valLabels[2].setText(en.getValue().isCancelled()+"");
				});
			}
			
			try {
				Thread.sleep(50); //20 ticks/second rate
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
