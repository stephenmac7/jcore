package com.stephenmac7.jcore.furigana

object AnkiPresenter {
  def showFurigana(furigana: List[Reading]): String = {
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

  private def bracket(x: String) = "[" + x + "]"

  private def term(reading: Reading) = reading match {
    case (kanji, reading) => if (reading.isEmpty) kanji else kanji + bracket(reading)
  }
}
