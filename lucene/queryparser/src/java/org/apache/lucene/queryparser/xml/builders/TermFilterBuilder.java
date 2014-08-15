package org.apache.lucene.queryparser.xml.builders;

import org.apache.lucene.search.Filter;
import org.apache.lucene.queries.TermFilter;
import org.apache.lucene.queryparser.xml.DOMUtils;
import org.apache.lucene.queryparser.xml.SingleTermProcessor;
import org.apache.lucene.queryparser.xml.TermBuilder;
import org.apache.lucene.queryparser.xml.FilterBuilder;
import org.apache.lucene.queryparser.xml.ParserException;
import org.w3c.dom.Element;

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

/**
 * Builder for {@link TermFilter}
 */
public class TermFilterBuilder implements FilterBuilder {

  protected final TermBuilder termBuilder;

  public TermFilterBuilder(TermBuilder termBuilder) {
    this.termBuilder = termBuilder;
  }

  @Override
  public Filter getFilter(final Element e) throws ParserException {

    SingleTermProcessor tp = new SingleTermProcessor();
    String field = DOMUtils.getAttributeWithInheritanceOrFail(e, "fieldName");
    //extract the value and fail if there is no value. 
    //This is a query builder for one and only one term
    String value =  DOMUtils.getNonBlankTextOrFail(e);
    this.termBuilder.extractTerms(tp, field, value);
    
    try {
      return new TermFilter(tp.getTerm());
    } catch (ParserException ex){
      throw new ParserException(ex.getMessage() + " field:" + field 
          + " value:" + value + ". Check the query anlyser configured on this field." );
    }
  }

}
