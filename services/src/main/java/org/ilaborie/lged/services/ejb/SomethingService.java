package org.ilaborie.lged.services.ejb;

import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.ilaborie.lged.commons.model.Something;
import org.ilaborie.lged.commons.services.ISomethingService;

/**
 * The Class SomethingService.
 */
@Stateless
@Local(ISomethingService.class)
public class SomethingService implements ISomethingService {

   /** Logger */
   private static final Logger LOG = LoggerFactory.getLogger(SomethingService.class);

   /** The map. */
   private Map<Long, Something> map;

   /**
    * Instantiates a new something service.
    */
   public SomethingService() {
      super();
   }

   /**
    * Initialize.
    */
   @PostConstruct
   protected void initialize() {
      LOG.trace("[{}] initialize...", this.getClass().getSimpleName());
      String[] labels = new String[] {"Lorem", "ipsum", "dolor", "sit", "amet"};
      this.map = Maps.newHashMap();
      Something something;
      for (int i = 0; i < labels.length; i++) {
         something = new Something();
         something.setId((long) i + 1);
         something.setLabel(labels[i]);

         this.map.put(something.getId(), something);
      }
      LOG.debug("[{}] initialized.", this.getClass().getSimpleName());
   }

   /**
    * {@inheritDoc}
    * @see org.ilaborie.lged.commons.services.ISomethingService#getById(long)
    */
   @Override
   public Something getById(long id) {
      LOG.debug("{}#getById({})", this.getClass().getSimpleName(), id);
      Something something = this.map.get(id);
      LOG.trace("{}#getById({}) returns: {}", new Object[] {this.getClass().getSimpleName(), id,
            something});
      return something;
   }

   /**
    * {@inheritDoc}
    * @see org.ilaborie.lged.commons.services.ISomethingService#getAll()
    */
   @Override
   public Set<Something> getAll() {
      LOG.debug("{}#getAll()", this.getClass().getSimpleName());
      Set<Something> set = Sets.newHashSet(this.map.values());
      LOG.trace("{}#getAll() returns: {}", this.getClass().getSimpleName(), set);
      return set;
   }

}
