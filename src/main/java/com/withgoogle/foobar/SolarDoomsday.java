package com.withgoogle.foobar;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @see <a href="https://foobar.withgoogle.com/">https://foobar.withgoogle.com/</a>
 * @see <a href="https://livecodestream.dev/challenge/solar-doomsday/">https://livecodestream.dev/challenge/solar-doomsday/</a>
 */
public class SolarDoomsday {
	private final Integer area;
	
	private SolarDoomsday(final String input) {
		this.area = Integer.valueOf(input);
	}
	
	public static SolarDoomsday create(final String input) {
		return new SolarDoomsday(input);
	}

	public static void main(final String[] args) throws Exception {
		assert SolarDoomsday.create("1").solve().equals("1");
		assert SolarDoomsday.create("2").solve().equals("1,1");
		assert SolarDoomsday.create("3").solve().equals("1,1,1");
		assert SolarDoomsday.create("4").solve().equals("4");
		assert SolarDoomsday.create("5").solve().equals("4,1");
		assert SolarDoomsday.create("6").solve().equals("4,1,1");
		assert SolarDoomsday.create("7").solve().equals("4,1,1,1");
		assert SolarDoomsday.create("8").solve().equals("4,4");
		assert SolarDoomsday.create("9").solve().equals("9");
		assert SolarDoomsday.create("10").solve().equals("9,1");
		assert SolarDoomsday.create("11").solve().equals("9,1,1");
		assert SolarDoomsday.create("12").solve().equals("9,1,1,1");
		assert SolarDoomsday.create("13").solve().equals("9,4");
		assert SolarDoomsday.create("14").solve().equals("9,4,1");
		assert SolarDoomsday.create("15").solve().equals("9,4,1,1");
		assert SolarDoomsday.create("16").solve().equals("16");
		assert SolarDoomsday.create("17").solve().equals("16,1");
		assert SolarDoomsday.create("18").solve().equals("16,1,1");
		assert SolarDoomsday.create("19").solve().equals("16,1,1,1");
		assert SolarDoomsday.create("20").solve().equals("16,4");
		assert SolarDoomsday.create("21").solve().equals("16,4,1");
		assert SolarDoomsday.create("22").solve().equals("16,4,1,1");
		assert SolarDoomsday.create("23").solve().equals("16,4,1,1,1");
		assert SolarDoomsday.create("24").solve().equals("16,4,4");
		assert SolarDoomsday.create("25").solve().equals("25");
		assert SolarDoomsday.create("26").solve().equals("25,1");
		assert SolarDoomsday.create("27").solve().equals("25,1,1");
		assert SolarDoomsday.create("28").solve().equals("25,1,1,1");
		assert SolarDoomsday.create("29").solve().equals("25,4");
		assert SolarDoomsday.create("30").solve().equals("25,4,1");
		assert SolarDoomsday.create("31").solve().equals("25,4,1,1");
		assert SolarDoomsday.create("32").solve().equals("25,4,1,1,1");
		assert SolarDoomsday.create("33").solve().equals("25,4,4");
		assert SolarDoomsday.create("34").solve().equals("25,9");
		assert SolarDoomsday.create("35").solve().equals("25,9,1");
		assert SolarDoomsday.create("36").solve().equals("36");
		assert SolarDoomsday.create("37").solve().equals("36,1");
		assert SolarDoomsday.create("38").solve().equals("36,1,1");
		assert SolarDoomsday.create("39").solve().equals("36,1,1,1");
		assert SolarDoomsday.create("40").solve().equals("36,4");
		assert SolarDoomsday.create("41").solve().equals("36,4,1");
		assert SolarDoomsday.create("42").solve().equals("36,4,1,1");
		assert SolarDoomsday.create("43").solve().equals("36,4,1,1,1");
		assert SolarDoomsday.create("44").solve().equals("36,4,4");
		assert SolarDoomsday.create("45").solve().equals("36,9");
		assert SolarDoomsday.create("46").solve().equals("36,9,1");
		assert SolarDoomsday.create("47").solve().equals("36,9,1,1");
		assert SolarDoomsday.create("48").solve().equals("36,9,1,1,1");
		assert SolarDoomsday.create("49").solve().equals("49");
		assert SolarDoomsday.create("50").solve().equals("49,1");
		assert SolarDoomsday.create("51").solve().equals("49,1,1");
		assert SolarDoomsday.create("52").solve().equals("49,1,1,1");
		assert SolarDoomsday.create("53").solve().equals("49,4");
		assert SolarDoomsday.create("54").solve().equals("49,4,1");
		assert SolarDoomsday.create("55").solve().equals("49,4,1,1");
		assert SolarDoomsday.create("56").solve().equals("49,4,1,1,1");
		assert SolarDoomsday.create("57").solve().equals("49,4,4");
		assert SolarDoomsday.create("58").solve().equals("49,9");
		assert SolarDoomsday.create("59").solve().equals("49,9,1");
		assert SolarDoomsday.create("60").solve().equals("49,9,1,1");
		assert SolarDoomsday.create("61").solve().equals("49,9,1,1,1");
		assert SolarDoomsday.create("62").solve().equals("49,9,4");
		assert SolarDoomsday.create("64").solve().equals("64");
		lap("Panels", () -> SolarDoomsday.create(INPUT).solve());
		lap("Panels", () -> SolarDoomsday.create("310").solve());
		lap("Panels", () -> SolarDoomsday.create("15324").solve());
		lap("Panels", () -> SolarDoomsday.create("112233").solve());
		lap("Panels", () -> SolarDoomsday.create("9999").solve());
		lap("Panels", () -> SolarDoomsday.create("987654321").solve());
	}
	
	private static final String INPUT = "12345678";

	protected static <V> void lap(final String prefix, final Callable<V> callable) throws Exception {
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

	public String solve() {
	    final List<Integer> ans = new ArrayList<>();
	    int rest = this.area;
	    while (rest > 0) {
	        final int root = sqrt(rest);
	        ans.add(root * root);
	        rest -= (root * root);
	    }
	    return ans.stream().map(String::valueOf).collect(joining(","));
	}
	
	private int sqrt(final int number) {
		return Double.valueOf(Math.floor(Math.sqrt(number))).intValue();
	}
}