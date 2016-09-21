package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.gsc.Index;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.Dates;
import com.alexstyl.specialdates.events.namedays.NameCelebrations;

import java.text.Collator;
import java.util.ArrayList;

public class CharacterNode implements Node {

    private static final NameCelebrations NO_NAMEDAYS = new NameCelebrations("", new Dates());

    private final Character keySound;
    private final ArrayList<CharacterNode> nodes;
    private NameCelebrations dates; // Name & dates it is being celebrated

    public CharacterNode() {
        this(null);
    }

    private CharacterNode(Character s) {
        this.keySound = s;
        this.nodes = new ArrayList<>();
    }

    @Override
    public void clear() {
        this.nodes.clear();
        this.dates = null;
    }

    @Override
    public void addDate(String name, Date date) {
        Index index = new Index(name.length());
        if (index.hasNotStarted()) {
            if (dates == null) {
                dates = new NameCelebrations(name);
            }
        }
        index.stepUp();
        addDateInternal(index, name, date);
    }

    private void addDateInternal(Index index, String word, Date date) {
        Character s = null;
        if (!index.hasEnded()) {
            s = word.charAt(index.intValue());
            index.stepUp();
            if (index.intValue() == word.length()) {
                index.end();
            }
        }

        if (s == null) {
            if (dates == null) {
                dates = new NameCelebrations(word);
            }
            dates.addDate(date);

        } else {
            CharacterNode theNode = null;
            Collator collator = getCollator();
            for (CharacterNode node : nodes) {
                if (collator.compare(node.keySound.toString(), s.toString()) == 0) {
                    theNode = node;
                    break;
                }
            }
            if (theNode == null) {
                theNode = new CharacterNode(s);
                nodes.add(theNode);
            }
            theNode.addDateInternal(index, word, date);

        }

    }

    @Override
    public NameCelebrations getDates(String name) {
        Index index = new Index(name.length());
        if (index.hasEnded()) {
            return getDates();
        } else {
            index.stepUp();
            return getDatesInternal(index, name);
        }
    }

    private NameCelebrations getDatesInternal(Index index, String name) {
        Character s = null;
        if (!index.hasEnded()) {
            s = name.charAt(index.intValue());
            index.stepUp();
            if (index.intValue() >= name.length()) {
                index.end();
            }
        }
        if (s == null) {
            return getDates();
        } else {
            Collator collator = getCollator();
            for (CharacterNode node : nodes) {
                if (collator.compare(node.keySound.toString(), s.toString()) == 0) {
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

    public static Collator getCollator() {
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.PRIMARY);
        return collator;
    }

    private NameCelebrations getDates() {
        if (dates == null) {
            return NO_NAMEDAYS;
        } else {
            return dates;
        }
    }
}
