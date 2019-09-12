package com.daikit.graphql.spring.demo.beans;

import org.springframework.stereotype.Component;

import com.daikit.graphql.dynamicattribute.GQLDynamicAttributeSetter;
import com.daikit.graphql.dynamicattribute.IGQLDynamicAttributeSetter;
import com.daikit.graphql.spring.demo.data.Entity1;

/**
 * {@link IGQLDynamicAttributeSetter} that will automatically be registered on
 * {@link Entity1} in GraphQL schema
 *
 * @author Thibaut Caselli
 */
@Component
public class DynamicAttributeSetterEntity1DynamicAttribute2 extends GQLDynamicAttributeSetter<Entity1, String> {

	/**
	 * Constructor
	 */
	public DynamicAttributeSetterEntity1DynamicAttribute2() {
		super("dynamicAttribute2");
	}

	@Override
	public void setValue(final Entity1 source, final String valueToSet) {
		source.setStringAttr(valueToSet);
	}

}
