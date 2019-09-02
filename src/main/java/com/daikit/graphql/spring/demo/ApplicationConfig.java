package com.daikit.graphql.spring.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.daikit.graphql.execution.GQLErrorProcessor;
import com.daikit.graphql.execution.GQLExecutor;

/**
 * {@link ApplicationConfig} spring component
 *
 * @author tcaselli
 * @version $Revision$ Last modifier: $Author$ Last commit: $Date$
 */
@Configuration
public class ApplicationConfig {

	@Autowired
	private GQLErrorProcessor gqlErrorProcessor;
	@Autowired
	protected DataModel dataModel;

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
		return new GQLExecutorBuilder().build(gqlErrorProcessor, dataModel);
	}
}