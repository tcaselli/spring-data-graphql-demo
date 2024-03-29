package com.daikit.graphql.spring.demo.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class 4
 *
 * @author Thibaut Caselli
 */
public class Entity4 extends AbstractEntity {

	private List<Entity1> entity1s = new ArrayList<>();

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	// GETTERS / SETTERS
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

	/**
	 * @return the entity1s
	 */
	public List<Entity1> getEntity1s() {
		return entity1s;
	}

	/**
	 * @param entity1s
	 *            the entity1s to set
	 */
	public void setEntity1s(final List<Entity1> entity1s) {
		this.entity1s = entity1s;
	}
}
