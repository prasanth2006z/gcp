package com.dataflow.example.pardo;

import org.apache.beam.sdk.coders.BigEndianIntegerCoder;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.View;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionView;
import org.apache.beam.sdk.values.TupleTag;

public class WordCountFn extends DoFn<String, String> {
    PCollectionView<Integer> maxWordLengthCutOffView;
     TupleTag<String> wordsBelowCutOffTag ;
     TupleTag<String> wordLengthsAboveCutOffTag ;

    public WordCountFn(PCollectionView<Integer> maxWordLengthCutOffView,TupleTag<String> wordsBelowCutOffTag,TupleTag<String> wordLengthsAboveCutOffTag){
        this.maxWordLengthCutOffView=maxWordLengthCutOffView;
        this.wordsBelowCutOffTag=wordsBelowCutOffTag;
        this.wordLengthsAboveCutOffTag=wordLengthsAboveCutOffTag;
    }

    @ProcessElement
    public void processElement(ProcessContext c) {
        String word = c.element();
        System.out.println("word===>"+word);
        int lengthCutOff = c.sideInput(maxWordLengthCutOffView);
        System.out.println("length===>"+lengthCutOff);
        if (word.length() <= lengthCutOff) {
            c.output(wordsBelowCutOffTag,word);
        }else{
            c.output(wordLengthsAboveCutOffTag,word);
        }
    }

    //@ProcessElement
    public void processElement(ProcessContext c,@Element String element, OutputReceiver<String> receiver) {
        System.out.println("zxzx===>"+element);
        int lengthCutOff = c.sideInput(maxWordLengthCutOffView);
        System.out.println("===>"+lengthCutOff);
        if (element.length() <= lengthCutOff) {
            receiver.output(element);
        }
    }
}
