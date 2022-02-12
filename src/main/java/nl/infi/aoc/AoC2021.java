package nl.infi.aoc;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

/**
 *
<h1><a href="https://aoc.infi.nl/">Pakjes paniek</a></h1>
<p>
Santa wil zijn elfjes speelgoed laten maken, maar hij heeft zijn <br/>
administratie nog niet op orde. Hij heeft een lijst van onderdelen <br/>
die in elk soort speelgoed zitten. Het probleem is alleen dat <br/>
sommige onderdelen weer uit verdere onderdelen bestaan, wat het <br/>
tellen van het aantal onderdelen moeilijk maakt.
</p>
<p>
Stel bijvoorbeeld dat hij deze lijst zou krijgen (de missende <br/>
onderdelen kan je voor nu negeren):
</p>
<pre>
46 onderdelen missen
Zoink: 9 Oink, 5 Dink
Floep: 2 Flap, 4 Dink
Flap: 4 Oink, 3 Dink
</pre>
<p>
In dit voorbeeld zijn Zoinks makkelijk: er zitten in totaal 14 <br/>
(9+5) onderdelen in. Een Floep is lastiger, omdat de Flappen die <br/>
erin zitten elk ook uit meerdere onderdelen bestaan. Elke Flap <br/>
bestaat uit 7 onderdelen. In een Floep zitten dus <b>18 (2*7+4)</b> <br/>
onderdelen.
</p>
<h1>Deel 2</h1>
<p>
Terwijl je bezig was met het tellen van de onderdelen, waren een <br/>
paar ijverige elfjes al begonnen! Ze hebben speelgoed in elkaar <br/>
gezet en ingepakt, maar zijn alweer vergeten wat erin zat. Gek <br/>
genoeg weten ze nog wel hoeveel onderdelen ze hebben gebruikt.
</p>
<p>
De elfjes hebben alleen speelgoed ingepakt, en geen onderdelen <br/>
zoals accu's of ijzer. Blijkbaar is er niemand stout geweest dit <br/>
jaar.
</p>
<p>
In het eerdere voorbeeld waren er 46 missende onderdelen. Stel dat <br/>
er 3 cadeaus al ingepakt waren. Zoinks en Floepen zijn hier de <br/>
mogelijke stukken speelgoed, want ze worden niet als onderdelen <br/>
gebruikt. Er is maar 1 manier om met alle missende onderdelen <br/>
onderdelen precies 3 stukken speelgoed te maken: 2 Zoinks en 1 <br/>
Floep <b>(14+14+18=46)</b>.
</p>
<p>
Als je de stukken speelgoed weet, kan je je antwoord vinden door <br/>
de eerste letters van het speelgoed op alfabetische volgorde te <br/>
zetten. In het bovenstaande voorbeeld zou dat dus <code>FZZ</code> worden.
</p>
<p>
Er zijn al 20 cadeaus ingepakt. Gegeven het aantal missende <br/>
onderdelen in de speelgoedlijst, wat zijn dan de beginletters (op <br/>
alfabetische volgorde)?
 */
public class AoC2021 extends AocBase {

    private final int missen;
    private final Map<String, List<Pair<Integer, String>>> speelgoeden;
    
    private AoC2021(final boolean debug, final String input) {
        super(debug);
        final String[] lines = input.split("\\r?\\n");
        this.missen = Integer.valueOf(lines[0].split(" ")[0]);
        this.speelgoeden = IntStream.range(1, lines.length)
            .mapToObj(i -> lines[i])
            .flatMap(l -> {
                final String[] splits = l.split(": ");
                return Arrays.stream(splits[1].split(", "))
                        .map(s -> {
                            final String[] ss = s.split(" ");
                            return Pair.of(splits[0],
                                    Pair.of(Integer.valueOf(ss[0]), ss[1]));
                        });
            })
            .collect(groupingBy(Pair::getLeft, mapping(Pair::getRight, toList())));
    }
    
    private int aantalOnderdelen(final String speelgoed) {
        if (!this.speelgoeden.keySet().contains(speelgoed)) {
            return 1;
        }
        return this.speelgoeden.get(speelgoed).stream()
                .mapToInt(p -> p.getLeft() * aantalOnderdelen(p.getRight()))
                .sum();
    }

	@Override
    public Integer solvePart1() {
	    return this.speelgoeden.keySet().stream()
	        .mapToInt(this::aantalOnderdelen)
	        .max().orElseThrow();
    }
	
	private Stream<String> rest(final Map<Integer, String> itos, final int max) {
	    return itos.entrySet().stream()
	        .filter(e -> e.getKey() <= max)
	        .sorted((e1, e2) -> Integer.compare(e2.getKey(), e1.getKey()))
	        .map(Entry::getValue);
	}
	
	private String encodeer(final String[] a) {
	    return Arrays.stream(a)
	            .filter(Objects::nonNull)
	            .map(s -> s.substring(0, 1))
	            .sorted()
	            .collect(joining());
	}
	
	private boolean dfs(
	        final Set<String> klaar,
	        final String[] a,
	        final int i,
	        final Map<Integer, String> itos,
	        final Map<String, Integer> stoi
	    ) {
	    final int totaal = Arrays.stream(a)
	            .filter(Objects::nonNull)
	            .mapToInt(s -> stoi.get(s).intValue())
	            .sum();
	    if (i == a.length) {
	        return totaal == this.missen;
	    }
        for (final String s : rest(itos, this.missen - totaal).collect(toSet())) {
	        a[i] = s;
	        final String e = encodeer(a);
            if (!klaar.contains(e)) {
	            klaar.add(e);
                if (dfs(klaar, a, i + 1, itos, stoi)) {
                    return true;
                }
            }
	        a[i] = null;
	    }
	    return false;
	}
	
	private String solve2(final int ingepakt) {
	    final Set<String> onderdelen = this.speelgoeden.values().stream()
	            .flatMap(v -> v.stream())
	            .map(Pair::getRight)
	            .collect(toSet());
	    final Map<Integer, String> itos = this.speelgoeden.keySet().stream()
	            .filter(s -> !onderdelen.contains(s))
	            .collect(toMap(this::aantalOnderdelen, d -> d));
	    final Map<String, Integer> stoi = itos.entrySet().stream()
	        .collect(toMap(Entry::getValue, Entry::getKey));
	    log(stoi);
	    final HashSet<String> klaar = new HashSet<>();
	    final String[] a = new String[ingepakt];
        if (dfs(klaar, a, 0, itos, stoi)) {
	        log(klaar.size());
	        log(Arrays.stream(a).collect(joining(", ")));
	        return encodeer(a);
	    }
	    throw new IllegalStateException("Unsolvable");
	}

    @Override
    public String solvePart2() {
        return solve2(20);
    }

    public static final AoC2021 create(final String input) {
		return new AoC2021(false, input);
	}

	public static final AoC2021 createDebug(final String input) {
		return new AoC2021(true, input);
	}
	
	public static void main(final String[] args) throws Exception {
	    assert AoC2021.createDebug(TEST).solvePart1() == 18;
	    assert AoC2021.createDebug(TEST).solve2(3).equals("FZZ");
		lap("Part 1", () -> AoC2021.create(INPUT).solvePart1());
		lap("Part 2", () -> AoC2021.create(INPUT).solvePart2());
	}
	
	private static final String TEST =
	        "46 onderdelen missen\r\n"
	        + "Zoink: 9 Oink, 5 Dink\r\n"
	        + "Floep: 2 Flap, 4 Dink\r\n"
	        + "Flap: 4 Oink, 3 Dink";
	private static final String INPUT =
	        "1966115 onderdelen missen\r\n"
	        + "Lightsaber: 59 Batterij, 19 Unobtanium\r\n"
	        + "HandheldComputer: 29 Batterij, 5 Printplaat, 5 Plastic\r\n"
	        + "ElectrischeRacebaan: 59 AutoChassis, 43 Printplaat, 47 Plastic, 89 Batterij, 67 Wiel\r\n"
	        + "QuadDrone: 41 Accu, 5 Plastic, 79 Printplaat\r\n"
	        + "PikachuPlushy: 5 Batterij\r\n"
	        + "Trampoline: 17 Schokdemper, 31 IJzer\r\n"
	        + "BatmobileReplica: 97 BatmobileChassis, 29 Schokdemper, 89 Unobtanium, 61 Wiel\r\n"
	        + "DanceDanceRevolutionMat: 31 Schokdemper, 37 Batterij\r\n"
	        + "Printplaat: 41 Hars, 73 Koper, 83 Chip, 41 Led\r\n"
	        + "Accu: 71 Batterij\r\n"
	        + "Schokdemper: 61 IJzer, 59 Staal\r\n"
	        + "Batterij: 43 Staal\r\n"
	        + "BatmobileChassis: 71 AutoChassis, 47 Staal\r\n"
	        + "AutoChassis: 73 IJzer\r\n"
	        + "Wiel: 11 Rubber, 37 IJzer\r\n"
	        + "Unobtanium: 43 IJzer, 97 Kryptonite";
}
