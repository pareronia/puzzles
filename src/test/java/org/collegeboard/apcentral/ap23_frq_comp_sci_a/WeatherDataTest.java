package org.collegeboard.apcentral.ap23_frq_comp_sci_a;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class WeatherDataTest {

    @Test
    void cleanData() {
        final WeatherData data = new WeatherData(
            List.of(99.1, 142.0, 85.0, 85.1, 84.6, 94.3, 124.9, 98.0, 101.0, 102.5));
        data.cleanData(85.0, 120.0);
        assertThat(data.getTemperatures()).containsExactly(
                99.1, 85.0, 85.1, 94.3, 98.0, 101.0, 102.5);
    }
    
    @Test
    void longestHeatWave() {
        final WeatherData data = new WeatherData(
            List.of(100.5, 98.5, 102.0, 103.9, 87.5, 105.2, 90.3, 94.8, 109.1, 102.1, 107.4, 93.2));
        assertThat(data.longestHeatWave(100.5)).isEqualTo(3);
        assertThat(data.longestHeatWave(95.2)).isEqualTo(4);
    }
}
