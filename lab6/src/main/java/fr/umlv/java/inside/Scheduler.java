package fr.umlv.java.inside;

import java.util.ArrayDeque;
import java.util.Objects; 
import java.util.Random;

public class Scheduler {
	
	public enum Strategy{
		FIFO, STACK, RANDOM
	}
	
	
	private Strategy exec;
	public static final ContinuationScope SCOPE_SCHEDULER = new ContinuationScope("publicScope");
	private ArrayDeque<Continuation> array;
	
	public Scheduler(Strategy exec) {
		this.exec = exec;
		array = new ArrayDeque<Continuation>();
	}
		
		
	public void enqueue(ContinuationScope cnt) {
		Objects.requireNonNull(Continuation.getCurrentContinuation(cnt));
		array.offer(Continuation.getCurrentContinuation(cnt));
		Continuation.yield((cnt));
	}
	
	public void runLoop() {
		while(!array.isEmpty()) {
			var elem = array.pollLast();
			elem.run();
		}
	}
}
