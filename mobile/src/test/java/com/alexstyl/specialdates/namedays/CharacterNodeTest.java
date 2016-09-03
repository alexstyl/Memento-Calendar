package com.alexstyl.specialdates.namedays;

import com.alexstyl.specialdates.events.namedays.calendar.CharacterNode;
import com.alexstyl.specialdates.events.namedays.calendar.Node;

public class CharacterNodeTest extends NodeTest {

    @Override
    Node buildNode() {
        return new CharacterNode();
    }

}
