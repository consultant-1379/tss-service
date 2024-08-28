package integration.isolation.rar.test;

import integration.isolation.rar.deployment.TSSTestDeployments;

import javax.annotation.Resource;

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

import com.ericsson.oss.mediation.tss.*;

/**
 * Test used to verify tss rar is working
 * 
 * @author edejket
 * 
 */
@RunWith(Arquillian.class)
public class TSSRarIsolationTest {

    /**
     * Logger for this test
     */
    private static final Logger log = LoggerFactory
            .getLogger(TSSRarIsolationTest.class);

    /**
     * Since we want different scenarios, we will controll arq deployment
     * manually
     * 
     */
    @ArquillianResource
    private ContainerController controller;

    @ArquillianResource
    private Deployer deployer;

    @Resource(lookup = "java:/eis/AccessControlService")
    AccessControl accessControlService;

    /**
     * Create tss-service.ear deployment
     * 
     * @return tss-service.ear.ear
     */

    @Deployment(name = "tss-service.ear", testable = true, managed = false)
    public static Archive<?> createTSSServiceEARDeployment() {
        log.debug("******Creating tss-service.ear deployment and deploying it to server******");
        return TSSTestDeployments.createTSSServicerchive();
    }

    /**
     * First test, we test that our pmservice.ear deploys on server
     * 
     * @throws Exception
     */
    @Test
    @InSequence(1)
    @OperateOnDeployment("tss-service.ear")
    public void testDeployPMService() throws Exception {

        this.deployer.deploy("tss-service.ear");
        log.info(" ---------------------------------- DEPLOY tss-service.ear ----------------------------------");

    }

    @Test
    @InSequence(2)
    @OperateOnDeployment("tss-service.ear")
    public void testGetNodeAuthInfo() throws Exception {
        final AccessControlContext accContext = this.accessControlService
                .getAccessControlContext();
        final NodeAuthInfo nodeAuthInfo = accContext.getPasswordService()
                .getNodeAuthInfo("LTE01ERBS120GZIP", "NORMAL");

        Assert.assertEquals("LTE01ERBS120GZIP", nodeAuthInfo.getUsername());
        Assert.assertEquals("secret", nodeAuthInfo.getPassword());
        accContext.close();

    }

}
