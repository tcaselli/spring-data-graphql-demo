package com.daikit.graphql.spring.demo;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;

import com.daikit.graphql.dynamicattribute.IGQLAbstractDynamicAttribute;
import com.daikit.graphql.meta.GQLController;
import com.daikit.graphql.meta.GQLMetaModel;
import com.daikit.graphql.spring.demo.data.AbstractEntity;
import com.daikit.graphql.spring.demo.data.EmbeddedData1;
import com.daikit.graphql.spring.demo.data.EmbeddedData2;
import com.daikit.graphql.spring.demo.data.EmbeddedData3;
import com.daikit.graphql.spring.demo.data.Entity1;
import com.daikit.graphql.spring.demo.data.Entity2;
import com.daikit.graphql.spring.demo.data.Entity3;
import com.daikit.graphql.spring.demo.data.Entity4;
import com.daikit.graphql.spring.demo.data.Entity5;
import com.daikit.graphql.spring.demo.data.Entity6;

/**
 * Meta data for building the schema.
 *
 * @author Thibaut Caselli
 */
public class GQLMetaModelBuilder {

	/**
	 * Build the test GraphQL data meta entity
	 *
	 * @param applicationContext
	 *            the Spring {@link ApplicationContext}
	 *
	 * @return the built {@link GQLMetaModel}
	 */
	public GQLMetaModel build(final ApplicationContext applicationContext) {
		final Collection<IGQLAbstractDynamicAttribute<?>> dynamicAttributes = applicationContext
				.getBeansOfType(IGQLAbstractDynamicAttribute.class, false, false).values().stream()
				.map(dynamicAttribute -> (IGQLAbstractDynamicAttribute<?>) dynamicAttribute)
				.collect(Collectors.toList());
		final Collection<Object> customMethods = applicationContext.getBeansWithAnnotation(GQLController.class)
				.values();
		final Collection<Class<?>> entityClasses = Arrays.asList(AbstractEntity.class, Entity1.class, Entity2.class,
				Entity3.class, Entity4.class, Entity5.class, Entity6.class);
		final Collection<Class<?>> availableEmbeddedEntityClasses = Arrays.asList(EmbeddedData1.class,
				EmbeddedData2.class, EmbeddedData3.class);

		return GQLMetaModel.createFromEntityClasses(entityClasses, availableEmbeddedEntityClasses, dynamicAttributes,
				customMethods);
	}

}
