package com.daikit.graphql.spring.demo.beans;

import org.springframework.stereotype.Component;

import com.daikit.graphql.dynamicattribute.GQLDynamicAttributeGetter;
import com.daikit.graphql.spring.demo.data.Entity1;

/**
 * {@link DynamicAttributeGetterEntity1DynamicAttribute1} that will
 * automatically be registered on {@link Entity1} in GraphQL schema
 *
 * @author Thibaut Caselli
 */
@Component
public class DynamicAttributeGetterEntity1DynamicAttribute1 extends GQLDynamicAttributeGetter<Entity1, String> {

	/**
	 * Constructor
	 */
	public DynamicAttributeGetterEntity1DynamicAttribute1() {
		super("dynamicAttribute1");
	}

	@Override
	public String getValue(final Entity1 source) {
		return "dynamicValue" + source.getId();
	}

}
