package com.daikit.graphql.spring.demo.beans;

import org.springframework.stereotype.Component;

import com.daikit.graphql.custommethod.GQLCustomMethod1Arg;
import com.daikit.graphql.custommethod.GQLCustomMethod2Arg;
import com.daikit.graphql.spring.demo.data.EmbeddedData1;
import com.daikit.graphql.spring.demo.data.Entity1;

/**
 * {@link GQLCustomMethod1Arg} that will automatically registered in GraphQL
 * schema
 *
 * @author Thibaut Caselli
 */
@Component
public class CustomMethodQuery2 extends GQLCustomMethod2Arg<Entity1, String, EmbeddedData1> {

	/**
	 * Constructor
	 */
	public CustomMethodQuery2() {
		super("customMethodQuery2", false, "arg1", "arg2");
	}

	@Override
	public Entity1 apply(final String arg1, final EmbeddedData1 arg2) {
		final Entity1 result = new Entity1();
		result.setIntAttr(5);
		result.setStringAttr(arg1);
		result.setEmbeddedData1(arg2);
		return result;
	}
}
