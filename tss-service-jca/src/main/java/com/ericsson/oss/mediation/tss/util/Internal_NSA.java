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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.tss.util.configuration.TSSConfiguration;

public class Internal_NSA extends NameServiceAgent {

    private static String nameServiceName = "NameService";

    private static String nameServFormat = "NameService=corba";

    private static Internal_NSA internal_instance = null;

    private static int nameServicePort = INTERNAL; //NOPMD

    private String nameServiceHost;

    private String nameServiceUrl = null; //NOPMD

    /* Create Logger instance. */
    private static Logger log = LoggerFactory.getLogger(Internal_NSA.class);

    public static Internal_NSA create() {
        if (internal_instance == null) {
            internal_instance = new Internal_NSA(INTERNAL);
        }

        log.debug("create(int): ------> Internal Instance : "
                + internal_instance.toString());
        return internal_instance;

    }

    /**
     * Private constructor.
     */
    protected Internal_NSA(final int portnumber) {
        log.debug(
                "Internal_NSA protected constructor called with portnumber=[{}]",
                portnumber);
        nameServicePort = portnumber;

        final String orbInitRef = getNameServiceRef();
        log.debug("orbInitRef is set to:  " + orbInitRef.toString());

        if ((orbInitRef != null) && orbInitRef.startsWith("NameService=iiop")) {
            log.info("Found property ORBInitRef=\"" + orbInitRef + "\"");
            nameServiceUrl = orbInitRef;
            try {
                final StringTokenizer tokens = new StringTokenizer(orbInitRef,
                        ":");
                if (tokens.countTokens() >= 2) {
                    tokens.nextToken(); //Not interesting, NameService=iioploc
                    final StringTokenizer hostTokens = new StringTokenizer(
                            tokens.nextToken(), "//");
                    nameServiceHost = hostTokens.nextToken();

                    // --> 2002-03-22 eandbur & ehsfrjo fixing CIFTR2481
                    if (nameServiceHost.equalsIgnoreCase("localhost")) {
                        nameServiceHost = InetAddress.getLocalHost()
                                .getHostName();
                    }
                    // <-- 2002-03-22 eandbur & ehsfrjo

                    final StringTokenizer tokens2 = new StringTokenizer(
                            tokens.nextToken(), "/");

                    if (tokens2.countTokens() > 0) {
                        tokens2.nextToken(); //Move past the port number token
                        nameServiceName = tokens2.nextToken();
                    }
                }
            } catch (UnknownHostException uhe) {
                log.error("UnknownHostException caught, stack trace {}", uhe);
                throw new IllegalStateException(uhe);
            }
        }

        else {
            log.warn("Could not find valid \"ORBInitRef\" in properties. Checking \"localhost\"");

            try {
                nameServiceHost = InetAddress.getLocalHost().getHostName();
                log.debug("<constructor>(int): nameServiceHost set to: "
                        + nameServiceHost);

                nameServiceUrl = "NameService=corbaloc://"
                        + InetAddress.getLocalHost().getHostName() + ":"
                        + nameServicePort + "/" + nameServiceName;
                log.info("<constructor>(int): Constructed nameServiceUrl for localhost: \""
                        + nameServiceUrl + "\"");

            } catch (UnknownHostException uhe) {
                log.error("UnknownHostException caught, stack trace {}", uhe);
                throw new IllegalStateException(uhe);
            }
        }
    }

    /**
     * This method parses the ORBInitRef string and retrieves the name service
     * reference stored with in it.
     * 
     * ORBInitRef can have multiple services referenced. Each service is
     * seperated by a semi-colon (;)
     * 
     * @return String - ORBInitRef value for the name service
     * @author David Nee
     */
    static public String getNameServiceRef() {

        final String orbInitRef = TSSConfiguration.getInstance()
                .getCorbaProperties().getProperty("ORBInitRef");

        log.debug("ORBInitRef was resolved to [{}]", orbInitRef);
        String nextToken = null;
        /**
         * Parse the OrbInitRef string to find the NameService.
         */
        final StringTokenizer tokens = new StringTokenizer(orbInitRef, ";");
        while (tokens.hasMoreTokens()) {
            nextToken = tokens.nextToken();
            if (nextToken.startsWith(nameServFormat)) {
                break;
            }
        }
        log.debug("getNameServiceRef returning [{}]", nextToken);
        return nextToken;
    }

    /**
     * Retrieves a corba object from the Naming Service on the specified host.
     * 
     * @param host
     *            The host to retrieve the corba objet from.
     * @param path
     *            The path to the corba object.
     * @param name
     *            The name of the corba object.
     * @exception NameServiceException
     *                If the corba object could not be retrieved.
     */
    @Override
    public org.omg.CORBA.Object resolve(final String host,
            final NameComponent[] path, final String name)
            throws NameServiceException {

        log.debug("resolve(String, NameComponent[], String): <------ " + host
                + ", " + path.toString() + ", " + name);

        org.omg.CORBA.Object obj = null;

        final String url = "corbaloc::" + host + ":" + nameServicePort + "/"
                + nameServiceName;

        try {
            /*
             * Connect to the hosts Naming Service.
             */
            final NamingContext remoteNs = getNamingContext(url);

            /*
             * Retrieve server from remote NS
             */
            NameComponent[] fullName = new NameComponent[path.length + 1];
            System.arraycopy(path, 0, fullName, 0, path.length);
            fullName[path.length] = new NameComponent(name, "");

            obj = remoteNs.resolve(fullName);
            log.debug("remoteNs.resolve(fullName) returns " + obj.toString());

        } catch (Exception e) {

            log.error("resolve: Unable to resolve Object, host = " + host
                    + " path = " + NameServiceAgent.toString(path) + " name = "
                    + name);
            throw new NameServiceException("resolve", nameServiceUrl,
                    nameServiceHost, nameServicePort, name, path.toString(),
                    null, null, null, url,
                    "Error when trying to resolve with \"home made\" IOR."
                            + "resolve: Unable to resolve Object, host = "
                            + host + " path = "
                            + NameServiceAgent.toString(path) + " name = "
                            + name, e.toString());
        }

        log.debug("resolve(String, NameComponent[], String): ------> "
                + obj.toString());
        return obj;
    }

    /**
     * Convert from java.lang.String to org.omg.CosNaming.NameComponent[]
     * 
     * @param name
     *            Name as a formatted string; id[.kind][/id[.kind]]
     * @return NameComponent array if ok. If name was an empty string return
     *         NameComponent[0] If name was null return null.
     */
    public static NameComponent[] toNameComponent(final String name) {
        log.debug("toNameComponent(String): <------ " + name);

        NameComponent[] result = new NameComponent[0];

        if (name != null) {

            if (!name.equals("")) {

                final StringTokenizer ncs = new StringTokenizer(name, "/");
                result = new NameComponent[ncs.countTokens()];
                String dummy;
                String id;
                String kind;
                int i = 0;

                while (ncs.hasMoreElements()) {

                    dummy = ncs.nextToken();
                    final StringTokenizer nc = new StringTokenizer(dummy, ".");
                    id = nc.nextToken();
                    kind = "";

                    if (nc.countTokens() >= 1) {
                        kind = nc.nextToken();
                    }

                    result[i++] = new NameComponent(id, kind);
                }
            }
        }

        log.debug("toNameComponent(String): ------> " + result);
        return result;
    }

    /**
     * Method to get the stringified Ior.
     * 
     * @param host
     *            the name of the host
     * @param namingContext
     *            the naming context
     * @return the stringifiedIor or null if one of the parameters are null
     */
    public static String getStringifiedIor(final String host,
            final String namingContext) {
        log.debug("getStringifiedIor(String, String): <------ " + host + ", "
                + namingContext);

        String ior = null;

        if ((host == null) || (namingContext == null)) {
            log.warn("getStringifiedIor: the host or the namingcontext is null");
        } else {
            ior = "corbaname::" + host + ":" + INTERNAL + "/" + nameServiceName
                    + "#" + namingContext;
        }

        log.debug("getStringifiedIor(String, String): ------> "
                + ior.toString());
        return ior;
    }

    /**
     * Gets the NamingContext from the Naming Service with the given ior, local
     * or remote NS. The ior can be any type of stringified ior, prior Corba 2.3
     * or iioploc://....
     * 
     * @param ior
     *            Stringified ior.
     * @return The NamingContext object reference
     */
    private NamingContext getNamingContext(final String ior)
            throws NameServiceException {
        log.debug("getNamingContext(String): <------ " + ior);

        final ORB orb = OrbInitializer.getOrb();

        NamingContext nc = null;

        nc = NamingContextHelper.narrow(orb.string_to_object(ior));

        if (nc == null) {

            log.error("getNamingContext: Naming context not created");
            throw new NameServiceException("getNamingContext", nameServiceUrl,
                    nameServiceHost, nameServicePort, null, null, null,
                    "NULL Context Returned", null, ior,
                    "Naming Service not found with IOR", null);
        } else {
            log.debug("getNamingContext: Naming Context created");
        }

        log.debug("getNamingContext(String): ------> " + nc.toString());
        return nc;
    }

}
