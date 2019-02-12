package me.suff.regeneration.util;

public interface IEnum<E extends Enum<E>> {
	
	int ordinal();
	
	default E next() {
		E[] ies = this.getAllValues();
		return this.ordinal() != ies.length - 1 ? ies[this.ordinal() + 1] : null;
	}
	
	default E previous() {
		return this.ordinal() != 0 ? this.getAllValues()[this.ordinal() - 1] : null;
	}
	
	@SuppressWarnings("unchecked")
	default E[] getAllValues() {
		IEnum[] ies = this.getClass().getEnumConstants();
		return (E[]) ies;
	}
}
