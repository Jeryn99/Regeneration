package me.fril.regeneration.debugger.util;

import me.fril.regeneration.debugger.IDebugChannel;
import me.fril.regeneration.util.RegenState.Transition;

import java.util.function.Supplier;

public class ConditionalDebugChannelProxy implements IDebugChannel {

	private final IDebugChannel target;
	private final Supplier<Boolean> condition;

	public ConditionalDebugChannelProxy(IDebugChannel target, Supplier<Boolean> condition) {
		this.target = target;
		this.condition = condition;
	}

	@Override
	public void notifyCancel(Transition action, long wasInTicks) {
		if (condition.get())
			target.notifyCancel(action, wasInTicks);
	}

	@Override
	public void notifyExecution(Transition action, long atTick) {
		if (condition.get())
			target.notifyExecution(action, atTick);
	}

	@Override
	public void notifySchedule(Transition action, long inTicks) {
		if (condition.get())
			target.notifySchedule(action, inTicks);
	}

	@Override
	public void warn(Transition action, String msg) {
		if (condition.get())
			target.warn(action, msg);
	}

	@Override
	public void warn(String msg) {
		if (condition.get())
			target.warn(msg);
	}

	@Override
	public void out(Transition action, String msg) {
		if (condition.get())
			target.out(action, msg);
	}

	@Override
	public void out(String msg) {
		if (condition.get())
			target.out(msg);
	}

	@Override
	public void notifyLoaded() {
		if (condition.get())
			target.notifyLoaded();
	}

}
