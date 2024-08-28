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
package com.ericsson.oss.mediation.tss.exception;

public class AccessControlUnavailableException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -1758820877782843390L;

    public AccessControlUnavailableException(final Throwable t) {
        super(t);
    }

}
