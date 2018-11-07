package me.fril.regeneration.debugger.util;

import java.util.function.Supplier;

import me.fril.regeneration.debugger.IDebugChannel;
import me.fril.regeneration.util.TimerChannel;

public class UnloadedPlayerTempChannelProxy implements IDebugChannel {
	private final Supplier<IDebugChannel> channelSupplier;
	private IDebugChannel channel;
	
	public UnloadedPlayerTempChannelProxy(Supplier<IDebugChannel> channelSupplier) {
		this.channelSupplier = channelSupplier;
	}
	
	private boolean isLoaded() {
		if (channel == null) {
			IDebugChannel gottenChannel = channelSupplier.get();
			if (gottenChannel != null) {
				channel = gottenChannel;
				return true;
			} else
				return false;
		} else
			return true;
	}
	
	
	
	@Override
	public void update(long currentTick) {
		if (isLoaded())
			this.channel.update(currentTick);
	}
	
	@Override
	public void notifyCancel(TimerChannel channel, long inTicks, long scheduledTick) {
		if (isLoaded())
			this.channel.notifyCancel(channel, inTicks, scheduledTick);
	}

	@Override
	public void notifyExecution(TimerChannel channel, long tick) {
		if (isLoaded())
			this.channel.notifyExecution(channel, tick);
	}

	@Override
	public void notifySchedule(TimerChannel channel, long inTicks, long scheduledTick) {
		if (isLoaded())
			this.channel.notifySchedule(channel, inTicks, scheduledTick);
	}

	@Override
	public void notifyScheduleBlank(TimerChannel channel) {
		if (isLoaded())
			this.channel.notifyScheduleBlank(channel);
	}

	@Override
	public void warn(String msg) {
		if (isLoaded())
			this.channel.warn(msg);
	}

}
