# jcore
## Library for processing Japanese text
Runs text through [Kuromoji](https://github.com/atilika/kuromoji), a Japanese morphlogical analyzer and cleans up the results. Also has support for furigana generation.

### Using jcore
There's not much to it at the moment. This example program shows how get a sentence:
```scala
import com.stephenmac7.jcore.UnidicImporter

object Main {
    def main(args: Array[String]) = {
        val sentence = UnidicImporter.toSentence("お寿司が食べたい。")
        println(sentence)
    }
}
```


### Thanks to...
- The developers of [Kuromoji](https://github.com/atilika/kuromoji), for their great self-contained morphological analyzer
- Doublevil for the [JmdictFurigana project](https://github.com/Doublevil/JmdictFurigana) which makes sticking the right furigana to the right characters painless
- The UniDic Consortium, for the [UniDic](https://osdn.jp/projects/unidic/), the great dictionary Kuromoji uses internally

### License
jcore is licensed under the Apache License, Version 2.0. See LICENSE for details.

### Contributing
Open up an issue if something isn't working properly or as expected. Likewise, open an issue for any feature requests. Pull requests are also more than welcome.
