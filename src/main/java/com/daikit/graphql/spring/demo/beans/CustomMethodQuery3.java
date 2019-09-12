package com.daikit.graphql.spring.demo.beans;

import java.util.List;

import org.springframework.stereotype.Component;

import com.daikit.graphql.custommethod.GQLCustomMethod1Arg;
import com.daikit.graphql.custommethod.GQLCustomMethod5Arg;
import com.daikit.graphql.spring.demo.data.EmbeddedData1;
import com.daikit.graphql.spring.demo.data.Entity1;
import com.daikit.graphql.spring.demo.data.Enum1;

/**
 * {@link GQLCustomMethod1Arg} that will automatically registered in GraphQL
 * schema
 *
 * @author Thibaut Caselli
 */
@Component
public class CustomMethodQuery3
		extends
			GQLCustomMethod5Arg<Entity1, Enum1, List<String>, List<Enum1>, List<EmbeddedData1>, String> {

	/**
	 * Constructor
	 */
	public CustomMethodQuery3() {
		super("customMethodQuery3", false, "arg1", "arg2", "arg3", "arg4", "arg5");
	}

	@Override
	public Entity1 apply(Enum1 arg1, List<String> arg2, List<Enum1> arg3, List<EmbeddedData1> arg4, String arg5) {
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

}
