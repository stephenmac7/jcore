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

import org.specs2.mutable.Specification

class FuriganaTest extends Specification {
  def furigana(text: String) = Furigana.fromSentence(UnidicImporter.toSentence(text))

  "Furigana Spec" >> {
    "general" >> {
      // にっぽん? にほん?
      furigana("日本はブラジルからコーヒーを輸入している。") must_== "日本[にっぽん]はブラジルからコーヒーを 輸[ゆ]入[にゅう]している。"
      furigana("活発にボランティア活動を行う。") must_== "活[かっ]発[ぱつ]にボランティア 活[かつ]動[どう]を 行[おこな]う。"
      furigana("投げるな。最後まで粘れ") must_== "投[な]げるな。 最[さい]後[ご]まで 粘[ねば]れ"
      furigana("火星は肉眼でも見える。") must_== "火[か]星[せい]は 肉[にく]眼[がん]でも 見[み]える。"
      furigana("彼の存在そのものが我々に災いをもたらすのだ") must_== "彼[かれ]の 存[そん]在[ざい]そのものが 我[われ]々[われ]に 災[わざわ]いをもたらすのだ"
      furigana("私達は南米を旅行した。") must_== "私[わたくし]達[たち]は 南[なん]米[べい]を 旅[りょ]行[こう]した。"
    }

    "quotes" >> {
      // Maybe it shouldn't output わたくし...
      furigana("私が一番感動した映画は「生きる」です。") must_== "私[わたくし]が 一[いち]番[ばん]感[かん]動[どう]した 映[えい]画[が]は「 生[い]きる」です。"
    }

    "tricky" >> {
      furigana("同じ失敗を繰り返すな。") must_== "同[おな]じ 失[しっ]敗[ぱい]を 繰[く]り 返[かえ]すな。"
      furigana("熱いうちに、どうぞ召し上がって下さい。") must_== "熱[あつ]いうちに、どうぞ 召[め]し 上[あ]がって 下[くだ]さい。"
    }

    "single words" >> {
      furigana("取引") must_== "取[とり]引[ひき]"
    }
  }
}
