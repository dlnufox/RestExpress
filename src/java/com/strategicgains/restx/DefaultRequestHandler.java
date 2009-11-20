/*
 * Copyright 2009, Strategic Gains, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.strategicgains.restx;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.strategicgains.restx.route.Request;
import com.strategicgains.restx.route.Resolver;
import com.strategicgains.restx.route.Response;
import com.strategicgains.restx.route.Service;

/**
 * @author toddf
 * @since Nov 13, 2009
 */
@ChannelPipelineCoverage("all")
public class DefaultRequestHandler
extends SimpleChannelUpstreamHandler
{
	// SECTION: INSTANCE VARIABLES

	private Resolver<Service> serviceResolver;


	// SECTION: CONSTRUCTORS

	public DefaultRequestHandler(Resolver<Service> serviceResolver)
	{
		super();
		setServiceResolver(serviceResolver);
	}

	
	// SECTION: ACCESSORS/MUTATORS

	/**
	 * @return the resolver
	 */
	public Resolver<Service> getServiceResolver()
	{
		return serviceResolver;
	}

	/**
	 * @param resolver
	 *            the resolver to set
	 */
	public void setServiceResolver(Resolver<Service> resolver)
	{
		this.serviceResolver = resolver;
	}


	// SECTION: SIMPLE-CHANNEL-UPSTREAM-HANDLER

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event)
	throws Exception
	{
		// Determine which service to call via URL & parameters.
		// Throw exception if no service found/available.
		Request request = createRequest((HttpRequest) event.getMessage());
		Service service = serviceResolver.resolve(request);

		// Deserialize/marshal the request contents, if necessary.
		// Call the service, passing the marshaled object(s).
		Object result = service.process(request, service.deserialize(request));

		// Serialize/Unmarshal the response, if necessary.
		Response response = service.serialize(request, result);

		// Set resonse and accept headers, if appropriate.
		writeResponse(request, response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent event)
	throws Exception
	{
		event.getCause().printStackTrace();
		event.getChannel().close();
	}

	/**
     * @param request
     * @return
     */
    private Request createRequest(HttpRequest request)
    {
	    // TODO Auto-generated method stub
	    return null;
    }

    /**
     * @param message
     * @return
     */
    private void writeResponse(Request request, Response response)
    {
	    // TODO Auto-generated method stub
    }
}
