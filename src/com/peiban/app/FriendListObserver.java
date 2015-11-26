package com.peiban.app;

import java.util.Observable;

public class FriendListObserver extends Observable{
	private static FriendListObserver observable;
	static {
		observable = new FriendListObserver();
	}
	
	public static FriendListObserver getInstance(){
		return observable;
	}
	
	public static void notifyObserver(Object object){
		observable.setChanged();
		observable.notifyObservers(object);
		
	}
}
