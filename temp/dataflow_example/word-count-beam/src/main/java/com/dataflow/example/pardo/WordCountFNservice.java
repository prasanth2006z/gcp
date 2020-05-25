package com.dataflow.example.pardo;

import com.dataflow.example.pardo.WordCountFn;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.BigEndianIntegerCoder;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.*;

public class WordCountFNservice {

    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline p = Pipeline.create(options);
        final TupleTag<String> wordsBelowCutOffTag = new TupleTag<String>() {
        };
        final TupleTag<String> wordLengthsAboveCutOffTag = new TupleTag<String>() {
        };
        PCollection<String> words = p.apply(Create.of("Hellommmm", "ab", "ucv").withCoder(StringUtf8Coder.of()));
        PCollection<Integer> maxWordLengthCutOff = p.apply(Create.of(3).withCoder(BigEndianIntegerCoder.of()));
        final PCollectionView<Integer> maxWordLengthCutOffView =
                maxWordLengthCutOff.apply(View.<Integer>asSingleton());


        PCollectionTuple results = words.apply(ParDo.of(new WordCountFn(maxWordLengthCutOffView, wordsBelowCutOffTag, wordLengthsAboveCutOffTag))
                .withSideInputs(maxWordLengthCutOffView)
                .withOutputTags(wordsBelowCutOffTag, TupleTagList.of(wordLengthsAboveCutOffTag)));

        words = results.get(wordsBelowCutOffTag);
        //words.apply(TextIO.write().to("testmm"));
        p.run().waitUntilFinish();
    }
}
