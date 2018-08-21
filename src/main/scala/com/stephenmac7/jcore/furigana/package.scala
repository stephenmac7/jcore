package com.stephenmac7.jcore

import com.mariten.kanatools.KanaConverter

package object furigana {
  type Reading = (String, String)
  type RangedReading = JmdictFurigana.RangedReading

  def fromSentence(sentence: Sentence): String = {
    AnkiPresenter.showFurigana(sentence.words.flatMap(fromWord))
  }

  def fromWord(word: Word): List[Reading] = {
    val pronunciation = toHiragana(VowelGuesser.fillProlongedSoundMark(
      word.lemma_reading, word.literal_pronunciation
    ))
    val literal = (word.literal, pronunciation)
    val lemma = (word.lemma, toHiragana(word.lemma_reading))

    if (noKanji(word.literal) || word.literal_pronunciation == "*")
      List((word.literal, ""))
    else {
      lazy val noFrills = noFrillsToFurigana(word.literal, pronunciation)
      dict.get(literal).orElse(dict.get(lemma)) match {
        case Some(rrs) => {
          val jm = JmdictFurigana.toFurigana(word.literal, rrs)
          if (furiganaToReading(jm) == pronunciation)
            jm
          else
            noFrills
        }
        case None => noFrills
      }
    }
  }

  def toHiragana(x: String): String = KanaConverter.convertKana(x, KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA)

  def noKanji(word: String): Boolean = {
    // CJK Unified Ideographs Unicode Block is between '\u4E00' and '\u9FFF'
    !word.exists(c => c >= '\u4E00' && c <= '\u9FFF')
  }

  lazy val dict = JmdictFurigana.dict

  def noFrillsToFurigana(surface: String, reading: String): List[Reading] = {
    val endL = ((surface.reverse, reading.reverse).zipped takeWhile { case (s, r) => s == r }).size
    surface.splitAt(surface.length - endL) match {
      case (kanji, okurigana) =>
        val furigana = reading.take(reading.length - endL)
        val okReading = if (okurigana.isEmpty) List() else List((okurigana, ""))
        if (kanji.isEmpty) okReading else (kanji, furigana) :: okReading
    }
  }

  // Utility
  def furiganaToReading(furigana: List[Reading]): String = {
    def extractReading(readingTuple: Reading): String = readingTuple match {
      case (x, "") => x
      case (_, r) => r
    }
    furigana.map(extractReading).mkString
  }
}
