package com.alexstyl.specialdates.events.namedays.calendar;

import com.alexstyl.gsc.Index;
import com.alexstyl.gsc.Sound;
import com.alexstyl.gsc.SoundRules;
import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.events.namedays.Dates;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SoundNode implements Serializable, Node {

    private static final NameCelebrations NO_NAMEDAYS = new NameCelebrations("", new Dates());

    private Sound keySound; // gk
    private List<SoundNode> nodes; // a b c d
    private NameCelebrations dates; // Name

    public SoundNode() {
        this(null);
    }

    private SoundNode(Sound s) {
        this.keySound = s;
        this.nodes = new ArrayList<>();
    }

    /**
     * Clears the current node, by removing the NamedateBundle associated to this Node
     * and the links to all the other nodes from this one.
     */
    @Override
    public void clear() {
        nodes.clear();
        dates = null;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (keySound != null) {
            str.append(keySound.toString()).append(" ");
        }
        if (nodes != null) {
            str.append(nodes.size());
        }
        if (dates != null) {
            str.append(" ").append(dates);
        }
        return str.toString();
    }

    /**
     * Adds the given date for the given word
     */
    @Override
    public void addDate(String word, DayDate date) {
        addDateInternal(new Index(word.length()), word, date);
    }

    private void addDateInternal(Index index, String word, DayDate date) {
        Sound s = (index.hasEnded() ? null : SoundRules.getInstance().getNextSound(word, index, false));
        if (s == null) {
            if (dates == null) {
                dates = new NameCelebrations(word);
            }
            dates.addDate(date);

        } else {
            SoundNode theNode = null;
            for (SoundNode node : nodes) {
                if (node.keySound.equals(s)) {
                    theNode = node;
                    break;
                }
            }
            if (theNode == null) {
                theNode = new SoundNode(s);
                nodes.add(theNode);
            }
            theNode.addDateInternal(index, word, date);

        }
    }

    @Override
    public NameCelebrations getDates(String name) {
        return getDatesInternal(new Index(name.length()), name);

    }

    private NameCelebrations getDatesInternal(Index index, String name) {
        Sound s = (index.hasEnded() ? null : SoundRules.getInstance().getNextSound(name, index, false));
        if (s == null) {
            return getDates();
        } else {
            for (SoundNode node : nodes) {
                if (node.keySound.equals(s)) {
                    int currentIndex = index.intValue();
                    NameCelebrations e = node.getDatesInternal(index, name);
                    if (e != null) {
                        return e;
                    }
                    index.setTo(currentIndex);
                }
            }
            return new NameCelebrations(name);
        }
    }

    private NameCelebrations getDates() {
        if (dates == null) {
            return NO_NAMEDAYS;
        } else {
            return dates;
        }
    }

}
