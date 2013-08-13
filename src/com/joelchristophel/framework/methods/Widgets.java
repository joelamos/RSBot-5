package com.joelchristophel.framework.methods;

import java.awt.Point;
import java.util.logging.Logger;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Component;

public class Widgets extends org.powerbot.script.methods.Widgets {

	public Logger log = Logger.getLogger(this.getClass().getSimpleName());

	public Widgets(MethodContext ctx) {
		super(ctx);
	}

	private static int componentTimeoutPeriod = 5000;

	public Component get(final int id1, final int id2, boolean wait) {

		int[] intArray = new int[] { id1, id2 };

		return wait ? waitUntilComponentIsVisible(intArray) : getChild(intArray);
	}

	public Component get(final int id1, final int id2, final int id3, boolean wait) {
		int[] intArray = new int[] { id1, id2, id3 };
		return wait ? waitUntilComponentIsVisible(intArray) : getChild(intArray);
	}

	public Component waitUntilOneOfTheComponentsIsVisible(Component[] components, String componentDescription) {
		boolean printedConsoleMessage = false;
		Timer timer = new Timer(componentTimeoutPeriod);

		while (true) {
			for (Component component : components) {
				if (component.isVisible()) {
					return component;
				} else if (!timer.isRunning() && !printedConsoleMessage) {
					log.severe("Script error: could not locate a " + componentDescription + " Component");

					printedConsoleMessage = true;
				}
			}

			sleep(200);
		}
	}

	public void waitUntilComponentIsVisible(Component component) {
		boolean printedConsoleMessage = false;
		Timer timer = new Timer(componentTimeoutPeriod);

		while (!component.isVisible()) {
			if (!timer.isRunning() && !printedConsoleMessage) {
				log.severe("Script error: could not locate Component of ID: "
						+ (component.getParent().getParent() == null ? component.getParent().getIndex() + ", "
								+ component.getIndex() : component.getParent().getParent().getIndex() + ", "
								+ component.getParent().getIndex() + ", " + component.getIndex()));

				printedConsoleMessage = true;
			}

			sleep(200);
		}
	}

	public boolean oneOfTheComponentsIsVisible(Component[] children) {
		for (Component child : children) {
			if (child.isVisible()) {
				return true;
			}
		}

		return false;
	}

	public void clickPointInsideComponent(Component child) {
		clickPointInsideComponent(child, true);
	}
	
	public void clickPointInsideComponent(Component child, boolean leftClick) {
		Point point = child.getAbsoluteLocation();
		int x = point.x;
		int y = point.y;

		ctx.mouse.click(new Point(Random.nextInt(x, x + child.getWidth()), Random.nextInt(y, y + child.getHeight())),
				leftClick);
	}

	private Component waitUntilComponentIsVisible(int[] componentsIDs) {
		boolean printedConsoleMessage = false;
		Timer timer = new Timer(componentTimeoutPeriod);
		int arrayLength = componentsIDs.length;

		Component component = getChild(componentsIDs);

		while (!component.isVisible()) {
			component = getChild(componentsIDs);

			sleep(400);

			if (!timer.isRunning() && !printedConsoleMessage) {
				String logMessage = "Script error: could not locate Component: ";

				for (int i = 0; i < arrayLength; i++) {
					logMessage += (i == arrayLength - 1 ? componentsIDs[i] + "\n" : componentsIDs[i] + ", ");
				}

				log.severe(logMessage);

				printedConsoleMessage = true;
			}
		}

		return (Component) component;
	}

	private Component getChild(int[] componentIDs) {
		Component component = get(componentIDs[0], componentIDs[1]);
		return componentIDs.length == 3 ? component.getChild(componentIDs[2]) : component;
	}
}