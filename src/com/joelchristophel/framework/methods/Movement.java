package com.joelchristophel.framework.methods;

import java.util.ArrayList;

import javax.swing.SwingWorker;

import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.LocalPath;
import org.powerbot.script.wrappers.Locatable;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Tile;
import org.powerbot.script.wrappers.TileMatrix;

import com.joelchristophel.framework.MethodContext;
import com.joelchristophel.framework.enums.LodestoneLocation;
import com.joelchristophel.framework.wrappers.obstacles.*;

public class Movement extends org.powerbot.script.methods.Movement {

	private MethodContext ctx;
	private static final int CONSECUTIVE_STEPS = 9;
	private SwingWorker<Void, Void> cameraMover;
	private SwingWorker<Void, Void> movementListener;
	private volatile boolean cameraActive = false;
	private volatile ArrayList<Integer> steps = new ArrayList<Integer>(CONSECUTIVE_STEPS);

	public Movement(MethodContext ctx) {
		super(ctx);

		this.ctx = ctx;
	}

	public boolean canTraverse(Locatable[]... paths) {
		for (Locatable[] path : paths) {
			for (Locatable locatable : path) {
				TileMatrix matrix = locatable.getLocation().getMatrix(ctx);

				if (matrix.isReachable() && matrix.isOnMap()) {
					return true;
				}
			}
		}

		return false;
	}

	public Locatable[] reverse(Locatable[] forward) {
		Locatable[] backward = new Locatable[forward.length];

		for (int i = 0, j = forward.length - 1; i < forward.length; i++, j--) {
			backward[j] = forward[i] instanceof Obstacle ? ((Obstacle) forward[i]).reverse() : forward[i];
		}

		return backward;
	}

	public Locatable[][] reverse(Locatable[]... forward) {
		Locatable[][] backward = new Locatable[forward.length][];

		for (int i = 0, j = forward.length - 1; i < forward.length; i++, j--) {
			backward[j] = reverse(forward[i]);
		}

		return backward;
	}

	private Locatable[] getSection(Locatable[]... paths) {
		int currentPlane = ctx.players.local().getLocation().plane;
		int pathPosition = 0, locatablePosition = 0;

		outer: for (pathPosition = paths.length - 1; pathPosition >= 0; pathPosition--) {
			for (locatablePosition = paths[pathPosition].length - 1; locatablePosition >= 0; locatablePosition--) {
				TileMatrix matrix = paths[pathPosition][locatablePosition].getLocation().getMatrix(ctx);

				if (matrix.isOnMap() && matrix.getLocation().plane == currentPlane) {
					break outer;
				}
			}
		}

		Locatable[] path = new Locatable[paths[pathPosition].length - locatablePosition];

		for (int i = 0; i < path.length; i++, locatablePosition++) {
			path[i] = paths[pathPosition][locatablePosition];
		}

		return path;
	}

	private Locatable[] getNextPath(Locatable[][] paths, Locatable[] currentPath) {
		if (paths.length == 1) {
			return null;
		}

		int currentPathIndex = -1;

		for (int i = 0; i < paths.length; i++) {
			boolean allMatch = true;

			for (int k = 0; k < currentPath.length; k++) {
				boolean oneMatches = false;

				for (int j = 0; j < paths[i].length; j++) {

					if (paths[i][j].getLocation().equals(currentPath[k].getLocation())) {
						oneMatches = true;
						break;
					}
				}

				if (!oneMatches) {
					allMatch = false;
					break;
				}
			}

			if (allMatch) {
				currentPathIndex = i;
				break;
			}
		}

		return currentPathIndex == -1 || currentPathIndex + 1 >= paths.length ? null : paths[currentPathIndex + 1];
	}

	private double getDistance(Tile tile1, Tile tile2) {
		return tile1.distanceTo(new Tile(tile2.x, tile2.y, tile1.plane));
	}

	public void traverse(Locatable[]... paths) {
		Tile lastTile = paths[paths.length - 1][paths[paths.length - 1].length - 1].getLocation();

		while (getDistance(ctx.players.local().getLocation(), lastTile) > 2) {
			Locatable[] path = getSection(paths);

			currentPath: for (int i = 0; i < path.length; i++) {
				Locatable[] nextPath = getNextPath(paths, path);

				if (nextPath != null && canTraverse(nextPath)) {
					break;
				}
				
				Tile tile = path[i].getLocation();
				TileMatrix matrix = tile.getMatrix(ctx);

				if (matrix.isOnMap()) {
					if (ctx.utility.scriptIsStopped()) {
						return;
					}
					
					while (ctx.utility.scriptIsPaused()) {
						sleep(500);
					}

					if (path[i] instanceof Obstacle) {
						if (!((Obstacle) path[i]).isPresent()
								|| (i >= path.length && !path[i + 1].getLocation().getMatrix(ctx).isOnScreen())) {
							if (ctx.camera.isMoving()) {
								sleep(300);
							}

							ctx.movement.stepTowards(tile);
						}
						((Obstacle) path[i]).overcome();
					} else {
						ctx.camera.waitUntilStill();
						ctx.movement.stepTowards(tile);
					}

					Tile currentLocation = ctx.players.local().getLocation();
					Timer timer = new Timer(4000);
					
					while (!(path[i] instanceof Ladder) && currentLocation.distanceTo(tile) > 5) {
						if (!timer.isRunning()) {
							break currentPath;
						}

						sleep(500);
						currentLocation = ctx.players.local().getLocation();
					}
				}
			}
		}

		while (ctx.players.local().isInMotion()) {
			sleep(500);
		}
	}

	public void traverse(Locatable[][] mainPath, LodestoneLocation location, Locatable[][] toMainPath) {
		if (canTraverse(mainPath)) {
			traverse(mainPath);
		} else {
			ctx.lodestone.teleport(location);

			if (toMainPath != null) {

				Locatable[][] fullPath = new Locatable[toMainPath.length + mainPath.length][];
				int fullPathCounter = 0;

				for (int i = 0; i < toMainPath.length; i++) {
					fullPath[fullPathCounter] = toMainPath[i];
					fullPathCounter++;
				}

				for (int i = 0; i < mainPath.length; i++) {
					fullPath[fullPathCounter] = mainPath[i];
					fullPathCounter++;
				}

				traverse(fullPath);
			} else {
				traverse(mainPath);
			}
		}
	}

	public void traverse(LocalPath path) {
		while (path.traverse()) {
			sleep(500, 600);
		}

		Player player = ctx.players.local();

		while (player.isInMotion()) {
			sleep(500, 600);
		}
	}
	
	public void startCamera() {
		if (!cameraActive) {

			cameraActive = true;

			movementListener = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					while (true) {
						int newStep = -1;

						while (steps.size() != CONSECUTIVE_STEPS) {
							steps.add(newStep == -1 ? getMovementDirection() : newStep);
							newStep = -1;
						}

						while (cameraActive) {
							if (steps.size() != CONSECUTIVE_STEPS) {
								break;
							} else {
								newStep = getMovementDirection();

								if (steps.size() == CONSECUTIVE_STEPS) {
									steps.remove(0);
									steps.add(newStep);
								}
							}
						}

						if (!cameraActive) {
							return null;
						}
					}
				}
			};

			cameraMover = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					while (true) {
						if (isCancelled()) {
							return null;
						}

						Integer direction = getNotableMovementDirection();
						steps = new ArrayList<Integer>(CONSECUTIVE_STEPS);

						if (!isCancelled() && !ctx.camera.isMoving()) {
							ctx.camera.setAbsolutePosition(direction, 90, true);
						}
					}
				}
			};
		}

		movementListener.execute();
		cameraMover.execute();
	}

	public void stopCamera() {
		if (cameraActive) {
			if (cameraMover != null) {
				cameraMover.cancel(true);
			}

			if (movementListener != null) {
				movementListener.cancel(true);
			}

			cameraActive = false;
		}
	}

	private Integer getNotableMovementDirection() {
		if (!cameraActive) {
			return -1;
		}

		Integer[] direction = steps.toArray(new Integer[steps.size()]);

		while (direction.length < CONSECUTIVE_STEPS) {
			if (!cameraActive) {
				return -1;
			}

			sleep(200);

			direction = steps.toArray(new Integer[steps.size()]);

		}

		int min = -1;
		int max = -1;

		for (int i = 0; i < direction.length; i++) {
			if (i == 0) {
				min = direction[0];
				max = direction[0];
			} else {
				if (direction[i] > max) {
					max = direction[i];
				} else if (direction[i] < min) {
					min = direction[i];
				}
			}
		}

		boolean specialCase = true;
		boolean north = false;
		boolean northeast = false;

		for (int index : direction) {
			if (index == 1) {
				north = true;
			} else if (index == 315) {
				northeast = true;
			} else {
				specialCase = false;
				break;
			}
		}

		if (specialCase) {
			specialCase = north && northeast;
		}

		if (max - min > 45 && !specialCase) {
			sleep(200);
			return getNotableMovementDirection();
		}

		if (specialCase) {
			for (int i = 0; i < direction.length; i++) {
				if (direction[i] == 1) {
					direction[i] = 360;
				}
			}
		}

		int sum = 0;

		for (int index : direction) {
			sum += index;
		}

		int average = sum / direction.length;

		if (Math.abs(ctx.camera.getAngleTo(average)) >= 45) {
			return average;
		}

		sleep(200);
		return getNotableMovementDirection();
	}

	private int getMovementDirection() {
		Tile tile1 = ctx.players.local().getLocation();

		while (ctx.players.local().getLocation().equals(tile1) && cameraActive) {
			sleep(100);
		}

		return getOrientationYaw();
	}

	private int getOrientationYaw() {
		int orientation = ctx.players.local().getOrientation();
		int yaw = orientation + 270;

		if (yaw > 358) {
			yaw -= 359;
		} else if (yaw < 1) {
			yaw += 359;
		}

		return yaw;
	}
}