package org.dieschnittstelle.ess.mip.components.shopping;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * following: https://stackoverflow.com/questions/31374994/how-to-inject-entitymanager-in-cdi-weld
 * but does not work yet. see: https://www.sitepoint.com/cdi-weld-inject-jpa-hibernate-entity-managers/
 */
@ApplicationScoped
public class EntityManagerProvider {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ShoppingDataAccessor {

    }

    @Produces
    @ShoppingDataAccessor
    @PersistenceContext(unitName = "shopping_PU")
    private EntityManager em;

}