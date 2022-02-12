package nl.infi.aoc;

import static java.util.stream.Collectors.summingLong;

import java.util.stream.Stream;

/**
 * 
<h1><a href="https://aoc.infi.nl/2020">Bepakt en bezakt in 2020</a></h1>
<p>
In het bijzondere jaar 2020 blijkt de magie van kerstman <br/>
verbazingwekkend als altijd, want ondanks dat normale mensen <br/>
het een raar jaar vinden, gaat het dit jaar op de Noordpool <br/>
verbazingwekkend goed.
</p>
<p>
De elven hebben van alle kinderen, die lijstje opgestuurd <br/>
hebben, in record tempo hun voorkeuren uitgezocht en geteld.<br/>
De cadeautjes zijn gekocht en ingepakt en alle pakjes liggen al klaar.<br/>
Ze hoeven alleen nog maar in een zak gedaan te worden, zodat <br/>
de kerstman deze op kerstavond mee kan nemen.
<p>
<p>
En dat is dan wel het enige puntje, waar de elven dit jaar wat <br/>
hulp bij nodig hebben, want ze weten niet hoe groot de zak <br/>
moet gaan worden.<br/>
De magie van de kerstman en de supersonische snelheid van de <br/>
arreslee met de rendieren, zorgt er voor dat tijdens het <br/>
vervoer de pakjes en de zak <i>tweedimensionaal</i> lijken.<br/>
Ook zijn, op magische wijze, alle pakjes even groot <br/>
op het moment dat ze in de zak komen. Tevens weten ze, dat voor <br/>
een optimale verhouding tussen benodigd materiaal om de zak te <br/>
maken en inhoud van de zak deze tijdens het vervoer de vorm <br/>
moet krijgen van een <b>Regelmatige achthoek</b>.
</p>
<p>
Echter omdat er alleen maar gehele pakjes in de zak kunnen, <br/>
hebben de elven niets aan de bekende formules voor het <br/>
berekenen van de oppervlakte en dus hebben ze om hulp gevraagd <br/>
om te helpen met de berekeningen.
</p>
<h5>Vereisten:</h5>
<p>
De zak moet met een platte kant op de bodem van de slee <br/>
komen te staan.<br/>
Alle acht de zijden van de zak moeten precies even lang zijn<br/>
De schuine zijden van de zak staan onder een hoek van 135°<br/>
Ieder 1x1 vakje binnen de zak is een plek voor een pakje
</p>
<h5>Legenda</h5>
<p>
<ul>
<li><b>‾</b>: De bodem</li>
<li><b>/</b> of <b>\</b>: een schuine zijde</li>
<li><b>|</b>: Een zijkant</li>
<li><b>_</b>: De bovenkant</li>
<li><b>.</b>: Ruimte voor één pakje</li>
</ul>
</p>
<h5>Achthoek met zijden van lengte: <b>2</b></h5>
<pre>
   _
  /..\
 /....\
|......|
|......|
 \..../
  \../
   ¯¯
</pre>
<p>Het aantal pakjes dat in deze zak passen is: <b>24</b>
</p>
<h5>Achthoek met zijden van lengte: <b>3</b></h5>
<pre>
    ___
   /...\
  /.....\
 /.......\
|.........|
|.........|
|.........|
 \......./
  \...../
   \.../
    ¯¯¯
</pre>
<p>Het aantal pakjes dat in deze zak passen is: <b>57</b>
</p>
<h5>Extra voorbeelden</h5>
<p>
In een zak met zijde van lengte <b>1</b> passen: <b>5</b> pakjes<br/>
In een zak met zijde van lengte <b>4</b> passen: <b>104</b> pakjes<br/>
In een zak met zijde van lengte <b>10</b> passen: <b>680</b> pakjes<br/>
In een zak met zijde van lengte <b>25</b> passen: <b>4.325</b> pakjes<br/>
</p><p>
In Nederland wonen momenteel <b>17.486.751</b> inwoners.<br/>
De elven willen graag alle pakjes voor Nederland in één zak stoppen.
</p><p>
<b>Wat is de minimale lengte van een zijde voor de zak die zak?</b>
</p>
<hr/>
<p>
Om een zak te kunnen maken hebben de elven natuurlijk wel <br/>
voldoende stof nodig. De zak bestaat uit:
<ul>
<li>een bodem <b>‾</b>.</li>
<li>de schuine zijdes <b>/</b> of <b>\</b>.</li>
<li>De zijkanten <b>|</b>.</li>
<li>De bovenkant <b>_</b>.</li>
</ul>
<h5>Conclusie</h5>
<p>
Voor een zak met zijde van lengte <b>1</b> zijn <b>8</b> stukken stof nodig<br/>
Voor een zak met zijde van lengte <b>4</b> zijn <b>32</b> stukken stof nodig<br/>
Voor een zak met zijde van lengte <b>25</b> zijn <b>200</b> stukken stof nodig<br/>
</p><p>
De kerstman kan op zijn tocht in 1 zak alle cadeautjes voor 1 <br/>
continent meenemen.<br/>
De elven zetten van tevoren alle zakken klaar op de noordpool.<br/>
Na ieder continent komt hij even terug op de noordpool om de <br/>
volgende zak op te halen.<br/>
De elven weten per continent hoeveel cadeautjes er bezorgt (sic)<br/>
moeten worden:
<table>
<tr><td>Asia</td><td style="text-align: right"><b>4.541.363.434</b></td></tr>
<tr><td>Africa</td><td style="text-align: right"><b>1.340.832.532</b></td></tr>
<tr><td>Europe</td><td style="text-align: right"><b>747.786.585</b></td></tr>
<tr><td>South America</td><td style="text-align: right"><b>430.832.752</b></td></tr>
<tr><td>North America</td><td style="text-align: right"><b>369.012.203</b></td></tr>
<tr><td>Oceania</td><td style="text-align: right"><b>42.715.772</b></td></tr>
</table>
</p>
<b>Hoeveel stukken stof moeten de elven mimimaal kopen om alle <br/>
zakken te kunnen maken?</b>
*/
public class AoC2020 extends AocBase {
	
	final Long[] input;
	
	private AoC2020(final boolean debug, final Long[] input) {
		super(debug);
		this.input = input;
	}

	public static final AoC2020 create(final Long... input) {
		return new AoC2020(false, input);
	}

	public static final AoC2020 createDebug(final Long... input) {
		return new AoC2020(true, input);
	}

	private long berekenPakjesVoorZijde(final long zijde) {
		long pakjes = zijde;
		long summant = zijde;
		for (long i = 0; i < zijde - 1; i++) {
			summant += 2;
			pakjes += summant;
		}
		pakjes *= 2;
		summant += 2;
		pakjes += zijde * summant;
		return pakjes;
	}
	
	private long zoekZijdeVoorPakjes(final long pakjes) {
		long i = 1;
		while (berekenPakjesVoorZijde(i) < pakjes) {
			i++;
		}
		return i;
	}

	@Override
	public Long solvePart1() {
		assert this.input.length == 1;
		return zoekZijdeVoorPakjes(input[0]);
	}
	
	@Override
	public Long solvePart2() {
		return 8 * Stream.of(this.input)
				.collect(summingLong(i -> zoekZijdeVoorPakjes(i)));
	}
	
	public static void main(final String[] args) throws Exception {
		assert AoC2020.createDebug(5L).solvePart1() == 1;
		assert AoC2020.createDebug(24L).solvePart1() == 2;
		assert AoC2020.createDebug(57L).solvePart1() == 3;
		assert AoC2020.createDebug(104L).solvePart1() == 4;
		assert AoC2020.createDebug(680L).solvePart1() == 10;
		assert AoC2020.createDebug(4325L).solvePart1() == 25;
		lap("Part 1", () -> AoC2020.create(17_486_751L).solvePart1());
		lap("Part 2", () -> AoC2020.create(
				4_541_363_434L,
				1_340_832_532L,
				747_786_585L,
				430_832_752L,
				369_012_203L,
				42_715_772L).solvePart2());
	}
}
