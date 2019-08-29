package com.daikit.graphql.spring.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.daikit.graphql.execution.GQLErrorProcessor;
import com.daikit.graphql.spring.GQLRequestHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Application config
 *
 * @author Thibaut Caselli
 */
@Configuration
public class ApplicationConfig {

	/**
	 * @return the {@link WebMvcConfigurer}
	 */
	@Bean
	public WebMvcConfigurer webConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(final ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/graphiql/**").addResourceLocations("/graphiql/");
			}

			@Override
			public void addCorsMappings(final CorsRegistry registry) {
				registry.addMapping("/**").allowCredentials(true);
			}
		};
	}

	/**
	 * @return the created {@link ObjectMapper} for GraphQL read/write
	 */
	@Bean
	public ObjectMapper createGQLObjectMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	/**
	 * @return the created {@link GQLErrorProcessor}
	 */
	@Bean
	public GQLErrorProcessor createGQLErrorProcessor() {
		return new GQLErrorProcessor();
	}

	/**
	 * Create the {@link GQLRequestHandler}
	 *
	 * @param gqlObjectMapper
	 *            the {@link ObjectMapper} for GraphQL read/write
	 * @param gQLExecutorComponent
	 *            the {@link GQLExecutorComponent}
	 * @return the created {@link GQLRequestHandler}
	 */
	@Bean
	public GQLRequestHandler createGQLRequestHandler(@Autowired ObjectMapper gqlObjectMapper,
			@Autowired GQLExecutorComponent gQLExecutorComponent) {
		return new GQLRequestHandler(gqlObjectMapper, gQLExecutorComponent.getExecutor());
	}

}
