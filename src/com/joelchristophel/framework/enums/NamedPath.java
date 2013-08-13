package com.joelchristophel.framework.enums;

import org.powerbot.script.wrappers.Tile;
import org.powerbot.script.wrappers.TilePath;

import com.joelchristophel.framework.MethodContext;
import com.joelchristophel.framework.MethodProvider;

public enum NamedPath {

	EXAMPLE(new Tile(0, 0, 0));

	MethodContext ctx = MethodProvider.ctx;
	TilePath path;
	Tile[] tiles;

	NamedPath(Tile... path) {
		this.path = new TilePath(ctx, path);
		this.tiles = path;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	public TilePath get() {
		return path;
	}
}