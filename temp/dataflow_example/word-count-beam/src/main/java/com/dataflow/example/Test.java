package com.dataflow.example;

import com.dataflow.example.pardo.WordCountFn;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.BigEndianIntegerCoder;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.*;

import java.util.Arrays;

public class Test {




    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline p = Pipeline.create(options);
        //To build a PCollection from in-memory data.
        //PCollection<Integer> pc = p.apply(Create.of(3, 4, 5).withCoder(BigEndianIntegerCoder.of()));
        PCollection<String> words = p.apply(Create.of("Hellommmm","ab","ucv").withCoder(StringUtf8Coder.of()));
        PCollection<Integer> maxWordLengthCutOff = p.apply(Create.of(3).withCoder(BigEndianIntegerCoder.of()));

        final PCollectionView<Integer> maxWordLengthCutOffView =
                maxWordLengthCutOff.apply(View.<Integer>asSingleton());


        final TupleTag<String> wordsBelowCutOffTag = new TupleTag<String>(){};
        final TupleTag<String> wordLengthsAboveCutOffTag = new TupleTag<String>(){};
     //   PCollection<String> wordsBelowCutOff =
             //  words.apply(ParDo.of(new WordCountFn(maxWordLengthCutOffView)
//                new DoFn<String, String>() {
//                    @ProcessElement
//                    public void processElement(ProcessContext c) {
//                        String word = c.element();
//                        System.out.println("===>"+word);
//                        int lengthCutOff = c.sideInput(maxWordLengthCutOffView);
//                        System.out.println("===>"+lengthCutOff);
//                        if (word.length() <= lengthCutOff) {
//                            c.output(word);
//                        }
//                    }}
                 //  ).withSideInputs(maxWordLengthCutOffView));



//        PCollectionTuple results = words.apply(ParDo.of(new WordCountFn(maxWordLengthCutOffView,wordsBelowCutOffTag,wordLengthsAboveCutOffTag))
//                .withSideInputs(maxWordLengthCutOffView)
//                .withOutputTags(wordsBelowCutOffTag, TupleTagList.of(wordLengthsAboveCutOffTag)))
//                ;

        //words=results.get(wordsBelowCutOffTag);


        // IOs to read data from an external storage system
        PCollection<String> text = p.apply(TextIO.read().from("/Users/prasanthpaattathil/Documents/GCP-LAB/gcp/dataflow_example/word-count-beam/test.txt"));
        text = text.apply(FlatMapElements.into(TypeDescriptors.strings()).via((String line) -> Arrays.asList(line.split(" "))));
        text = text.apply(Filter.by((String word) -> !word.isEmpty()));
        PCollection<KV<String, Long>> text1  = text.apply(Count.perElement());
        text=  text1.apply(
                MapElements.into(TypeDescriptors.strings())
                        .via(
                                (KV<String, Long> wordCount) ->
                                        wordCount.getKey() + ": " + wordCount.getValue()));

        //text.apply(TextIO.write().to("test"));


        //words.apply(TextIO.write().to("testmm"));
        //results.apply("wordsBelowCutOffTag",results,TextIO.write().to("test"));
        p.run().waitUntilFinish();
    }
}
