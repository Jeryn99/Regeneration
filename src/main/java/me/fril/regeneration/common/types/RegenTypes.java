package me.fril.regeneration.common.types;

/**
 * Created by Sub
 * on 25/09/2018.
 */
public class RegenTypes {
	
	public static IRegenType FIERY, LAYDOWN;
	
	public static void init() {
		FIERY = new TypeFiery();
		LAYDOWN = new TypeLayFade();
	}
	
}
