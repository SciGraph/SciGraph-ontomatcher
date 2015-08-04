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

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;

import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.sdsc.scigraph.annotation.EntityProcessor;
import edu.sdsc.scigraph.frames.NodeProperties;
import edu.sdsc.scigraph.internal.CypherUtil;
import edu.sdsc.scigraph.neo4j.Neo4jModule;
import edu.sdsc.scigraph.owlapi.GraphOwlVisitor;
import edu.sdsc.scigraph.owlapi.OwlPostprocessor;
import edu.sdsc.scigraph.owlapi.curies.CurieUtil;
import edu.sdsc.scigraph.owlapi.loader.OwlLoadConfiguration;
import edu.sdsc.scigraph.owlapi.loader.OwlLoaderModule;
import edu.sdsc.scigraph.owlapi.loader.OwlLoadConfiguration.MappedProperty;
import edu.sdsc.scigraph.util.GraphTestBase;

public class OntologyMatcherTest extends GraphTestBase {

  static OntologyMatcher matcher;
  static CurieUtil util;

  @BeforeClass
  public static void loadPizza() throws Exception {
	  OwlLoadConfiguration config;
	  Injector i =
			  Guice.createInjector(new OwlLoaderModule(config), new Neo4jModule(config.getGraphConfiguration()));

    //Injector injector = Guice.createInjector(new OntologyMatcherModule());
	EntityProcessor processor = injector.getInstance(EntityProcessor.class);

    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    String uri = Resources.getResource("pizza.owl").toURI().toString();
    IRI iri = IRI.create(uri);
    manager.loadOntologyFromOntologyDocument(iri);
    OWLOntologyWalker walker = new OWLOntologyWalker(manager.getOntologies());

    MappedProperty mappedProperty = new MappedProperty(NodeProperties.LABEL);
    List<String> properties = new ArrayList<String>();
    properties.add("http://www.w3.org/2000/01/rdf-schema#label");
    properties.add("http://www.w3.org/2004/02/skos/core#prefLabel");
    mappedProperty.setProperties(properties);

    ArrayList<MappedProperty> mappedPropertyList = new ArrayList<MappedProperty>();
    mappedPropertyList.add(mappedProperty);

    GraphOwlVisitor visitor = new GraphOwlVisitor(walker, graph, mappedPropertyList);
    walker.walkStructure(visitor);
    Map<String, String> categories = new HashMap<>();
    categories.put("http://www.co-ode.org/ontologies/pizza/pizza.owl#NamedPizza", "pizza");
    categories.put("http://www.co-ode.org/ontologies/pizza/pizza.owl#PizzaTopping", "topping");
    try (Transaction tx = graphDb.beginTx()) {
      OwlPostprocessor postprocessor = new OwlPostprocessor(graphDb, categories);
      postprocessor.processCategories(categories);
      postprocessor.processSomeValuesFrom();
      tx.success();
    }

    Map<String, String> map = new HashMap<>();
    map.put("pizza", "http://www.co-ode.org/ontologies/pizza/pizza.owl#");
    util = new CurieUtil(map);
    CypherUtil cypherUtil = new CypherUtil(graphDb, util);
    

 
    matcher = new OntologyMatcher(graphDb, util, graph, cypherUtil, processor);
  }

  @Test
  public void matchTest() {
    matcher.matchAll();
  }

}
