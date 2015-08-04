/**
 * Copyright (C) 2014 The SciGraph authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.scigraph.ontomatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;

import com.google.common.base.Optional;

import edu.sdsc.scigraph.annotation.EntityProcessor;
import edu.sdsc.scigraph.frames.CommonProperties;
import edu.sdsc.scigraph.frames.NodeProperties;
import edu.sdsc.scigraph.internal.CypherUtil;
import edu.sdsc.scigraph.neo4j.Graph;
import edu.sdsc.scigraph.owlapi.curies.CurieUtil;

public class OntologyMatcher {

  private final GraphDatabaseService graphDb;
  private final CurieUtil curieUtil;
  private final Graph graph;
  private final CypherUtil cypherUtil;
  private final GlobalGraphOperations graphOps;
  
  @Inject
  private EntityProcessor processor;


  @Inject
  OntologyMatcher(GraphDatabaseService graphDb, CurieUtil curieUtil, Graph graph,
      CypherUtil cypherUtil, EntityProcessor processor) {
    this.graphDb = graphDb;
    graphOps =  GlobalGraphOperations.at(graphDb);
    this.curieUtil = curieUtil;
    this.graph = graph;
    this.cypherUtil = cypherUtil;
    this.processor = processor;
  }

  public void matchAll() {
	  System.out.println("Matching all");
	  System.out.println("Processor: "+processor);
	  graphDb.beginTx();
	  for (Node n : graphOps.getAllNodes()) {
		  System.out.println("NODE="+n);
		  
	  }
	  
	  
  }
}
