package com.alexstyl.specialdates.namedays;

import com.alexstyl.specialdates.events.namedays.calendar.Node;
import com.alexstyl.specialdates.events.namedays.calendar.SoundNode;

public class SoundNodeTest extends NodeTest {

    @Override
    Node buildNode() {
        return new SoundNode();
    }

}
