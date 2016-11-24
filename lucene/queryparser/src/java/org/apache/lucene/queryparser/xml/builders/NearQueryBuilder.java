package org.apache.lucene.queryparser.xml.builders;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.xml.DOMUtils;
import org.apache.lucene.queryparser.xml.ParserException;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class NearQueryBuilder extends SpanBuilderBase {
  final private SpanQueryBuilder factory;

  public NearQueryBuilder(SpanQueryBuilder factory) {
    super();
    this.factory = factory;
  }

  @Override
  public SpanQuery getSpanQuery(Element e) throws ParserException {
    int slop = DOMUtils.getAttribute(e, "slop", 0);
    boolean inOrder = DOMUtils.getAttribute(e, "inOrder", false);

    List<SpanQuery> spans = new ArrayList<>();
    for (Node kid = e.getFirstChild(); kid != null; kid = kid.getNextSibling()) {
      if (kid.getNodeType() == Node.ELEMENT_NODE) {
        Query q = factory.getQuery((Element) kid);
        if (!(q instanceof MatchAllDocsQuery)) {
          SpanQuery sq = factory.getSpanQuery((Element)kid);
          spans.add(sq);
        }
      }
    }

    switch (spans.size())
    {
      case 0:
        return null;
      case 1:
        return spans.get(0);
     default:
       SpanQuery[] spanQueries = spans.toArray(new SpanQuery[spans.size()]);
       return new SpanNearQuery(spanQueries, slop, inOrder);
    }
  }
}
