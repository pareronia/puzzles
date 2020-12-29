package nl.infi.aoc;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

/**
<h1><a href="https://aoc.infi.nl/2019">Noordpool gesmolten: <br/>
Rendieren verkouden</a></h1>
<p>Vanwege de klimaatverandering smelt het ijs op de <br/>
noordpool erg hard en hebben alle rendieren natte <br/>
voeten gekregen. Daardoor zijn ze verkouden <br/>
geworden en moet de kerstman dit jaar zonder <br/>
rendieren op pad. Dit jaar zal de kerstman daarom <br/>
niet met zijn slee door de lucht vliegen om <br/>
iedereen cadeautjes te bezorgen.</p>
<p>Een kerst zonder cadeautjes kan natuurlijk niet, <br/>
daarom heeft de kerstman zich door zijn elven een <br/>
paar springlaarzen aan laten meten waarmee hij van <br/>
flatgebouw naar flatgebouw kan springen om zo snel <br/>
door de stad te kunnen reizen en toch bij alle <br/>
kinderen een cadeautje te bezorgen.</p>
<p>De kerstman moest wel even wennen aan zijn <br/>
springlaarzen en heeft de afgelopen maanden <br/>
geoefend. Daarbij ging het ook nog wel eens mis, <br/>
waardoor de kerstman, nogal pijnlijk, op de grond <br/>
belandde in plaats van op het volgende flatgebouw <br/>
en kon hij opnieuw beginnen.</p>
<p>Ook de Noordpool doet aan big data en de elven <br/>
hebben alle oefentochtjes van de kerstman gevolgd, <br/>
gecodeerd en opgeslagen.</p>
<p>De bewegingen van de kerstman hebben de big data <br/>
elven gecodeerd naar 'stappen':</p>
<ul>
<li>Voor de eerste stap staat de kerstman op het <br/>
dak van het eerste flatgebouw.</li>
<li>Elke stap beweegt hij 1 plaats naar rechts <br/>
(+1 in de x positie).</li>
<li>Tijdens elke stap kan de kerstman 1 keer zijn <br/>
springlaarzen gebruiken.</li>
<li>Met de springlaarzen kan de kerstman een extra <br/>
verplaatsing doen in de positieve x en positieve <br/>
y richting waarbij 0 =< x + y=< 4.</li>
<li>Als de kerstman zich na de verplaatsing op of <br/>
boven het dak van een flatgebouw bevindt dan <br/>
landt hij veilig op dat dak. Zijn nieuwe y <br/>
positie is dan de hoogte van het dak.</li>
<li>Als de kerstman zich na de verplaatsing niet <br/>
op of boven het dak van een flatgebouw bevindt dan <br/>
valt hij op de grond en is de tocht mislukt.</li>
<li>De kerstman kan voor een ander gebouw langs <br/>
springen, mits deze sprong voldoet aan de <br/>
verplaatsingsregels.</li>
<li>Een flatgebouw heeft altijd een breedte van 1.</li>
</ul>
<p><b>Na hoeveel stappen valt de kerstman op de grond?.</b></br>
Het antwoord is het stapnummer van de stap waarbij </br>
de kerstman op de grond valt, of 0 als de kerstman </br>
het dak van het laatste flatgebouw bereikt.</p>
 */
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
	
	private Pair<List<Positie>, List<Positie>> parse() {
		final String oneline = input.replace("\r\n", "");
		final String flatsRegExp = "\"flats\":\\s?\\[(.*)\\],\\s?\"";
		final String sprongenRegExp = "\"sprongen\":\\s?\\[(.*)\\]";
		final Function<String, List<Positie>> parseFunctie = regExp -> {
			final List<Positie> posities = new ArrayList<>();
			final Matcher matcher = Pattern.compile(regExp).matcher(oneline);
			matcher.find();
			final String match
					= matcher.group(1).replace("[", "").replace("]", "");
			final String[] split = match.split(",");
			for (int i = 0; i < split.length; i += 2) {
				posities.add(Positie.of(Integer.valueOf(split[i]),
										Integer.valueOf(split[i + 1])));
			}
			return posities;
		};
		final List<List<Positie>> resultaat
				= Stream.of(flatsRegExp, sprongenRegExp)
						.map(parseFunctie)
						.collect(toList());
		return Pair.of(resultaat.get(0), resultaat.get(1));
	}
	
	public void visualiseerPart1(List<Positie> flats) {
		final Integer maxFlatX = flats.stream()
				.max(comparing(p -> p.x))
				.map(p -> p.x)
				.orElseThrow(() -> new RuntimeException("Empty stream"));
		final Integer maxFlatY = flats.stream()
				.max(comparing(p -> p.y))
				.map(p -> p.y)
				.orElseThrow(() -> new RuntimeException("Empty stream"));
		Stream.iterate(maxFlatY, i -> i - 1).limit(maxFlatY).forEach(y -> {
			Stream.iterate(1, i -> i + 1).limit(maxFlatX + 1).forEach(x -> {
				final Optional<Positie> flat = flats.stream()
						.filter(f -> f.x == x)
						.findFirst();
				if (flat.isPresent()) {
					if (y > flat.get().y) {
						System.out.print(" ");
					} else {
						System.out.print("\u2592");
					}
				} else {
					System.out.print(" ");
				}
			});
			System.out.println("");
		});
	}
	
	public long solvePart1() {
		final Pair<List<Positie>, List<Positie>> parsed = parse();
		log(parsed);
		final List<Positie> flats = parsed.getLeft();
		final List<Positie> sprongen = parsed.getRight();
		visualiseerPart1(flats);
		int flat = 0;
		int sprong = 0;
		Positie positie = flats.get(flat);
		for (int i = 0; i < sprongen.size(); i++) {
			sprong = i + 1;
			final Positie deSprong = sprongen.get(i);
			positie = Positie.of(positie.x + 1 + deSprong.x,
								 positie.y + deSprong.y);
			int nieuweFlat = flat;
			for (int j = flat + 1; j < flats.size(); j++) {
				final Positie deFlat = flats.get(j);
				if (positie.x == deFlat.x && positie.y >= deFlat.y) {
					positie = Positie.of(positie.x, deFlat.y);
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
	
	public static void main(String[] args) throws Exception {
		assert AoC2019.createDebug(TEST).solvePart1() == 4;
		AoC2019.lap("Part 1", () -> AoC2019.create(INPUT1).solvePart1());
		AoC2019.lap("Part 1", () -> AoC2019.create(INPUT2).solvePart1());
	}

	private static final String TEST = 
			"{\r\n" + 
			"\"flats\": [[1,4],[3,8],[4,3],[5,7],[7,4],[10,3]],\r\n" + 
			"\"sprongen\": [[2,0],[0,4],[1,0],[0,0]]\r\n" + 
			"}";
	
	private static final String INPUT1 =
			"{\"flats\":[[5,2],[6,3],[7,5],[9,6],[11,9],[15,10],[18,6],[22,7],"
			+ "[23,8],[25,6],[27,9],[31,6],[34,5],[36,7],[37,9],[38,10],"
			+ "[40,4],[42,7],[46,6],[48,8],[52,2],[54,4],[58,5],[59,7],[61,8]],"
			+ "\"sprongen\":[[0,1],[0,2],[1,1],[0,0],[3,1],[2,0],[3,1],[0,1],"
			+ "[1,0],[1,3],[3,0],[2,0],[1,2],[0,2],[0,1],[1,0],[1,3],[3,0],"
			+ "[1,2],[3,0],[1,2],[3,1],[0,2],[1,1]]}";
	
	private static final String INPUT2 =
			"{\"flats\":[[2,4],[5,6],[6,8],[9,10],[12,4],[13,7],[14,9],[16,5],"
			+ "[19,7],[21,10],[26,8],[27,9],[30,10],[31,3],[34,4],[35,7],"
			+ "[36,9],[40,3],[41,4],[42,7],[43,10],[46,5],[48,6],[49,7],"
			+ "[51,9],[56,3],[58,6],[60,9]],"
			+ "\"sprongen\":[[2,2],[0,2],[2,2],[2,0],[0,3],[0,0],[1,0],[2,2]"
			+ ",[1,3],[4,0],[0,1],[2,1],[0,0],[2,1],[0,3],[0,2],[3,0],[0,1]"
			+ ",[0,3],[0,3],[2,0],[1,1],[0,1],[1,2],[4,0],[1,3],[1,3]]}";
	
	private static final class Positie {
		private final Integer x;
		private final Integer y;
		
		private Positie(Integer x, Integer y) {
			this.x = Objects.requireNonNull(x, "Expect non-null x value");
			this.y = Objects.requireNonNull(y, "Expect non-null y value");
		}
		
		public static Positie of(Integer x, Integer y) {
			return new Positie(x, y);
		}

		@Override
		public String toString() {
			return "(x=" + x + ", y=" + y + ")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x.hashCode();
			result = prime * result + y.hashCode();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			final Positie other = (Positie) obj;
			return this.x.equals(other.x) && this.y.equals(other.y);
		}
	}
}
