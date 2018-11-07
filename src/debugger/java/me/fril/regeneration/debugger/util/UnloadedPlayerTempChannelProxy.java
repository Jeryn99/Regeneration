package me.fril.regeneration.debugger.util;

import java.util.function.Supplier;

import me.fril.regeneration.debugger.IDebugChannel;
import me.fril.regeneration.util.TimerChannel;

public class UnloadedPlayerTempChannelProxy implements IDebugChannel {
	private final Supplier<IDebugChannel> channel;
	
	public UnloadedPlayerTempChannelProxy(Supplier<IDebugChannel> channelSupplier) {
		this.channel = channelSupplier;
	}
	
	@Override
	public void notifyCancel(TimerChannel channel, long inTicks, long scheduledTick) {
		if (this.channel.get() != null)
			this.channel.get().notifyCancel(channel, inTicks, scheduledTick);
	}

	@Override
	public void notifyExecution(TimerChannel channel, long tick) {
		if (this.channel.get() != null)
			this.channel.get().notifyExecution(channel, tick);
	}

	@Override
	public void notifySchedule(TimerChannel channel, long inTicks, long scheduledTick) {
		if (this.channel.get() != null)
			this.channel.get().notifySchedule(channel, inTicks, scheduledTick);
	}

	@Override
	public void notifyScheduleBlank(TimerChannel channel) {
		if (this.channel.get() != null)
			this.channel.get().notifyScheduleBlank(channel);
	}

	@Override
	public void warn(String msg) {
		if (this.channel.get() != null)
			this.channel.get().warn(msg);
	}
	
}
