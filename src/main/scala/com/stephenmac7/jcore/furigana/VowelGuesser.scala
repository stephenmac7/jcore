package com.stephenmac7.jcore.furigana

object VowelGuesser {
  private final val VOWELS: Map[Char, Char] = Map(
    'ア' -> 'ア', 'イ' -> 'イ', 'ウ' -> 'ウ', 'エ' -> 'イ', 'オ' -> 'ウ',
    'カ' -> 'ア', 'キ' -> 'イ', 'ク' -> 'ウ', 'ケ' -> 'イ', 'コ' -> 'ウ',
    'サ' -> 'ア', 'シ' -> 'イ', 'ス' -> 'ウ', 'セ' -> 'イ', 'ソ' -> 'ウ',
    'タ' -> 'ア', 'チ' -> 'イ', 'ツ' -> 'ウ', 'テ' -> 'イ', 'ト' -> 'ウ',
    'ナ' -> 'ア', 'ニ' -> 'イ', 'ヌ' -> 'ウ', 'ネ' -> 'イ', 'ノ' -> 'ウ',
    'ハ' -> 'ア', 'ヒ' -> 'イ', 'フ' -> 'ウ', 'ヘ' -> 'イ', 'ホ' -> 'ウ',
    'マ' -> 'ア', 'ミ' -> 'イ', 'ム' -> 'ウ', 'メ' -> 'イ', 'モ' -> 'ウ',
    'ヤ' -> 'ア', 'ユ' -> 'ウ', 'ヨ' -> 'ウ',
    'ラ' -> 'ア', 'リ' -> 'イ', 'ル' -> 'ウ', 'レ' -> 'イ', 'ロ' -> 'ウ',
    'ワ' -> 'ア',
    'ャ' -> 'ア', 'ュ' -> 'ウ', 'ョ' -> 'ウ',
    'ガ' -> 'ア', 'ギ' -> 'イ', 'グ' -> 'ウ', 'ゲ' -> 'イ', 'ゴ' -> 'ウ',
    'ザ' -> 'ア', 'ジ' -> 'イ', 'ズ' -> 'ウ', 'ゼ' -> 'イ', 'ゾ' -> 'ウ',
    'ダ' -> 'ア', 'ヂ' -> 'イ', 'ヅ' -> 'ウ', 'デ' -> 'イ', 'ド' -> 'ウ',
    'バ' -> 'ア', 'ビ' -> 'イ', 'ブ' -> 'ウ', 'ベ' -> 'イ', 'ボ' -> 'ウ',
    'パ' -> 'ア', 'ピ' -> 'イ', 'プ' -> 'ウ', 'ペ' -> 'イ', 'ポ' -> 'ウ'
  )

  private final val ALTERNATE_VOWELS: Map[Char, Char] = Map(
    'エ' -> 'エ', 'オ' -> 'オ',
    'ケ' -> 'エ', 'コ' -> 'オ',
    'セ' -> 'エ', 'ソ' -> 'オ',
    'テ' -> 'エ', 'ト' -> 'オ',
    'ネ' -> 'エ', 'ノ' -> 'オ',
    'ヘ' -> 'エ', 'ホ' -> 'オ',
    'メ' -> 'エ', 'モ' -> 'オ',
    'ヨ' -> 'オ',
    'レ' -> 'エ', 'ロ' -> 'オ',
    'ョ' -> 'オ',
    'ゲ' -> 'エ', 'ゴ' -> 'オ',
    'ゼ' -> 'エ', 'ゾ' -> 'オ',
    'デ' -> 'エ', 'ド' -> 'オ',
    'ベ' -> 'エ', 'ボ' -> 'オ',
    'ペ' -> 'エ', 'ポ' -> 'オ'
  )

  // Requires: index < string.length
  def guess(string: String, index: Int): Char = {
    if (index == 0) {
      string.charAt(0)
    }
    else {
      VOWELS.getOrElse(string.charAt(index - 1), 'ー')
    }
  }

  def valid(string: String, index: Int, char: Char): Boolean = {
    if (index == 0) {
      return true
    }
    val prev = string.charAt(index - 1)
    val cur = string.charAt(index)

    VOWELS.get(prev).contains(char) || ALTERNATE_VOWELS.get(prev).contains(char)
  }

  def fillProlongedSoundMark(withVowels: String, withoutVowels: String): String = {
    withoutVowels.zipWithIndex.map{
      case (character, index) =>
        if (character == 'ー') {
          if (index < withVowels.length) {
            val other_char = withVowels.charAt(index)
            if (valid(withoutVowels, index, other_char)) other_char else guess(withoutVowels, index)
          }
          else {
            guess(withoutVowels, index)
          }
        }
        else {
          character
        }
    }.mkString
  }
}
