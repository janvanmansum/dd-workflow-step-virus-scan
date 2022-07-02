/*
 * Copyright (C) 2022 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.knaw.dans.virusscan;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.knaw.dans.virusscan.core.service.ClamdServiceImpl;
import nl.knaw.dans.virusscan.core.service.DataverseApiServiceImpl;
import nl.knaw.dans.virusscan.core.service.VirusScannerImpl;
import nl.knaw.dans.virusscan.resource.InvokeResourceImpl;

public class DdWorkflowStepVirusScanApplication extends Application<DdWorkflowStepVirusScanConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DdWorkflowStepVirusScanApplication().run(args);
    }

    @Override
    public String getName() {
        return "Dd Workflow Step Virus Scan";
    }

    @Override
    public void initialize(final Bootstrap<DdWorkflowStepVirusScanConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final DdWorkflowStepVirusScanConfiguration configuration, final Environment environment) {
        final var client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
            .build(getName());
        //        environment.jersey().register(new ExternalServiceResource(client));

        var clamdService = new ClamdServiceImpl(configuration.getVirusscanner().getClamd());
        var dataverseApiService = new DataverseApiServiceImpl(configuration.getDataverse(), client);
        var virusScanner = new VirusScannerImpl(configuration.getVirusscanner(), clamdService);

        var resource = new InvokeResourceImpl(client, dataverseApiService, virusScanner);

        environment.jersey().register(resource);

    }

}