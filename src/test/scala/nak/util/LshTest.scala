package nak.util

import org.scalatest.FunSpec

/**
 * Test LocalitySensitiveHash using Twitter data.
 */ 
class LocalitySensitiveHashSpec extends FunSpec {

    describe("Locality Sensitive Hash") {
    val tweets = this.getClass.getResourceAsStream("/data/twitter/example_tweets.txt")

    it ("should index and find near matches") {
      //val lines = io.Source.fromFile(args(0)).getLines
      val lines = io.Source.fromInputStream(tweets).getLines
        .map(line => line.replaceAll("\\s+"," ").toLowerCase)
        .toIndexedSeq
        .zipWithIndex
      
      // Hash all all documents read from file
      val lsh = new LocalitySensitiveHash(
        shingleLength=3,
        minHashLength=100,
        numberBands=10,
        lines,
        threshold = 0.3
      )
      
      // find the documents that are most similar to the below string
      val testString = "RT @Adam_Schefter:QB Tebow broke the combined record for QBs with a 38-inch vertical jump. He also ran an impressive 40 time "
      
      lsh.createHash()

      val similarEntries =
         lsh.findSimilar(testString.replaceAll("\\s+"," ").toLowerCase)

      val matchingIds = similarEntries.map(lines).map(_._2).toList.sorted

      // Need to deal better with randomness
      assert((matchingIds == List(34,193,851)) | (matchingIds == List(851)))
    }
  }

}
