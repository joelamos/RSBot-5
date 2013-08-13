package com.joelchristophel.framework.methods;

import org.powerbot.script.methods.MethodContext;

public class Menu extends org.powerbot.script.methods.Menu {

	public Menu(MethodContext ctx) {
		super(ctx);
	}

	public void waitUntilOpen() {
		while (this.isOpen()) {
			sleep(100);
		}
	}
}