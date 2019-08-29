package com.daikit.graphql.spring.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.daikit.graphql.data.output.GQLExecutionResult;
import com.daikit.graphql.exception.GQLException;
import com.daikit.graphql.execution.GQLErrorProcessor;
import com.daikit.graphql.spring.GQLIOUtils;
import com.daikit.graphql.spring.GQLRequestHandler;
import com.daikit.graphql.utils.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * GRaphQL simple controller
 *
 * @author tcaselli
 * @version $Revision$ Last modifier: $Author$ Last commit: $Date$
 */
@Controller
public class GQLController {

	@Autowired
	private GQLErrorProcessor gqlErrorProcessor;
	@Autowired
	private GQLRequestHandler gqlRequestHandler;
	@Autowired
	private ObjectMapper gqlObjectMapper;

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	// METHODS
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

	/**
	 * Get GraphQL schema instrospection
	 *
	 * @param response
	 *            the {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/graphql/alltypes", produces = MediaType.APPLICATION_JSON_VALUE)
	public void introspection(final HttpServletResponse response) {
		try {
			gqlRequestHandler.handleIntrospectionRequest(response);
		} catch (final Exception e) {
			handleError(response, e);
		}
	}

	/**
	 * Get schema instrospection fragments for GraphQL engine initialization
	 *
	 * @param response
	 *            the {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/graphql/introspectionfragments", produces = MediaType.APPLICATION_JSON_VALUE)
	public void introspectionFragments(final HttpServletResponse response) {
		try {
			gqlRequestHandler.handleIntrospectionFragmentsRequest(response);
		} catch (final Exception e) {
			handleError(response, e);
		}
	}

	/**
	 * Run a query/mutation against GraphQL engine
	 *
	 * @param request
	 *            the {@link HttpServletRequest}
	 * @param response
	 *            the {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/graphql", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
	public void query(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			gqlRequestHandler.handleRequest(request, response);
		} catch (final Exception e) {
			handleError(response, e);
		}
	}

	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	// PRIVATE METHODS
	// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

	private void handleError(final HttpServletResponse response, Exception e) {
		try {
			GQLIOUtils.writeInResponse(response,
					gqlObjectMapper.writeValueAsString(new GQLExecutionResult(gqlErrorProcessor.handleError(e))));
		} catch (final JsonProcessingException e1) {
			throw new GQLException(Message.format("An error happened while writing error result {}", e1.getMessage()),
					e1);
		}
	}

}
