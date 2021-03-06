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

import io.scigraph.annotation.EntityAnnotation;
import io.scigraph.annotation.EntityFormatConfiguration;
import io.scigraph.annotation.EntityProcessor;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.inject.Inject;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.tooling.GlobalGraphOperations;

public class OntologyMatcher {

	private final GraphDatabaseService graphDb;
	private final GlobalGraphOperations graphOps;
	private final EntityProcessor processor;
	private final EntityFormatConfiguration entityFormatConfiguration;

	@Inject
	OntologyMatcher(GraphDatabaseService graphDb, EntityProcessor processor, EntityFormatConfiguration config) {
		this.graphDb = graphDb;
		graphOps =  GlobalGraphOperations.at(graphDb);
		this.processor = processor;
		entityFormatConfiguration = config;
	}

	/**
	 * Do an all x all lexical comparison for each node in graph;
	 * 
	 * For each comparison annotate the {label,syn} of n_i and find the subset of nodes
	 * that match, either partial or complete
	 * 
	 * This can be used to build a graph of edges between n_i and n_j, of type
	 * lexically equivalent vs lexically subset of (analogous to and hypotheses for
	 * EquivalentClasses and SubClassOf)
	 * 
	 * @throws IOException
	 */
	public void matchAll() throws IOException {
		System.out.println("Matching all");
		System.out.println("Processor: "+processor);
		graphDb.beginTx();
		int numAnns = 0;
		for (Node n : graphOps.getAllNodes()) {
			//String label = getFirst(TinkerGraphUtil.getProperties(n, NodeProperties.LABEL, String.class), null);

			// TODO: no cast
			String label = "";
			for (String k : n.getPropertyKeys()) {
				Object v = n.getProperty(k);
				if (v instanceof String) {
					label = (String)v;
					//String label = (String) n.getProperty(NodeProperties.LABEL);
					System.out.println("p="+k+ " V="+label);
					
					EntityFormatConfiguration.Builder configBuilder = 
							new EntityFormatConfiguration.Builder(new StringReader(label));

					List<EntityAnnotation> anns = processor.annotateEntities(entityFormatConfiguration);
					for (EntityAnnotation a : anns) {
						System.out.println("Node="+label+ " Ann="+a);

					}
				}
			}
		}
		System.out.println("#anns="+numAnns);
	}
}
