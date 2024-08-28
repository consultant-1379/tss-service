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
import javax.resource.spi.ConnectionManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccessControlImplTest {

    private AccessControlImpl accessControlImpl;

    @Mock
    private ConnectionManager cxManager;

    @Mock
    private AccessControlManagedConnectionFactory mcf;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        accessControlImpl = new AccessControlImpl(mcf, cxManager);
    }

    @Test
    public void testGetAccessControlContext() throws ResourceException {
        accessControlImpl.getAccessControlContext();
        Mockito.verify(cxManager, Mockito.times(1)).allocateConnection(mcf,
                null);

    }

}
