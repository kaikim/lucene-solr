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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenFilter;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.LuceneTestCase;
import org.junit.AfterClass;

import java.io.IOException;
import java.io.InputStream;

public class TestCoreParserBase extends LuceneTestCase {

  final private static String defaultField = "contents";

  private static Analyzer analyzer;
  private static CoreParser coreParser;

  private static CoreParserTestIndexData indexData;

  protected Analyzer newAnalyzer() {
    // TODO: rewrite test (this needs to set QueryParser.enablePositionIncrements, too, for work with CURRENT):
    return new MockAnalyzer(random(), MockTokenizer.WHITESPACE, true, MockTokenFilter.ENGLISH_STOPSET);
  }

  protected CoreParser newCoreParser(String defaultField, Analyzer analyzer) {
    return new CoreParser(defaultField, analyzer);
  }

  @AfterClass
  public static void afterClass() throws Exception {
    if (indexData != null) {
      indexData.close();
      indexData = null;
    }
    coreParser = null;
    analyzer = null;
  }

  //================= Helper methods ===================================

  protected String defaultField() {
    return defaultField;
  }

  protected Analyzer analyzer() {
    if (analyzer == null) {
      analyzer = newAnalyzer();
    }
    return analyzer;
  }

  protected CoreParser coreParser() {
    if (coreParser == null) {
      coreParser = newCoreParser(defaultField, analyzer());
    }
    return coreParser;
  }

  private CoreParserTestIndexData indexData() {
    if (indexData == null) {
      try {
        indexData = new CoreParserTestIndexData(analyzer());
      } catch (Exception e) {
        fail("caught Exception "+e);
      }
    }
    return indexData;
  }

  protected IndexReader reader() {
    return indexData().reader;
  }

  protected IndexSearcher searcher() {
    return indexData().searcher;
  }

  protected void parseShouldFail(String xmlFileName, String expectedParserExceptionMessage) throws IOException {
    Query q = null;
    ParserException pe = null;
    try {
      q = parse(xmlFileName);
    } catch (ParserException e) {
      pe = e;
    }
    assertNull("for "+xmlFileName+" unexpectedly got "+q, q);
    assertNotNull("expected a ParserException for "+xmlFileName, pe);
    assertEquals("expected different ParserException for "+xmlFileName,
        expectedParserExceptionMessage, pe.getMessage());
  }

  protected Query parse(String xmlFileName) throws ParserException, IOException {
    try (InputStream xmlStream = TestCoreParserBase.class.getResourceAsStream(xmlFileName)) {
      assertNotNull("Test XML file " + xmlFileName + " cannot be found", xmlStream);
      Query result = coreParser().parse(xmlStream);
      return result;
    }
  }

  protected Query rewrite(Query q) throws IOException {
    return q.rewrite(reader());
  }

  protected void dumpResults(String qType, Query q, int numDocs) throws IOException {
    if (VERBOSE) {
      System.out.println("TEST: qType=" + qType + " numDocs=" + numDocs + " " + q.getClass().getCanonicalName() + " query=" + q);
    }
    final IndexSearcher searcher = searcher();
    TopDocs hits = searcher.search(q, numDocs);
    final boolean producedResults = (hits.totalHits > 0);
    if (!producedResults) {
      System.out.println("TEST: qType=" + qType + " numDocs=" + numDocs + " " + q.getClass().getCanonicalName() + " query=" + q);
    }
    if (VERBOSE) {
      ScoreDoc[] scoreDocs = hits.scoreDocs;
      for (int i = 0; i < Math.min(numDocs, hits.totalHits); i++) {
        Document ldoc = searcher.doc(scoreDocs[i].doc);
        System.out.println("[" + ldoc.get("date") + "]" + ldoc.get("contents"));
      }
      System.out.println();
    }
    assertTrue(qType + " produced no results", producedResults);
  }
}
