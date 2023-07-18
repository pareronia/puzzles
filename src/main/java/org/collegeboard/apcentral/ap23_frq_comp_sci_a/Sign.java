package org.collegeboard.apcentral.ap23_frq_comp_sci_a;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

public class Sign {

	private final List<String> lines;

	public Sign(String str, int x) {
		this.lines = new ArrayList<>();
		int begin = 0;
		while (begin < str.length()) {
			final int end = Math.min(begin + x, str.length());
			lines.add(str.substring(begin, end));
			begin += x;
		}
	}

	public int numberOfLines() {
		return lines.size();
	}
	
	public String getLines() {
		return lines.stream().collect(joining(";"));
	}
}
