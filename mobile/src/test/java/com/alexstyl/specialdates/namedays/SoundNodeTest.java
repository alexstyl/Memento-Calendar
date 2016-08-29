package com.alexstyl.specialdates.namedays;

import com.alexstyl.specialdates.events.namedays.Node;
import com.alexstyl.specialdates.events.namedays.SoundNode;

public class SoundNodeTest extends NodeTest {

    @Override
    Node buildNode() {
        return new SoundNode();
    }

}
