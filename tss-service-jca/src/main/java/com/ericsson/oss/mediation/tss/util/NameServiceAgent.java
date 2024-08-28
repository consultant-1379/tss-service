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
package com.ericsson.oss.mediation.tss.util;

import org.omg.CosNaming.NameComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NameServiceAgent contains methods to retrieve servers from a remote Naming
 * Service. The Naming Service always run on the same port on all hosts and with
 * the same name "NameService".
 * <p>
 * Note1: Before the NameServiceAgent methods can be used, the orb has to be
 * initialized using the OrbInitializer class.
 * <p>
 * Note2: The NameServiceAgent encapsulates all Vendor specific code needed to
 * use the Naming Service(s).
 * <p>
 * 
 * @author Johnni Sigeti
 * @author edejket - Modified code from original sources, for TOR needs
 */

public class NameServiceAgent {

    /** Port to be used for internal NamingService */
    //The default port 12468 is from the song 2-4-6-8 prefixed with a one - Tomas Johnson
    public final static int INTERNAL = 12468;

    public static final NameComponent[] TSS_SERVICE_REF = new NameComponent[] {
            new NameComponent("com", ""), new NameComponent("ericsson", ""),
            new NameComponent("nms", ""), new NameComponent("tss", "") };

    public static final String TSS_PASSWORD_SERVICE = "Password";

    /**
     * Create logger
     */
    private static Logger log = LoggerFactory.getLogger(NameServiceAgent.class);

    /**
     * Protected constructor
     */
    protected NameServiceAgent() {
        log.debug("NameServiceAgent constructor called.");
    }

    /**
     * Creates a Singleton instance of NameServiceAgent for Internal use. If an
     * instance was created previously, that instance is returned (Singleton).
     * 
     * @return A NameServiceAgent instance.
     */
    public static NameServiceAgent getInstance() {
        final NameServiceAgent nsaInst = Internal_NSA.create();
        return nsaInst;
    }

    /**
     * This method is not supported in this Base-class,
     * 
     * @see Internal_NameServiceAgent.resolve(String,NameComponent[],String).
     * @throws NotSupportedException
     *             if called.
     */
    public org.omg.CORBA.Object resolve(final String host,
            final NameComponent[] path, final String name)
            throws NameServiceException {
        log.error("resolve(String, NameComponent[], String): Method not supported");
        throw new NotSupportedException("Resolve is not supported on BOTH!");
    }

    /**
     * Converts a sequence of NameComponents into its String representation eg:
     * "baseContext/subContext1.ctxt/subContext2/objectName.obj"
     * 
     * @param name
     *            NameComponent sequence
     * @return String representation of name
     */
    public static String toString(final org.omg.CosNaming.NameComponent[] name) {
        log.debug("toString(NameComponent) called with name=[{}]", name);

        String result = null;
        if (name != null) {
            result = "";
            for (int i = 0; i < name.length; i++) {
                result += name[i].id;
                result += (name[i].kind.equals("") ? "" : ("." + name[i].kind));
                if (i < name.length - 1) {
                    result += "/";
                }
            }
        }
        log.debug("toString(NameComponent) returning=[{}]", result);
        return result;
    }

    /**
     * Converts a String representation of an object-path to a NameComponent
     * sequence.
     * 
     * Example of a valid object-path in String form:
     * "baseContext/subContext1.ctxt/subContext2/objectName.obj"
     * 
     * @param name
     *            a valid String representation of an object-path
     * @return NameComponent sequence corresponding to the supplied path
     */
    public static NameComponent[] toNameComponent(final String name) {

        log.debug("toNameComponent called with name=[{}]", name);
        // erobrus 20050509 - I changed the way the function is called because it is static.
        final NameComponent[] ncs = Internal_NSA.toNameComponent(name);
        return ncs;
    }

}
