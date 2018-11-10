package me.fril.regeneration.debugger.util;

import java.util.function.Supplier;

import me.fril.regeneration.debugger.IDebugChannel;

public class UnloadedPlayerTempChannelProxy implements IDebugChannel { //TODO queue messages when unloaded?
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
	
	
	
	/*@Override
	public void update(long currentTick) {
		if (isLoaded())
			this.channel.update(currentTick);
	}*/
	
	@Override
	public void notifyCancel(String identifier, long inTicks, long scheduledTick) {
		if (isLoaded())
			this.channel.notifyCancel(identifier, inTicks, scheduledTick);
	}
	
	@Override
	public void notifyExecution(String identifier, long tick) {
		if (isLoaded())
			this.channel.notifyExecution(identifier, tick);
	}
	
	@Override
	public void notifySchedule(String identifier, long inTicks, long scheduledTick) {
		if (isLoaded())
			this.channel.notifySchedule(identifier, inTicks, scheduledTick);
	}
	
	@Override
	public void notifyScheduleBlank(String channel) {
		if (isLoaded())
			this.channel.notifyScheduleBlank(channel);
	}
	
	@Override
	public void warn(String identifier, String msg) {
		if (isLoaded())
			this.channel.warn(identifier, msg);
	}
	
}
