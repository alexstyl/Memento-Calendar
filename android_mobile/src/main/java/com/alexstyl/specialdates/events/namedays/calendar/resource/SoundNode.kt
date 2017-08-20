package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.gsc.Index
import com.alexstyl.gsc.Sound
import com.alexstyl.gsc.SoundRules
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Dates
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import java.util.*


class SoundNode private constructor(
        private val keySound: Sound? // gk
) : Node {
    private val nodes = ArrayList<SoundNode>() // a b c d
    private var dates: NameCelebrations? = null // Name

    constructor() : this(null)


    /**
     * Clears the current node, by removing the NamedateBundle associated to this Node
     * and the links to all the other nodes from this one.
     */
    override fun clear() {
        nodes.clear()
        dates = null
    }

    override fun toString(): String {
        val str = StringBuilder()
        if (keySound != null) {
            str.append(keySound.toString()).append(" ")
        }
        str.append(nodes.size)
        if (dates != null) {
            str.append(" ").append(dates)
        }
        return str.toString()
    }

    /**
     * Adds the given date for the given word
     */
    override fun addDate(word: String, date: Date) {
        addDateInternal(Index(word.length), word, date)
    }

    private fun addDateInternal(index: Index, word: String, date: Date) {
        val nextSound = SoundRules.INSTANCE.getNextSound(word, false).take(100)


//        val s = if (index.hasEnded()) null else nextSound
//        if (s == null) {
//            if (dates == null) {
//                dates = NameCelebrations(word)
//            }
//            dates!!.addDate(date)
//
//        } else {
//            var theNode: SoundNode? = null
//            for (node in nodes!!) {
//                if (node.keySound == s) {
//                    theNode = node
//                    break
//                }
//            }
//            if (theNode == null) {
//                theNode = SoundNode(s)
//                nodes.add(theNode)
//            }
//            theNode.addDateInternal(index, word, date)
//
//        }
    }

    override fun getDates(name: String): NameCelebrations {
//        var nextSound = SoundRules.INSTANCE.getNextSound(name, false)
//
//
//        val s = if (index.hasEnded()) null else SoundRules.getInstance().getNextSound(name, index, false)
//        if (s == null) {
//            return getDates()
//        } else {
//            for (node in nodes) {
//                if (node.keySound.equals(s)) {
//                    val currentIndex = index.intValue()
//                    val e = node.getDatesInternal(index, name)
//                    if (e != null) {
//                        return e
//                    }
//                    index.setTo(currentIndex)
//                }
//            }
//            return NameCelebrations(name)
//        }
        return NO_NAMEDAYS
    }


    private fun getDates(): NameCelebrations {
        return if (dates == null) {
            NO_NAMEDAYS
        } else {
            dates!!
        }
    }

    private val NO_NAMEDAYS = NameCelebrations("", Dates())
}
