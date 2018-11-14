package me.fril.regeneration.common.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.events.RegenStateEvents.RegenStateBaseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This is unfortunately not as <i>generic</i> as I'd like it to be, but that's because the forge event bus doesn't handle <i>generics</i> very well.
 * This is in turn caused by the all-great phenomenon of "type-erasure".
 * 
 * @author HoldYourWaffle
 */
public class SidedEventHandlerProxy {
	private final Map<Class<?>, Method> clientMethodCache, serverMethodCache;
	private final Predicate<RegenStateBaseEvent> serverTest;
	
	public SidedEventHandlerProxy(Predicate<RegenStateBaseEvent> serverTest, Class<?> clientHandlerClazz, Class<?> serverHandlerClazz) {
		this.clientMethodCache = buildCache(clientHandlerClazz);
		this.serverMethodCache = buildCache(serverHandlerClazz);
		
		this.serverTest = serverTest;
	}
	
	@SubscribeEvent
	public void onEvent(RegenStateBaseEvent ev) throws IllegalAccessException, InvocationTargetException { //XXX debug messages
		Map<Class<?>, Method> cache = serverTest.test(ev) ? serverMethodCache : clientMethodCache;
		
		if (cache.containsKey(ev.getClass())) {
			cache.get(ev.getClass()).invoke(null, ev);
		} else RegenerationMod.DEBUGGER.getChannelFor(ev.player).warn("No handler for "+ev.getClass().getSimpleName()+" on "+(serverTest.test(ev) ? "server" : "client"));
	}
	
	
	
	private Map<Class<?>, Method> buildCache(Class<?> handlerClass) {
		Map<Class<?>, Method> cache = new HashMap<>();
		
		for (Method m : handlerClass.getMethods()) {
			if (!m.isAnnotationPresent(SubscribeEvent.class) || !Modifier.isStatic(m.getModifiers()))
				continue;
			
			if (m.getParameterCount() != 1)
				throw new IllegalStateException("SubscribeEvent method has != 1 parameter "+handlerClass.getSimpleName()+"#"+m.getName());
			
			if (cache.containsKey(m.getParameterTypes()[0]))
				throw new IllegalStateException("Duplicate event handler for "+m.getParameterTypes()[0].getSimpleName()+" in "+handlerClass.getSimpleName());
			
			System.out.println("Putting on "+handlerClass.getSimpleName()+":"+m.getParameterTypes()[0].getSimpleName()+": "+m.getName());
			cache.put(m.getParameterTypes()[0], m);
		}
		
		return cache;
	}
	
}
