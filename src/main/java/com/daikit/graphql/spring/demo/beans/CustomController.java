package com.daikit.graphql.spring.demo.beans;

import java.util.List;

import org.springframework.stereotype.Component;

import com.daikit.graphql.enums.GQLMethodType;
import com.daikit.graphql.meta.GQLController;
import com.daikit.graphql.meta.GQLMethod;
import com.daikit.graphql.meta.GQLParam;
import com.daikit.graphql.spring.demo.data.EmbeddedData1;
import com.daikit.graphql.spring.demo.data.Entity1;
import com.daikit.graphql.spring.demo.data.Enum1;

/**
 * Test controller for custom methods
 *
 * @author Thibaut Caselli
 */
@Component
@GQLController
public class CustomController {

	/**
	 * Custom method query
	 *
	 * @param arg1
	 *            first argument with type String
	 * @return and {@link Entity1}
	 */
	@GQLMethod(type = GQLMethodType.QUERY)
	public Entity1 customMethodQuery1(@GQLParam("arg1") String arg1) {
		final Entity1 result = new Entity1();
		result.setStringAttr(arg1);
		final EmbeddedData1 embeddedData1 = new EmbeddedData1();
		embeddedData1.setStringAttr(arg1);
		result.setEmbeddedData1(embeddedData1);
		return result;
	}

	/**
	 * Custom method query 2
	 *
	 * @param arg1
	 *            first argument with type String
	 * @param arg2
	 *            second argument with type EmbeddedData1
	 * @return an {@link Entity1}
	 */
	@GQLMethod(type = GQLMethodType.QUERY)
	public Entity1 customMethodQuery2(@GQLParam("arg1") String arg1, @GQLParam("arg2") EmbeddedData1 arg2) {
		final Entity1 result = new Entity1();
		result.setIntAttr(5);
		result.setStringAttr(arg1);
		result.setEmbeddedData1(arg2);
		return result;
	}

	/**
	 * Custom method query 3
	 *
	 * @param arg1
	 *            first argument with type String
	 * @param arg2
	 *            second argument with type List of String
	 * @param arg3
	 *            third argument with type List of Enum1
	 * @param arg4
	 *            fourth argument with type List of EmbeddedData1
	 * @param arg5
	 *            fifth argument with type String
	 * @return an {@link Entity1}
	 */
	@GQLMethod(type = GQLMethodType.QUERY)
	public Entity1 customMethodQuery3(@GQLParam("arg1") Enum1 arg1, @GQLParam("arg2") List<String> arg2,
			@GQLParam("arg3") List<Enum1> arg3, @GQLParam("arg4") List<EmbeddedData1> arg4,
			@GQLParam("arg5") String arg5) {
		final Entity1 result = new Entity1();
		result.setEnumAttr(arg1);
		result.setStringList(arg2);
		result.setEnumList(arg3);
		result.setEmbeddedData1s(arg4);
		if (arg5 == null) {
			result.setStringAttr("NULLVALUE");
		}
		return result;
	}

	/**
	 * Custom method mutation 1
	 *
	 * @param arg1
	 *            first argument of type String
	 * @return an {@link Entity1}
	 */
	@GQLMethod(type = GQLMethodType.MUTATION)
	public Entity1 customMethodMutation1(@GQLParam("arg1") String arg1) {
		final Entity1 result = new Entity1();
		result.setStringAttr(arg1);
		return result;
	}

}
