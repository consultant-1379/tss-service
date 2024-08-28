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
package com.ericsson.oss.mediation.tss.service.api;

import java.io.Serializable;

import javax.ejb.Remote;

import com.ericsson.oss.itpf.sdk.core.annotation.EService;
import com.ericsson.oss.mediation.tss.NodeAuthInfo;
import com.ericsson.oss.mediation.tss.exception.AccessControlEntryException;
import com.ericsson.oss.mediation.tss.exception.AccessControlUnavailableException;
import com.ericsson.oss.mediation.tss.service.api.enums.NodeSecurityType;

/**
 * The <code>AccessControlService</code> allows users to get node authentication
 * information from TSS. This Enterprise Java Bean interface should be injected
 * into code, when TSS information is needed.
 * 
 */
@EService
@Remote
public interface AccessControlService extends Serializable {

    /**
     * Method that will fetch node security info from tss, service.
     * 
     * @param nodeName
     *            nodeName
     * @param nodeType
     *            Type of the node, can be NORMAL or SECURE
     * @return NodeAuthInfo containing username and password
     */
    NodeAuthInfo getAuthenticationInfo(final String nodeName,
            final NodeSecurityType nodeType)
            throws AccessControlEntryException,
            AccessControlUnavailableException;

}
