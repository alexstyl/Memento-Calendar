package com.alexstyl.specialdates.util;

import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class HashMapListTest {

    @Test
    public void name() throws Exception {
        HashMapList<Integer, Integer> hashMapList = new HashMapList<>();
        hashMapList.addValue(1, 5);
        List<Integer> integers = hashMapList.get(1);
        assertThat(integers.get(0)).isEqualTo(5);
    }
}
