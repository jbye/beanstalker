package br.com.ingenieux.mojo.beanstalk.env;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoSince;

import br.com.ingenieux.mojo.beanstalk.AbstractBeanstalkMojo;
import br.com.ingenieux.mojo.beanstalk.cmd.env.swap.SwapCNamesCommand;
import br.com.ingenieux.mojo.beanstalk.cmd.env.swap.SwapCNamesContext;
import br.com.ingenieux.mojo.beanstalk.cmd.env.swap.SwapCNamesContextBuilder;

/**
 * Lists the available solution stacks
 * 
 * See the docs for the <a href=
 * "http://docs.amazonwebservices.com/elasticbeanstalk/latest/api/API_SwapEnvironmentCNAMEs.html"
 * >SwapEnvironmentCNAMEs API</a> call.
 * 
 * @author Aldrin Leal
 * 
 */
@MojoGoal("swap-environment-cnames")
@MojoSince("0.2.3")
public class SwapEnvironmentCnamesMojo extends AbstractBeanstalkMojo {
	/**
	 * Source Environment Name
	 * 	 
	 */
	@MojoParameter(expression="${beanstalk.sourceEnvironmentName}")
	String sourceEnvironmentName;

	/**
	 * Source Environment Id
	 */
	@MojoParameter(expression="${beanstalk.sourceEnvironmentId}")
	String sourceEnvironmentId;

	/**
	 * Destination Environment Name
	 */
	@MojoParameter(expression="${beanstalk.destinationEnvironmentName}")
	String destinationEnvironmentName;

	/**
	 * Destination Environment Id
	 */
	@MojoParameter(expression="${beanstalk.destinationEnvironmentId}")
	String destinationEnvironmentId;

	@Override
	protected Object executeInternal() throws AbstractMojoExecutionException {
		SwapCNamesContext context = SwapCNamesContextBuilder.swapCNamesContext()//
		    .withSourceEnvironmentId(sourceEnvironmentId)//
		    .withSourceEnvironmentName(sourceEnvironmentName)//
		    .withDestinationEnvironmentId(destinationEnvironmentId)//
		    .withDestinationEnvironmentName(destinationEnvironmentName)//
		    .build();
		SwapCNamesCommand command = new SwapCNamesCommand(this);

		return command.execute(context);
	}
}
