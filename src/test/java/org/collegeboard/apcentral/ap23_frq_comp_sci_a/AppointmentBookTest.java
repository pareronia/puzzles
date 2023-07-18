package org.collegeboard.apcentral.ap23_frq_comp_sci_a;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

class AppointmentBookTest {

	@Test
	void findFreeBlock() {
		final AppointmentBook book = new AppointmentBook(Map.of(
				2, Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 45,
						46, 47, 48, 49)));
		
		assertThat(book.findFreeBlock(2, 15)).isEqualTo(30);
		assertThat(book.findFreeBlock(2, 9)).isEqualTo(30);
		assertThat(book.findFreeBlock(2, 20)).isEqualTo(-1);
	}

	@Test
	void makeAppointment() {
		final Set<Integer> reserved2 = new HashSet<>();
		IntStream.rangeClosed(0, 24).forEach(reserved2::add);
		IntStream.rangeClosed(30, 59).forEach(reserved2::add);
		final Set<Integer> reserved3 = new HashSet<>();
		IntStream.rangeClosed(15, 40).forEach(reserved3::add);
		final Set<Integer> reserved4 = new HashSet<>();
		IntStream.rangeClosed(0, 4).forEach(reserved4::add);
		IntStream.rangeClosed(30, 43).forEach(reserved4::add);
		final Map<Integer, Set<Integer>> reserved = new HashMap<>(); 
		reserved.put(2, reserved2);
		reserved.put(3, reserved3);
		reserved.put(4, reserved4);
		final AppointmentBook book = new AppointmentBook(reserved);
		
		assertThat(book.makeAppointment(2, 4, 22)).isTrue();
		assertThat(book.makeAppointment(3, 4, 3)).isTrue();
		assertThat(book.makeAppointment(2, 4, 30)).isFalse();
		assertThat(book.isFreeBlock(2, 0, 25)).isFalse();
		assertThat(book.isFreeBlock(2, 25, 5)).isTrue();
		assertThat(book.isFreeBlock(2, 30, 30)).isFalse();
		assertThat(book.isFreeBlock(3, 0, 3)).isFalse();
		assertThat(book.isFreeBlock(3, 3, 12)).isTrue();
		assertThat(book.isFreeBlock(3, 15, 26)).isFalse();
		assertThat(book.isFreeBlock(3, 41, 19)).isTrue();
		assertThat(book.isFreeBlock(4, 0, 27)).isFalse();
		assertThat(book.isFreeBlock(4, 27, 3)).isTrue();
		assertThat(book.isFreeBlock(4, 30, 14)).isFalse();
		assertThat(book.isFreeBlock(4, 44, 16)).isTrue();
	}
}
