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
package integration.isolation.rar.deployment;

import integration.isolation.rar.dependencies.Artifact;
import integration.isolation.rar.test.TSSRarIsolationTest;

import java.io.File;

import org.jboss.arquillian.container.test.api.Testable;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TSSTestDeployments {

    private static final Logger logger = LoggerFactory
            .getLogger(TSSTestDeployments.class);

    /**
     * Archive names
     */
    public static final String TSS_SERVICE_EAR = "tss-service.ear";
    public static final String TSS_SERVICE_TEST_WAR = "tss-service-test";

    /**
     * Create test archive
     * 
     * @return test archive with our arquillian test
     */
    public static final WebArchive createTestArchive() {

        /**
         * Create deployment for the test
         */
        logger.debug("******Creating war archive containing tests******");
        final WebArchive tssTestWar = ShrinkWrap.create(WebArchive.class,
                TSS_SERVICE_TEST_WAR + ".war");
        /**
         * add the test class in the deployment
         */
        tssTestWar.addClass(TSSRarIsolationTest.class);
        logger.debug("******Test archive created******");
        return tssTestWar;
    }

    /**
     * Create AccessControl archive, used in this test
     * 
     * @return tss-service.ear
     * 
     */
    public static final Archive<?> createTSSServicerchive() {
        logger.debug("******Creating tss-service.ear archive******");

        final File archiveFile = Artifact
                .resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_MEDIATION___TSS_EAR);
        if (archiveFile == null) {
            throw new IllegalStateException("Unable to resolve artifact "
                    + Artifact.COM_ERICSSON_OSS_MEDIATION___TSS_EAR);
        }
        /**
         * Create arq ear archive from file we got from maven repository
         */
        final EnterpriseArchive tssEAR = ShrinkWrap.createFromZipFile(
                EnterpriseArchive.class, archiveFile);
        /**
         * add test war inside tss-service.ear since that is what we are testing
         */
        tssEAR.addAsModule(Testable.archiveToTest(createTestArchive()));
        Artifact.createCustomApplicationXmlFile(tssEAR, TSS_SERVICE_TEST_WAR);

        /**
         * This is how we rename nexus artifact that has version in its name, if
         * we dont want version as part of deployment name, for example if we
         * need to do specific jndi lookup that is bound to deployment name, we
         * would like to always know the name upfront
         */
        /*
         * EnterpriseArchive deployment = ShrinkWrap.create(
         * EnterpriseArchive.class, TSS_SERVICE_EAR); deployment.merge(tssEAR);
         */

        return tssEAR;

    }

}
