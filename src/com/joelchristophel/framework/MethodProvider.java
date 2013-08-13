package com.joelchristophel.framework;

import java.util.logging.Logger;

public class MethodProvider extends org.powerbot.script.methods.MethodProvider {

	public static MethodContext ctx;
	public Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	public MethodProvider(MethodContext ctx) {
		super(ctx);
		
		MethodProvider.ctx = ctx;
	}
}