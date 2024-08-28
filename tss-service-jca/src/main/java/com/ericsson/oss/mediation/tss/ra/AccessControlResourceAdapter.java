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

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.tss.util.NameServiceAgent;

/**
 * TSS Resource Adapter class
 * 
 * @author edejket
 * 
 */
public class AccessControlResourceAdapter implements ResourceAdapter,
        java.io.Serializable {

    /** The serial version UID */
    private static final long serialVersionUID = 1L;

    /** The logger */
    private static Logger log = LoggerFactory
            .getLogger(AccessControlResourceAdapter.class);

    /**
     * This is called when a resource adapter instance is bootstrapped.
     * 
     * @param ctx
     *            A bootstrap context containing references
     * @throws ResourceAdapterInternalException
     *             indicates bootstrap failure.
     */
    @Override
    public void start(final BootstrapContext ctx)
            throws ResourceAdapterInternalException {
        log.debug("Starting TSS Resource adapter");
        try {
            NameServiceAgent.getInstance();
        } catch (Exception e) {
            throw new ResourceAdapterInternalException(e);
        }
    }

    /**
     * This is called when a resource adapter instance is undeployed or during
     * application server shutdown.
     */
    @Override
    public void stop() {
        log.trace("stop()");
    }

    /**
     * This method is called by the application server during crash recovery.
     * 
     * @param specs
     *            An array of ActivationSpec JavaBeans
     * @throws ResourceException
     *             generic exception
     * @return An array of XAResource objects
     */
    @Override
    public XAResource[] getXAResources(final ActivationSpec[] specs)
            throws ResourceException {
        log.trace("getXAResources()");
        return null;
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
        if (!(other instanceof AccessControlResourceAdapter)) {
            return false;
        }
        final boolean result = true;
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.resource.spi.ResourceAdapter#endpointActivation(javax.resource.
     * spi.endpoint.MessageEndpointFactory, javax.resource.spi.ActivationSpec)
     */
    @Override
    public void endpointActivation(
            final MessageEndpointFactory endpointFactory,
            final ActivationSpec spec) throws ResourceException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.resource.spi.ResourceAdapter#endpointDeactivation(javax.resource
     * .spi.endpoint.MessageEndpointFactory, javax.resource.spi.ActivationSpec)
     */
    @Override
    public void endpointDeactivation(
            final MessageEndpointFactory endpointFactory,
            final ActivationSpec spec) {
    }
}
