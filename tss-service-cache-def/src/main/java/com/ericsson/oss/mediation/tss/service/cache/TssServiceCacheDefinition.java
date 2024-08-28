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
package com.ericsson.oss.mediation.tss.service.cache;

import com.ericsson.oss.itpf.sdk.modeling.cache.CacheMode;
import com.ericsson.oss.itpf.sdk.modeling.cache.EvictionStrategy;
import com.ericsson.oss.itpf.sdk.modeling.cache.annotation.ModeledNamedCacheDefinition;
import com.ericsson.oss.mediation.tss.NodeAuthInfo;

/**
 * This class defines local cache which will be used to store NodeAuthInfo
 * retrieved from tss service. Records in the cache will be evicted after 1 hour
 * (timeToLive value may change in the future to benefit from performance
 * increase and keep values in the cache updated at the same time)
 * 
 */
@ModeledNamedCacheDefinition(name = "TssServiceCache", description = "Cached node security information", cacheMode = CacheMode.LOCAL, transactional = true, evictionStrategy = EvictionStrategy.NONE, timeToLive = 3600000, keyClass = String.class, valueClass = NodeAuthInfo.class)
public class TssServiceCacheDefinition {

}
