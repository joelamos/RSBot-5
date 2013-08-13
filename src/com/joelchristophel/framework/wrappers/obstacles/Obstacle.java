package com.joelchristophel.framework.wrappers.obstacles;

import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Locatable;
import org.powerbot.script.wrappers.Tile;

public abstract class Obstacle implements Locatable {
	
	public abstract void overcome();

	public abstract Obstacle reverse();

	public abstract GameObject getGameObject();
	
	public boolean isPresent() {
		return getGameObject().getId() == -1 ? false : true;
	}
	
	@Override
	public Tile getLocation() {
		return getGameObject().getLocation();
	}
}