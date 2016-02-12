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

object InflectedForm extends Enumeration {
  type InflectedForm = Value
  val KuWording, Conditional, General, Integrated, Imperative, Realis,
      AuxiliaryInflection, VolitionalTentative, Irrealis, Sa, Se, EuphonicChangeN,
      Conclusive, EuphonicChangeU, EuphonicChangeT, WordStem, Attributive,
      EuphonicChangeI, Abbreviation, Continuative, ChangeTo, ChangeNi,
      LongSound, Uninflected, UnknownInflection = Value
}

object PartOfSpeech extends Enumeration {
  type PartOfSpeech = Value
  val Pronoun, Adverb, AuxiliaryVerb, Particle, Binding, Adverbial,
      Conjunctive, Case, Nominal, PhraseFinal, Verb, General, Bound, Noun,
      Auxiliary, Proper, Name, Firstname, Surname, Place, Country, Numeral,
      Common, VerbalSuru, VerbalAdjectival, AdverbialSuffix, Counter,
      Adjectival, AdjectiveI, AdjectivalNoun, Tari, Interjection, Filler,
      Suffix, Verbal, NominalSuffix, CounterSuffix, AdjectiveISuffix,
      AdjectivalNounSuffix, Conjunction, Prefix, Whitespace,
      SupplementarySymbol, AsciiArt, Emoticon, Period, BracketOpen,
      BracketClose, Comma, Symbol, Character, Adnominal, UnknownWords,
      Katakana, ChineseWriting, Hesitation, ErrorsOmissions, Dialect,
      LatinAlphabet, NewUnknownWords, UnknownPartOfSpeech = Value
}
