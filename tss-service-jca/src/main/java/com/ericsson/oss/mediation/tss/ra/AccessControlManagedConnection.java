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
import java.util.*;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TSS.Password;
import TSS.PasswordHelper;

import com.ericsson.oss.mediation.tss.AccessControlContext;
import com.ericsson.oss.mediation.tss.PasswordService;
import com.ericsson.oss.mediation.tss.util.NameServiceAgent;
import com.ericsson.oss.mediation.tss.util.NameServiceException;
import com.ericsson.oss.mediation.tss.util.configuration.TSSConfiguration;
import com.ericsson.oss.mediation.tss.wrapper.PasswordServiceImpl;

/**
 * TSSManagedConnection
 * 
 * @version $Revision: $
 */
public class AccessControlManagedConnection implements ManagedConnection {

    /** The logger */
    private static Logger log = LoggerFactory
            .getLogger(AccessControlManagedConnection.class);

    /** The logwriter */
    private PrintWriter logwriter;

    /** ManagedConnectionFactory */
    private final AccessControlManagedConnectionFactory mcf;

    /** Listeners */
    private final List<ConnectionEventListener> listeners;

    /** Connection */
    private AccessControlContext connection = null;

    /**
     * Default constructor
     * 
     * @param mcf
     *            mcf
     */
    public AccessControlManagedConnection(
            final AccessControlManagedConnectionFactory mcf) {
        this.mcf = mcf;
        try {
            this.logwriter = this.mcf.getLogWriter();
        } catch (final ResourceException re) {
            log.error(
                    "Unable to set logwriter on managed connection, stack trace {}",
                    re);
            this.logwriter = null;
        }
        this.listeners = Collections
                .synchronizedList(new ArrayList<ConnectionEventListener>(1));
        this.connection = null;
    }

    /**
     * Creates a new connection handle for the underlying physical connection
     * represented by the ManagedConnection instance.
     * 
     * @param subject
     *            Security context as JAAS subject
     * @param cxRequestInfo
     *            ConnectionRequestInfo instance
     * @return generic Object instance representing the connection handle.
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public Object getConnection(final Subject subject,
            final ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        log.debug(
                "getConnection called with Subject=[{}], and cxRequestInfo=[{}]",
                new Object[] { subject, cxRequestInfo });
        connection = new AccessControlContextImpl(this);
        return connection;
    }

    /**
     * Used by the container to change the association of an application-level
     * connection handle with a ManagedConneciton instance.
     * 
     * @param connection
     *            Application-level connection handle
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void associateConnection(final Object connection)
            throws ResourceException {
        log.debug("associateConnection()");

        if (connection == null) {
            throw new ResourceException("Null connection handle");
        }

        if (!(connection instanceof AccessControlContext)) {
            throw new ResourceException(
                    "Wrong connection handle class type, expected class type is com.ericsson.oss.mediation.tss.AccessControlContext");
        }

        this.connection = (AccessControlContext) connection;
    }

    /**
     * Application server calls this method to force any cleanup on the
     * ManagedConnection instance.
     * 
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void cleanup() throws ResourceException {
        log.debug("cleanup()");
        this.connection = null;
    }

    /**
     * Destroys the physical connection to the underlying resource manager.
     * 
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void destroy() throws ResourceException {
        log.debug("destroy()");
        this.connection = null;
    }

    /**
     * Adds a connection event listener to the ManagedConnection instance.
     * 
     * @param listener
     *            A new ConnectionEventListener to be registered
     */
    @Override
    public void addConnectionEventListener(
            final ConnectionEventListener listener) {
        log.debug("addConnectionEventListener()");
        if (listener == null) {
            throw new IllegalArgumentException("Listener is null");
        }
        listeners.add(listener);
    }

    /**
     * Removes an already registered connection event listener from the
     * ManagedConnection instance.
     * 
     * @param listener
     *            already registered connection event listener to be removed
     */
    @Override
    public void removeConnectionEventListener(
            final ConnectionEventListener listener) {
        log.debug("removeConnectionEventListener()");
        if (listener == null) {
            throw new IllegalArgumentException("Listener is null");
        }
        listeners.remove(listener);
    }

    /**
     * Close handle
     * 
     * @param handle
     *            The handle
     */
    void closeHandle(final AccessControlContext handle) {
        final ConnectionEvent event = new ConnectionEvent(this,
                ConnectionEvent.CONNECTION_CLOSED);
        event.setConnectionHandle(handle);
        for (ConnectionEventListener cel : listeners) {
            cel.connectionClosed(event);
        }

    }

    /**
     * Gets the log writer for this ManagedConnection instance.
     * 
     * @return Character ourput stream associated with this Managed-Connection
     *         instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        log.debug("getLogWriter()");
        return logwriter;
    }

    /**
     * Sets the log writer for this ManagedConnection instance.
     * 
     * @param out
     *            Character Output stream to be associated
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void setLogWriter(final PrintWriter out) throws ResourceException {
        log.debug("setLogWriter()");
        logwriter = out;
    }

    /**
     * Returns an <code>javax.resource.spi.LocalTransaction</code> instance.
     * 
     * @return LocalTransaction instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new NotSupportedException("LocalTransaction not supported");
    }

    /**
     * Returns an <code>javax.transaction.xa.XAresource</code> instance.
     * 
     * @return XAResource instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public XAResource getXAResource() throws ResourceException {
        throw new NotSupportedException(
                "GetXAResource not supported not supported");
    }

    /**
     * Gets the metadata information for this connection's underlying EIS
     * resource manager instance.
     * 
     * @return ManagedConnectionMetaData instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        log.debug("getMetaData()");
        return null;
    }

    /**
     * Create wrapper class around actual tss password service
     * 
     * @return PasswordService
     */
    public PasswordService getPasswordService() throws ResourceException {
        final String hostName = TSSConfiguration.resolveNameServiceHost();
        final NameServiceAgent nsa = NameServiceAgent.getInstance();
        try {
            final Object tssObject = nsa.resolve(hostName,
                    NameServiceAgent.TSS_SERVICE_REF,
                    NameServiceAgent.TSS_PASSWORD_SERVICE);
            final Password tssPasswordService = PasswordHelper
                    .narrow((org.omg.CORBA.Object) tssObject);
            return new PasswordServiceImpl(tssPasswordService);

        } catch (NameServiceException nse) {
            throw new ResourceException(nse);
        }

    }

    /**
     * @return the connection
     */
    public AccessControlContext getConnection() {
        return connection;
    }

    /**
     * @param connection
     *            the connection to set
     */
    public void setConnection(final AccessControlContext connection) {
        this.connection = connection;
    }

}
