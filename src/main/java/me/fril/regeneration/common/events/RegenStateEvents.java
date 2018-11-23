package me.fril.regeneration.common.events;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.RegenState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class RegenStateEvents {
	
	protected static class RegenStateBaseEvent extends Event {
		public final IRegeneration capability;
		public final EntityPlayer player;
		
		public RegenStateBaseEvent(IRegeneration capability) {
			this.capability = capability;
			this.player = capability.getPlayer();
		}
		
	}
	
	
	
	public static class RegenEnterGraceEvent extends RegenStateBaseEvent {
		
		public RegenEnterGraceEvent(IRegeneration capability) {
			super(capability);
		}
		
	}
	
	public static class RegenTickEvent extends RegenStateBaseEvent {
		public final double stateProgress;
		public final RegenState state;
		
		public RegenTickEvent(IRegeneration capability) {
			super(capability);
			state = capability.getState();
			stateProgress = capability.getStateManager().getStateProgress();
		}
		
	}
	
	public static class RegenGoCriticalEvent extends RegenStateBaseEvent {
		
		public RegenGoCriticalEvent(IRegeneration capability) {
			super(capability);
		}
		
	}

	public static class RegenTriggerEvent extends RegenStateBaseEvent {
		
		public RegenTriggerEvent(IRegeneration capability) {
			super(capability);
		}
		
	}
	
	public static class RegenFinishEvent extends RegenStateBaseEvent {
		
		public RegenFinishEvent(IRegeneration capability) {
			super(capability);
		}
		
	}
	
}
