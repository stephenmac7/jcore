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

import com.stephenmac7.jcore.PartOfSpeech.PartOfSpeech
import com.stephenmac7.jcore.InflectedForm.InflectedForm

case class Word(lemma : String, // Used to test for uniqueness and to compare to dictionary
                lemma_reading: String, // Reading of the lemma itself
                lemma_pronunciation: String, // Pronunciation of the lemma
                literal: String, // The word, as it appears in the text
                literal_pronunciation: String, // Pronunciation of only this surface form
                parts_of_speech : List[PartOfSpeech], // The major part of speech for word
                conjugationForm: List[InflectedForm], // The way this word has been conjugated in this instance
                position : Int) // Position where word starts in String
{
  def is(pos: PartOfSpeech) = parts_of_speech.contains(pos)

  // I chose argonaut based on http://lollyrock.com/articles/scala-implicit-conversion/
  /*import argonaut._, Argonaut._
  val jsonFormatter = EncodeJson((w:Word) =>
      ("lemma" := w.lemma) ->:
      ("lemma_reading" := w.lemma_reading) ->:
      ("lemma_pronunciation" := w.lemma_pronunciation) ->:
      ("literal" := w.literal) ->:
      ("literal_pronunciation" := w.literal_pronunciation) ->:
      ("pos" := w.parts_of_speech.map(_.toString)) ->:
      ("conjugation" := w.conjugationForm.map(_.toString)) ->:
      ("position" := w.position) ->:
      jEmptyObject)
  val json = () => jsonFormatter(this)*/
}

case class Sentence(text : String,
                    words : List[Word])
/*{
  import argonaut._, Argonaut._
  val jsonFormatter = EncodeJson((s:Sentence) =>
      ("text" := s.text) ->:
      ("words" := s.words.map(_.json())) ->:
      jEmptyObject)
  val json = () => jsonFormatter(this)
}*/
