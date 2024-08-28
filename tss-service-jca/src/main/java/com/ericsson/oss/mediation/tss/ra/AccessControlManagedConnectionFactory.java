/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.ericsson.oss.mediation.tss.ra;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;

/**
 * TSSManagedConnectionFactory
 * 
 * @version $Revision: $
 */
public class AccessControlManagedConnectionFactory implements
        ManagedConnectionFactory, ResourceAdapterAssociation {

    /** The serial version UID */
    private static final long serialVersionUID = 1L;

    /** The logger */
    private static Logger log = Logger.getLogger("TSSManagedConnectionFactory");

    /** The resource adapter */
    private ResourceAdapter ra;

    /** The logwriter */
    private PrintWriter logwriter;

    /**
     * Creates a Connection Factory instance.
     * 
     * @param cxManager
     *            ConnectionManager to be associated with created EIS connection
     *            factory instance
     * @return EIS-specific Connection Factory instance or
     *         javax.resource.cci.ConnectionFactory instance
     * @throws ResourceException
     *             Generic exception
     */
    @Override
    public Object createConnectionFactory(final ConnectionManager cxManager)
            throws ResourceException {
        log.finest("createConnectionFactory()");
        return new AccessControlImpl(this, cxManager);
    }

    /**
     * Creates a Connection Factory instance.
     * 
     * @return EIS-specific Connection Factory instance or
     *         javax.resource.cci.ConnectionFactory instance
     * @throws ResourceException
     *             Generic exception
     */
    @Override
    public Object createConnectionFactory() throws ResourceException {
        throw new ResourceException(
                "This resource adapter doesn't support non-managed environments");
    }

    /**
     * Creates a new physical connection to the underlying EIS resource manager.
     * 
     * @param subject
     *            Caller's security information
     * @param cxRequestInfo
     *            Additional resource adapter specific connection request
     *            information
     * @throws ResourceException
     *             generic exception
     * @return ManagedConnection instance
     */
    @Override
    public ManagedConnection createManagedConnection(final Subject subject,
            final ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        log.finest("createManagedConnection()");
        return new AccessControlManagedConnection(this);
    }

    /**
     * Returns a matched connection from the candidate set of connections.
     * 
     * @param connectionSet
     *            Candidate connection set
     * @param subject
     *            Caller's security information
     * @param cxRequestInfo
     *            Additional resource adapter specific connection request
     *            information
     * @throws ResourceException
     *             generic exception
     * @return ManagedConnection if resource adapter finds an acceptable match
     *         otherwise null
     */
    @Override
    public ManagedConnection matchManagedConnections(final Set connectionSet,
            final Subject subject, final ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {
        log.finest("matchManagedConnections()");
        ManagedConnection result = null;
        final Iterator it = connectionSet.iterator();
        while (result == null && it.hasNext()) {
            final ManagedConnection mc = (ManagedConnection) it.next();
            if (mc instanceof AccessControlManagedConnection) {
                final AccessControlManagedConnection acm = (AccessControlManagedConnection) mc;
                if (acm.getConnection() == null) {
                    result = mc;
                }
            }

        }
        return result;
    }

    /**
     * Get the log writer for this ManagedConnectionFactory instance.
     * 
     * @return PrintWriter
     * @throws ResourceException
     *             generic exception
     */
    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        log.finest("getLogWriter()");
        return logwriter;
    }

    /**
     * Set the log writer for this ManagedConnectionFactory instance.
     * 
     * @param out
     *            PrintWriter - an out stream for error logging and tracing
     * @throws ResourceException
     *             generic exception
     */
    @Override
    public void setLogWriter(final PrintWriter out) throws ResourceException {
        log.finest("setLogWriter()");
        logwriter = out;
    }

    /**
     * Get the resource adapter
     * 
     * @return The handle
     */
    @Override
    public ResourceAdapter getResourceAdapter() {
        log.finest("getResourceAdapter()");
        return ra;
    }

    /**
     * Set the resource adapter
     * 
     * @param ra
     *            The handle
     */
    @Override
    public void setResourceAdapter(final ResourceAdapter ra) {
        log.finest("setResourceAdapter()");
        this.ra = ra;
    }

    /**
     * Returns a hash code value for the object.
     * 
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int result = 17;
        return result;
    }

    /**
     * Indicates whether some other object is equal to this one.
     * 
     * @param other
     *            The reference object with which to compare.
     * @return true if this object is the same as the obj argument, false
     *         otherwise.
     */
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof AccessControlManagedConnectionFactory)) {
            return false;
        }

        final boolean result = true;
        return result;
    }

}
