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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccessControlContextImplTest {

    private AccessControlContextImpl accessControlContextImpl;
    @Mock
    private AccessControlManagedConnection managedConnection;

    @Before
    public void setUp() {
        accessControlContextImpl = new AccessControlContextImpl(
                managedConnection);
    }

    @Test
    public void testGetPasswordService() throws ResourceException {
        accessControlContextImpl.getPasswordService();
        Mockito.verify(managedConnection, Mockito.times(1))
                .getPasswordService();
    }

    @Test
    public void testClose() throws ResourceException {
        accessControlContextImpl.close();
        Mockito.verify(managedConnection, Mockito.times(1)).closeHandle(
                accessControlContextImpl);
    }

}
