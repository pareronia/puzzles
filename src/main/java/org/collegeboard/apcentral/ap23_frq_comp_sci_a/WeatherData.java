package org.collegeboard.apcentral.ap23_frq_comp_sci_a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeatherData {

    private List<Double> temperatures;
    
    public WeatherData(final List<Double> temperatures) {
        this.temperatures = new ArrayList<>(temperatures);
    }

    public List<Double> getTemperatures() {
        return temperatures;
    }

    public void cleanData(final double lower, final double upper) {
        temperatures = temperatures.stream()
                .filter(t -> t >= lower && t <= upper)
                .toList();
    }
    
    public int longestHeatWave(final double treshold) {
        final int[] sums = new int[temperatures.size()];
        int sum = 0;
        for (int i = 0; i < temperatures.size(); i++) {
            if (temperatures.get(i) > treshold) {
                sum++;
            } else {
                sum = 0;
            }
            sums[i] = sum;
        }
        return Arrays.stream(sums).max().getAsInt();
    }
}
