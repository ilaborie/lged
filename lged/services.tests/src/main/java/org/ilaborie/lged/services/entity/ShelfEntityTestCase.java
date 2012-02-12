package org.ilaborie.lged.services.entity;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import junit.framework.Assert;

import org.ilaborie.lged.commons.model.Shelf;
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
public class ShelfEntityTestCase {

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
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "test.jar");
		jar.addPackages(true, Shelf.class.getPackage());
		jar.addClass(ShelfEntity.class);
		jar.addAsManifestResource("test-persistence.xml", "persistence.xml");
		jar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

		// Create a maven dependency resolver
		MavenDependencyResolver resolver = DependencyResolvers.use(
		        MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

		// Build the EAR
		EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class,
		        "test-entity.ear");
		archive.addAsModule(jar);

		// logging
		archive.addAsLibraries(resolver.artifact("org.slf4j:slf4j-api")
		        .resolveAsFiles());
		archive.addAsLibraries(resolver.artifact("org.slf4j:jcl-over-slf4j")
		        .resolveAsFiles());
		return jar;
	}

	/**
	 * Test mapping.
	 * @throws SystemException 
	 * @throws NotSupportedException 
	 * @throws Exception 
	 */
	@Test
	public void testMapping() throws Exception {
		String id = "1";
		String name = "Plop";

		this.utx.begin();
		// Create entity
		ShelfEntity entity = new ShelfEntity();
		entity.setName(name);
		entity.setUuid(id);
		// Test Persistence
		this.em.persist(entity);
		this.utx.commit();

		this.utx.begin();
		// Should retrieve the entity
		entity = this.em.find(ShelfEntity.class, id);
		Assert.assertNotNull(entity);
		Assert.assertEquals(name, entity.getName());

		// Test remove
		this.em.remove(entity);
		this.utx.commit();

		// The entity is removed
		entity = this.em.find(ShelfEntity.class, id);
		Assert.assertNull(entity);
	}
}
