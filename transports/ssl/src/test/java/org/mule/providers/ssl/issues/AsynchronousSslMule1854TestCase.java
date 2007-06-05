package org.mule.providers.ssl.issues;

import org.mule.extras.client.MuleClient;
import org.mule.impl.model.seda.SedaComponent;
import org.mule.tck.FunctionalTestCase;
import org.mule.tck.functional.CounterCallback;
import org.mule.tck.functional.FunctionalTestComponent;
import org.mule.umo.UMOMessage;
import org.mule.umo.model.UMOModel;

import java.util.HashMap;
import java.util.Map;

public class AsynchronousSslMule1854TestCase extends FunctionalTestCase {

    protected static String TEST_MESSAGE = "Test Request";
    private static int NUM_MESSAGES = 100;

    public AsynchronousSslMule1854TestCase()
    {
        setDisposeManagerPerSuite(true);
    }

    protected String getConfigResources()
    {
        return "ssl-functional-test.xml";
    }

    public void testAsynchronous() throws Exception
    {
        MuleClient client = new MuleClient();
        client.dispatch("asyncEndpoint", TEST_MESSAGE, null);
        UMOMessage response = client.receive("asyncEndpoint", 5000);
        assertNotNull("Response is null", response);
        assertEquals(TEST_MESSAGE + " Received Async", response.getPayloadAsString());
    }

}