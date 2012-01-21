/*
 * 
 */
package org.ilaborie.lged.services.ejb;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.ilaborie.lged.commons.services.IIndexService;
import org.ilaborie.lged.commons.services.ISearchService;
import org.ilaborie.lged.commons.utils.functions.FileToIndexableFile;
import org.ilaborie.lged.commons.utils.predicates.FilesPredicates;
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

import com.google.common.collect.Collections2;

/**
 * The Class SomethingServiceITestCase.
 */
@RunWith(Arquillian.class)
public class IndexableFileITestCase {


	/** The index service. */
	@Inject
	private IIndexService indexService;

	/** The service. */
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
		ejb.addClass(SearchSolrService.class);
		ejb.addClass(IndexableFileService.class);
		ejb.addAsResource("solr.properties");
		ejb.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		// Test
		ejb.addClass(IndexableFileITestCase.class);

		// Create a maven dependency resolver
		MavenDependencyResolver resolver = DependencyResolvers.use(
				MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

		// Build the EAR
		EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class,
				"test-file-ejb.ear");
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
	public void testIndexDocs() {
		long count;
		// Clear
		this.indexService.clear();
		count = this.searchService.countDocuments();
		Assert.assertEquals(0L, count);

		// Index all file
		File rootDir = new File("/Users/laborie/Documents/Workspaces/LocalGed/org.ilaborie.lged/services-test/src/test/resources/data");
		this.indexDir(rootDir);

		count = this.searchService.countDocuments();
		Assert.assertTrue(count > 0);
	}

	/**
	 * Index dir.
	 * 
	 * @param rootDir
	 *            the root dir
	 */
	private void indexDir(File rootDir) {
		List<File> files = Arrays.asList(rootDir.listFiles());
		// Index Files
		Collection<File> onlyFiles = Collections2.filter(files,
				FilesPredicates.classicsFiles());
		this.indexService.indexAll(Collections2.transform(onlyFiles,
				new FileToIndexableFile()));

		// Index sub directory
		Collection<File> onlyDirectories = Collections2.filter(files,
				FilesPredicates.classicsDirectories());
		for (File subDir : onlyDirectories) {
			this.indexDir(subDir);
		}
	}

}
