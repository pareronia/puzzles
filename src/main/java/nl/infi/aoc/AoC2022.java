package nl.infi.aoc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <h1><a href="https://aoc.infi.nl/2021">Navigatie narigheid</a></h1>
 * <p>
 * Dit jaar wil de kerstman, net als voorgaande jaren, weer <br/>
 * alle huizen langs om de cadeautjes bij alle lieve kinderen <br/>
 * te brengen. Helaas blijkt tijdens de voorbereidingen dat <br/>
 * zijn Kerstman Positioning System (KPS) het niet meer doet <br/>
 * en met het wereldwijde chiptekort zit een nieuwe bestellen <br/>
 * er niet in.
 * </p>
 * <p>
 * Een van de elfjes herinnert zich de ouderwetse methode en <br/>
 * vraagt de kerstman in het dashboardkastje van de slee te <br/>
 * kijken. Hij vindt hier een grote magische rol met <br/>
 * navigatie-instructies. Deze lijst staat vol met regels om <br/>
 * te lopen, te draaien en te springen.
 * </p>
 * <p>
 * Aan de rol hangt een klein kaartje met daarop een losse <br/>
 * instructie om altijd richting zijn huis te kijken wanneer <br/>
 * hij begint met de navigatie-instructies. Zoals iedereen <br/>
 * weet woont de kerstman natuurlijk op de Noordpool omdat hij <br/>
 * extreem allergisch is voor pinguïns.
 * </p>
 * <p>
 * Stel dat de volgende navigatie-instructies op de lijst staan:
 * <code>draai 90<br/>loop 6<br/>spring 2<br/>draai -45<br/>loop 2</code>
 * In dit voorbeeld staan vijf instructies.
 * <ul>
 * <li>Volgens het kleine kaartje moet de kerstman beginnen met <br/>
 * zijn gezicht naar het noorden.</li>
 * <li>Met de eerste instructie draait de kerstman 90 graden (naar <br/>
 * rechts) en kijkt richting het oosten.</li>
 * <li>Als hij de tweede instructie volgt doet hij 6 stappen <br/>
 * (richting het oosten).</li>
 * <li>Instructie nummer drie maakt dat de kerstman een sprong <br/>
 * vooruit doet. De afstand die hij springt is gelijk aan die <br/>
 * van twee stappen.</li>
 * <li>De vierde instructie maakt dat hij -45 graden draait. Deze <br/>
 * instructie zorgt ervoor dat de kerstman richting het noord-<br/>
 * oosten kijkt.</li>
 * <li>Met de vijfde en laatste instructie zet de kerstman twee <br/>
 * stappen richting het noord-oosten. Als de kerstman één <br/>
 * diagonale step zet, komt dit qua afstand overeen met één <br/>
 * stap in horizontale richting en één stap in de verticale <br/>
 * richting.</li>
 * </ul>
 * Het zweet breekt de kerstman uit. Een van de belangrijkste <br/>
 * functies van zijn KPS ontbreekt voor zijn tijdsplanning en <br/>
 * dat is de
 * <a href="https://nl.wikipedia.org/wiki/Manhattan-metriek">Manhattan afstand</a>
 * tussen het startpunt en <br/>
 * het eindpunt. In het voorbeeld hierboven is dat een Manhattan <br/>
 * afstand van 12.
 * <p>
 * Gegeven de navigatie-instructies, vind de Manhattan afstand <br/>
 * tussen het begin- en eindpunt. Deze afstand is vervolgens <br/>
 * het antwoord op deel 1.
 * </p>
 * <h1>Deel 2</h1>
 * <p>
 * Terwijl de kerstman druk bezig was met alle navigatie-<br/>
 * instructies begon het net te sneeuwen. Hierdoor heeft de <br/>
 * kerstman een spoor van stappen achtergelaten in de sneeuw. <br/>
 * Terwijl de kerstman vertrekt met zijn arrenslee kijkt hij <br/>
 * nog een keer achterom en ziet dat zijn stappen een patroon <br/>
 * van letters hebben gemaakt.
 * </p>
 * <p>
 * Kijk goed naar de sporen in de sneeuw en zoek naar het <br/>
 * woord wat de kerstman met zijn stappen in de sneeuw heeft <br/>
 * achtergelaten. Dit woord is het antwoord op deel 2.
 * </p>
 */
public class AoC2022 extends AocBase {
    
    private static final Richting[] WINDROOS = new Richting[] {
        Richting.of(0, 1),   // N
        Richting.of(1, 1),   // NO
        Richting.of(1, 0),   // O
        Richting.of(1, -1),  // ZO
        Richting.of(0, -1),  // Z
        Richting.of(-1, -1), // ZW
        Richting.of(-1, 0),  // W
        Richting.of(-1, 1),  // NW
    };
    
    private final List<String> input;

    protected AoC2022(final boolean debug, final List<String> input) {
        super(debug);
        this.input = input;
    }

    public static AoC2022 createDebug(final List<String> input) {
        return new AoC2022(true, input);
    }

    public static AoC2022 create(final List<String> input) {
        return new AoC2022(false, input);
    }

    private List<Positie> navigeer() {
        final List<Positie> posities = new ArrayList<>();
        Positie pos = Positie.of(0, 0);
        posities.add(pos);
        int r = 0;
        for (final String line : this.input) {
            final var splits = line.split(" ");
            final int hvh = Integer.parseInt(splits[1]);
            switch (splits[0]) {
            case "loop" -> {
                for (int i = 0; i < hvh; i++) {
                    pos = pos.translatie(WINDROOS[r], 1);
                    posities.add(pos);
                }
            }
            case "spring" -> {
                pos = pos.translatie(WINDROOS[r], hvh);
                posities.add(pos);
            }
            case "draai" ->
                r = (WINDROOS.length + r + (hvh / 45)) % WINDROOS.length;
            }
        }
        return posities;
    }

    @Override
    public Integer solvePart1() {
        final List<Positie> posities = navigeer();
        return posities.get(posities.size() - 1).manhattan();
    }

    @Override
    public Integer solvePart2() {
        final Set<Positie> posities = new HashSet<>(navigeer());
        final var statsX = posities.stream()
                .mapToInt(Positie::x).summaryStatistics();
        final var statsY = posities.stream()
                .mapToInt(Positie::y).summaryStatistics();
        for (int y = statsY.getMax(); y >= statsY.getMin(); y--) {
            final var sb = new StringBuilder();
            for (int x = statsX.getMin(); x <= statsX.getMax(); x++) {
                if (posities.contains(Positie.of(x, y))) {
                    sb.append('\u2592');
                } else {
                    sb.append(' ');
                }
            }
            System.out.println(sb.toString());
        }
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022.createDebug(TEST).solvePart1() == 12;

		final var path = Paths.get("src/main/resources/AoC2022.txt");
		final var lines = Files.readAllLines(path);
		lap("Part 1", () -> AoC2022.create(lines).solvePart1());
		lap("Part 2", () -> AoC2022.create(lines).solvePart2());
    }

    private static final List<String> TEST = """
            draai 90
            loop 6
            spring 2
            draai -45
            loop 2
            """.lines().toList();

    final record Positie(int x, int y) {
        public static Positie of(final int x, final int y) {
            return new Positie(x, y);
        }
        
        public Positie translatie(final Richting r, final int hvh) {
            Objects.requireNonNull(r);
            return Positie.of(this.x + hvh * r.x, this.y + hvh * r.y);
        }
        
        public int manhattan() {
            return Math.abs(this.x) + Math.abs(this.y);
        }
    }
    
    final record Richting(int x, int y) {
        public static Richting of(final int x, final int y) {
            return new Richting(x, y);
        }
    }
}
