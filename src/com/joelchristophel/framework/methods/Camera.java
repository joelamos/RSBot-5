package com.joelchristophel.framework.methods;

import org.powerbot.script.wrappers.Locatable;

import com.joelchristophel.framework.MethodContext;

public class Camera extends org.powerbot.script.methods.Camera {

	private boolean movingVertically = false;
	private boolean movingHorizontally = false;

	public Camera(MethodContext ctx) {
		super(ctx);
	}

	/**
	 * Concurrently sets camera angle and pitch
	 * 
	 * @param angle
	 *            - the camera angle to be set. Can be a value from 0 - 360.
	 * @param pitch
	 *            - the camera pitch to be set. Can be a value from 0 - 97.
	 */
	public synchronized void setAbsolutePosition(final int angle, final int pitch, boolean waitForFinish) {
		if (!movingVertically) {
			new Thread(new Runnable() {
				public void run() {
					movingHorizontally = true;
					setAngle(angle);
					movingHorizontally = false;
				}
			}).start();
		}
		if (!movingHorizontally) {
			new Thread(new Runnable() {
				public void run() {
					movingVertically = true;
					setPitch(pitch);
					movingVertically = false;
				}
			}).start();
		}

		if (waitForFinish) {
			waitUntilStill();
		}
	}

	public boolean isMoving() {
		if (movingVertically || movingHorizontally) {
			return true;
		}

		int yaw = getYaw();
		int pitch = getPitch();

		sleep(100, 200);

		if (yaw == getYaw() && pitch == getPitch()) {
			return false;
		}

		return true;

	}

	public void waitUntilStill() {
		while (true) {
			if (!isMoving()) {
				return;
			}
		}
	}

	public void turnTo(int... ids) {
		if (!ctx.objects.select().id(ids).nearest().first().isEmpty()) {
			turnTo(ctx.objects.iterator().next());
		}
	}

	@Override
	public void turnTo(Locatable locatable) {
		movingHorizontally = true;
		super.turnTo(locatable);
		movingHorizontally = false;
	}

	@Override
	public void setAngle(int angle) {
		movingHorizontally = true;
		super.setAngle(angle);
		movingHorizontally = false;
	}
	
	public void setPitch(int pitch) {
	}

	public int getPitch() {
		return 0;
	}
}