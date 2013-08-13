package com.joelchristophel.framework.methods;

import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Tile;
import org.powerbot.script.wrappers.TileMatrix;

import com.joelchristophel.framework.MethodContext;
import com.joelchristophel.framework.MethodProvider;
import com.joelchristophel.framework.enums.LodestoneLocation;
import com.joelchristophel.framework.enums.NamedComponent;

public class Lodestone extends MethodProvider {

	public Lodestone(MethodContext ctx) {
		super(ctx);
	}

	public void openNetwork() {
		if (!networkOpen()) {
			NamedComponent.HOME_TELEPORT_BUTTON.get(true).click();
		}

		NamedComponent.LODESTONE_NETWORK.get(true); // wait until network is open
	}

	public boolean networkOpen() {
		if (NamedComponent.LODESTONE_NETWORK.get(false).isOnScreen()) {
			return true;
		}

		return false;
	}

	public boolean activated(LodestoneLocation location) {
		if (location == LodestoneLocation.LUMBRIDGE || location == LodestoneLocation.BURTHOPE) {
			return true;
		}

		openNetwork();
		getNetworkComponent(location, true).hover();
		return NamedComponent.LODESTONE_ACTIVE_MESSAGE.get(true).getText().contains("Click to teleport") ? true : false;
	}

	public boolean teleport(LodestoneLocation location) {
		Tile tile = location.getTeleportTile();
		TileMatrix matrix = tile.getMatrix(ctx);

		if (matrix.isReachable() && matrix.isOnMap()) { // walk to lodestone if on map
			ctx.movement.stepTowards(tile);

			waitUntilArrived(location, false);

			return true;
		}

		openNetwork();

		if (activated(location)) {
			getNetworkComponent(location, true).click(true);

			waitUntilArrived(location, true);

			return true;
		}

		NamedComponent.CLOSE_LODESTONE_NETWORK_BUTTON.get(true).click(true);

		while (NamedComponent.CLOSE_LODESTONE_NETWORK_BUTTON.get(false).isOnScreen()) {
			sleep(500);
		}

		return false;

	}

	public void waitUntilArrived(LodestoneLocation location, boolean teleported) {
		Tile tile = location.getTeleportTile();
		Player player = ctx.players.local();

		while ((teleported && !player.getLocation().equals(tile)) || (!teleported && tile.distanceTo(player) > 4)) {
			sleep(1000);
		}
	}

	public Component getNetworkComponent(LodestoneLocation location, boolean wait) {
		return ctx.widgets.get(1092, location.getWidgetChildIndex(), wait);
	}
}