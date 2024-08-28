package com.ericsson.oss.mediation.tss.util;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.tss.util.configuration.TSSConfiguration;

/**
 * This class initializes the orb and makes it possible for automatic launches.
 * <p>
 * 
 * @author Johnni Sigeti
 */
public class OrbInitializer {
    private static ORB orb = null;
    private static OrbInitializer instance;

    // logger instance
    private static Logger log = LoggerFactory.getLogger(OrbInitializer.class);

    /**
     * 
     */
    private OrbInitializer() {
    }

    public static OrbInitializer getOrbInitializer() {
        if (instance == null) {
            instance = new OrbInitializer();
        }
        return instance;
    }

    /**
     * Returns the orb.
     * 
     * @return The orb.
     */
    public static ORB getOrb() {
        log.debug("getOrb(): <---------");

        if (orb == null) {
            try {
                log.debug("***Init orb from our properties****");
                orb = OrbInitializer.initOrb(new String[0], TSSConfiguration
                        .getInstance().getCorbaProperties());
                log.debug("OrbinitIalizer.getOrb() the value returned when this class gets the ORB is"
                        + orb.toString());
            } catch (Exception t) {
                log.error(
                        "getOrb(): Could not initialize the ORB, stack trace {}",
                        t);
            }
        }

        log.debug("getOrb(): ------->" + orb.toString());
        return orb;
    }

    /**
     * Initializes the Orb for a client.
     * 
     * @param args
     *            Arguments to init the orb with.
     * @param properties
     *            Properties to init the orb with.
     * 
     * @return The orb.
     */
    public static ORB initClient(final String[] args,
            final Properties properties) {
        log.debug("initClient called with args=[{}] and properties=[{}]", args,
                properties);

        orb = ORB.init(args, properties);
        log.debug("OrbInitializer.initClient() returning orb [{}]", orb);
        return orb;
    }

    /**
     * Initializes the Orb for a server.
     * 
     * @param args
     *            Arguments to init the orb with.
     * @param properties
     *            Properties to init the orb with.
     * @return The orb.
     */
    public static ORB initOrb(final String[] args, final Properties properties) {
        log.debug("initOrb called with args=[{}] and properties=[{}]", args,
                properties);
        if (orb == null) {
            orb = ORB.init(args, properties);
        }
        log.debug("ORBInitializer.initOrb() returning orb {}", orb);
        return orb;
    }

}
