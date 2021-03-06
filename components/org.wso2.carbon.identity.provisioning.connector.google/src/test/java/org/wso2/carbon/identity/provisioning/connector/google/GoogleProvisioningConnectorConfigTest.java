/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.provisioning.connector.google;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.wso2.carbon.identity.provisioning.connector.google.GoogleProvisioningConnectorTestConstants
        .EMAIL_NAME_CLAIM;
import static org.wso2.carbon.identity.provisioning.connector.google.GoogleProvisioningConnectorTestConstants
        .LAST_NAME_CLAIM;
import static org.wso2.carbon.identity.provisioning.connector.google.GoogleProvisioningConnectorTestConstants
        .REQUIRED_FIELDS;
import static org.wso2.carbon.identity.provisioning.connector.google.GoogleProvisioningConnectorTestConstants
        .STREET_ADDRESS_CLAIM;

@PrepareForTest({LogFactory.class})
public class GoogleProvisioningConnectorConfigTest {

    private static final int CASE_1 = 1;
    private static final int CASE_2 = 2;
    private static final int CASE_3 = 3;

    @Mock
    private Log log;

    @DataProvider(name = "requiredFieldsProvider")
    public Object[][] provideTestData() {

        Properties properties1 = new Properties();
        properties1.setProperty(GoogleConnectorConstants.PropertyConfig.REQUIRED_FIELDS, REQUIRED_FIELDS);

        Properties properties2 = new Properties();

        return new Object[][]{
                {properties1, 2},
                {properties2, 0}
        };
    }

    @Test(dataProvider = "requiredFieldsProvider")
    public void testGetRequiredAttributeNames(Properties properties, int size) throws Exception {

        setLogging(false);
        GoogleProvisioningConnectorConfig connectorConfig = new GoogleProvisioningConnectorConfig(properties);

        Assert.assertEquals(connectorConfig.getRequiredAttributeNames().size(), size);
    }


    @DataProvider(name = "getValuesProvider")
    public Object[][] provideGetValueData() {

        Properties properties1 = new Properties();
        properties1.setProperty(GoogleConnectorConstants.PropertyConfig.REQUIRED_FIELDS, REQUIRED_FIELDS);

        Properties properties2 = new Properties();

        return new Object[][]{
                {properties1, REQUIRED_FIELDS},
                {properties2, null}
        };
    }
    @Test(dataProvider = "getValuesProvider")
    public void testGetValue(Properties properties, String expectedValue) throws Exception {

        setLogging(false);
        GoogleProvisioningConnectorConfig connectorConfig = new GoogleProvisioningConnectorConfig(properties);

        Assert.assertEquals(connectorConfig.getValue(GoogleConnectorConstants.PropertyConfig.REQUIRED_FIELDS),
                            expectedValue);
    }

    @DataProvider(name = "userIdProvider")
    public Object[][] provideUserIdData() {

        Properties properties1 = new Properties();
        properties1.setProperty(GoogleConnectorConstants.PropertyConfig.USER_ID_CLAIM, LAST_NAME_CLAIM);
        Properties properties2 = new Properties();
        properties2.setProperty(GoogleConnectorConstants.ATTRIBUTE_PRIMARYEMAIL, EMAIL_NAME_CLAIM);
        Properties properties3 = new Properties();

        return new Object[][]{
                // CASE_1: USER_ID_CLAIM property is present in connector configs.
                {properties1, false, LAST_NAME_CLAIM},
                // CASE_2: ATTRIBUTE_PRIMARYEMAIL property is present in connector configs.
                {properties2, true, EMAIL_NAME_CLAIM},
                // CASE_3: No external properties in connector configs. Default values are set.
                {properties3, false, STREET_ADDRESS_CLAIM},
        };
    }

    @Test(dataProvider = "userIdProvider")
    public void testGetUserIdClaim(Properties properties, boolean debugEnabled, String expectedValue) throws Exception {
        setLogging(debugEnabled);
        GoogleProvisioningConnectorConfig connectorConfig = new GoogleProvisioningConnectorConfig(properties);

        Assert.assertEquals(connectorConfig.getUserIdClaim(), expectedValue);
    }

    private void setLogging(boolean debugEnabled) {

        mockStatic(LogFactory.class);
        when(LogFactory.getLog(any(Class.class))).thenReturn(log);

        doNothing().when(log).warn(Matchers.any());

        when(log.isDebugEnabled()).thenReturn(debugEnabled);
        doNothing().when(log).debug(any());
    }
}
