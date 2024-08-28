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
package integration.tss.deployment;

import integration.tss.dependencies.Artifact;
import integration.tss.test.TSSIntegrationTest;
import integration.tss.test.TSSUpgradeTest;

import java.io.File;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.integration.tss.mock.TargetResolverMock;

public final class TSSIntegrationTestDeployments {

    private static final Logger logger = LoggerFactory
            .getLogger(TSSIntegrationTestDeployments.class);

    /**
     * Archive names
     */
    public static final String TSS_SERVICE_EAR = "tss-service.ear";
    public static final String TSS_SERVICE_TEST_WAR = "tss-service-test.war";
    public static final String TSS_SERVICE_TEST_JAR = "tss-service-test.jar";

    /**
     * Create test archive
     * 
     * @return test archive with our arquillian test
     */
    public static final Archive<?> createTestArchive() {

        /**
         * Create deployment for the test
         */
        logger.debug("******Creating war archive containing tests******");
        final WebArchive tssTestWar = ShrinkWrap.create(WebArchive.class,
                TSS_SERVICE_TEST_WAR);
        /**
         * add the test class in the deployment
         */
        tssTestWar.addClass(TSSIntegrationTest.class);
        tssTestWar.addClass(TSSUpgradeTest.class);
        tssTestWar.addAsWebInfResource("META-INF/beans.xml", "beans.xml");

        /**
         * add the mock client
         */
        tssTestWar.addAsLibrary(createMockResolverJar());

        logger.debug("******Test archive created******");
        return tssTestWar;
    }

    /**
     * Create MediationService archive, used in this test
     * 
     * @return MediationService.ear
     * 
     */
    public static final Archive<?> createTSSServicerchive() {
        logger.debug("******Creating tss-service.ear archive******");

        final File archiveFile = Artifact
                .resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_NMS_MEDIATION___TSS_EAR);
        if (archiveFile == null) {
            throw new IllegalStateException("Unable to resolve artifact "
                    + Artifact.COM_ERICSSON_NMS_MEDIATION___TSS_EAR);
        }
        /**
         * Create arq ear archive from file we got from maven repository
         */
        final EnterpriseArchive tssEAR = ShrinkWrap.createFromZipFile(
                EnterpriseArchive.class, archiveFile);

        logger.debug(
                "******Created tss-service.ear for deployment, created from maven artifact with coordinates {} ******",
                Artifact.COM_ERICSSON_NMS_MEDIATION___TSS_EAR);
        return tssEAR;

    }

    /**
     * Create mocked mediation service to query tss service for authentication
     * info
     * 
     */

    public static EnterpriseArchive createMockMediationResolverEAR() {

        final EnterpriseArchive mockEar = ShrinkWrap.create(
                EnterpriseArchive.class, "mocked-resolver.ear");
        mockEar.addAsManifestResource(EmptyAsset.INSTANCE, "MANIFEST.MF");
        mockEar.addAsLibraries(Artifact
                .resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_NMS_MEDIATION___TSS_API_JAR));
        mockEar.addAsLibraries(Artifact
                .resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_NMS_MEDIATION___TSS_COMMON_JAR));

        /**
         * Add service framework dependencies inside test ear.
         */

        /**
         * 
         */

        mockEar.addAsLibraries(Artifact.getMavenResolver()
                .artifact(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CLUSTER_CORE_JAR)
                .resolveAsFiles());
        mockEar.addAsLibraries(Artifact.getMavenResolver()
                .artifact(Artifact.COM_ERICSSON_OSS_ITPF_SDK_SERVICES_CORE_JAR)
                .resolveAsFiles());
        mockEar.addAsLibraries(Artifact.getMavenResolver()
                .artifact(Artifact.COM_ERICSSON_OSS_ITPF_SDK_CONFIG_CACHE_JAR)
                .resolveAsFiles());
        mockEar.addAsLibraries(Artifact.getMavenResolver()
                .artifact(Artifact.COM_ERICSSON_OSS_ITPF_SDK__CORE_API_JAR)
                .resolveAsFiles());
        mockEar.addAsLibraries(Artifact.getMavenResolver()
                .artifact(Artifact.COM_ERICSSON_OSS_IPTF_SDK_UPGRADE_CORE)
                .resolveAsFiles());
        mockEar.addAsLibraries(Artifact
                .getMavenResolver()
                .artifact(
                        Artifact.COM_ERICSSON_OSS_ITPF_SDK___CACHE_INFINISPAN_JAR)
                .exclusion(Artifact.ORG_JBOSS___INFINISPAN_CORE_JAR)
                .resolveAsFiles());

        /**
         * Add test.war inside ear, where test.war has mock ejb and actual test
         */
        mockEar.addAsModule(createTestArchive());
        mockEar.addAsApplicationResource("META-INF/jboss-ejb-client.xml",
                "jboss-ejb-client.xml");
        return mockEar;

    }

    /**
     * Creates a Mocked Resolver Jar to hold TargetResolverMock
     * 
     * @return
     */
    public static JavaArchive createMockResolverJar() {

        logger.debug("******Creating Jar archive containing tests******");
        final JavaArchive mockJar = ShrinkWrap.create(JavaArchive.class,
                TSS_SERVICE_TEST_JAR);
        mockJar.addManifest();

        /**
         * add the mock client
         */
        mockJar.addClass(TargetResolverMock.class);
        mockJar.addAsResource("META-INF/beans.xml", "META-INF/beans.xml");
        logger.debug("******Test archive JAR created******");
        return mockJar;
    }

    public static final Archive<?> createPIBArchive() {
        logger.debug("******Creating tss-service.ear archive******");

        final File archiveFile = Artifact
                .resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_OSS_ITPF_PIB);
        if (archiveFile == null) {
            throw new IllegalStateException("Unable to resolve artifact "
                    + Artifact.COM_ERICSSON_NMS_MEDIATION___TSS_EAR);
        }
        /**
         * Create arq ear archive from file we got from maven repository
         */
        final EnterpriseArchive PibEAR = ShrinkWrap.createFromZipFile(
                EnterpriseArchive.class, archiveFile);

        logger.debug(
                "******Created tss-service.ear for deployment, created from maven artifact with coordinates {} ******",
                Artifact.COM_ERICSSON_NMS_MEDIATION___TSS_EAR);
        return PibEAR;

    }

}