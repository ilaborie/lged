/*
 * 
 */
package org.ilaborie.lged.services.ejb;

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.ilaborie.lged.commons.model.IIndexableElement;
import org.ilaborie.lged.commons.services.IIndexService;
import org.ilaborie.lged.commons.services.ISearchService;
import org.ilaborie.lged.services.test.MockIndexableElement;
import org.ilaborie.lged.services.test.MockService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The Class SomethingServiceITestCase.
 */
@RunWith(Arquillian.class)
public class SearchSolrServiceITestCase {

	/** The index service. */
	@Inject
	private IIndexService indexService;

	/** The service. */
	@Inject
	private ISearchService service;

	/**
	 * Builds the archive.
	 * 
	 * @return the archive
	 */
	@Deployment
	public static Archive<?> buildArchive() {
		// Create an EJB jar
		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "ejb.jar");
		ejb.addPackages(true, "org.ilaborie.lged.commons");
		ejb.addPackages(true, "org.ilaborie.lged.services.utils");
		ejb.addClass(IndexSolrService.class);
		ejb.addClass(SearchSolrService.class);
		ejb.addClass(SearchSolrServiceITestCase.class);
		ejb.addAsResource("solr.properties");
		ejb.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		// Mock
		ejb.addClasses(MockService.class, MockIndexableElement.class);

		// Create a maven dependency resolver
		MavenDependencyResolver resolver = DependencyResolvers.use(
				MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

		// Build the EAR
		EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class,
				"test-search-ejb.ear");
		archive.addAsModule(ejb);

		// Add Guava
		archive.addAsLibraries(resolver.artifact("com.google.guava:guava")
				.resolveAsFiles());

		// Add SolR
		archive.addAsLibraries(resolver.artifact("org.apache.solr:solr-solrj")
				.resolveAsFiles());

		// logging
		archive.addAsLibraries(resolver.artifact("org.slf4j:slf4j-api")
				.resolveAsFiles());
		archive.addAsLibraries(resolver.artifact("org.slf4j:jcl-over-slf4j")
				.resolveAsFiles());

		return archive;
	}

	/**
	 * Test count documents.
	 */
	@Test
	public void testCountDocuments() {
		long count;
		// First Clear
		this.indexService.clear();
		count = this.service.countDocuments();
		Assert.assertEquals(0L, count);

		// Add a random element
		this.indexService.index(new MockIndexableElement(String.valueOf(System
				.nanoTime())));
		count = this.service.countDocuments();
		Assert.assertEquals(1L, count);
	}

	/**
	 * Test all.
	 */
	@Test
	public void testAll() {
		// Clear
		this.indexService.clear();

		// Lorem, ipsum, dolor, sit, amet
		IIndexableElement[] elts = new IIndexableElement[] {
				new MockIndexableElement("Lorem"),
				new MockIndexableElement("ipsum"),
				new MockIndexableElement("dolor"),
				new MockIndexableElement("sit"),
				new MockIndexableElement("amet") };
		// Add
		this.indexService.index(elts);

		// Get All
		List<IIndexableElement> all = this.service.getAll();
		Assert.assertNotNull(all);
		Assert.assertEquals(elts.length, all.size());
	}

	/**
	 * Test all.
	 */
	@Test
	public void testFirst() {
		// Clear
		this.indexService.clear();

		// Lorem, ipsum, dolor, sit, amet
		IIndexableElement[] elts = new IIndexableElement[] {
				new MockIndexableElement("Lorem"),
				new MockIndexableElement("ipsum"),
				new MockIndexableElement("dolor"),
				new MockIndexableElement("sit"),
				new MockIndexableElement("amet") };
		// Add
		this.indexService.index(elts);

		// Get First 3
		List<IIndexableElement> first3 = this.service.getFirst(3);
		Assert.assertNotNull(first3);
		Assert.assertEquals(3, first3.size());

		// Get First 10
		List<IIndexableElement> first10 = this.service.getFirst(10);
		Assert.assertNotNull(first10);
		Assert.assertEquals(elts.length, first10.size());
	}

	/**
	 * Test search full text.
	 */
	@Test
	public void testSearchFullText() {
		// Clear
		this.indexService.clear();

		// Lorem, ipsum, dolor, sit, amet
		IIndexableElement[] elts = new IIndexableElement[] {
				new MockIndexableElement("Lorem"),
				new MockIndexableElement("ipsum"),
				new MockIndexableElement("dolor"),
				new MockIndexableElement("sit"),
				new MockIndexableElement("amet") };
		// Add
		this.indexService.index(elts);

		List<IIndexableElement> list;
		// Search toto
		list = this.service.searchFullText("toto", 1);
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());

		// Search ipsum
		list = this.service.searchFullText("ipsum", 1);
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
	}

	/**
	 * Test search terms.
	 */
	@Test
	public void testSearchTerms() {
		// Clear
		this.indexService.clear();

		// Lorem, ipsum, dolor, sit, amet
		IIndexableElement[] elts = new IIndexableElement[] {
				new MockIndexableElement("Lorem"),
				new MockIndexableElement("ipsum"),
				new MockIndexableElement("dolor"),
				new MockIndexableElement("sit"),
				new MockIndexableElement("amet") };
		// Add
		this.indexService.index(elts);

		List<String> terms;
		// Search all terms
		terms = this.service.searchTerms("", 10);
		Assert.assertNotNull(terms);
		Assert.assertEquals(elts.length, terms.size());

		// Search ip terms
		terms = this.service.searchTerms("ip", 10);
		Assert.assertNotNull(terms);
		Assert.assertEquals(1, terms.size());
	}

}
