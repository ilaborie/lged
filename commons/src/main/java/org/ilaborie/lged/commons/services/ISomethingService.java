package org.ilaborie.lged.commons.services;

import java.util.Set;

import org.ilaborie.lged.commons.model.Something;

/**
 * The Interface ISomethingService.
 */
public interface ISomethingService {
   
   /**
    * Gets the something by id.
    *
    * @param id the id
    * @return the something
    */
   Something getById(long id);
   
   /**
    * Gets the all.
    *
    * @return the all
    */
   Set<Something> getAll();

}
