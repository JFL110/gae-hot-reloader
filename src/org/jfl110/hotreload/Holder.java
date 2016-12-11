package org.jfl110.hotreload;

/**
 * Utility value holder
 *
 * @author JFL110
 */
class Holder<T> {

	private T value;
	
	Holder(T value){
		this.value = value;
	}
	
	T get(){
		return value;
	}
	
	void set(T value){
		this.value = value;
	}
}
