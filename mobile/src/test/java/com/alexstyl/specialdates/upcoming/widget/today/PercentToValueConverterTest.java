package com.alexstyl.specialdates.upcoming.widget.today;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class PercentToValueConverterTest {

    // 0/5 -> 0.0
    // 1/5 -> 0.25
    // 2/5 -> 0.50
    // 3/5 -> 0.75
    // 5/5 -> 1.00

    private final PercentToValueConverter converter = new PercentToValueConverter();

    @Test
    public void testPercentToValue() {
        assertThat(converter.percentToProgress(0)).isEqualTo(0);
        assertThat(converter.percentToProgress(0.25f)).isEqualTo(1);
        assertThat(converter.percentToProgress(0.50f)).isEqualTo(2);
        assertThat(converter.percentToProgress(0.75f)).isEqualTo(3);
        assertThat(converter.percentToProgress(1.0f)).isEqualTo(4);
    }

    @Test
    public void testValueToPercentage() {
        assertThat(converter.progressToPercent(0)).isEqualTo(0);
        assertThat(converter.progressToPercent(1)).isEqualTo(0.25f);
        assertThat(converter.progressToPercent(2)).isEqualTo(0.50f);
        assertThat(converter.progressToPercent(3)).isEqualTo(0.75f);
        assertThat(converter.progressToPercent(4)).isEqualTo(1.00f);
    }
}
