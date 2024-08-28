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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.cache.Cache;
import javax.resource.ResourceException;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.mediation.tss.*;
import com.ericsson.oss.mediation.tss.exception.AccessControlEntryException;
import com.ericsson.oss.mediation.tss.exception.AccessControlUnavailableException;
import com.ericsson.oss.mediation.tss.service.api.enums.NodeSecurityType;

@RunWith(MockitoJUnitRunner.class)
public class AccessControlServiceBeanTest {

    private static final String NODE_NAME = "RNC01RBS01";
    private static final String NODE_PASSWORD = "secret";
    private static final String NODE_SECURITY_TYPE = "SECURE";

    @Mock
    private Cache<String, NodeAuthInfo> tssCache;

    @Mock
    private AccessControl accessControlService;

    @InjectMocks
    private AccessControlServiceBean accessControlServiceBean;

    @Mock
    PasswordService passwordService;

    @Mock
    AccessControlContext accContext;

    NodeAuthInfo nodeAuthInfo;

    @Before
    public void setUp() throws AccessControlUnavailableException,
            AccessControlEntryException, ResourceException,
            IllegalAccessException {

        MockitoAnnotations.initMocks(this);
        nodeAuthInfo = new NodeAuthInfo(NODE_NAME, "secret");
        when(passwordService.getNodeAuthInfo(NODE_NAME, NODE_SECURITY_TYPE))
                .thenReturn(nodeAuthInfo);
        when(accContext.getPasswordService()).thenReturn(passwordService);
        when(accessControlService.getAccessControlContext()).thenReturn(
                accContext);

    }

    @Test
    public void getAuthenticationInfoReturnsCorrectValues()
            throws AccessControlEntryException,
            AccessControlUnavailableException, ResourceException {
        NodeAuthInfo result = accessControlServiceBean.getAuthenticationInfo(
                NODE_NAME, NodeSecurityType.SECURE);
        Assert.assertEquals(NODE_NAME, result.getUsername());
        Assert.assertEquals(NODE_PASSWORD, result.getPassword());
        result = accessControlServiceBean.getAuthenticationInfo(NODE_NAME,
                NodeSecurityType.SECURE);
        verify(accessControlService, Mockito.times(2))
                .getAccessControlContext();
    }

    @Test
    public void getAuthenticationInfoReturnsValuesFromCache()
            throws AccessControlEntryException,
            AccessControlUnavailableException, ResourceException {
        when(tssCache.get(NODE_NAME)).thenReturn(nodeAuthInfo);
        NodeAuthInfo result = accessControlServiceBean.getAuthenticationInfo(
                NODE_NAME, NodeSecurityType.SECURE);
        Assert.assertEquals(NODE_NAME, result.getUsername());
        Assert.assertEquals(NODE_PASSWORD, result.getPassword());
        result = accessControlServiceBean.getAuthenticationInfo(NODE_NAME,
                NodeSecurityType.SECURE);
        verify(accessControlService, Mockito.times(0))
                .getAccessControlContext();
    }

}
