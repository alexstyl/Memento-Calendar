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
    public void setUp() {
        List<String> allNames = new ArrayList<>();
        allNames.add("Γιώργος");
        allNames.add("Αλέξανδρος");
        greekNameFilter = new NameFilter(allNames, new SoundWordComparator());

    }

    @Test
    public void givenTheNameInGreeklish_thenTheNameIsReturned() {
        List<String> giwrgos = greekNameFilter.performFiltering("Giwrgos");
        assertThat(giwrgos.get(0)).isEqualTo("Γιώργος");
    }

    @Test
    public void givenPartOfTheNameInGreeklish_thenTheNameIsReturned() {
        List<String> giwrgos = greekNameFilter.performFiltering("Giwrgo");
        assertThat(giwrgos.get(0)).isEqualTo("Γιώργος");
    }

    @Test
    public void givenTheNameInGreek_thenTheNameIsReturned() {
        List<String> giwrgos = greekNameFilter.performFiltering("Γιώργος");
        assertThat(giwrgos.get(0)).isEqualTo("Γιώργος");
    }

    @Test
    public void givenPartOfTheNameInGreek_thenTheNameIsReturned() {
        List<String> giwrgos = greekNameFilter.performFiltering("Γιώργο");
        assertThat(giwrgos.get(0)).isEqualTo("Γιώργος");
    }

}
