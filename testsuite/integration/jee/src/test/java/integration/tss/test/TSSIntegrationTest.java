package integration.tss.test;

import integration.tss.deployment.TSSIntegrationTestDeployments;

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
import com.ericsson.oss.mediation.tss.service.api.enums.NodeSecurityType;

/**
 * Test used to verify tss rar is working
 * 
 * @author edejket
 * 
 */
@RunWith(Arquillian.class)
public class TSSIntegrationTest {

    @Inject
    TargetResolverMock targetresolver;
    /**
     * Logger for this test
     */
    private static final Logger log = LoggerFactory
            .getLogger(TSSIntegrationTest.class);

    /**
     * Since we want different scenarios, we will controll arq deployment
     * manually
     * 
     */
    @ArquillianResource
    private ContainerController controller;

    @ArquillianResource
    private Deployer deployer;

    /**
     * Create tss-service.ear deployment
     * 
     * @return tss-service.ear.
     */
    @Deployment(name = "tss-service.ear", managed = false, testable = false, order = 1)
    public static Archive<?> createTSSServiceEARDeployment() {
        log.debug("******Creating tss-service.ear deployment and deploying it to server******");
        return TSSIntegrationTestDeployments.createTSSServicerchive();
    }

    /**
     * Create mocked-resolver.ear - client
     * 
     * @return mocked-resolver.ear
     */
    @Deployment(name = "mocked-resolver.ear", testable = true, managed = false, order = 2)
    public static Archive<?> createMockedMediationResolverEARDeployment() {
        log.debug("******Creating mocked-resolver.ear deployment and deploying it to server******");
        return TSSIntegrationTestDeployments.createMockMediationResolverEAR();
    }

    /**
     * First test, we test that our tss-service.ear deploys on server
     * 
     * @throws Exception
     */
    @Test
    @InSequence(1)
    @OperateOnDeployment("tss-service.ear")
    public void testDeployTssService() throws Exception {

        this.deployer.deploy("tss-service.ear");
        log.info(" ---------------------------------- DEPLOY tss-service.ear ----------------------------------");

    }

    @Test
    @InSequence(2)
    @OperateOnDeployment("mocked-resolver.ear")
    public void testDeployMockedresolver() throws Exception {

        this.deployer.deploy("mocked-resolver.ear");
        log.info(" ---------------------------------- DEPLOY mocked-resolver.ear ----------------------------------");

    }

    @Test
    @InSequence(3)
    @OperateOnDeployment("mocked-resolver.ear")
    public void testGetNodeAuthInfo() throws Exception {

        Assert.assertEquals("LTE01ERBS120GZIP", targetresolver.getUsername(
                "LTE01ERBS120GZIP", NodeSecurityType.NORMAL));
        Assert.assertEquals("secret", targetresolver.getPassword(
                "LTE01ERBS120GZIP", NodeSecurityType.NORMAL));

    }
}