package com.alexstyl.specialdates.namedays;

import com.alexstyl.specialdates.events.namedays.CharacterNode;
import com.alexstyl.specialdates.events.namedays.Node;

public class CharacterNodeTest extends NodeTest {

    @Override
    Node buildNode() {
        return new CharacterNode();
    }

}
