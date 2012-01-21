/**
 * 
 */
package org.ilaborie.lged.commons.model;

import com.google.common.base.Objects;

/**
 * A business object. <P>
 * Explanation goes here. <P>
 */
public class Something {

   /** The id. */
   private Long id;

   /** The label. */
   private String label;

   /**
    * Instantiates a new something.
    */
   public Something() {
      super();
   }

   /**
    * {@inheritDoc}
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("id", this.id).add("label", this.label).toString();
   }

   /**
    * {@inheritDoc}
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode() {
      return Objects.hashCode(this.getLabel());
   }

   /**
    * {@inheritDoc}
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof Something)) {
         return false;
      }
      Something other = (Something) obj;
      if (label == null) {
         if (other.label != null) {
            return false;
         }
      } else if (!label.equals(other.label)) {
         return false;
      }
      return true;
   }

   /**
    * Gets the id.
    *
    * @return the id
    */
   public Long getId() {
      return id;
   }

   /**
    * Sets the id.
    *
    * @param id the new id
    */
   public void setId(Long id) {
      this.id = id;
   }

   /**
    * Gets the label.
    *
    * @return the label
    */
   public String getLabel() {
      return label;
   }

   /**
    * Sets the label.
    *
    * @param label the new label
    */
   public void setLabel(String label) {
      this.label = label;
   }

}
