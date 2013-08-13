package com.joelchristophel.framework.wrappers.obstacles;

import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Locatable;
import org.powerbot.script.wrappers.Tile;

import com.joelchristophel.framework.MethodContext;

public class Door extends Obstacle implements Locatable {

	private MethodContext ctx;
	private int closedId;
	private Tile location;

	public Door(MethodContext ctx, int closedId, Tile location) {
		this.ctx = ctx;
		this.closedId = closedId;
		this.location = location;
	}

	@Override
	public void overcome() {
		GameObject door = getGameObject();

		while (ctx.players.local().isInMotion()) {
			ctx.utility.sleep(300);
		}

		ctx.utility.sleep(300);

		if (door.interact("Open")) {
			while (location.distanceTo(ctx.players.local().getLocation()) > 2 || ctx.players.local().isInMotion()) {
				ctx.utility.sleep(300);
			}

			ctx.utility.sleep(300);
		}
	}

	@Override
	public Door reverse() {
		return this;
	}

	@Override
	public GameObject getGameObject() {
		for (GameObject door : ctx.objects.select().id(closedId).nearest().limit(20)) {
			if (door.getLocation().distanceTo(location) < 2) {
				return door;
			}
		}

		return ctx.objects.getNil();
	}

	@Override
	public Tile getLocation() {
		return location;
	}
}