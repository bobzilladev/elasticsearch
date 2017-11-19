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

package org.elasticsearch.search.aggregations.bitmask;

import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;
import org.elasticsearch.search.aggregations.bucket.terms.BitmaskAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.BitmaskParser;
import org.elasticsearch.search.aggregations.bucket.terms.LongBitmask;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;

import java.util.List;

import static java.util.Collections.singletonList;

public class BitmaskAggregationPlugin extends Plugin implements SearchPlugin {
    @Override
    public List<AggregationSpec> getAggregations() {
        return singletonList(new AggregationSpec(BitmaskAggregationBuilder.NAME, BitmaskAggregationBuilder::new,
            BitmaskAggregationBuilder::parse)
            .addResultReader(LongBitmask.NAME, LongBitmask::new));
    }
}
