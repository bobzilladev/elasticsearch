/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.search.aggregations.bucket.terms;

import org.elasticsearch.search.aggregations.AggregationExecutionException;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.AggregatorFactory;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.InternalOrder;
import org.elasticsearch.search.aggregations.bucket.BucketUtils;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregator;
import org.elasticsearch.search.aggregations.support.ValuesSource;
import org.elasticsearch.search.aggregations.support.ValuesSourceConfig;
import org.elasticsearch.search.internal.SearchContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

final class BitmaskAggregatorFactory extends BitmaskTermsAggregatorFactory {

    private final BucketOrder order;
    private final IncludeExclude includeExclude;
    private final Aggregator.SubAggCollectionMode collectMode;
    private final TermsAggregator.BucketCountThresholds bucketCountThresholds;
    private final boolean showTermDocCountError;

    BitmaskAggregatorFactory(String name,
                             ValuesSourceConfig<ValuesSource> config,
                             BucketOrder order,
                             IncludeExclude includeExclude,
                             String executionHint,
                             Aggregator.SubAggCollectionMode collectMode,
                             TermsAggregator.BucketCountThresholds bucketCountThresholds,
                             boolean showTermDocCountError,
                             SearchContext context,
                             AggregatorFactory<?> parent,
                             AggregatorFactories.Builder subFactoriesBuilder,
                             Map<String, Object> metaData) throws IOException {
        super(name, config, order, includeExclude, executionHint, collectMode, bucketCountThresholds,
            showTermDocCountError, context, parent, subFactoriesBuilder, metaData);
        // we need to save these locally for local doCreateInternal
        this.order = order;
        this.includeExclude = includeExclude;
        this.collectMode = collectMode;
        this.bucketCountThresholds = bucketCountThresholds;
        this.showTermDocCountError = showTermDocCountError;
    }

    static final TermsAggregator.BucketCountThresholds DEFAULT_BUCKET_COUNT_THRESHOLDS = new TermsAggregator.BucketCountThresholds(1, 0, 10,
        -1);


    @Override
    protected Aggregator doCreateInternal(ValuesSource valuesSource, Aggregator parent, boolean collectsFromSingleBucket,
                                          List<PipelineAggregator> pipelineAggregators, Map<String, Object> metaData) throws IOException {
        if (collectsFromSingleBucket == false) {
            return asMultiBucketAggregator(this, context, parent);
        }
        TermsAggregator.BucketCountThresholds bucketCountThresholds = new TermsAggregator.BucketCountThresholds(this.bucketCountThresholds);
        if (InternalOrder.isKeyOrder(order) == false
            && bucketCountThresholds.getShardSize() == DEFAULT_BUCKET_COUNT_THRESHOLDS.getShardSize()) {
            // The user has not made a shardSize selection. Use default
            // heuristic to avoid any wrong-ranking caused by distributed
            // counting
            bucketCountThresholds.setShardSize(BucketUtils.suggestShardSideQueueSize(bucketCountThresholds.getRequiredSize(),
                context.numberOfShards()));
        }
        bucketCountThresholds.ensureValidity();

        if ((includeExclude != null) && (includeExclude.isRegexBased())) {
            throw new AggregationExecutionException("Aggregation [" + name + "] cannot support regular expression style include/exclude "
                + "settings as they can only be applied to string fields. "
                + "Use an array of numeric values for include/exclude clauses used to filter numeric fields");
        }

        if (valuesSource instanceof ValuesSource.Numeric) {
            IncludeExclude.LongFilter longFilter = null;
            Aggregator.SubAggCollectionMode cm = collectMode;
            if (cm == null) {
                if (factories != AggregatorFactories.EMPTY) {
                    cm = subAggCollectionMode(bucketCountThresholds.getShardSize(), -1);
                } else {
                    cm = Aggregator.SubAggCollectionMode.DEPTH_FIRST;
                }
            }
            if (((ValuesSource.Numeric) valuesSource).isFloatingPoint()) {
                if (includeExclude != null) {
                    longFilter = includeExclude.convertToDoubleFilter();
                }
                return new DoubleTermsAggregator(name, factories, (ValuesSource.Numeric) valuesSource, config.format(), order,
                    bucketCountThresholds, context, parent, cm, showTermDocCountError, longFilter,
                    pipelineAggregators, metaData);
            }
            if (includeExclude != null) {
                longFilter = includeExclude.convertToLongFilter(config.format());
            }
            return new BitmaskAggregator(name, factories, (ValuesSource.Numeric) valuesSource, config.format(), order,
                bucketCountThresholds, context, parent, cm, showTermDocCountError, longFilter, pipelineAggregators,
                metaData);
        }

        throw new AggregationExecutionException("terms aggregation cannot be applied to field [" + config.fieldContext().field()
            + "]. It can only be applied to numeric or string fields.");
    }

}
