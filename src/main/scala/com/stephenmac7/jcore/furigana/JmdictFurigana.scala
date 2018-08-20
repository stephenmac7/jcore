package com.stephenmac7.jcore.furigana

import scala.io.Source

object JmdictFurigana {
  type FuriganaDict = Map[(String, String), Array[RangedReading]]
  type RangedReading = ((Int, Int), String)

  def toFurigana(kanji: String, furigana: Array[RangedReading]): List[Reading] = {
    toFuriganaWithIndex(0, kanji, furigana)
  }

  private def toFuriganaWithIndex(n: Int, kanji: String, pats: Array[RangedReading]): List[Reading] = {
    if (kanji.isEmpty) List() // Nothing left to parse
    else {
      if (pats.isEmpty) List((kanji, "")) // No more readings, but there are more characters (should be kana)
      else {
        val lower = pats.head._1._1
        val upper = pats.head._1._2
        val reading = pats.head._2

        // The next pattern starts further in the kanji sequence,
        // so the furigana starts with a sequence which has no reading
        if (n < lower) kanji.splitAt(lower - n) match {
          case (k0, k1) => (k0, "") :: toFuriganaWithIndex(lower, k1, pats)
        }
        // The first pattern matches a prefix of kanji
        else kanji.splitAt(upper - lower + 1) match {
          case (k0, k1) => (k0, reading) :: toFuriganaWithIndex(upper + 1, k1, pats.tail)
        }
      }
    }
  }

  /** Reads a file to FuriganaDict */
  lazy val dict: FuriganaDict =
    Source.fromInputStream(getClass.getResourceAsStream("/JmdictFurigana.txt"), "UTF-8").getLines().map(readEntry).toMap

  /** Reads a JmdictEntry */
  private def readEntry(entryS: String): ((String, String), Array[RangedReading]) =
    entryS.split('|') match {
      case Array(kanji, kana, guideString) =>
        val rangedReadings = guideString.split(';') map rangedReading
        (kanji, kana) -> rangedReadings
    }

  /** Returns a RangedReading from a JmdictFurigana entry's third field. */
  private def rangedReading(x: String): RangedReading =
    x.split(':') match {
      case Array(rangeS, reading) => (rangeStringToTuple(rangeS), reading)
    }

  /** Returns inclusive range from a String
    *
    * {{{
    *   scala> rangeStringToTuple "1-3"
    *   res0: (Int, Int) = (1,3)
    *   scala> rangeStringToTuple "5"
    *   res1: (Int, Int) = (5,5)
    * }}}
    */
  private def rangeStringToTuple(x: String): (Int, Int) = {
    x.split('-') match {
      case Array(lower, upper) => (lower.toInt, upper.toInt)
      case Array(idxS) =>
        val idx = idxS.toInt
        (idx, idx)
    }
  }
}
