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
package com.ericsson.oss.mediation.tss.service.ejb;

import javax.annotation.Resource;
import javax.cache.Cache;
import javax.ejb.*;
import javax.inject.Inject;
import javax.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.cache.annotation.NamedCache;
import com.ericsson.oss.mediation.tss.*;
import com.ericsson.oss.mediation.tss.exception.AccessControlEntryException;
import com.ericsson.oss.mediation.tss.exception.AccessControlUnavailableException;
import com.ericsson.oss.mediation.tss.service.api.AccessControlService;
import com.ericsson.oss.mediation.tss.service.api.enums.NodeSecurityType;

/**
 * Enterprise Java Bean exposing TSS service methods.</b>This EJB should be
 * injected in code that wants to use tss-service, to obtain security
 * informations.
 * 
 * @author edejket
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AccessControlServiceBean implements AccessControlService {

    private static final long serialVersionUID = 3377549751005012715L;

    private static final Logger log = LoggerFactory
            .getLogger(AccessControlServiceBean.class);

    @Inject
    @NamedCache("TssServiceCache")
    private Cache<String, NodeAuthInfo> tssCache;

    @Resource(lookup = "java:/eis/AccessControlService")
    private AccessControl accessControlService;

    /**
     * Method that will fetch node security info from local cache, or if info
     * not present in the cache, it will connect to tss service.
     * 
     * @param nodeName
     *            nodeName
     * @param nodeType
     *            Type of the node, can be NORMAL or SECURE
     * @return NodeAuthInfo containing username and password
     */

    @Override
    public NodeAuthInfo getAuthenticationInfo(final String nodeName,
            final NodeSecurityType nodeType)
            throws AccessControlEntryException,
            AccessControlUnavailableException {
        NodeAuthInfo authenticationInfo = null;
        AccessControlContext acContext = null;

        try {
            authenticationInfo = tssCache.get(nodeName);
            if (authenticationInfo != null) {
                return authenticationInfo;
            } else {
                acContext = accessControlService.getAccessControlContext();
                authenticationInfo = acContext.getPasswordService()
                        .getNodeAuthInfo(nodeName, nodeType.name());

                tssCache.put(nodeName, authenticationInfo);
                return authenticationInfo;
            }
        } catch (final ResourceException re) {
            log.error("Resource exception caught, stacktrace {}", re);
            throw new AccessControlUnavailableException(re);
        } finally {
            log.debug("Calling close connection on tss-resource adapter");
            if (acContext != null) {
                acContext.close();
                log.debug("Called close on tss-resource adatper");
            } else {
                log.debug("tss-connection was null, no need to call close method of jca adapter");
            }

        }

    }
}
