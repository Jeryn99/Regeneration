package me.fril.regeneration.debugger.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

import me.fril.regeneration.debugger.IDebugChannel;
import me.fril.regeneration.util.RegenState.Transition;

public class UnloadedPlayerTempChannelProxy implements IDebugChannel {
	private final Supplier<IDebugChannel> channelSupplier;
	private final Queue<Runnable> unloadedBuffer;
	private IDebugChannel channel;
	
	public UnloadedPlayerTempChannelProxy(Supplier<IDebugChannel> channelSupplier) {
		this.channelSupplier = channelSupplier;
		this.unloadedBuffer = new LinkedList<>();
	}
	
	private boolean isLoaded() {
		if (channel == null) {
			IDebugChannel gottenChannel = channelSupplier.get();
			if (gottenChannel != null) {
				channel = gottenChannel;
				unloadedBuffer.forEach(r->r.run());
				unloadedBuffer.clear();
				return true;
			} else return false;
		} else return true;
	}
	
	@Override
	public void notifyLoaded() {
		if (!isLoaded() || unloadedBuffer.size() > 0) //will flush the buffer
			throw new IllegalStateException("Loaded notification was a lie...");
		this.channel.notifyLoaded();
	}
	
	@Override
	public void notifyCancel(Transition action, long inTicks) {
		if (isLoaded())
			this.channel.notifyCancel(action, inTicks);
		else
			unloadedBuffer.add(()->notifyCancel(action, inTicks));
	}
	
	@Override
	public void notifyExecution(Transition action, long tick) {
		if (isLoaded())
			this.channel.notifyExecution(action, tick);
		else
			unloadedBuffer.add(()->notifyExecution(action, tick));
	}
	
	@Override
	public void notifySchedule(Transition action, long inTicks) {
		if (isLoaded())
			this.channel.notifySchedule(action, inTicks);
		else
			unloadedBuffer.add(()->notifySchedule(action, inTicks));
	}
	
	@Override
	public void warn(Transition action, String msg) {
		if (isLoaded())
			this.channel.warn(action, msg);
		else
			unloadedBuffer.add(()->warn(action, msg));
	}
	
	@Override
	public void out(String msg) {
		if (isLoaded())
			this.channel.out(msg);
		else
			unloadedBuffer.add(()->out(msg));
	}
	
}
