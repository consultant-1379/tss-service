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
package com.ericsson.oss.mediation.tss.ra;

import javax.resource.ResourceException;

import com.ericsson.oss.mediation.tss.AccessControlContext;
import com.ericsson.oss.mediation.tss.PasswordService;

/**
 * AccessControlContext implementation, this class contains code needed to get
 * all tss services that will be exposed through
 * <code>AccessControlContext<code> interface.
 * 
 * @author edejket
 * 
 */
public class AccessControlContextImpl implements AccessControlContext {

    /**
     * 
     */
    private static final long serialVersionUID = 824164549168932862L;

    /** ManagedConnection */
    private final AccessControlManagedConnection managedConnection;

    /**
     * Constructor
     * 
     * @param mc
     *            TSSManagedConnection ManagedConnection taking care of this
     *            context
     */
    public AccessControlContextImpl(final AccessControlManagedConnection mc) {
        this.managedConnection = mc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ericsson.oss.mediation.tss.AccessControlContext#getPasswordService()
     */
    @Override
    public PasswordService getPasswordService() throws ResourceException {
        return this.managedConnection.getPasswordService();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.mediation.tss.AccessControlContext#close()
     */
    @Override
    public void close() {
        this.managedConnection.closeHandle(this);
    }

}
