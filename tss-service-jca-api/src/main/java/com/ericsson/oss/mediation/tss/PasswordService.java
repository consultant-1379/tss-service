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

import com.ericsson.oss.mediation.tss.exception.AccessControlEntryException;
import com.ericsson.oss.mediation.tss.exception.AccessControlUnavailableException;

/**
 * Password service wrapper arround actual tss password service. This interface
 * should declare all methods that are being used from tss password service.
 * 
 * @author edejket
 * 
 */
public interface PasswordService {

    /**
     * Provides authentication information for given node. This method will
     * return <code>NodeAuthInfo</code> object, that will contain username and
     * password for given node.
     * 
     * @param nodeName
     *            Name of this node, example: <code>LTE05ERBS00005</code>
     * @param nodeType
     *            Type of this node, can be <code>SECURE</code> or
     *            <code>NORMAL</code>
     * @return NodeAuthInfo object with username and password
     * @throws AccessControlUnavailableException
     *             This exception will be thrown if there is a problem accessing
     *             underlying tss password service.
     * @throws AccessControlEntryException
     *             This exception will be thrown if there is no entry for this
     *             nodeName and nodeType in tss service.
     */
    NodeAuthInfo getNodeAuthInfo(final String nodeName, final String nodeType)
            throws AccessControlUnavailableException,
            AccessControlEntryException;
}
