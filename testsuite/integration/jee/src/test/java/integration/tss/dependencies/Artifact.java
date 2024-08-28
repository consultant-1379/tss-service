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

package integration.tss.dependencies;

import java.io.*;
import java.util.*;

import org.jboss.arquillian.protocol.servlet.arq514hack.descriptors.api.application.ApplicationDescriptor;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * <b>Dependencies and artifacts used in TSSRarIsolationTest</b> </br>This class
 * contains all dependencies used in this test listed as constants for easier
 * use. Dependencies should be added in sections, for service framework, this
 * project artifacts etc...</br> When adding dependencies here, make sure same
 * are added into pom file as well. </br><b>It is forbidden to put versions of
 * dependencies here all versions need to be defined in pom files, this list
 * here should never contain dependency with version.<b>
 * 
 * </br></br>In order to be able to maintain this file easily, please add only
 * required dependencies, and make sure they are used. If any dependency is not
 * used any more, make sure it is removed from this file and pom file.
 * 
 * @author edejket
 * 
 */
public class Artifact {
    public static final String COM_ERICSSON_NMS_MEDIATION___TSS_EAR = "com.ericsson.nms.mediation:tss-service-ear:ear";
    public static final String COM_ERICSSON_NMS_MEDIATION___TSS_API_JAR = "com.ericsson.nms.mediation:tss-service-api:jar";
    public static final String COM_ERICSSON_NMS_MEDIATION___TSS_COMMON_JAR = "com.ericsson.nms.mediation:tss-service-common:jar";

    /* Service Framework Artifacts */
    public static final String COM_ERICSSON_OSS_ITPF_SDK_CLUSTER_CORE_JAR = "com.ericsson.oss.itpf.sdk:sdk-cluster-core:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK_SERVICES_CORE_JAR = "com.ericsson.oss.itpf.sdk:sdk-services-core:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK_CONFIG_CACHE_JAR = "com.ericsson.oss.itpf.sdk:sdk-config-cache:jar";
    public static final String COM_ERICSSON_OSS_ITPF_SDK__CORE_API_JAR = "com.ericsson.oss.itpf.sdk:sdk-core-api:jar";
    public static final String COM_ERICSSON_OSS_IPTF_SDK_UPGRADE_CORE = "com.ericsson.oss.itpf.sdk:sdk-upgrade-core:jar";

    public static final String COM_ERICSSON_OSS_ITPF_PIB = "com.ericsson.oss.itpf.common:PlatformIntegrationBridge-ear:ear";

    public static final String COM_ERICSSON_OSS_ITPF_SDK___CACHE_INFINISPAN_JAR = "com.ericsson.oss.itpf.sdk:sdk-cache-infinispan:jar";
    public static final String ORG_JBOSS___INFINISPAN_CORE_JAR = "org.infinispan:infinispan-core:jar";

    /**
     * Resolve artifact with given coordinates without any dependencies, this
     * method should be used to resolve just the artifact with given name, and
     * it can be used for adding artifacts as modules into EAR
     * 
     * If artifact can not be resolved, or the artifact was resolved into more
     * then one file then the IllegalStateException will be thrown
     * 
     * 
     * @param artifactCoordinates
     *            in usual maven format
     * 
     *            <pre>
     * {@code<groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}
     * </pre>
     * @return File representing resolved artifact
     */
    public static File resolveArtifactWithoutDependencies(
            final String artifactCoordinates) {
        final File[] artifacts = getMavenResolver()
                .artifact(artifactCoordinates).exclusion("*").resolveAsFiles();
        if (artifacts == null) {
            throw new IllegalStateException("Artifact with coordinates "
                    + artifactCoordinates + " was not resolved");
        }
        if (artifacts.length != 1) {
            throw new IllegalStateException(
                    "Resolved more then one artifact with coordinates "
                            + artifactCoordinates);
        }
        return artifacts[0];
    }

    /**
     * Resolve dependencies for artifact with given coordinates, if artifact can
     * not be resolved IllegalState exception will be thrown
     * 
     * @param artifactCoordinates
     *            in usual maven format
     * 
     *            <pre>
     * {@code<groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}
     * </pre>
     * 
     * @return resolved dependencies
     */
    public static File[] resolveArtifactDependencies(
            final String artifactCoordinates) {

        File[] artifacts = getMavenResolver().artifact(artifactCoordinates)
                .scope("compile").exclusion(artifactCoordinates)
                .resolveAsFiles();

        if (artifacts == null) {
            throw new IllegalStateException(
                    "No dependencies resolved for artifact with coordinates "
                            + artifactCoordinates);
        }
        if (artifacts.length > 0) {
            // find artifact that has given coordinates
            final File artifact = resolveArtifactWithoutDependencies(artifactCoordinates);
            final List<File> filteredDeps = new ArrayList<File>(
                    Arrays.asList(artifacts));
            // remove it from the list
            filteredDeps.remove(artifact);
            artifacts = new File[0];
            // return the resolved list with only dependencies
            return filteredDeps.toArray(artifacts);
        } else {
            return artifacts;
        }

    }

    /**
     * Maven resolver that will try to resolve dependencies using pom.xml of the
     * project where this Artifact class is located.
     * 
     * @return MavenDependencyResolver
     */
    public static MavenDependencyResolver getMavenResolver() {
        return DependencyResolvers.use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml");

    }

    /**
     * Utility method to add war to existing ear Context and war name are the
     * same.
     * 
     * @param serviceEar
     *            Name of the ear
     * @param webModuleName
     *            Name of the war and context (without .war)
     */
    public static void createCustomApplicationXmlFile(
            final EnterpriseArchive serviceEar, final String webModuleName) {

        final Node node = serviceEar.get("META-INF/application.xml");
        ApplicationDescriptor desc = Descriptors.importAs(
                ApplicationDescriptor.class).fromStream(
                node.getAsset().openStream());

        desc.webModule(webModuleName + ".war", webModuleName);
        final String descriptorAsString = desc.exportAsString();

        serviceEar.delete(node.getPath());
        desc = Descriptors.importAs(ApplicationDescriptor.class).fromString(
                descriptorAsString);

        final Asset asset = new Asset() {
            @Override
            public InputStream openStream() {
                final ByteArrayInputStream bi = new ByteArrayInputStream(
                        descriptorAsString.getBytes());
                return bi;
            }
        };
        serviceEar.addAsManifestResource(asset, "application.xml");
    }
}
