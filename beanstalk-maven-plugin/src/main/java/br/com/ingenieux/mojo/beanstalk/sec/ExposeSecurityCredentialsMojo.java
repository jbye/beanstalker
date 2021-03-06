package br.com.ingenieux.mojo.beanstalk.sec;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoRequiresProject;
import org.jfrog.maven.annomojo.annotations.MojoSince;

import br.com.ingenieux.mojo.aws.Expose;
import br.com.ingenieux.mojo.beanstalk.AbstractBeanstalkMojo;

/**
 * Exposes (i.e., copies) the security credentials from settings.xml into
 * project properties
 * 
 * <p>
 * You can define the server, or not. If you don't, it will work if you did
 * something like that
 * </p>
 * 
 * <pre>
 * &lt;configuration&gt;
 * &nbsp;&nbsp;&lt;exposes&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;expose&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;serverId&gt;${beanstalk.serverId}&lt;/serverId&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;accessKey&gt;aws.accessKey&lt;/accessKey&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;secretKey&gt;aws.accessKey&lt;/secretKey&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/expose&gt;
 * &nbsp;&nbsp;&lt;/exposes&gt;
 * &lt;/configuration&gt;
 * </pre>
 * 
 * <p>
 * While it might look silly (and silly enough to get its own Plugin instead of
 * beanstalker), it power comes when combined with the <a
 * href="http://mojo.codehaus.org/properties-maven-plugin/">Properties Maven
 * Plugin</a>
 * </p>
 */
@MojoGoal("expose-security-credentials")
@MojoSince("0.2.7-RC4")
@MojoRequiresProject
public class ExposeSecurityCredentialsMojo extends AbstractBeanstalkMojo {
	@MojoParameter(description = "Which Server Settings to Expose?")
	Expose[] exposes = new Expose[0];

	@MojoParameter(defaultValue = "${project}")
	MavenProject project;

	protected Object executeInternal() throws MojoExecutionException,
			MojoFailureException {
		/**
		 * Fill in defaults if needed
		 */
		if (0 == exposes.length) {
			exposes = new Expose[1];
			exposes[0] = new Expose();
			exposes[0].setServerId(this.serverId);
			exposes[0].setAccessKey("aws.accessKey");
			exposes[0].setSharedKey("aws.secretKey");
		} else {
			/**
			 * Validate parameters, for gods sake
			 */
			for (Expose e : exposes) {
				Validate.isTrue(StringUtils.isNotBlank(e.getServerId()),
						"serverId must not supplied");
				Validate.isTrue(StringUtils.isNotBlank(e.getAccessKey()),
						"accessKey must not supplied");
				Validate.isTrue(StringUtils.isNotBlank(e.getSharedKey()),
						"sharedKey must not supplied");
			}
		}

		for (Expose e : exposes) {
			Expose realExpose = super.exposeSettings(e.getServerId());

			getLog().info(
					String.format(
							"Writing Security Settings from serverId ('%s') into properties '%s' (accessKey) and '%s' (secretKey)",
							e.getServerId(), e.getAccessKey(), e.getSharedKey()));

			project.getProperties().put(e.getAccessKey(),
					realExpose.getAccessKey());
			
			project.getProperties().put(e.getSharedKey(),
					realExpose.getSharedKey());
		}

		return null;
	}
}
