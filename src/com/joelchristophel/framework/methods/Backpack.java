package com.joelchristophel.framework.methods;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Item;

public class Backpack extends org.powerbot.script.methods.Backpack {

	public Backpack(MethodContext arg0) {
		super(arg0);
	}

	public boolean contains(int id) {
		return select().id(id).isEmpty() ? false : true;
	}

	public Item getItem(int id) {
		for (Item item : select().id(id).first()) {
			return item;
		}
		
		return ctx.backpack.getNil();
	}
	
	public void selectItem(int id) {
		for (Item item : select().id(id).first()){
			item.interact("Select");
		}
	}
}
