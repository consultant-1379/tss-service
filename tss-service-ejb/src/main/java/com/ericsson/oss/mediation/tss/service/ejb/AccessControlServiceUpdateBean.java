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

import javax.cache.Cache;
import javax.ejb.*;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.cache.annotation.NamedCache;
import com.ericsson.oss.mediation.tss.NodeAuthInfo;

/**
 * Stateless Session Bean responsible for deleting entry from Access Control
 * Service internal cache.
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AccessControlServiceUpdateBean {

    @Inject
    @NamedCache("TssServiceCache")
    private Cache<String, NodeAuthInfo> tssCache;

    private static final Logger log = LoggerFactory
            .getLogger(AccessControlServiceUpdateBean.class);

    /**
     * This method will try to remove NodeAuthInfo from Access Control Cache
     * based on supplied nodeName
     * 
     * @param nodeName
     *            - key used to remove entry from cache
     */
    public void updateCacheEntry(final String nodeName) {
        if (tssCache.remove(nodeName)) {
            log.debug("Node {} has been removed form the cache", nodeName);
        } else {
            log.debug("Node {} was not present in the cache", nodeName);
        }
    }
}
