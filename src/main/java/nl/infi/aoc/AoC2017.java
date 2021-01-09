package nl.infi.aoc;

import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AoC2017 extends AocBase {

	private final Data data;
	
	protected AoC2017(boolean debug, String input) {
		super(debug);
		this.data = parse(input);
		log(data);
	}
	
	public static AoC2017 createDebug(String input) {
		return new AoC2017(true, input);
	}
	
	public static AoC2017 create(String input) {
		return new AoC2017(false, input);
	}
	
	private Data parse(String input) {
		final Matcher m = Pattern.compile("\\[[0-9]+,[0-9]+\\]").matcher(input);
		final List<Positie> starts = new ArrayList<>();
		int end = 0;
		while (m.find()) {
			String match = m.group(0);
			match = match.substring(1, match.length() - 1);
			final String[] splits = match.split(",");
			final Integer x = Integer.valueOf(splits[0]);
			final Integer y = Integer.valueOf(splits[1]);
			starts.add(Positie.of(x, y));
			end = m.end();
		}
		final String[] splits
				= input.substring(end + 1, input.length() - 1).split("\\)\\(");
		final List<Positie> posities = Stream.of(splits).map(split -> {
			final String[] splitSplits = split.split(",");
			final Integer x = Integer.valueOf(splitSplits[0]);
			final Integer y = Integer.valueOf(splitSplits[1]);
			return Positie.of(x, y);
		}).collect(toList());
		return new Data(starts, posities);
	}

	@Override
	@SuppressWarnings("unchecked")
	public long solvePart1() {
		final Iterator<Positie> iterators[] = new Iterator[data.aantalRobots()];
		final Positie posities[] = new Positie[data.aantalRobots()];
		for (int i = 0; i < data.aantalRobots(); i++) {
			iterators[i] = data.bewegingenRobot(i);
			posities[i] = data.startPositieRobot(i);
		}
		int knelpunten = 0;
		while (iterators[0].hasNext()) {
			final Set<Positie> nieuwePosities = new HashSet<>();
			for (int i = 0; i < data.aantalRobots(); i++) {
				posities[i] = posities[i].add(iterators[i].next());
				nieuwePosities.add(posities[i]);
			}
			if (nieuwePosities.size() == 1)	{
				knelpunten++;
			}
		}
		return knelpunten;
	}

	public static void main(String[] args) throws Exception {
		assert AoC2017.createDebug(TEST).solvePart1() == 2;

		final Path path = Paths.get("src/main/resources/AoC2017.txt");
		final List<String> lines = Files.readAllLines(path);
		assert lines.size() == 1;
		final String input = lines.get(0);
		lap("Part 1", () -> AoC2017.create(input).solvePart1());
	}
	
	private static final String TEST = "[0,0][1,1](1,0)(0,-1)(0,1)(-1,0)(-1,0)"
			+ "(0,1)(0,-1)(1,0)";
	
	private static final class Data {
		final List<Positie> starts;
		final List<Positie> posities;

		public Data(List<Positie> starts, List<Positie> posities) {
			this.starts = starts;
			this.posities = posities;
		}
		
		public int aantalRobots() {
			return starts.size();
		}
		
		public Positie startPositieRobot(int robot) {
			return starts.get(robot);
		}
		
		public Iterator<Positie> bewegingenRobot(int robot) {
			return new Iterator<Positie>() {
				int i = robot % aantalRobots();
				
				@Override
				public Positie next() {
					final Positie positie = posities.get(i);
					i += aantalRobots();
					return positie;
				}
				
				@Override
				public boolean hasNext() {
					return i < posities.size();
				}
			};
		}

		@Override
		public String toString() {
			return "Data [" + System.lineSeparator()
					+ " starts=" + starts + "," + System.lineSeparator()
					+ " posities=" + posities + System.lineSeparator()
					+ "]";
		}
	}
}
