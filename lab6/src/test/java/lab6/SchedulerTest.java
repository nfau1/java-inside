package lab6;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import fr.umlv.java.inside.Scheduler;

class SchedulerTest {

	@Tag("Fifo scheduler")
	@Test
	public void fifoTest() {
		var fifo = new Scheduler(Scheduler.Strategy.FIFO);
		var scope = new ContinuationScope("scope");
		var strb= new StringBuilder();
		var continuation1 = new Continuation(scope, () -> {
			strb.append("start 1\n");
			fifo.enqueue(scope);
			strb.append("middle 1\n");
			fifo.enqueue(scope);
			strb.append("end 1\n");
		});
		var continuation2 = new Continuation(scope, () -> {
			strb.append("start 2\n");
			fifo.enqueue(scope);
			strb.append("middle 2\n");
			fifo.enqueue(scope);
			strb.append("end 2\n");
		});
		
		var response = new StringBuilder();
		response.
				append("start 1\n").
				append("start 2\n").
				append("middle 1\n").
				append("middle 2\n").
				append("end 1\n").
				append("end 2\n");
		
		var list = List.of(continuation1, continuation2);
	    list.forEach(Continuation::run);
	    fifo.runLoop();
	    assertEquals(response.toString(), strb.toString());
	}
	
	@Tag("Stack scheduler")
	@Test
	public void stackTest() {
		var stack = new Scheduler(Scheduler.Strategy.STACK);
		var scope = new ContinuationScope("scope");
		var strb= new StringBuilder();
		var continuation1 = new Continuation(scope, () -> {
			strb.append("start 1\n");
			stack.enqueue(scope);
			strb.append("middle 1\n");
			stack.enqueue(scope);
			strb.append("end 1\n");
		});
		var continuation2 = new Continuation(scope, () -> {
			strb.append("start 2\n");
			stack.enqueue(scope);
			strb.append("middle 2\n");
			stack.enqueue(scope);
			strb.append("end 2\n");
		});
		
		var response = new StringBuilder();
		response.
				append("start 1\n").
				append("start 2\n").
				append("middle 2\n").
				append("end 2\n").
				append("middle 1\n").
				append("end 1\n");
		
		var list = List.of(continuation1, continuation2);
	    list.forEach(Continuation::run);
	    stack.runLoop();
	    assertEquals(response.toString(), strb.toString());
	}

}