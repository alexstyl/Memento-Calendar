package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.gsc.Sound
import com.alexstyl.gsc.SoundRules
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NoNameCelebrations
import com.alexstyl.specialdates.events.namedays.ArrayNameCelebrations
import com.alexstyl.specialdates.events.namedays.MutableNameCelebrations


class SoundNode private constructor(private val keySound: Sound?) : Node {
    constructor() : this(null)

    private val nodes = ArrayList<SoundNode>() // a b c d
    private var dates: MutableNameCelebrations? = null




    /**
     * Adds the given date for the given word
     */
    override fun addDate(word: String, date: Date) {
        val nextSounds = SoundRules.INSTANCE.getNextSound(word, false)
        addDate(word, date, nextSounds)
    }

    private fun addDate(word: String, date: Date, iterator: Iterator<Sound?>) {
        if (!iterator.hasNext()) {
            if (dates == null) {
                dates = ArrayNameCelebrations(word)
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

    private fun getDates(name: String, iterator: Iterator<Sound?>): NameCelebrations? {
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
            return NoNameCelebrations(name)
        }
    }

    private fun getNameCelebrations(name: String): NameCelebrations {
        return if (dates == null) {
            NoNameCelebrations(name)
        } else {
            dates!!
        }
    }

}
