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
package com.ericsson.oss.mediation.tss.util.configuration;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.tss.util.NameServiceAgent;

/**
 * Utility singleton used to create CORBA properties needed to initialize
 * ORB.</br> It will set:</br> 1. org.omg.CORBA.ORBClass property by default to
 * <code>org.jacorb.orb.ORB</code></br> 2. org.omg.CORBA.ORBSingletonClass
 * property by default to <code>org.jacorb.orb.ORBSingleton</code></br> 3.
 * ORBInitRef property by default to
 * <code>iioploc://${ns.host}:${ns.port}/NameService</code></br></br>
 * 
 * Variables ns.host and ns.port will be resolved using following
 * algorithm:</br> 1.Look for environment variable named OSS_IP or for port
 * NAMESERVICE_PORT </br>2.Look for system variable named OSS_IP or for port
 * NAMESERVICE_PORTM</br> 3.Set default values masterservice/12468
 * 
 * 
 * 
 * 
 * 
 * @author edejket
 * 
 */
public class TSSConfiguration {

    private static final Logger log = LoggerFactory
            .getLogger(TSSConfiguration.class);

    private static TSSConfiguration tssConfiguration = null;
    public static final String OSSRC_IPADDRESS = "OSS_IP";
    public static final String NAMESERVICE_PORT = "NAMESERVICE_PORT";
    private static final String ORBInitRefTemplate = "iioploc://${ns.host}:${ns.port}/NameService";

    private static final String nsIPdef = "masterservice";
    private static final String nsPortDEF = NameServiceAgent.INTERNAL + "";

    private final Properties corbaProps;

    private TSSConfiguration() {
        corbaProps = new Properties();

        corbaProps.put("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
        corbaProps.put("org.omg.CORBA.ORBSingletonClass",
                "org.jacorb.orb.ORBSingleton");
        corbaProps.put(
                "ORBInitRef",
                ORBInitRefTemplate.replace("${ns.host}",
                        resolveNameServiceHost()).replace("${ns.port}",
                        resolveNameServicePort()));
        log.debug("Resolved properties for orb=[{}]", corbaProps);

    }

    /**
     * Creates instance for TSSConfiguration
     * 
     * @return TSSConfiguration instance
     */
    public static TSSConfiguration getInstance() {
        if (tssConfiguration == null) {
            tssConfiguration = new TSSConfiguration();
            return tssConfiguration;
        } else {
            return tssConfiguration;
        }
    }

    public static String resolveNameServiceHost() {
        final String nsIPENV = System.getenv(OSSRC_IPADDRESS);
        final String nsIPSYS = System.getProperty(OSSRC_IPADDRESS);
        if (nsIPENV != null) {
            return nsIPENV;
        } else if (nsIPSYS != null) {
            return nsIPSYS;
        } else {
            return nsIPdef;
        }
    }

    public static String resolveNameServicePort() {
        final String nsPortENV = System.getenv(NAMESERVICE_PORT);
        final String nsPortSYS = System.getProperty(NAMESERVICE_PORT);
        if (nsPortENV != null) {
            return nsPortENV;
        } else if (nsPortSYS != null) {
            return nsPortSYS;
        } else {
            return nsPortDEF;
        }

    }

    /**
     * Return corba init properties
     * 
     * @return Properties used for ORB init
     */
    public Properties getCorbaProperties() {
        return getInstance().corbaProps;
    }

}
