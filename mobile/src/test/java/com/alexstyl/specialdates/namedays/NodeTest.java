package com.alexstyl.specialdates.namedays;

import com.alexstyl.specialdates.events.DayDate;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public abstract class NodeTest {

    abstract Node buildNode();

    @Test
    public void addingADate_isPlacedUnderTheRightNode() {
        Node node = buildNode();
        DayDate date = DayDate.newInstance(1, 2, 1990);
        node.addDate("Alex", date);

        NameCelebrations extracted = node.getDates("Alex");
        assertThat(extracted.getDate(0)).isEqualTo(date);
    }

    @Test
    public void gettingDateFromEmptyNodeReturnsNoDates() throws Exception {
        Node node = buildNode();
        NameCelebrations extracted = node.getDates("Alex");
        assertThat(extracted.getDates().size()).isZero();
    }

}
