package com.daikit.graphql.spring.demo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daikit.graphql.config.GQLSchemaConfig;
import com.daikit.graphql.data.input.GQLListLoadConfig;
import com.daikit.graphql.data.output.GQLDeleteResult;
import com.daikit.graphql.data.output.GQLExecutionResult;
import com.daikit.graphql.data.output.GQLListLoadResult;
import com.daikit.graphql.datafetcher.GQLAbstractDeleteDataFetcher;
import com.daikit.graphql.datafetcher.GQLAbstractGetByIdDataFetcher;
import com.daikit.graphql.datafetcher.GQLAbstractGetListDataFetcher;
import com.daikit.graphql.datafetcher.GQLAbstractSaveDataFetcher;
import com.daikit.graphql.datafetcher.GQLCustomMethodDataFetcher;
import com.daikit.graphql.datafetcher.GQLDynamicAttributeRegistry;
import com.daikit.graphql.datafetcher.GQLPropertyDataFetcher;
import com.daikit.graphql.dynamicattribute.IGQLDynamicAttributeSetter;
import com.daikit.graphql.execution.GQLErrorProcessor;
import com.daikit.graphql.execution.GQLExecutor;
import com.daikit.graphql.execution.IGQLExecutorCallback;
import com.daikit.graphql.meta.GQLMetaModel;

import graphql.schema.DataFetcher;

/**
 * Builder for {@link GQLExecutor}
 *
 * @author Thibaut Caselli
 */
public class GQLExecutorBuilder {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Build {@link GQLExecutor}
	 *
	 * @param gqlErrorProcessor
	 *            the {@link GQLErrorProcessor}
	 * @param schemaConfig
	 *            the {@link GQLSchemaConfig}
	 * @param metaModel
	 *            the {@link GQLMetaModel}
	 * @param dataModel
	 *            the {@link DataModel}
	 * @return the built {@link GQLExecutor}
	 */
	public GQLExecutor build(final GQLSchemaConfig schemaConfig, final GQLMetaModel metaModel,
			final GQLErrorProcessor gqlErrorProcessor, final DataModel dataModel) {
		logger.debug("START creating GraphQL executor...");
		final GQLExecutor executor = new GQLExecutor(schemaConfig, metaModel, gqlErrorProcessor,
				createExecutorCallback(), createGetByIdDataFetcher(dataModel), createListDataFetcher(dataModel),
				createSaveDataFetcher(schemaConfig, dataModel), createDeleteDataFetcher(dataModel),
				createCustomMethodDataFetcher(), createPropertyDataFetchers());
		logger.debug("END creating GraphQL executor");
		return executor;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	// PRIVATE UTILS
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

	private IGQLExecutorCallback createExecutorCallback() {
		return new IGQLExecutorCallback() {
			@Override
			public void onAfterExecute(final graphql.ExecutionInput executionInput,
					final GQLExecutionResult executionResult) {
				logger.debug("After execution with input : " + executionInput + " and result : " + executionResult);
			}
		};
	}

	private DataFetcher<?> createGetByIdDataFetcher(final DataModel dataModel) {
		return new GQLAbstractGetByIdDataFetcher() {

			@Override
			protected Object getById(final Class<?> entityClass, final String id) {
				return dataModel.getById(entityClass, id);
			}

		};
	}

	private DataFetcher<GQLListLoadResult> createListDataFetcher(final DataModel dataModel) {
		return new GQLAbstractGetListDataFetcher() {

			@Override
			protected GQLListLoadResult getAll(final Class<?> entityClass, final GQLListLoadConfig listLoadConfig) {
				return dataModel.getAll(entityClass, listLoadConfig);
			}

			@Override
			protected Object getById(final Class<?> entityClass, final String id) {
				return dataModel.getById(entityClass, id);
			}

		};
	}

	private DataFetcher<?> createSaveDataFetcher(final GQLSchemaConfig schemaConfig, final DataModel dataModel) {
		return new GQLAbstractSaveDataFetcher<Object>() {

			@Override
			protected void save(final Object entity) {
				dataModel.save(entity);
			}

			@SuppressWarnings("unchecked")
			@Override
			protected Object getOrCreateAndSetProperties(final Class<?> entityClass,
					final GQLDynamicAttributeRegistry dynamicAttributeRegistry,
					final Map<String, Object> fieldValueMap) {
				// Find or create entity
				final String id = (String) fieldValueMap.get(schemaConfig.getAttributeIdName());
				final Optional<?> existing = StringUtils.isEmpty(id)
						? Optional.empty()
						: dataModel.getById(entityClass, id);
				Object entity;
				try {
					entity = existing.isPresent() ? existing.get() : entityClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				// Set properties
				fieldValueMap.entrySet().stream().forEach(entry -> {
					final Optional<IGQLDynamicAttributeSetter<Object, Object>> dynamicAttributeSetter = dynamicAttributeRegistry
							.getSetter(entityClass, entry.getKey());
					if (!schemaConfig.getAttributeIdName().equals(entry.getKey())) {
						Object value = entry.getValue();
						if (entry.getValue() instanceof Map) {
							final Class<?> propertyType = FieldUtils.getField(entity.getClass(), entry.getKey(), true)
									.getType();
							value = getOrCreateAndSetProperties(propertyType, dynamicAttributeRegistry,
									(Map<String, Object>) entry.getValue());
						}
						if (dynamicAttributeSetter.isPresent()) {
							dynamicAttributeSetter.get().setValue(entity, value);
						} else {
							try {
								FieldUtils.writeField(entity, entry.getKey(), value, true);
							} catch (final IllegalAccessException e) {
								throw new RuntimeException(e);
							}
						}
					}
				});
				return entity;
			}
		};
	}

	private DataFetcher<GQLDeleteResult> createDeleteDataFetcher(final DataModel dataModel) {
		return new GQLAbstractDeleteDataFetcher() {
			@Override
			protected void delete(final Class<?> entityClass, final String id) {
				dataModel.delete(entityClass, id);
			}
		};
	}

	private DataFetcher<?> createCustomMethodDataFetcher() {
		return new GQLCustomMethodDataFetcher();
	}

	private List<GQLPropertyDataFetcher<?>> createPropertyDataFetchers() {
		return Collections.emptyList();
	}

}
