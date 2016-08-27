package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.SoundWordComparator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class NameFilterTest {

    private NameFilter greekNameFilter;

    @Before
    public void setUp() throws Exception {
        List<String> allNames = new ArrayList<>();
        allNames.add("Γιώργος");
        greekNameFilter = new NameFilter(allNames, new SoundWordComparator());

    }

    @Test
    public void givenTheNameInGreeklish_thenTheNameIsReturned() throws Exception {
        List<String> giwrgos = greekNameFilter.performFiltering("Giwrgos");
        assertThat(giwrgos.get(0)).isEqualTo("Γιώργος");
    }

    @Test
    public void givenPartOfTheNameInGreeklish_thenTheNameIsReturned() throws Exception {
        List<String> giwrgos = greekNameFilter.performFiltering("Giwrgo");
        assertThat(giwrgos.get(0)).isEqualTo("Γιώργος");
    }

    @Test
    public void givenTheNameInGreek_thenTheNameIsReturned() throws Exception {
        List<String> giwrgos = greekNameFilter.performFiltering("Γιώργος");
        assertThat(giwrgos.get(0)).isEqualTo("Γιώργος");
    }

    @Test
    public void givenPartOfTheNameInGreek_thenTheNameIsReturned() throws Exception {
        List<String> giwrgos = greekNameFilter.performFiltering("Γιώργο");
        assertThat(giwrgos.get(0)).isEqualTo("Γιώργος");
    }
}
