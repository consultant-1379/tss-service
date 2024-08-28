/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package integration.tss.test;

import integration.tss.deployment.TSSIntegrationTestDeployments;

import java.io.*;
import java.net.*;
import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.*;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.integration.tss.mock.TargetResolverMock;

@RunWith(Arquillian.class)
public class TSSUpgradeTest {

    private static final Logger log = LoggerFactory
            .getLogger(TSSIntegrationTest.class);

    /**
     * Since we want different scenarios, we will controll arq deployment manually
     * 
     */
    @ArquillianResource
    private ContainerController controller;

    @ArquillianResource
    private Deployer deployer;

    @Inject
    TargetResolverMock targetresolver;

    /**
     * Create tss-service.ear deployment
     * 
     * @return tss-service.ear.
     */
    @Deployment(name = "tss-service.ear", managed = false, testable = false,
            order = 1)
    public static Archive<?> createTSSServiceEARDeployment() {
        log.debug("******Creating tss-service.ear deployment and deploying it to server******");
        return TSSIntegrationTestDeployments.createTSSServicerchive();
    }

    @Deployment(name = "pib.ear", managed = false, testable = false, order = 1)
    public static Archive<?> createPIBDeployment() {
        log.debug("******Creating tss-service.ear deployment and deploying it to server******");
        return TSSIntegrationTestDeployments.createPIBArchive();
    }

    @Deployment(name = "mocked-resolver.ear", testable = true, managed = false,
            order = 2)
    public static Archive<?> createMockedMediationResolverEARDeployment() {
        log.debug("******Creating mocked-resolver.ear deployment and deploying it to server******");
        return TSSIntegrationTestDeployments.createMockMediationResolverEAR();
    }

    @Test
    @InSequence(1)
    @OperateOnDeployment("tss-service.ear")
    public void testDeployTssService() throws Exception {

        this.deployer.deploy("tss-service.ear");
        log.info(" ---------------------------------- DEPLOY tss-service.ear ----------------------------------");

    }

    @Test
    @InSequence(2)
    @OperateOnDeployment("pib.ear")
    public void testDeployPIB() throws Exception {

        this.deployer.deploy("pib.ear");
        log.info(" ---------------------------------- DEPLOY PIB.ear ----------------------------------");

    }

    @Test
    @InSequence(3)
    @OperateOnDeployment("mocked-resolver.ear")
    public void testDeployMockedresolver() throws Exception {

        this.deployer.deploy("mocked-resolver.ear");
        log.info(" ---------------------------------- DEPLOY mocked-resolver.ear ----------------------------------");

    }

    @Test
    @InSequence(4)
    @OperateOnDeployment("mocked-resolver.ear")
    public void testRestCall() throws Exception {
        try {
            String returnVal = "";

            URL url = new URL(generateRestURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                returnVal = "Failed : HTTP error code : "
                        + conn.getResponseCode();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                returnVal += output;
            }
            log.debug("Upgrade id: {}", returnVal);
            conn.disconnect();
            Thread.sleep(2000);
            List<String> response = targetresolver.getUpgradeStatus();
            Assert.assertEquals("OK", response.get(0));
            Assert.assertEquals(returnVal, response.get(1));

        } catch (MalformedURLException e) {
            log.debug("Error in test rest call: {}", e);
        } catch (IOException e) {
            log.debug("Error in test rest call: {}", e);
        }
    }

    /**
     * @return
     */
    private String generateRestURL() {
        String instanceId = getSystemPropertyById("com.ericsson.oss.sdk.node.identifier");
        String portOffset = getSystemPropertyById("jboss.socket.binding.port-offset");
        Integer port = new Integer(portOffset) + 8080;
        StringBuilder sbuil = new StringBuilder();
        sbuil.append("http://localhost:");
        sbuil.append(port.toString());
        sbuil.append("/pib/upgradeService/startUpgrade?user=guest&password=guestp&app_server_identifier=");
        sbuil.append(instanceId);
        sbuil.append("&service_identifier=tss-service-ear&upgrade_operation_type=SERVICE&upgrade_phase=SERVICE_INSTANCE_UPGRADE_PREPARE");
        
        log.debug("URL: {}", sbuil.toString());
        return sbuil.toString();
    }

    /**
     * @param string
     * @return
     */
    private String getSystemPropertyById(String string) {
        return System.getProperty(string);
    }
}
