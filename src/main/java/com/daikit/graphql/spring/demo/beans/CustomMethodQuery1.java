package com.daikit.graphql.spring.demo.beans;

import org.springframework.stereotype.Component;

import com.daikit.graphql.custommethod.GQLCustomMethod1Arg;
import com.daikit.graphql.spring.demo.data.EmbeddedData1;
import com.daikit.graphql.spring.demo.data.Entity1;

/**
 * {@link GQLCustomMethod1Arg} that will automatically registered in GraphQL
 * schema
 *
 * @author Thibaut Caselli
 */
@Component
public class CustomMethodQuery1 extends GQLCustomMethod1Arg<Entity1, String> {

	/**
	 * Constructor
	 */
	public CustomMethodQuery1() {
		super("customMethodQuery1", false, "arg1");
	}

	@Override
	public Entity1 apply(final String arg1) {
		final Entity1 result = new Entity1();
		result.setStringAttr(arg1);
		final EmbeddedData1 embeddedData1 = new EmbeddedData1();
		embeddedData1.setStringAttr(arg1);
		result.setEmbeddedData1(embeddedData1);
		return result;
	}

}
