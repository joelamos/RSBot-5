package com.joelchristophel.framework.methods;

import org.powerbot.script.methods.Game;

import com.joelchristophel.framework.MethodContext;
import com.joelchristophel.framework.MethodProvider;

public class Utility extends MethodProvider {

	private boolean scriptPaused = false;
	private boolean scriptStopped = false;

	public Utility(MethodContext ctx) {
		super(ctx);
	}

	public void waitUntilGameIsLoggedIn() {
		boolean alreadyLoggedIn = true;

		while (ctx.game.getClientState() != Game.INDEX_MAP_LOADED) {
			sleep(1000);
			alreadyLoggedIn = false;
		}

		if (!alreadyLoggedIn) {
			sleep(6500);
		}
	}

	public boolean scriptIsPaused() {
		return scriptPaused;
	}

	public boolean scriptIsStopped() {
		return scriptStopped;
	}

	public void setScriptPaused(boolean scriptPaused) {
		this.scriptPaused = scriptPaused;
	}

	public void setScriptStopped(boolean scriptStopped) {
		this.scriptStopped = scriptStopped;
	}
}