package com.joelchristophel.framework;

import com.joelchristophel.framework.methods.Camera;
import com.joelchristophel.framework.methods.Chat;
import com.joelchristophel.framework.methods.Interactable;
import com.joelchristophel.framework.methods.Backpack;
import com.joelchristophel.framework.methods.Lodestone;
import com.joelchristophel.framework.methods.Menu;
import com.joelchristophel.framework.methods.Movement;
import com.joelchristophel.framework.methods.Utility;
import com.joelchristophel.framework.methods.Widgets;

public class MethodContext extends org.powerbot.script.methods.MethodContext {

	public Camera camera;
	public Chat chat;
	public Interactable interactable;
	public Backpack inventory;
	public Movement movement;
	public Lodestone lodestone;
	public Menu menu;
	public Utility utility;
	public Widgets widgets;

	public MethodContext(org.powerbot.script.methods.MethodContext ctx) {
		super(ctx.getBot());
	}

	@Override
	public void init(org.powerbot.script.methods.MethodContext ctx) {
		super.init(ctx);

		camera = new Camera(this);
		widgets = new Widgets(this); // must come before chat because a Chat field uses ctx.widgets
		chat = new Chat(this);
		interactable = new Interactable(this);
		inventory = new Backpack(this);
		lodestone = new Lodestone(this);
		menu = new Menu(this);
		utility = new Utility(this);
		movement = new Movement(this);
	}
}