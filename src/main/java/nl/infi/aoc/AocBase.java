package nl.infi.aoc;

import java.util.concurrent.Callable;

public abstract class AocBase {

	protected final boolean debug;

	public static <V> void lap(String prefix, Callable<V> callable) throws Exception {
	    final long timerStart = System.nanoTime();
	    final V answer = callable.call();
	    final long timeSpent = (System.nanoTime() - timerStart) / 1000;
	    double time;
	    String unit;
	    if (timeSpent < 1000) {
	        time = timeSpent;
	        unit = "µs";
	    } else if (timeSpent < 1_000_000) {
	        time = timeSpent / 1000.0;
	        unit = "ms";
	    } else {
	        time = timeSpent / 1_000_000.0;
	        unit = "s";
	    }
	    System.out.println(String.format("%s : %s, took: %.3f %s",
	    								 prefix, answer, time, unit));
	}

	protected AocBase(boolean debug) {
		this.debug = debug;
	}
	
	public long solvePart1() {
		return 0L;
	}
	
	public long solvePart2() {
		return 0L;
	}
	
	protected void log(Object obj) {
		if (!debug) {
			return;
		}
		System.out.println(obj);
	}
}