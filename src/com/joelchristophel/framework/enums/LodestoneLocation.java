package com.joelchristophel.framework.enums;

import org.powerbot.script.wrappers.Tile;

public enum LodestoneLocation {
	AL_KHARID(new Tile(0, 0, 0), 40), ARDOUGNE(new Tile(0, 0, 0), 41), BANDIT_CAMP(new Tile(0, 0, 0), 7), BURTHOPE(
			new Tile(0, 0, 0), 42), CANIFIS(new Tile(0, 0, 0), 53), CATHERBY(new Tile(0, 0, 0), 43), DRAYNOR_VILLAGE(
			new Tile(0, 0, 0), 44), EAGLES_PEAK(new Tile(0, 0, 0), 54), EDGEVILLE(new Tile(0, 0, 0), 45), FALADOR(
			new Tile(0, 0, 0), 46), FREMENICK_PROVINCE(new Tile(0, 0, 0), 55), KARAMJA(new Tile(0, 0, 0), 56), LUMBRIDGE(
			new Tile(3200, 3229, 0), 47), LUNAR_ISLE(new Tile(0, 0, 0), 39), OOGLOG(new Tile(0, 0, 0), 57), PORT_SARIM(
			new Tile(0, 0, 0), 48), SEERS_VILLAGE(new Tile(0, 0, 0), 49), TAVERLY(new Tile(0, 0, 0), 50), TIRANNWN(
			new Tile(0, 0, 0), 58), VARROCK(new Tile(0, 0, 0), 51), WILDERNESS_VOLCANO(new Tile(0, 0, 0), 59), YANILLE(
			new Tile(0, 0, 0), 52);

	private Tile teleportTile;
	private int widgetChildIndex;

	private LodestoneLocation(Tile teleportTile, int widgetChildIndex) {
		this.teleportTile = teleportTile;
		this.widgetChildIndex = widgetChildIndex;
	}
	
	public Tile getTeleportTile() {
		return teleportTile;
	}
	
	public int getWidgetChildIndex() {
		return widgetChildIndex;
	}
}