package org.ilaborie.lged.services.ejb;

import java.util.Set;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.ilaborie.lged.commons.model.Something;
import org.ilaborie.lged.commons.services.ISomethingService;

/**
 * The Class SomethingServiceITestCase.
 */
@RunWith(Arquillian.class)
public class SomethingServiceITestCase {

   /** The service. */
   @Inject
   private ISomethingService service;

   /**
    * Builds the archive.
    *
    * @return the archive
    */
   @Deployment
   public static Archive<?> buildArchive() {
      // Create a dependency JAR
      JavaArchive dependencies = ShrinkWrap.create(JavaArchive.class, "dependencies.jar");
      // add Guava
      dependencies.addPackages(true, "com.google.common");
      // add Logging
      dependencies.addPackages(true, "org.slf4j");
      dependencies.addPackages(true, "org.apache.log4j");
      // Add logging test configuration
      dependencies.addAsResource("log4j.properties");

      // Create an EJB jar
      JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "ejb.jar");
      // Add all org.ilaborie.lged.commons classes
      ejb.addPackages(true, "org.ilaborie.lged.commons");
      // Add the service implementation
      ejb.addClass(SomethingService.class);
      // Add the test
      ejb.addClass(SomethingServiceITestCase.class);
      // Add an empty 'bean.xml' file for CDI
      ejb.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

      // Build the EAR
      EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class, "test-something-ejb.ear");
      archive.addAsModule(ejb);
      archive.addAsLibrary(dependencies);
      return archive;
   }

   /**
    * Test get by id.
    */
   @Test
   public void testGetById() {
      Something something;

      // Test with an invalid id
      something = this.service.getById(-1L);
      Assert.assertNull(something);

      // Test with a valid
      something = this.service.getById(1L);
      Assert.assertNotNull(something);
   }

   /**
    * Test get all.
    */
   @Test
   public void testGetAll() {
      Set<Something> all;
      // Test get all
      all = this.service.getAll();
      Assert.assertNotNull(all);
      Assert.assertFalse(all.isEmpty());
   }

}
