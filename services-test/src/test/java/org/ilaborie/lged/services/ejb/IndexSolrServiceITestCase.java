/*
 * 
 */
package org.ilaborie.lged.services.ejb;

import java.util.Collections;

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
public class IndexSolrServiceITestCase {

	/** The service. */
	@Inject
	private IIndexService service;

	@Inject
	private ISearchService searchService;

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
		ejb.addClass(IndexSolrServiceITestCase.class);
		ejb.addAsResource("solr.properties");
		ejb.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		// Mock
		ejb.addClasses(MockService.class, MockIndexableElement.class);

		// Create a maven dependency resolver
		MavenDependencyResolver resolver = DependencyResolvers.use(
				MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

		// Build the EAR
		EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class,
				"test-index-ejb.ear");
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
	 * Test get by id.
	 */
	@Test
	public void testAdd() {
		long count;
		boolean result;

		// Clear the index
		this.service.clear();
		count = this.searchService.countDocuments();
		Assert.assertEquals(0L, count);

		IIndexableElement elt;

		// Add Lorem
		elt = new MockIndexableElement("Lorem");
		result = this.service.index(elt);
		Assert.assertTrue(result);
		count = this.searchService.countDocuments();
		Assert.assertEquals(1L, count);

		// Add Ipsum
		elt = new MockIndexableElement("Ipsum");
		result = this.service.indexAll(Collections.singleton(elt));
		Assert.assertTrue(result);
		count = this.searchService.countDocuments();
		Assert.assertEquals(2L, count);
	}

	/**
	 * Test clear.
	 */
	@Test
	public void testClear() {
		boolean result;
		long count;
		// First Clear
		result = this.service.clear();
		Assert.assertTrue(result);
		count = this.searchService.countDocuments();
		Assert.assertEquals(0L, count);

		// Add a random element
		this.service.index(new MockIndexableElement(String.valueOf(System
				.nanoTime())));
		count = this.searchService.countDocuments();
		Assert.assertNotSame(0L, count);

		// Second Clear
		result = this.service.clear();
		Assert.assertTrue(result);
		count = this.searchService.countDocuments();
		Assert.assertEquals(0L, count);
	}

	@Test
	public void testDelete() {
		long count;
		boolean result;

		IIndexableElement elt;
		String loremId;
		String ipsumId;

		// Clear the index
		this.service.clear();
		// Add Lorem
		elt = new MockIndexableElement("Lorem");
		loremId = elt.getId();
		this.service.index(elt);

		// Add Ipsum
		elt = new MockIndexableElement("Ipsum");
		ipsumId = elt.getId();
		this.service.index(elt);

		// Initial state
		count = this.searchService.countDocuments();
		Assert.assertEquals(2L, count);

		// Delete Lorem
		result = this.service.delete(loremId);
		Assert.assertTrue(result);
		count = this.searchService.countDocuments();
		Assert.assertEquals(1L, count);

		// Delete Lorem
		result = this.service.delete(Collections.singleton(ipsumId));
		Assert.assertTrue(result);
		count = this.searchService.countDocuments();
		Assert.assertEquals(0L, count);
	}

	/**
	 * Test optimize.
	 */
	@Test
	public void testOptimize() {
		boolean result;

		// Clear the index
		this.service.clear();

		IIndexableElement elt;

		// Add Lorem
		elt = new MockIndexableElement("Lorem");
		result = this.service.index(elt);
		Assert.assertTrue(result);

		// Add Ipsum
		elt = new MockIndexableElement("Ipsum");
		result = this.service.indexAll(Collections.singleton(elt));
		Assert.assertTrue(result);

		// Optimize
		result = this.service.optimize();
		Assert.assertTrue(result);
	}

}
