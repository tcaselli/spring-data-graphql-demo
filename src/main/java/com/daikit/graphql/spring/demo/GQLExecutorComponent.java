package com.daikit.graphql.spring.demo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.daikit.graphql.constants.GQLSchemaConstants;
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
import com.daikit.graphql.utils.GQLPropertyUtils;

import graphql.schema.DataFetcher;

/**
 * {@link GQLExecutorComponent} spring component
 *
 * @author tcaselli
 * @version $Revision$ Last modifier: $Author$ Last commit: $Date$
 */
@Component
public class GQLExecutorComponent {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GQLErrorProcessor gqlErrorProcessor;
	@Autowired
	protected DataModel dataModel;

	private GQLExecutor executor;

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	// INITIALIZATION METHODS
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

	/**
	 * Initialization method
	 */
	@PostConstruct
	public void initialize() {
		logger.debug("START initializing GraphQL...");
		executor = new GQLExecutor(createMetaModel(), gqlErrorProcessor, createExecutorCallback(),
				createGetByIdDataFetcher(), createListDataFetcher(), createSaveDataFetcher(), createDeleteDataFetcher(),
				createCustomMethodDataFetcher(), createPropertyDataFetchers());
		logger.debug("END initializing GraphQL");
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	// GETTERS / SETTERS
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

	/**
	 * @return the executor
	 */
	public GQLExecutor getExecutor() {
		return executor;
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	// PRIVATE UTILS
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

	private GQLMetaModel createMetaModel() {
		return new GQLMetaModelBuilder().build();
	}

	private IGQLExecutorCallback createExecutorCallback() {
		return new IGQLExecutorCallback() {
			@Override
			public void onAfterExecute(graphql.ExecutionInput executionInput, GQLExecutionResult executionResult) {
				logger.debug("After execution with input : " + executionInput + " and result : " + executionResult);
			}
		};
	}

	private DataFetcher<?> createGetByIdDataFetcher() {
		return new GQLAbstractGetByIdDataFetcher() {

			@Override
			protected Object getById(Class<?> entityClass, String id) {
				return dataModel.getById(entityClass, id);
			}

		};
	}

	private DataFetcher<GQLListLoadResult> createListDataFetcher() {
		return new GQLAbstractGetListDataFetcher() {

			@Override
			protected GQLListLoadResult getAll(Class<?> entityClass, GQLListLoadConfig listLoadConfig) {
				return dataModel.getAll(entityClass, listLoadConfig);
			}

			@Override
			protected Object getById(Class<?> entityClass, String id) {
				return dataModel.getById(entityClass, id);
			}

		};
	}

	private DataFetcher<?> createSaveDataFetcher() {
		return new GQLAbstractSaveDataFetcher<Object>() {

			@Override
			protected void save(Object entity) {
				dataModel.save(entity);
			}

			@SuppressWarnings("unchecked")
			@Override
			protected Object getOrCreateAndSetProperties(Class<?> entityClass,
					GQLDynamicAttributeRegistry dynamicAttributeRegistry, Map<String, Object> fieldValueMap) {
				// Find or create entity
				final String id = (String) fieldValueMap.get(GQLSchemaConstants.FIELD_ID);
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
					if (!GQLSchemaConstants.FIELD_ID.equals(entry.getKey())) {
						Object value = entry.getValue();
						if (entry.getValue() instanceof Map) {
							final Class<?> propertyType = GQLPropertyUtils.getPropertyType(entity.getClass(),
									entry.getKey());
							value = getOrCreateAndSetProperties(propertyType, dynamicAttributeRegistry,
									(Map<String, Object>) entry.getValue());
						}
						if (dynamicAttributeSetter.isPresent()) {
							dynamicAttributeSetter.get().setValue(entity, value);
						} else {
							GQLPropertyUtils.setPropertyValue(entity, entry.getKey(), value);
						}
					}
				});
				return entity;
			}
		};
	}

	private DataFetcher<GQLDeleteResult> createDeleteDataFetcher() {
		return new GQLAbstractDeleteDataFetcher() {
			@Override
			protected void delete(Class<?> entityClass, String id) {
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
