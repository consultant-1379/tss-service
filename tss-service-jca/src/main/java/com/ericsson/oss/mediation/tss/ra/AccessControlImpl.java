package com.ericsson.oss.mediation.tss.ra;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.tss.AccessControl;
import com.ericsson.oss.mediation.tss.AccessControlContext;

/**
 * NodeSecurityServiceImpl implementation for NodeSecurityService In JCA terms
 * this would be ConnectionFactoryImpl.
 * 
 * @author edejket
 * 
 */
public class AccessControlImpl implements AccessControl {
    /** The serial version UID */
    private static final long serialVersionUID = 1L;

    /** The logger */
    private static Logger log = LoggerFactory
            .getLogger(AccessControlImpl.class);

    /** Reference */
    private Reference reference;

    /** ManagedConnectionFactory */
    private final AccessControlManagedConnectionFactory mcf;

    /** ConnectionManager */
    private final ConnectionManager connectionManager;

    /**
     * Default constructor
     * 
     * @param mcf
     *            ManagedConnectionFactory
     * @param cxManager
     *            ConnectionManager
     */
    public AccessControlImpl(final AccessControlManagedConnectionFactory mcf,
            final ConnectionManager cxManager) {
        this.mcf = mcf;
        this.connectionManager = cxManager;
    }

    /**
     * Get connection from factory
     * 
     * @return TSSConnection instance
     * @exception ResourceException
     *                Thrown if a connection can't be obtained
     */
    @Override
    public AccessControlContext getAccessControlContext()
            throws ResourceException {
        log.debug("getNodeAuthInfo called");
        return (AccessControlContext) connectionManager.allocateConnection(mcf,
                null);
    }

    /**
     * Get the Reference instance.
     * 
     * @return Reference instance
     * @exception NamingException
     *                Thrown if a reference can't be obtained
     */
    @Override
    public Reference getReference() throws NamingException {
        log.debug("getReference()");
        return reference;
    }

    /**
     * Set the Reference instance.
     * 
     * @param reference
     *            A Reference instance
     */
    @Override
    public void setReference(final Reference reference) {
        log.debug("setReference()");
        this.reference = reference;
    }

}
