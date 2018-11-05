package me.fril.regeneration.common.events;

import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class RegenStateEvent extends Event {
	protected final IRegeneration capability;
	protected final EntityPlayer player;
	
	public RegenStateEvent(IRegeneration capability) {
		this.capability = capability;
		this.player = capability.getPlayer();
	}
	
	
	
	/*public static class RegenDeathEvent extends RegenBaseEvent {
		
		public RegenDeathEvent(IRegeneration capability) {
			super(capability);
			capability.onDeath();
			// STuB Auto-generated constructor stub
		}
		
	}
	
	
	public static class RegenGraceEvent {
		
		public static class Enter extends RegenBaseEvent {

			public Enter(IRegeneration capability) {
				super(capability);
				capability.onEnterGrace();
				//STuB Auto-generated constructor stub
			}
			
		}
		
		public static class Change extends RegenBaseEvent {
			
			public Change(IRegeneration capability) {
				super(capability);
				capability.onChangeGrace();
				//STuB Auto-generated constructor stub
			}
			
		}
		
	}
	
	
	public static class RegenTriggerRegenerationEvent extends RegenBaseEvent {
		
		public RegenTriggerRegenerationEvent(IRegeneration capability) {
			super(capability);
			capability.onTriggerRegen();
			
			if (capability.getState() == RegenState.REGENERATING || capability.getState() == RegenState.ALIVE)
				throw new IllegalStateException("Triggering regeneration while in state " + capability.getState());
		}
		
	}
	
	
	public static class RegenFinishRegenerationEvent extends RegenBaseEvent {
		
		public RegenFinishRegenerationEvent(IRegeneration capability) {
			super(capability);
			capability.onFinishRegen();
			// STuB Auto-generated constructor stub
		}
		
	}*/
	
}
