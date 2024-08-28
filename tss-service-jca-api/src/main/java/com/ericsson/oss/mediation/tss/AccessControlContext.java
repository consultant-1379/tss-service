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
package com.ericsson.oss.mediation.tss;

import java.io.Serializable;

import javax.resource.ResourceException;

/**
 * Interface declaring all available tss services that can be used with
 * tss-service. Any service available through TSS should be exposed here using
 * wrapper class.
 * 
 * @author edejket
 * 
 */
public interface AccessControlContext extends Serializable {

    /**
     * Getter method for password service
     * 
     * @return Password service
     * @throws ResourceException
     *             throw this exception if unable to get password service
     */
    PasswordService getPasswordService() throws ResourceException;

    /**
     * Close this connection
     */
    void close();
}
