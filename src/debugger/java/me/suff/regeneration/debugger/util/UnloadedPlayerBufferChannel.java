package me.suff.regeneration.debugger.util;

import me.suff.regeneration.debugger.IDebugChannel;
import me.suff.regeneration.util.RegenState;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class UnloadedPlayerBufferChannel implements IDebugChannel {
	private final Queue<Consumer<IDebugChannel>> unloadedBuffer;
	private boolean flushed = false;
	
	public UnloadedPlayerBufferChannel() {
		this.unloadedBuffer = new LinkedList<>();
	}
	
	public void flush(IDebugChannel channel) {
		unloadedBuffer.forEach(bufEn -> bufEn.accept(channel));
		flushed = true;
	}
	
	@Override
	public void notifyCancel(RegenState.Transition action, long inTicks) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch -> ch.notifyCancel(action, inTicks));
	}
	
	@Override
	public void notifyExecution(RegenState.Transition action, long tick) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch -> ch.notifyExecution(action, tick));
	}
	
	@Override
	public void notifySchedule(RegenState.Transition action, long inTicks) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch -> ch.notifySchedule(action, inTicks));
	}
	
	@Override
	public void warn(RegenState.Transition action, String msg) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch -> ch.warn(action, msg));
	}
	
	@Override
	public void out(String msg) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch -> ch.out(msg));
	}
	
	@Override
	public void warn(String msg) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch -> ch.warn(msg));
	}
	
	@Override
	public void out(RegenState.Transition action, String msg) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch -> ch.out(msg));
	}
	
	@Override
	public void notifyLoaded() {
		throw new IllegalStateException("Notifying unloaded buffer that it's loaded");
	}
	
}
