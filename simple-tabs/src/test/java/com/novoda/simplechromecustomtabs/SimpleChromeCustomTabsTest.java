package com.novoda.simplechromecustomtabs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class SimpleChromeCustomTabsTest {

    @Test
    public void whenGettingInstance_thenInstanceIsReturned() {
        assertThat(SimpleChromeCustomTabs.getInstance()).isInstanceOf(SimpleChromeCustomTabs.class);
    }
}
