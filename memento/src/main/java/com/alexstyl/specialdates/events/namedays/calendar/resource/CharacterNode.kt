package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.gsc.Index
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.ArrayNameCelebrations
import com.alexstyl.specialdates.events.namedays.ImmutableNameCelebrations
import com.alexstyl.specialdates.events.namedays.MutableNameCelebrations
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NoCelebrations
import java.text.Collator

class CharacterNode(private val keySound: Char?) : Node {
    constructor() : this(null)

    private val nodes = ArrayList<CharacterNode>()
    private var dates: MutableNameCelebrations? = null // Name & dates it is being celebrated

    override fun addDate(name: String, date: Date) {
        val index = Index(name.length)
        if (index.hasNotStarted()) {
            if (dates == null) {
                dates = ArrayNameCelebrations(name)
            }
        }
        index.stepUp()
        addDateInternal(index, name, date)
    }

    private fun addDateInternal(index: Index, word: String, date: Date) {
        var s: Char? = null
        if (!index.hasEnded()) {
            s = word[index.intValue()]
            index.stepUp()
            if (index.intValue() == word.length) {
                index.end()
            }
        }

        if (s == null) {
            if (dates == null) {
                dates = ArrayNameCelebrations(word)
            }
            dates!!.addDate(date)

        } else {
            var theNode: CharacterNode? = null
            val collator = collator
            for (node in nodes) {
                if (collator.compare(node.keySound!!.toString(), s.toString()) == 0) {
                    theNode = node
                    break
                }
            }
            if (theNode == null) {
                theNode = CharacterNode(s)
                nodes.add(theNode)
            }
            theNode.addDateInternal(index, word, date)

        }

    }

    override fun getDates(name: String): NameCelebrations? {
        val index = Index(name.length)
        if (index.hasEnded()) {
            return getDates()
        } else {
            index.stepUp()
            return getDatesInternal(index, name)
        }
    }

    private fun getDatesInternal(index: Index, name: String): NameCelebrations? {
        var s: Char? = null
        if (!index.hasEnded()) {
            s = name[index.intValue()]
            index.stepUp()
            if (index.intValue() >= name.length) {
                index.end()
            }
        }
        if (s == null) {
            return getDates()
        } else {
            val collator = collator
            for (node in nodes) {
                if (collator.compare(node.keySound!!.toString(), s.toString()) == 0) {
                    val currentIndex = index.intValue()
                    val e = node.getDatesInternal(index, name)
                    if (e != null) {
                        return e
                    }
                    index.setTo(currentIndex)
                }
            }
            return NoCelebrations(name)
        }

    }

    private fun getDates(): NameCelebrations {
        return if (dates == null) {
            NoCelebrations("")
        } else {
            dates!!
        }
    }

    companion object {

        val collator: Collator
            get() {
                val collator = Collator.getInstance()
                collator.strength = Collator.PRIMARY
                return collator
            }
    }
}
