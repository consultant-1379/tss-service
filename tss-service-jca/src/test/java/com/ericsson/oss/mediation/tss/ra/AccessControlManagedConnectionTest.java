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
import javax.resource.spi.ConnectionEventListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.mediation.tss.AccessControlContext;

@RunWith(MockitoJUnitRunner.class)
public class AccessControlManagedConnectionTest {

    private AccessControlManagedConnection accManagedConnection;

    @Mock
    private AccessControlManagedConnectionFactory mcf;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.accManagedConnection = new AccessControlManagedConnection(mcf);
    }

    @Test
    public void testGetConnection() throws ResourceException {

        AccessControlContext actual = (AccessControlContext) this.accManagedConnection
                .getConnection(null, null);
        Assert.assertNotNull(actual);
    }

    @Test(expected = ResourceException.class)
    public void testAssociateConnectionWhenNull() throws Exception {
        this.accManagedConnection.associateConnection(null);
    }

    @Test(expected = ResourceException.class)
    public void testAssociateConnectionWhenWrongInstance() throws Exception {
        this.accManagedConnection.associateConnection(new String());
    }

    @Test
    public void testAssociateConnectionWhenNotNull() throws Exception {
        AccessControlContextImpl conn = new AccessControlContextImpl(
                accManagedConnection);
        this.accManagedConnection.associateConnection(conn);

    }

    @Test
    public void testCleanUp() throws Exception {
        this.accManagedConnection.cleanup();

    }

    @Test
    public void testDestroy() throws Exception {
        this.accManagedConnection.destroy();

    }

    @Test(expected = IllegalArgumentException.class)
    public void addConnectionEventListenerWithNull() {
        this.accManagedConnection.addConnectionEventListener(null);
    }

    @Test
    public void addConnectionEventListenerWithNonNull() {
        this.accManagedConnection.addConnectionEventListener(Mockito
                .mock(ConnectionEventListener.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeConnectionEventListenerWithNull() {
        this.accManagedConnection.removeConnectionEventListener(null);
    }

    @Test
    public void removeConnectionEventListenerWithNonNull() {
        this.accManagedConnection.removeConnectionEventListener(Mockito
                .mock(ConnectionEventListener.class));
    }

    /**
     * ???TODO:???
     * 
     * @throws ResourceException
     */
    @Test(expected = ResourceException.class)
    public void testGetPasswordService() throws ResourceException {
        this.accManagedConnection.getPasswordService();
    }
}
