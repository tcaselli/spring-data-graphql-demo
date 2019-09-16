package com.daikit.graphql.spring.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.daikit.graphql.config.GQLSchemaConfig;
import com.daikit.graphql.execution.GQLErrorProcessor;
import com.daikit.graphql.execution.GQLExecutor;
import com.daikit.graphql.meta.GQLMetaModel;

/**
 * {@link ApplicationConfig} spring component
 *
 * @author tcaselli
 * @version $Revision$ Last modifier: $Author$ Last commit: $Date$
 */
@Configuration
public class ApplicationConfig {

	@Autowired
	private GQLSchemaConfig schemaConfig;
	@Autowired
	private DataModel dataModel;
	@Autowired
	private GQLErrorProcessor gqlErrorProcessor;
	@Autowired
	private ApplicationContext applicationContext;

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	// INITIALIZATION METHODS
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

	/**
	 * Create GQL Executor bean
	 *
	 * @return the created {@link GQLExecutor}
	 */
	@Bean
	public GQLExecutor createExecutor() {
		final GQLMetaModel metaModel = new GQLMetaModelBuilder().build(applicationContext);
		return new GQLExecutorBuilder().build(schemaConfig, metaModel, gqlErrorProcessor, dataModel);
	}

}
