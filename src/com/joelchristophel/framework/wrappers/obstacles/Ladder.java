package com.joelchristophel.framework.wrappers.obstacles;

import java.util.NoSuchElementException;

import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;

import com.joelchristophel.framework.MethodContext;

public class Ladder extends Obstacle {

	private MethodContext ctx;
	private boolean up;
	private int upId;
	private int downId;

	public Ladder(MethodContext ctx, boolean up, int upId, int downId) {
		this.ctx = ctx;
		this.up = up;
		this.upId = upId;
		this.downId = downId;
	}

	@Override
	public void overcome() {
		GameObject ladder = getGameObject();

		System.out.println("Ladder is " + (ladder.isValid() ? "" : " not ") + "valid.");

		if (ladder.isValid()) {
			System.out.println("Ladder location: (" + ladder.getLocation().x + ", " + ladder.getLocation().y + ", "
					+ ladder.getLocation().plane + ")");
			System.out.println("Ladder has " + ladder.getActions().length + " actions.");
		}

		int startingPlane = ctx.players.local().getLocation().getPlane();

		String[] actions = ladder.getActions();

		while (ctx.players.local().isInMotion()) {
			ctx.utility.sleep(300);
		}
		ctx.utility.sleep(300);

		if (actions.length == 0) {
			ladder.click(false);

			for (String item : ctx.menu.getItems()) {
				String action = item.replace(ladder.getName(), "").trim();
				String lowercaseAction = action.toLowerCase();

				if ((lowercaseAction.contains("up") && up) || (lowercaseAction.contains("down") && !up)) {
					ctx.menu.click(action);
				}
			}
		} else {
			for (String action : ladder.getActions()) {
				String lowercaseAction = action.toLowerCase();

				if ((lowercaseAction.contains("up") && up) || (lowercaseAction.contains("down") && !up)) {
					ladder.interact(action);
					break;
				}
			}
		}

		Timer timer = new Timer(8000);

		while (startingPlane == ctx.players.local().getLocation().getPlane()) {
			if (!timer.isRunning()) {
				break;
			}

			ctx.utility.sleep(2000, 3000);
		}
	}

	@Override
	public Ladder reverse() {
		return new Ladder(ctx, !up, upId, downId);
	}

	@Override
	public GameObject getGameObject() {
		int id = up ? upId : downId;

		try {
			return ctx.objects.select().id(id).nearest().first().iterator().next();
		} catch (NoSuchElementException e) {
			return ctx.objects.getNil();
		}
	}
}