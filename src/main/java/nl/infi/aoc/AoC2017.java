package nl.infi.aoc;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
<h1><a href="https://aoc.infi.nl/2017">Welkom bij de Advent of Code puzzel van
 Infi!</a></h1>
<p></p>
<h5>Robots wil eat your job!</h5>
<p>Bij Infi houden wij niet van repetitief werk en dus proberen we dit jaar wat
tijdrovende taken te automatiseren. Voor het uitdelen van de kerstcadeaus
hebben wij daarom bezorgrobots gebouwd, zodat wij ons volledig kunnen richten
op code committen en kerstborrels bezoeken.</p>
<p>Helaas zitten we pas op release 0.9, want we kwamen er achter dat soms
meerdere robots op dezelfde plek uit kunnen komen en dat is natuurlijk niet
effici�nt. We moeten dit snel oplossen door te bepalen hoe vaak deze situatie
voorkomt, want het is al bijna de 25ste! Help jij mee?</p>
<p>Om te helpen met debuggen hebben we enkele logs beschikbaar gemaakt. Deze
zijn in het volgende formaat opgeslagen:</p>
<pre>[sx1,sy1][sx2,sy2](x1,y1)(x2,y2)(x1,y1)</pre>
<p>Eerst vind je tussen de blokhaken de startposities van de robots. Let op:
schaalbaarheid is belangrijk, dus het aantal robots is variabel! Vervolgens
bevat het log de bewegingen die door de robots uitgevoerd zijn, in dezelfde
volgorde als dat de robots zijn gedefinieerd.</p>
<p>Voorbeeld:</p>
<pre>[0,0][1,1](1,0)(0,-1)(0,1)(-1,0)(-1,0)(0,1)(0,-1)(1,0)</pre>
<ol>
    <li>Robot 1 begint op 0,0 en Robot 2 begint op 1,1</li>
    <li>Robot 1 gaat naar 1,0 (0,0 + 1,0)</li>
    <li>Robot 2 gaat naar 1,0 (1,1 + 0,-1)</li>
    <li>OEPS! Dit is dus een knelpunt.</li>
    <li>Robot 1 gaat naar 1,1 (1,0 + 0,1)</li>
    <li>Robot 2 gaat naar 0,0 (1,0 + -1,0)</li>
    <li>Robot 1 gaat naar 0,1 (1,1 + -1,0)</li>
    <li>Robot 2 gaat naar 0,1 (0,0 + 0,1)</li>
    <li>AI, Dit is ook een knelpunt.</li>
    <li>Robot 1 gaat naar 0,0 (0,1 + 0,-1)</li>
    <li>Robot 2 gaat naar 1,1 (0,1 + 1,0)</li>
</ol>
<p>Het komt in dit voorbeeld dus 2 keer voor dat de robots elkaar tegen komen.
<br/><b>Kun jij uitrekenen hoe vaak dit is gebeurd voor het volgende logbestand?
</b></p>
<hr/>
<h5>Breaking news: we zien een patroon!</h5>
<p>Dit is heel gek: tijdens het doorspitten van de logs ontdekten we een vreemd
patroon in de bewegingen van de robots. Het lijkt erop dat de robots slimmer
zijn dan we dachten en dat ze tijdens hun werkzaamheden een patroon hebben
gemaakt dat ons niet eerder was opgevallen. This could be the AI we�ve been
looking for: <b>kun jij het geheime bericht vinden?</b></p>
 */
public class AoC2017 extends AocBase {

	private final Data data;
	
	protected AoC2017(final boolean debug, final String input) {
		super(debug);
		this.data = parse(input);
	}
	
	public static AoC2017 createDebug(final String input) {
		return new AoC2017(true, input);
	}
	
	public static AoC2017 create(final String input) {
		return new AoC2017(false, input);
	}
	
	private Data parse(final String input) {
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

	@SuppressWarnings("unchecked")
	private List<Positie[]> doeBewegingen() {
		final Iterator<Positie> iterators[] = new Iterator[data.aantalRobots()];
		final Positie posities[] = new Positie[data.aantalRobots()];
		for (int i = 0; i < data.aantalRobots(); i++) {
			iterators[i] = data.bewegingenRobot(i);
			posities[i] = data.startPositieRobot(i);
		}
		final List<Positie[]> bezochtePosities = new ArrayList<>();
		while (iterators[0].hasNext()) {
			final Positie nieuwePosities[] = new Positie[data.aantalRobots()];
			for (int i = 0; i < data.aantalRobots(); i++) {
				posities[i] = posities[i].add(iterators[i].next());
				nieuwePosities[i] = posities[i];
			}
			bezochtePosities.add(nieuwePosities);
		}
		return bezochtePosities;
	}
	
	@Override
	public Long solvePart1() {
		return doeBewegingen().stream()
				.map(a -> Stream.of(a).collect(toSet()))
				.filter(s -> s.size() == 1)
				.count();
	}

	@Override
	public Long solvePart2() {
		final Rooster rooster = new Rooster(
				doeBewegingen().stream()
					.flatMap(Stream::of)
					.collect(toSet()));
		final int maxX = rooster.maxX();
		final int maxY = rooster.maxY();
		Stream.iterate(maxY, i -> i - 1).limit(maxY).forEach(y -> {
			Stream.iterate(0, i -> i + 1).limit(maxX).forEach(x -> {
				if (rooster.bevat(Positie.of(x, y))) {
					System.out.print("\u2592");
				} else {
					System.out.print(" ");
				}
			});
			System.out.println("");
		});
		return 0L;
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2017.createDebug(TEST).solvePart1() == 2;
		assert AoC2017.createDebug(TEST).solvePart2() == 0;

		final Path path = Paths.get("src/main/resources/AoC2017.txt");
		final List<String> lines = Files.readAllLines(path);
		assert lines.size() == 1;
		final String input = lines.get(0);
		lap("Part 1", () -> AoC2017.create(input).solvePart1());
		lap("Part 2", () -> AoC2017.create(input).solvePart2());
	}
	
	private static final String TEST = """
        [0,0][1,1](1,0)(0,-1)(0,1)(-1,0)(-1,0)(0,1)(0,-1)(1,0)""";
	
	private static final class Data {
		final List<Positie> starts;
		final List<Positie> posities;

		public Data(final List<Positie> starts, final List<Positie> posities) {
			this.starts = starts;
			this.posities = posities;
		}
		
		public int aantalRobots() {
			return starts.size();
		}
		
		public Positie startPositieRobot(final int robot) {
			return starts.get(robot);
		}
		
		public Iterator<Positie> bewegingenRobot(final int robot) {
			return new Iterator<>() {
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
	
	private static final class Rooster {
		private final Set<Positie> punten;

		public Rooster(final Set<Positie> punten) {
			this.punten = punten;
		}

		public Integer maxX() {
			return this.punten.stream()
					.max(comparing(p -> p.x))
					.map(p -> p.x)
					.orElseThrow(() -> new RuntimeException("Empty stream"));
		}
		
		public Integer maxY() {
			return this.punten.stream()
					.max(comparing(p -> p.y))
					.map(p -> p.y)
					.orElseThrow(() -> new RuntimeException("Empty stream"));
		}
		
		public boolean bevat(final Positie positie) {
			Objects.requireNonNull(positie);
			return this.punten.contains(positie);
		}
		
		@Override
		public String toString() {
			return "Rooster [punten=" + punten + "]";
		}
	}

    final record Positie(int x, int y) {
        
        public static Positie of(final Integer x, final Integer y) {
            return new Positie(x, y);
        }
        
        public Positie add(final Positie pos) {
            Objects.requireNonNull(pos);
            return Positie.of(this.x + pos.x, this.y + pos.y);
        }
    }
}
