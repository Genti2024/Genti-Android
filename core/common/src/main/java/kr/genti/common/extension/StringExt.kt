package kr.genti.common.extension

import java.text.BreakIterator

fun String.getGraphemeLength(): Int {
    val breakIterator: BreakIterator = BreakIterator.getCharacterInstance()

    breakIterator.setText(this)

    var count = 0
    while (breakIterator.next() != BreakIterator.DONE) {
        count++
    }

    return count
}

fun String.breakLines(): String = this.replace(Regex("\\s+"), " ")