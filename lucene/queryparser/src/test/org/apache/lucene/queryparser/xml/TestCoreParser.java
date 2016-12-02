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
package org.apache.lucene.queryparser.xml;

import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;

import java.io.IOException;

public class TestCoreParser extends TestCoreParserBase {

  public void testTermQueryXML() throws ParserException, IOException {
    Query q = parse("TermQuery.xml");
    dumpResults("TermQuery", q, 5);
  }

  public void testTermQueryEmptyXML() throws ParserException, IOException {
    parseShouldFail("TermQueryEmpty.xml",
        "TermQuery has no text");
  }

  public void testTermsQueryXML() throws ParserException, IOException {
    Query q = parse("TermsQuery.xml");
    dumpResults("TermsQuery", q, 5);
  }

  public void testBooleanQueryXML() throws ParserException, IOException {
    Query q = parse("BooleanQuery.xml");
    dumpResults("BooleanQuery", q, 5);
  }
  
  public void testDisjunctionMaxQueryXML() throws ParserException, IOException {
    Query q = parse("DisjunctionMaxQuery.xml");
    assertTrue(q instanceof DisjunctionMaxQuery);
    DisjunctionMaxQuery d = (DisjunctionMaxQuery)q;
    assertEquals(0.0f, d.getTieBreakerMultiplier(), 0.0001f);
    assertEquals(2, d.getDisjuncts().size());
    DisjunctionMaxQuery ndq = (DisjunctionMaxQuery) d.getDisjuncts().get(1);
    assertEquals(1.2f, ndq.getTieBreakerMultiplier(), 0.0001f);
    assertEquals(1, ndq.getDisjuncts().size());
  }

  public void testRangeQueryXML() throws ParserException, IOException {
    Query q = parse("RangeQuery.xml");
    dumpResults("RangeQuery", q, 5);
  }

  public void testUserQueryXML() throws ParserException, IOException {
    Query q = parse("UserInputQuery.xml");
    dumpResults("UserInput with Filter", q, 5);
  }

  public void testCustomFieldUserQueryXML() throws ParserException, IOException {
    Query q = parse("UserInputQueryCustomField.xml");
    int h = searcher().search(q, 1000).totalHits;
    assertEquals("UserInputQueryCustomField should produce 0 result ", 0, h);
  }

  public void testBoostingTermQueryXML() throws Exception {
    Query q = parse("BoostingTermQuery.xml");
    dumpResults("BoostingTermQuery", q, 5);
  }

  public void testSpanTermXML() throws Exception {
    Query q = parse("SpanQuery.xml");
    dumpResults("Span Query", q, 5);
  }

  public void testConstantScoreQueryXML() throws Exception {
    Query q = parse("ConstantScoreQuery.xml");
    dumpResults("ConstantScoreQuery", q, 5);
  }

  public void testMatchAllDocsPlusFilterXML() throws ParserException, IOException {
    Query q = parse("MatchAllDocsQuery.xml");
    dumpResults("MatchAllDocsQuery with range filter", q, 5);
  }

  public void testNestedBooleanQuery() throws ParserException, IOException {
    Query q = parse("NestedBooleanQuery.xml");
    dumpResults("Nested Boolean query", q, 5);
  }

  public void testNumericRangeQueryXML() throws ParserException, IOException {
    Query q = parse("LegacyNumericRangeQuery.xml");
    dumpResults("LegacyNumericRangeQuery", q, 5);
  }

  public void testNumericRangeQueryXMLWithoutLowerTerm() throws ParserException, IOException {
    Query q = parse("LegacyNumericRangeQueryWithoutLowerTerm.xml");
    dumpResults("LegacyNumericRangeQueryWithoutLowerTerm", q, 5);
  }

  public void testNumericRangeQueryXMLWithoutUpperTerm() throws ParserException, IOException {
    Query q = parse("LegacyNumericRangeQueryWithoutUpperTerm.xml");
    dumpResults("LegacyNumericRangeQueryWithoutUpperTerm", q, 5);
  }

  public void testNumericRangeQueryXMLWithoutRange() throws ParserException, IOException {
    Query q = parse("LegacyNumericRangeQueryWithoutRange.xml");
    dumpResults("LegacyNumericRangeQueryWithoutRange", q, 5);
  }
  
  public void testPointRangeQuery() throws ParserException, IOException {
    Query q = parse("PointRangeQuery.xml");
    dumpResults("PointRangeQuery", q, 5);
  }

  public void testPointRangeQueryWithoutLowerTerm() throws ParserException, IOException {
    Query q = parse("PointRangeQueryWithoutLowerTerm.xml");
    dumpResults("PointRangeQueryWithoutLowerTerm", q, 5);
  }

  public void testPointRangeQueryWithoutUpperTerm() throws ParserException, IOException {
    Query q = parse("PointRangeQueryWithoutUpperTerm.xml");
    dumpResults("PointRangeQueryWithoutUpperTerm", q, 5);
  }

  public void testPointRangeQueryWithoutRange() throws ParserException, IOException {
    Query q = parse("PointRangeQueryWithoutRange.xml");
    dumpResults("PointRangeQueryWithoutRange", q, 5);
  }
}
