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
package nl.knaw.dans.virusscan.core.health;

import nl.knaw.dans.virusscan.core.model.DataverseVersionResponse;
import nl.knaw.dans.virusscan.core.service.DataverseApiService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataverseHealthCheckTest {

    @Test
    void checkSuccessful() throws IOException {

        var service = Mockito.mock(DataverseApiService.class);
        Mockito.when(service.getDataverseInfo()).thenReturn(new DataverseVersionResponse("OK"));

        var result = new DataverseHealthCheck(service).check();

        assertTrue(result.isHealthy());
    }

    @Test
    void checkUnsuccessful() throws IOException {

        var service = Mockito.mock(DataverseApiService.class);
        Mockito.when(service.getDataverseInfo()).thenReturn(new DataverseVersionResponse("NOT OK"));

        var result = new DataverseHealthCheck(service).check();

        assertFalse(result.isHealthy());
    }

    @Test
    void checkIOExceptionsBeingCaught() throws IOException {

        var service = Mockito.mock(DataverseApiService.class);
        Mockito.doThrow(IOException.class).when(service).getDataverseInfo();

        var result = new DataverseHealthCheck(service).check();

        assertFalse(result.isHealthy());
    }
}