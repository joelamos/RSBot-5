package com.joelchristophel.framework.enums;

import com.joelchristophel.framework.MethodContext;
import com.joelchristophel.framework.MethodProvider;

public enum Quest {

	THE_RESTLESS_GHOST(2324, 5, 1, 1);
	
	private int index;
	private int steps;
	private int questPoints;
	private int spaceNeeded;
	MethodContext ctx = MethodProvider.ctx;

	private Quest(int index, int steps, int questPoints, int spaceNeeded) {
		this.index = index;
		this.steps = steps;
		this.questPoints = questPoints;
		this.spaceNeeded = spaceNeeded;
	}

	public int getIndex() {
		return index;
	}

	public int getSteps() {
		return steps;
	}

	public int getQuestPoints() {
		return questPoints;
	}

	public int getSpaceNeeded() {
		return spaceNeeded;
	}

	public int getStep() {
		return ctx.settings.get(getIndex());
	}
	
	public boolean isComplete() {
		return ctx.settings.get(getIndex()) >= getSteps();
	}

	public boolean isAtStep(int step) {
		return getStep() == step;
	}
}