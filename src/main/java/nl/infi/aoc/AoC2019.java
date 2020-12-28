package nl.infi.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

public class AoC2019 {
	
	private final String input;
	private final boolean debug;
	
	private AoC2019(String input, boolean debug) {
		this.input = input;
		this.debug = debug;
	}

	public static final AoC2019 create(String input) {
		return new AoC2019(input, false);
	}

	public static final AoC2019 createDebug(String input) {
		return new AoC2019(input, true);
	}
	
	private void log(Object obj) {
		if (!debug) {
			return;
		}
		System.out.println(obj);
	}
	
	private Pair<List<Pair<Integer, Integer>>, List<Pair<Integer, Integer>>> parse() {
		final String oneline = input.replace("\r\n", "");
		final Matcher flatsMatcher = Pattern.compile("\"flats\":\\s?\\[(.*)\\],\\s?\"").matcher(oneline);
		flatsMatcher.find();
		final String flatsMatch = flatsMatcher.group(1).replace("[", "").replace("]", "");
		final List<Pair<Integer, Integer>> flats = new ArrayList<>();
		final String[] flatsSplit = flatsMatch.split(",");
		for (int i = 0; i < flatsSplit.length; i+=2) {
			flats.add(Pair.of(Integer.valueOf(flatsSplit[i]), Integer.valueOf(flatsSplit[i+1])));
		}
		final Matcher sprongenMatcher = Pattern.compile("\"sprongen\":\\s?\\[(.*)\\]").matcher(oneline);
		sprongenMatcher.find();
		final String sprongenMatch = sprongenMatcher.group(1).replace("[", "").replace("]", "");
		final List<Pair<Integer, Integer>> sprongen = new ArrayList<>();
		final String[] sprongenSplit = sprongenMatch.split(",");
		for (int i = 0; i < sprongenSplit.length; i+=2) {
			sprongen.add(Pair.of(Integer.valueOf(sprongenSplit[i]), Integer.valueOf(sprongenSplit[i+1])));
		}
		return Pair.of(flats, sprongen);
	}
	
	public long solvePart1() {
		final Pair<List<Pair<Integer, Integer>>, List<Pair<Integer, Integer>>> parsed = parse();
		log(parsed);
		final List<Pair<Integer, Integer>> flats = parsed.getLeft();
		final List<Pair<Integer, Integer>> sprongen = parsed.getRight();
		int flat = 0;
		int sprong = 0;
		Pair<Integer, Integer> positie = flats.get(flat);
		for (int i = 0; i < sprongen.size(); i++) {
			sprong = i + 1;
			final Pair<Integer, Integer> deSprong = sprongen.get(i);
			positie = Pair.of(positie.getLeft() + 1 + deSprong.getLeft(), positie.getRight() + deSprong.getRight());
			int nieuweFlat = flat;
			for (int j = flat + 1; j < flats.size(); j++) {
				final Pair<Integer, Integer> deFlat = flats.get(j);
				if (positie.getLeft() == deFlat.getLeft() && positie.getRight() >= deFlat.getRight()) {
					positie = Pair.of(positie.getLeft(), deFlat.getRight());
					nieuweFlat = j;
					break;  // geland
				}
			}
			if (nieuweFlat > flat) {
				flat = nieuweFlat;
			} else {
				return sprong;  // niet geland
			}
		}
		if (flat == flats.size() - 1) {
			return 0;  // geland op laatste flat
		} else {
			throw new AssertionError("unreachable");
		}
	}

	public static <V> void lap(String prefix, Callable<V> callable) throws Exception {
	    long timerStart = System.nanoTime();
	    final V answer = callable.call();
	    long timeSpent = (System.nanoTime() - timerStart) / 1000;
	    double time;
	    String unit;
	    if (timeSpent < 1000) {
	        time = timeSpent;
	        unit = "µs";
	    } else if (timeSpent < 1000000) {
	        time = timeSpent / 1000.0;
	        unit = "ms";
	    } else {
	        time = timeSpent / 1000000.0;
	        unit = "s";
	    }
	    System.out.println(String.format("%s : %s, took: %.3f %s", prefix, answer, time, unit));
	}
	
	public static void main(String[] args) throws Exception {
		assert AoC2019.createDebug(TEST).solvePart1() == 4;
		AoC2019.lap("Part 1", () -> AoC2019.create(INPUT).solvePart1());
	}

	private static final String TEST = 
			"{\r\n" + 
			"\"flats\": [[1,4],[3,8],[4,3],[5,7],[7,4],[10,3]],\r\n" + 
			"\"sprongen\": [[2,0],[0,4],[1,0],[0,0]]\r\n" + 
			"}";
	
	private static final String INPUT =
			"{\"flats\":[[5,2],[6,3],[7,5],[9,6],[11,9],[15,10],[18,6],[22,7],[23,8],[25,6],[27,9],[31,6],[34,5],"
			+ "[36,7],[37,9],[38,10],[40,4],[42,7],[46,6],[48,8],[52,2],[54,4],[58,5],[59,7],[61,8]],"
			+ "\"sprongen\":[[0,1],[0,2],[1,1],[0,0],[3,1],[2,0],[3,1],[0,1],[1,0],[1,3],[3,0],[2,0],[1,2],"
			+ "[0,2],[0,1],[1,0],[1,3],[3,0],[1,2],[3,0],[1,2],[3,1],[0,2],[1,1]]}";
}
