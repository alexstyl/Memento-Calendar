package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.gsc.Sound
import com.alexstyl.gsc.SoundRules
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import java.util.*


class SoundNode private constructor(private val keySound: Sound?) : Node {
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

    /**
     * Adds the given date for the given word
     */
    override fun addDate(word: String, date: Date) {
        val nextSounds = SoundRules.INSTANCE.getNextSound(word, false)
        addDate(word, date, nextSounds)
    }

    fun addDate(word: String, date: Date, iterator: Iterator<Sound?>) {
        if (!iterator.hasNext()) {
            if (dates == null) {
                dates = NameCelebrations(word)
            }
            dates!!.addDate(date)
        } else {
            var theNode: SoundNode? = null
            iterator.forEach { s ->
                nodes.forEach { node ->
                    if (node.keySound!!.soundsLike(s!!)) {
                        theNode = node
                        return@forEach
                    }
                }
                if (theNode == null) {
                    theNode = SoundNode(s)
                    nodes.add(theNode!!)
                }
                theNode!!.addDate(word, date, iterator)
            }
        }
    }

    override fun getDates(name: String): NameCelebrations? = getDates(name, SoundRules.INSTANCE.getNextSound(name, false))

    fun getDates(name: String, iterator: Iterator<Sound?>): NameCelebrations? {
        if (!iterator.hasNext()) {
            return getNameCelebrations(name)
        } else {
            iterator.forEach { s ->
                nodes.forEach { node ->
                    if (node.keySound!!.soundsLike(s!!)) {
                        val nameCelebrations = node.getDates(name, iterator)
                        if (nameCelebrations != null) {
                            return nameCelebrations
                        }
                    }
                }
            }
            return NameCelebrations(name)
        }
    }

    private fun getNameCelebrations(name: String): NameCelebrations {
        return if (dates == null) {
            NameCelebrations(name)
        } else {
            dates!!
        }
    }

}
