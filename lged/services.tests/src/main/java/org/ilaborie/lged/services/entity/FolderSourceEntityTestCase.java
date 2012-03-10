package org.ilaborie.lged.services.entity;

import java.io.File;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import junit.framework.Assert;

import org.ilaborie.lged.commons.model.Source;
import org.ilaborie.search.commons.model.IIndexableElement;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FolderSourceEntityTestCase {

	/** The entity manager. */
	@PersistenceContext(name = "unit-test")
	private EntityManager em;

	/** The user transaction. */
	@Inject
	private UserTransaction utx;

	/**
	 * Creates the test archive.
	 * 
	 * @return the java archive
	 */
	@Deployment
	public static JavaArchive createTestArchive() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "FolderSourceEntityTestCase.jar");
		jar.addPackages(true, Source.class.getPackage()); // Commons
		jar.addClasses(IIndexableElement.class); // Search Commons
		jar.addPackages(true, "com.google.common.base"); // Guava

		jar.addClasses(SourceEntity.class, FolderSourceEntity.class, ShelfEntity.class); // Entities
		jar.addAsManifestResource("test-persistence.xml", "persistence.xml");
		
		jar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

		// Create a maven dependency resolver
		MavenDependencyResolver resolver = DependencyResolvers.use(
		        MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

		// Build the EAR
		EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class,
		        "test-entity.ear");
		archive.addAsModule(jar);

		// Add Guava
		archive.addAsLibraries(resolver.artifact("com.google.guava:guava")
		        .resolveAsFiles());

		// logging
		archive.addAsLibraries(resolver.artifact("org.slf4j:slf4j-api")
		        .resolveAsFiles());
		archive.addAsLibraries(resolver.artifact("org.slf4j:jcl-over-slf4j")
		        .resolveAsFiles());

		return jar;
	}

	/**
	 * Test mapping.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testMapping() throws Exception {
		String id = "1";
		String comment = "Plop";
		String folder = new File("test").getAbsoluteFile().getParentFile().getAbsolutePath();

		this.utx.begin();
		// Shelf
		ShelfEntity shelf = new ShelfEntity();
		shelf.setUuid("0");
		shelf.setName("Shelf");

		// Create entity
		SourceEntity entity = new FolderSourceEntity();
		entity.setShelf(shelf);
		entity.setComment(comment);
		entity.setUuid(id);
		((FolderSourceEntity)entity).setFolder(folder);
		
		// Test Persistence
		this.em.persist(shelf);
		this.em.persist(entity);
		this.utx.commit();

		this.utx.begin();
		// Should retrieve the entity
		
		entity = this.em.find(SourceEntity.class, id);
		Assert.assertNotNull(entity);
		Assert.assertEquals(comment, entity.getComment());

		// Test remove
		this.em.remove(entity);
		this.utx.commit();

		// The entity is removed
		entity = this.em.find(SourceEntity.class, id);
		Assert.assertNull(entity);
	}
}
