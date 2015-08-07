package io.scigraph.ontomatcher;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.sdsc.scigraph.annotation.EntityFormatConfiguration;
import edu.sdsc.scigraph.annotation.EntityModule;
import edu.sdsc.scigraph.annotation.EntityProcessor;
import edu.sdsc.scigraph.neo4j.Neo4jModule;
import edu.sdsc.scigraph.owlapi.loader.BatchOwlLoader;
import edu.sdsc.scigraph.owlapi.loader.OwlLoadConfiguration;
import edu.sdsc.scigraph.owlapi.loader.OwlLoaderModule;

public class GraphLoadSpike {

  static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

  @Rule
  public TemporaryFolder graphFolder = new TemporaryFolder();

  @Test
  public void test() throws InterruptedException, IOException {
    // Read the configuration
    OwlLoadConfiguration owlConfig = mapper.readValue(Resources.getResource("pizza.yaml"), OwlLoadConfiguration.class);
    // Set the graph location
    owlConfig.getGraphConfiguration().setLocation(graphFolder.getRoot().getAbsolutePath());

    // Load the ontology
    Injector i = Guice.createInjector(
        new Neo4jModule(owlConfig.getGraphConfiguration()),
        new OwlLoaderModule(owlConfig)
        );
    BatchOwlLoader loader = i.getInstance(BatchOwlLoader.class);
    loader.loadOntology();

    // Reload the injector to open the graphdb again
    i = Guice.createInjector(
        new EntityModule(),
        new Neo4jModule(owlConfig.getGraphConfiguration()));
    EntityProcessor processor = i.getInstance(EntityProcessor.class);
    Reader reader = new StringReader("This sentence is about shrimps.");
    EntityFormatConfiguration format = new EntityFormatConfiguration.Builder(reader).get();
    System.out.println(processor.annotateEntities(format));
  }

}
