package com.joelchristophel.framework.script;

import com.joelchristophel.framework.MethodContext;

 
// Source: https://gist.github.com/Strikegently/5806881#file-job-java
 
public abstract class Job {
	
	public MethodContext ctx;
	public boolean variablesInitialized = false;
	
    public Job(MethodContext ctx) {
        this.ctx = ctx;
    }
 
    /* override this to extend the sleep time */
    public int delay() {
        return 250;
    }
 
    /* returns the priority of the job. higher priority = executed first */
    public int priority() {
        return 0;
    }
 
    public void initializeVariables() {}
    
    public abstract String getDescription();
    
    public abstract boolean activate();
 
    public abstract void execute();
}
