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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.cache.Cache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import com.ericsson.oss.mediation.tss.NodeAuthInfo;

public class AccessControlServiceUpdateBeanTest {

    @Mock
    private Cache<String, NodeAuthInfo> tssCache;

    @InjectMocks
    private AccessControlServiceUpdateBean updateCacheBean;

    @Before
    public void initMocks() {

        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testUpdateCacheEntryWasCalled() {
        final String name = "meContext";
        updateCacheBean.updateCacheEntry(name);
        verify(tssCache, times(1)).remove(name);
    }

}
