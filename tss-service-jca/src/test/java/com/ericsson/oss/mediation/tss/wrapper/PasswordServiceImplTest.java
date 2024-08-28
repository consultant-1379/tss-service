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
package com.ericsson.oss.mediation.tss.wrapper;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import TSS.Password;
import TSSDef.*;

import com.ericsson.oss.mediation.tss.NodeAuthInfo;
import com.ericsson.oss.mediation.tss.exception.AccessControlEntryException;
import com.ericsson.oss.mediation.tss.exception.AccessControlUnavailableException;

@RunWith(MockitoJUnitRunner.class)
public class PasswordServiceImplTest {

    private PasswordServiceImpl passwdServiceImpl;
    private static final String NODE_NAME = "ERBS0001";
    private static final String NODE_SECURITY_TYPE = "SECURE";
    private static final String NODE_PASSWORD = "secret";

    @Mock
    private Password tssPasswordService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.passwdServiceImpl = new PasswordServiceImpl(tssPasswordService);
    }

    @Test
    public void testGetNodeAuthInfo() throws StoreNotAccessible,
            EntryNotDefined, AccessControlEntryException,
            AccessControlUnavailableException {

        TypedString system = new TypedString(NODE_NAME, NODE_SECURITY_TYPE);

        PasswordEntry[] passEntry = new PasswordEntry[] { new PasswordEntry(
                system, NODE_NAME, NODE_NAME) };

        Mockito.when(
                tssPasswordService.getAccounts(Mockito.any(TypedString.class)))
                .thenReturn(passEntry);

        Mockito.when(
                tssPasswordService.getPassword(Mockito.any(TypedString.class),
                        Mockito.any(String.class),
                        Mockito.any(TypedString.class),
                        Mockito.any(TypedString[].class))).thenReturn(
                NODE_PASSWORD);

        NodeAuthInfo ni = this.passwdServiceImpl.getNodeAuthInfo(NODE_NAME,
                NODE_SECURITY_TYPE);

        Mockito.verify(tssPasswordService, Mockito.times(1)).getAccounts(
                Mockito.any(TypedString.class));

        Mockito.verify(tssPasswordService, Mockito.times(1)).getPassword(
                Mockito.any(TypedString.class), Mockito.any(String.class),
                Mockito.any(TypedString.class),
                Mockito.any(TypedString[].class));

        Assert.assertEquals(NODE_NAME, ni.getUsername());
        Assert.assertEquals(NODE_PASSWORD, ni.getPassword());

    }

    @Test(expected = AccessControlUnavailableException.class)
    public void testGetNodeAuthInfoWhenStoreNotAccessible()
            throws StoreNotAccessible, EntryNotDefined,
            AccessControlEntryException, AccessControlUnavailableException {

        Mockito.when(
                tssPasswordService.getAccounts(Mockito.any(TypedString.class)))
                .thenThrow(new StoreNotAccessible());

        NodeAuthInfo ni = this.passwdServiceImpl.getNodeAuthInfo(NODE_NAME,
                NODE_SECURITY_TYPE);

    }

    @Test(expected = AccessControlEntryException.class)
    public void testGetNodeAuthInfoWhenNoEntryInTSS()
            throws StoreNotAccessible, EntryNotDefined,
            AccessControlEntryException, AccessControlUnavailableException {

        TypedString system = new TypedString(NODE_NAME, NODE_SECURITY_TYPE);
        PasswordEntry[] passEntry = new PasswordEntry[] { new PasswordEntry(
                system, NODE_NAME, NODE_NAME) };

        Mockito.when(
                tssPasswordService.getAccounts(Mockito.any(TypedString.class)))
                .thenReturn(passEntry);

        Mockito.when(
                tssPasswordService.getPassword(Mockito.any(TypedString.class),
                        Mockito.any(String.class),
                        Mockito.any(TypedString.class),
                        Mockito.any(TypedString[].class))).thenThrow(
                new EntryNotDefined());

        NodeAuthInfo ni = this.passwdServiceImpl.getNodeAuthInfo(NODE_NAME,
                NODE_SECURITY_TYPE);

    }

    @Test(expected = AccessControlUnavailableException.class)
    public void testGetNodeAuthInfoWhenGetPasswordThrowsStoreNotAccessible()
            throws StoreNotAccessible, EntryNotDefined,
            AccessControlEntryException, AccessControlUnavailableException {

        TypedString system = new TypedString(NODE_NAME, NODE_SECURITY_TYPE);
        PasswordEntry[] passEntry = new PasswordEntry[] { new PasswordEntry(
                system, NODE_NAME, NODE_NAME) };

        Mockito.when(
                tssPasswordService.getAccounts(Mockito.any(TypedString.class)))
                .thenReturn(passEntry);

        Mockito.when(
                tssPasswordService.getPassword(Mockito.any(TypedString.class),
                        Mockito.any(String.class),
                        Mockito.any(TypedString.class),
                        Mockito.any(TypedString[].class))).thenThrow(
                new StoreNotAccessible());

        NodeAuthInfo ni = this.passwdServiceImpl.getNodeAuthInfo(NODE_NAME,
                NODE_SECURITY_TYPE);

    }
}
