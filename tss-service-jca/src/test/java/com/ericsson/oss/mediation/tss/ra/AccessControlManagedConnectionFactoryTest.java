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

import java.util.HashSet;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccessControlManagedConnectionFactoryTest {

    private AccessControlManagedConnectionFactory mcf;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.mcf = new AccessControlManagedConnectionFactory();
    }

    @Test(expected = ResourceException.class)
    public void testCreateConnectionFactoryEmptyThrowsException()
            throws ResourceException {
        this.mcf.createConnectionFactory();
    }

    @Test
    public void testCreateConnectionFactory() throws ResourceException {
        Assert.assertNotNull(this.mcf.createConnectionFactory(Mockito
                .mock(ConnectionManager.class)));
    }

    @Test
    public void testCreateManagedConnection() throws ResourceException {
        this.mcf.createManagedConnection(null, null);
    }

    @Test
    public void testMatchManagedConnections() throws ResourceException {
        Set<AccessControlManagedConnection> managedConns = new HashSet<>();
        AccessControlManagedConnection acm = Mockito
                .mock(AccessControlManagedConnection.class);
        managedConns.add(acm);
        ManagedConnection actual = this.mcf.matchManagedConnections(
                managedConns, null, null);
        Assert.assertEquals(acm, actual);
    }

    @Test
    public void testMatchManagedConnectionsEmptySet() throws ResourceException {
        Set<AccessControlManagedConnection> managedConns = new HashSet<>();
        ManagedConnection actual = this.mcf.matchManagedConnections(
                managedConns, null, null);
        Assert.assertEquals(null, actual);
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(17, this.mcf.hashCode());
    }

    @Test
    public void testEqualsNotTrue() {
        Assert.assertNotSame(
                Mockito.mock(AccessControlManagedConnectionFactory.class),
                this.mcf);
    }

    @Test
    public void testEqualsTrueSelf() {
        Assert.assertEquals(this.mcf, this.mcf);
    }

    @Test
    public void testEqualsTrueOther() {
        Assert.assertEquals(new AccessControlManagedConnectionFactory(),
                this.mcf);
    }
}
