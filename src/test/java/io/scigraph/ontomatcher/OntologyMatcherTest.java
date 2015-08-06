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

import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import edu.sdsc.scigraph.annotation.EntityFormatConfiguration;
import edu.sdsc.scigraph.annotation.EntityProcessor;
import edu.sdsc.scigraph.owlapi.OntologyGraphRule;

public class OntologyMatcherTest {

  @ClassRule
  public static OntologyGraphRule ontologyGraph = new OntologyGraphRule("pizza.owl");

  OntologyMatcher matcher;

  @Before
  public void setup() throws Exception {
    EntityProcessor processor = mock(EntityProcessor.class);
    EntityFormatConfiguration config = mock(EntityFormatConfiguration.class);
    matcher = new OntologyMatcher(ontologyGraph.getGraphDb(), processor, config);
  }

  @Test
  public void matchTest() throws IOException {
    matcher.matchAll();
  }

}
