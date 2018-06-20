/*
 * Copyright 2016 Stephen McIntosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stephenmac7.jcore

import com.mariten.kanatools.KanaConverter
import com.mariten.kanatools.KanaAppraiser

import scala.io.Source

object Furigana {
  type Reading = (String, String)
  type RangedReading = ((Int, Int), String)
  type FuriganaDict = Map[(String, String), Array[RangedReading]]

  // Kana Functions
  def toHiragana(x: String): String = {
    // Imperative code to work with long vowels for now
    val vowelMap: Map[Char, Char] = Map(
      'あ' -> 'あ', 'い' -> 'い', 'う' -> 'う', 'え' -> 'い', 'お' -> 'う',
      'か' -> 'あ', 'き' -> 'い', 'く' -> 'う', 'け' -> 'い', 'こ' -> 'う',
      'さ' -> 'あ', 'し' -> 'い', 'す' -> 'う', 'せ' -> 'い', 'そ' -> 'う',
      'た' -> 'あ', 'ち' -> 'い', 'つ' -> 'う', 'て' -> 'い', 'と' -> 'う',
      'な' -> 'あ', 'に' -> 'い', 'ぬ' -> 'う', 'ね' -> 'い', 'の' -> 'う',
      'は' -> 'あ', 'ひ' -> 'い', 'ふ' -> 'う', 'へ' -> 'い', 'ほ' -> 'う',
      'ま' -> 'あ', 'み' -> 'い', 'む' -> 'う', 'め' -> 'い', 'も' -> 'う',
      'や' -> 'あ', 'ゆ' -> 'う', 'よ' -> 'う',
      'ら' -> 'あ', 'り' -> 'い', 'る' -> 'う', 'れ' -> 'い', 'ろ' -> 'う',
      'わ' -> 'あ',
      'ゃ' -> 'あ', 'ゅ' -> 'う', 'ょ' -> 'う',

      'が' -> 'あ', 'ぎ' -> 'い', 'ぐ' -> 'う', 'げ' -> 'い', 'ご' -> 'う',
      'ざ' -> 'あ', 'じ' -> 'い', 'ず' -> 'う', 'ぜ' -> 'い', 'ぞ' -> 'う',
      'だ' -> 'あ', 'ぢ' -> 'い', 'づ' -> 'う', 'で' -> 'い', 'ど' -> 'う',
      'ば' -> 'あ', 'び' -> 'い', 'ぶ' -> 'う', 'べ' -> 'い', 'ぼ' -> 'う',
      'ぱ' -> 'あ', 'ぴ' -> 'い', 'ぷ' -> 'う', 'ぺ' -> 'い', 'ぽ' -> 'う'
    )
    var lastVowel: Option[Char] = None
    KanaConverter.convertKana(x, KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA).map((c: Char) =>
      if (c == 'ー') lastVowel match {
        case Some(v) => v
        case None => c
      }
      else {
        lastVowel = vowelMap.get(c)
        c
      }
    )
  }

  /** Checks whether a word has kanji */
  def noReading(word: String): Boolean = {
    def inRange(lower: Char, upper: Char)(c: Char): Boolean = c >= lower && c <= upper
    !word.exists(inRange('\u4E00', '\u9FFF')) // Checks for characters in CJK Unified Ideographs Unicode Block
  }

  // Dealing with the input file
  /** Reads a file to FuriganaDict */
  lazy val dict: FuriganaDict =
    Source.fromInputStream(getClass.getResourceAsStream("/JmdictFurigana.txt"), "UTF-8").getLines().map(readEntry).toMap

  /** Reads a JmdictEntry */
  def readEntry(entryS: String): ((String, String), Array[RangedReading]) =
    entryS.split('|') match {
      case Array(kanji, kana, guideString) =>
        val rangedReadings = guideString.split(';') map rangedReading
        (kanji, kana) -> rangedReadings
    }

  /** Returns a RangedReading from a JmdictFurigana entry's third field. */
  def rangedReading(x: String): RangedReading =
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
  def rangeStringToTuple(x: String): (Int, Int) = {
    x.split('-') match {
      case Array(lower, upper) => (lower.toInt, upper.toInt)
      case Array(idxS) =>
        val idx = idxS.toInt
        (idx, idx)
    }
  }

  // Converting to Furigana
  def noFrillsToFurigana(surface: String, reading: String): List[Reading] = {
    val endL = ((surface.reverse, reading.reverse).zipped takeWhile { case (s, r) => s == r }).size
    surface.splitAt(surface.length - endL) match {
      case (kanji, okurigana) =>
        val furigana = reading.take(reading.length - endL)
        val okReading = if (okurigana.isEmpty) List() else List((okurigana, ""))
        if (kanji.isEmpty) okReading else (kanji, furigana) :: okReading
    }
  }

  def jmdToFurigana(kanji: String, furigana: Array[RangedReading]): List[Reading] = {
    def toFurigana(n: Int, kanji: String, pats: Array[RangedReading]): List[Reading] = {
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
            case (k0, k1) => (k0, "") :: toFurigana(lower, k1, pats)
          }
          // The first pattern matches a prefix of kanji
          else kanji.splitAt(upper - lower + 1) match {
            case (k0, k1) => (k0, reading) :: toFurigana(upper + 1, k1, pats.tail)
          }
        }
      }
    }
    toFurigana(0, kanji, furigana)
  }

  // Utility
  def furiganaToReading(furigana: List[Reading]): String = {
    def extractReading(readingTuple: Reading): String = readingTuple match {
      case (x, "") => x
      case (_, r) => r
    }
    furigana.map(extractReading) mkString ""
  }

  def showFurigana(furigana: List[Reading]): String = {
    def bracket(x: String) = "[" + x + "]"
    def term(reading: Reading) = reading match {
      case (kanji, reading) => if (reading.isEmpty) kanji else kanji + bracket(reading)
    }
    // Imperative code, but cleaner for this case
    var hadReading = true
    var result = StringBuilder.newBuilder
    furigana.foreach(r => {
      if (r._2.isEmpty) {
        hadReading = false
        result.append(r._1)
      }
      else {
        if (!hadReading) result.append(' ')
        hadReading = true
        result.append(term(r))
      }
    })
    result.toString
  }

  def fromWord(word: Word): List[Reading] = {
    lazy val literal = (word.literal, toHiragana(word.literal_pronunciation))
    lazy val lemma = (word.lemma, toHiragana(word.lemma_pronunciation))
    if (noReading(word.literal) || word.literal_pronunciation == "*")
      List((word.literal, ""))
    else {
      lazy val noFrills = noFrillsToFurigana(word.literal, literal._2)
      dict.get(literal).orElse(dict.get(lemma)) match {
        case Some(rrs) => {
          val jm = jmdToFurigana(word.literal, rrs)
          if (furiganaToReading(jm) == literal._2) jm else noFrills
        }
        case None => noFrills
      }
    }
  }
  def fromSentence(sentence: Sentence): String = {
    showFurigana(sentence.words.flatMap(fromWord))
  }
}
