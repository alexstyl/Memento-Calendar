package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;

import org.junit.Test;

import static com.alexstyl.specialdates.date.DateExtKt.dateOn;
import static org.fest.assertions.api.Assertions.assertThat;

public class CharacterNodeTest {

    @Test
    public void addingADate_isPlacedUnderTheRightNode() {
        Node node = new CharacterNode();
        Date date = dateOn(1, 2, 1990);
        node.addDate("Alex", date);

        NameCelebrations extracted = node.getDates("Alex");
        assertThat(extracted.getDates().get(0)).isEqualTo(date);
    }

    @Test
    public void gettingDateFromEmptyNodeReturnsNoDates() throws Exception {
        Node node = new CharacterNode();
        NameCelebrations extracted = node.getDates("Alex");
        assertThat(extracted.getDates().size()).isZero();
    }

}
