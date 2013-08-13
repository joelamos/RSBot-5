package com.joelchristophel.framework.script;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.util.Random;

import com.joelchristophel.framework.MethodContext;
 
public abstract class CustomScript extends PollingScript implements Script {
	
	protected MethodContext ctx = new MethodContext(super.ctx);
	
    public void init() {} // override for 'onStart'
 
    public abstract void execute(); // your 'loop'
 
    public void finish() {} // override for 'onFinish'
	
	@Override
	public void setContext(org.powerbot.script.methods.MethodContext mc) {
		this.ctx.init(mc);
	}
    
    @Override
    public void suspend() {
    	ctx.utility.setScriptPaused(true);
    }
    
    @Override
    public void resume() {
    	ctx.utility.setScriptPaused(false);
    }
    
    @Override
    public void stop() {
		ctx.movement.stopCamera();
    	ctx.utility.setScriptStopped(true);
    }
    
    public CustomScript() {
	//	this.ctx = new MethodContext(super.ctx);
    	
        getExecQueue(State.START).add(new Runnable() {
            public void run() {
                init(); // code on start
            }
        });
        getExecQueue(State.STOP).add(new Runnable() {
            public void run() {
                finish(); // code on stop
            }
        });
    }
 
    @Override
    public int poll() { // runs once
        execute();
        return Random.nextInt(50, 75);
    }
}