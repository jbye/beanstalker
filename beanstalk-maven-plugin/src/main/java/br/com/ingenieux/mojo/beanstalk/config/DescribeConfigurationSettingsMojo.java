package br.com.ingenieux.mojo.beanstalk.config;

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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoSince;

import br.com.ingenieux.mojo.beanstalk.AbstractNeedsEnvironmentMojo;

import com.amazonaws.services.elasticbeanstalk.model.DescribeConfigurationSettingsRequest;

/**
 * Returns the Configuration Settings
 * 
 * See the <a href=
 * "http://docs.amazonwebservices.com/elasticbeanstalk/latest/api/API_DescribeConfigurationSettings.html"
 * >DescribeConfigurationSettings API</a> call.
 * 
 */
@MojoGoal("describe-configuration-settings")
@MojoSince("0.2.0")
public class DescribeConfigurationSettingsMojo extends
    AbstractNeedsEnvironmentMojo {
	/**
	 * Template Name
	 */
	@MojoParameter(expression="${beanstalk.templateName}", description="Template Name")
	String templateName;

	protected Object executeInternal() throws MojoExecutionException,
	    MojoFailureException {
		boolean bNameDefined = org.apache.commons.lang.StringUtils
		    .isNotBlank(environmentName);
		boolean bTemplateNameDefined = org.apache.commons.lang.StringUtils
		    .isNotBlank(templateName);

		if (bNameDefined ^ bTemplateNameDefined)
			throw new MojoFailureException("You can define either environmentName or templateName, but not both");

		DescribeConfigurationSettingsRequest req = new DescribeConfigurationSettingsRequest()//
		    .withApplicationName(applicationName)//
		    .withEnvironmentName(environmentName)//
		    .withTemplateName(templateName)//
		;

		return getService().describeConfigurationSettings(req);
	}
}
