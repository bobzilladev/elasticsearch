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

import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregator;

import java.io.IOException;


public class BitmaskParser implements Aggregator.Parser {

    public static final ParseField BITMASK_FIELD = new ParseField("bitmask");

    public BitmaskParser() {
    }

//    @Override
//    protected boolean token(String aggregationName, String currentFieldName, XContentParser.Token token, XContentParser parser,
//                            Map<ParseField, Object> otherOptions) throws IOException {
//        if (BITMASK_FIELD.match(currentFieldName)) {
//            if (token == XContentParser.Token.VALUE_NUMBER) {
//                otherOptions.put(BITMASK_FIELD, parser.longValue());
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    protected BitmaskAggregationBuilder createFactory(String aggregationName, ValuesSourceType valuesSourceType,
//                                                      ValueType targetValueType, Map<ParseField, Object> otherOptions) {
//        BitmaskAggregationBuilder builder = new BitmaskAggregationBuilder(aggregationName);
//        Long mode = (Long)otherOptions.get(BITMASK_FIELD);
//        if (mode != null) {
//            builder.bitmask(mode);
//        }
//        return builder;
//    }

    @Override
    public AggregationBuilder parse(String aggregationName, XContentParser parser) throws IOException {
        return null;
    }
}
