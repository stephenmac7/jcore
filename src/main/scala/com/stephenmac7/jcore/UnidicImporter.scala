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

import com.atilika.kuromoji.unidic.{Token, Tokenizer}
import com.stephenmac7.jcore.PartOfSpeech._
import com.stephenmac7.jcore.InflectedForm._

import scala.collection.JavaConverters._

object UnidicImporter {
  val tokenizer = new Tokenizer()

  def getTokens(s : String): List[Token] = tokenizer.tokenize(s).asScala.toList

  def toSentence(text : String): Sentence = {
    val words = getTokens(text) map toWord
    new Sentence(text, words)
  }

  def toWord(token : Token): Word = {
    val literal = token.getSurface
    new Word(token.getLemma, token.getLemmaReadingForm, token.getPronunciationBaseForm, literal, token.getPronunciation,
      toPartsOfSpeech(token), (token.getConjugationForm split '-' map toInflectedForm).toList, token.getPosition)
  }

  def toPartsOfSpeech(token : Token): List[PartOfSpeech] = {
    val posList = List(token.getPartOfSpeechLevel1, token.getPartOfSpeechLevel2, token.getPartOfSpeechLevel3, token.getPartOfSpeechLevel4)
    posList.filter(x => x != "*").map(toPartOfSpeech(_))
  }

  // From https://gist.github.com/masayu-a/3e11168f9330e2d83a68
  val  toInflectedForm: Function[String, InflectedForm] = {
    case "ク語法" => InflectedForm.KuWording
    case "仮定形" => InflectedForm.Conditional
    case "一般" => InflectedForm.General
    case "融合" => InflectedForm.Integrated
    case "命令形" => InflectedForm.Imperative
    case "已然形" => InflectedForm.Realis
    case "補助" => InflectedForm.AuxiliaryInflection
    case "意志推量形" => InflectedForm.VolitionalTentative
    case "未然形" => InflectedForm.Irrealis
    case "サ" => InflectedForm.Sa
    case "セ" => InflectedForm.Se
    case "撥音便" => InflectedForm.EuphonicChangeN
    case "終止形" => InflectedForm.Conclusive
    case "ウ音便" => InflectedForm.EuphonicChangeU
    case "促音便" => InflectedForm.EuphonicChangeT
    case "語幹" => InflectedForm.WordStem
    case "連体形" => InflectedForm.Attributive
    case "イ音便" => InflectedForm.EuphonicChangeI
    case "省略" => InflectedForm.Abbreviation
    case "連用形" => InflectedForm.Continuative
    case "ト" => InflectedForm.ChangeTo
    case "ニ" => InflectedForm.ChangeNi
    case "長音" => InflectedForm.LongSound
    case "*" => InflectedForm.Uninflected
    case "_" => InflectedForm.UnknownInflection
  }
  // See https://gist.github.com/masayu-a/e3eee0637c07d4019ec9
  val toPartOfSpeech: Function[String, PartOfSpeech] = {
    case "代名詞" => PartOfSpeech.Pronoun
    case "副詞" => PartOfSpeech.Adverb
    case "助動詞" => PartOfSpeech.AuxiliaryVerb
    case "助詞" => PartOfSpeech.Particle
    case "係助詞" => PartOfSpeech.Binding
    case "副助詞" => PartOfSpeech.Adverbial
    case "接続助詞" => PartOfSpeech.Conjunctive
    case "格助詞" => PartOfSpeech.Case
    case "準体助詞" => PartOfSpeech.Nominal
    case "終助詞" => PartOfSpeech.PhraseFinal
    case "動詞" => PartOfSpeech.Verb
    case "一般" => PartOfSpeech.General
    case "非自立可能" => PartOfSpeech.Bound
    case "名詞" => PartOfSpeech.Noun
    case "助動詞語幹" => PartOfSpeech.Auxiliary
    case "固有名詞" => PartOfSpeech.Proper
    case "人名" => PartOfSpeech.Name
    case "名" => PartOfSpeech.Firstname
    case "姓" => PartOfSpeech.Surname
    case "地名" => PartOfSpeech.Place
    case "国" => PartOfSpeech.Country
    case "数詞" => PartOfSpeech.Numeral
    case "普通名詞" => PartOfSpeech.Common
    case "サ変可能" => PartOfSpeech.VerbalSuru
    case "サ変形状詞可能" => PartOfSpeech.VerbalAdjectival
    case "副詞可能" => PartOfSpeech.AdverbialSuffix
    case "助数詞可能" => PartOfSpeech.Counter
    case "形状詞可能" => PartOfSpeech.Adjectival
    case "形容詞" => PartOfSpeech.AdjectiveI
    case "形状詞" => PartOfSpeech.AdjectivalNoun
    case "タリ" => PartOfSpeech.Tari
    case "感動詞" => PartOfSpeech.Interjection
    case "フィラー" => PartOfSpeech.Filler
    case "接尾辞" => PartOfSpeech.Suffix
    case "動詞的" => PartOfSpeech.Verbal
    case "名詞的" => PartOfSpeech.NominalSuffix
    case "助数詞" => PartOfSpeech.CounterSuffix
    case "形容詞的" => PartOfSpeech.AdjectiveISuffix
    case "形状詞的" => PartOfSpeech.AdjectivalNounSuffix
    case "接続詞" => PartOfSpeech.Conjunction
    case "接頭辞" => PartOfSpeech.Prefix
    case "空白" => PartOfSpeech.Whitespace
    case "補助記号" => PartOfSpeech.SupplementarySymbol
    case "ＡＡ" => PartOfSpeech.AsciiArt
    case "顔文字" => PartOfSpeech.Emoticon
    case "句点" => PartOfSpeech.Period
    case "括弧閉" => PartOfSpeech.BracketOpen
    case "括弧開" => PartOfSpeech.BracketClose
    case "読点" => PartOfSpeech.Comma
    case "記号" => PartOfSpeech.Symbol
    case "文字" => PartOfSpeech.Character
    case "連体詞" => PartOfSpeech.Adnominal
    case "未知語" => PartOfSpeech.UnknownWords
    case "カタカナ文" => PartOfSpeech.Katakana
    case "漢文" => PartOfSpeech.ChineseWriting
    case "言いよどみ" => PartOfSpeech.Hesitation
    case "web誤脱" => PartOfSpeech.ErrorsOmissions
    case "方言" => PartOfSpeech.Dialect
    case "ローマ字文" => PartOfSpeech.LatinAlphabet
    case "新規未知語" => PartOfSpeech.NewUnknownWords
    case _            => PartOfSpeech.UnknownPartOfSpeech
  }
}
