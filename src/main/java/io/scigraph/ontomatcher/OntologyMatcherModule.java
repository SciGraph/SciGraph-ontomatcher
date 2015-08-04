package io.scigraph.ontomatcher;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import edu.sdsc.scigraph.annotation.EntityModule;
import edu.sdsc.scigraph.owlapi.curies.CurieModule;
import edu.sdsc.scigraph.vocabulary.Vocabulary;
import edu.sdsc.scigraph.vocabulary.VocabularyNeo4jImpl;

public class OntologyMatcherModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new CurieModule());
		bind(Vocabulary.class).to(VocabularyNeo4jImpl.class).in(Singleton.class);

		install(new EntityModule());	   		
	}

}
