package com.dataflow.example;

import com.dataflow.example.common.WriteOneFilePerWindow;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.joda.time.Duration;

public class PubSubToGCS {

    public static PipelineResult.State run(PubSubToGCSOptions options) {
        int numShards = 1;
        Pipeline pipeline = Pipeline.create(options);
        pipeline
                .apply("Read PubSub Messages", PubsubIO.readStrings().fromTopic(options.getInputTopic()))
                .apply(Window.into(FixedWindows.of(Duration.standardMinutes(options.getWindowSize()))))
                .apply("Write Files to GCS", new WriteOneFilePerWindow(options.getOutput(), numShards));
        return pipeline.run().waitUntilFinish();
    }

    public static void main(String[] args) {
        PubSubToGCSOptions options = PipelineOptionsFactory
                .fromArgs(args)
                .withValidation()
                .as(PubSubToGCSOptions.class);
        options.setStreaming(true);
        PipelineResult.State state=run(options);
        System.out.println(state);
    }

}
