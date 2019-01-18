package me.fril.regeneration.debugger.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import me.fril.regeneration.debugger.IDebugChannel;
import me.fril.regeneration.util.RegenState.Transition;

public class UnloadedPlayerBufferChannel implements IDebugChannel {
	private final Queue<Consumer<IDebugChannel>> unloadedBuffer;
	private boolean flushed = false;
	
	public UnloadedPlayerBufferChannel() {
		this.unloadedBuffer = new LinkedList<>();
	}
	
	public void flush(IDebugChannel channel) {
		unloadedBuffer.forEach(bufEn->bufEn.accept(channel));
		flushed = true;
	}
	
	@Override
	public void notifyCancel(Transition action, long inTicks) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch->ch.notifyCancel(action, inTicks));
	}
	
	@Override
	public void notifyExecution(Transition action, long tick) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch->ch.notifyExecution(action, tick));
	}
	
	@Override
	public void notifySchedule(Transition action, long inTicks) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch->ch.notifySchedule(action, inTicks));
	}
	
	@Override
	public void warn(Transition action, String msg) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch->ch.warn(action, msg));
	}
	
	@Override
	public void out(String msg) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch->ch.out(msg));
	}
	
	@Override
	public void warn(String msg) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch->ch.warn(msg));
	}
	
	@Override
	public void out(Transition action, String msg) {
		if (flushed)
			throw new IllegalStateException("Unloaded player buffer has already been flushed");
		unloadedBuffer.add(ch->ch.out(msg));
	}
	
	@Override
	public void notifyLoaded() {
		throw new IllegalStateException("Notifying unloaded buffer that it's loaded");
	}
	
}
