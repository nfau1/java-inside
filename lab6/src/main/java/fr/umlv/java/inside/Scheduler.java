package fr.umlv.java.inside;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects; 
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class Scheduler {
	
	public enum Strategy{
		FIFO, STACK, RANDOM
	}
	
	
	private final Supplier<Continuation> getFunc;
	public static final ContinuationScope SCOPE_SCHEDULER = new ContinuationScope("publicScope");
	private Collection<Continuation> array;
	
	public Scheduler(Strategy exec) {
		switch(exec) {
		case FIFO : 
			array = new ArrayDeque<Continuation>();
			getFunc = () -> ((ArrayDeque<Continuation>)array).pollFirst();
		break;	
		case STACK : 
			array = new ArrayDeque<Continuation>();
			getFunc = () -> ((ArrayDeque<Continuation>)array).pollLast();
		break;	
		case RANDOM : 
			array = new ArrayDeque<Continuation>();
			getFunc = () -> ((ArrayList<Continuation>)array).get(ThreadLocalRandom.current().nextInt(0, array.size()));
		break;		
		default : 
			getFunc = () -> new Continuation(null, null);
		}
	}
		
		
	public void enqueue(ContinuationScope cnt) {
		Objects.requireNonNull(Continuation.getCurrentContinuation(cnt));
		array.add(Continuation.getCurrentContinuation(cnt));
		Continuation.yield((cnt));
	}
	
	public void runLoop() {
		while(!array.isEmpty())
			getFunc.get().run();
	}
}
