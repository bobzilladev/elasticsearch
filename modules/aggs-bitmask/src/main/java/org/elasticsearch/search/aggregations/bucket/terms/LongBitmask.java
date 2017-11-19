package org.elasticsearch.search.aggregations.bucket.terms;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.search.DocValueFormat;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LongBitmask extends LongTerms {
    public static final String NAME = "lbitmask";

    public LongBitmask(String name, BucketOrder order, int requiredSize, long minDocCount, List<PipelineAggregator> pipelineAggregators, Map<String, Object> metaData, DocValueFormat format, int shardSize, boolean showTermDocCountError, long otherDocCount, List<Bucket> buckets, long docCountError) {
        super(name, order, requiredSize, minDocCount, pipelineAggregators, metaData, format, shardSize, showTermDocCountError, otherDocCount, buckets, docCountError);
    }

    public LongBitmask(StreamInput in) throws IOException {
        super(in);
    }
}
