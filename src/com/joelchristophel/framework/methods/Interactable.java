package com.joelchristophel.framework.methods;

import java.util.logging.Logger;

import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Interactive;
import org.powerbot.script.wrappers.Npc;

import com.joelchristophel.framework.MethodContext;
import com.joelchristophel.framework.MethodProvider;

public class Interactable extends MethodProvider {

	public Logger log = Logger.getLogger(this.getClass().getSimpleName());

	public Interactable(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Checks for presence of a GameObject of ID idCheck. If it is present, click it. Lastly, wait until a GameObject of
	 * ID idWait is present.
	 * 
	 * @param checkName
	 *      - the name of the GameObject to check for
	 * @param idCheck
	 *      - the ID of a GameObject to check for and click if present
	 * @param idWait
	 *      - the ID of a GameObject to wait upon before the method is exited
	 */
	public void checkClickWait(String checkName, int idCheck, int idWait) {
		GameObject check = getNearestObject(idCheck);

		if (check.isOnScreen()) {
			check.interact(check.getActions()[0]);
		}

		Timer timer = new Timer(5000);

		while (getNearestObject(idWait).getId() != idWait) {
			if (!timer.isRunning()) {
				log.severe("Scrip error: Could not find GameObject: " + idWait);
			}

			sleep(200);
		}

	}

	/**
	 * Returns the nearest Interactive with the given ID
	 * 
	 * @param id
	 *      - the ID of the Interactive
	 * @return the requested Interactive
	 */
	public Interactive getNearestInteractive(int id) {
		if (getNearestNpc(id).getLocation().x != -1) {
			return getNearestNpc(id);
		} else if (getNearestObject(id).getLocation().x != -1) {
			return getNearestObject(id);
		}

		return ctx.objects.getNil();
	}

	/**
	 * Returns the nearest Npc with the given name
	 * 
	 * @param name
	 *      - the name of the Npc
	 * @return the requested Npc
	 */
	public Npc getNearestNpc(String name) {
		for (Npc npc : ctx.npcs.select().name(name).nearest().first()) {
			return npc;
		}

		return ctx.npcs.getNil();
	}

	/**
	 * Returns the nearest Npc with the given ID
	 * 
	 * @param id
	 *      - the ID of the Npc
	 * @return the requested Npc
	 */
	public Npc getNearestNpc(int id) {
		for (Npc npc : ctx.npcs.select().id(id).nearest().first()) {
			return npc;
		}

		return ctx.npcs.getNil();
	}

	/**
	 * Returns the nearest GameObject with the given object ID
	 * 
	 * @param id
	 *      - the ID of the GameObject
	 * @return the requested GameObject
	 */
	public GameObject getNearestObject(int id) {
		for (GameObject gameObject : ctx.objects.select().id(id).nearest().first()) {
			return gameObject;
		}

		return ctx.objects.getNil();
	}

	/**
	 * Interacts with interactive using its first action. This method is a reflection patch until getActions() is added
	 * to the Interactive class.
	 * 
	 * @param interactive
	 *            - the Interactive object with which to interact
	 */
	public void interact(Interactive interactive) {
		try {
			interactive
					.interact(((String[]) interactive.getClass().getDeclaredMethod("getActions").invoke(interactive))[0]);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}