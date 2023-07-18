package org.collegeboard.apcentral.ap23_frq_comp_sci_a;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AppointmentBook {

	private final Map<Integer, Set<Integer>> reserved;

	public AppointmentBook(Map<Integer, Set<Integer>> reserved) {
		this.reserved = reserved;
	}

	private boolean isMinuteFree(int period, int minute) {
		assert 1 <= period && period <= 8;
		assert 0 <= minute && minute <= 59;

		return !this.reserved.getOrDefault(period, Set.of()).contains(minute);
	}

	private void reserveBlock(int period, int startMinute, int duration) {
		assert 1 <= period && period <= 8;
		assert 0 <= startMinute && startMinute <= 59;
		assert 1 <= duration && duration <= 60;

		for (int i = startMinute; i < startMinute + duration; i++) {
			this.reserved.computeIfAbsent(period, k -> new HashSet<>()).add(i);
		}
	}

	boolean isFreeBlock(int period, int startMinute, int duration) {
		for (int minute = startMinute; minute < startMinute + duration; minute++) {
			if (!isMinuteFree(period, minute)) {
				return false;
			}
		}
		return true;
	}

	int findFreeBlock(int period, int duration) {
		assert 1 <= period && period <= 8;
		assert 1 <= duration && duration <= 60;

		for (int startMinute = 0; startMinute + duration <= 60; startMinute++) {
			if (isFreeBlock(period, startMinute, duration)) {
				return startMinute;
			}
		}
		return -1;
	}

	public boolean makeAppointment(int startPeriod, int endPeriod, int duration) {
		assert 1 <= startPeriod && startPeriod <= 8;
		assert 1 <= endPeriod && endPeriod <= 8;
		assert startPeriod <= endPeriod;
		assert 1 <= duration && duration <= 60;

		for (int period = startPeriod; period <= endPeriod; period++) {
			final int startMinute = findFreeBlock(period, duration);
			if (startMinute >= 0) {
				reserveBlock(period, startMinute, duration);
				return true;
			}
		}
		return false;
	}
}
