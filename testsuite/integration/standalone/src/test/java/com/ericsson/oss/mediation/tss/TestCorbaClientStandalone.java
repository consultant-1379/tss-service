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
package com.ericsson.oss.mediation.tss;

import org.junit.*;
import org.omg.CosNaming.NameComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TSS.Password;
import TSS.PasswordHelper;
import TSSDef.*;

import com.ericsson.oss.mediation.tss.util.NameServiceAgent;
import com.ericsson.oss.mediation.tss.util.configuration.TSSConfiguration;

public class TestCorbaClientStandalone {

    private static NameServiceAgent nsa;
    private static final NameComponent[] TSS_SERVICE_REF = new NameComponent[] {
            new NameComponent("com", ""), new NameComponent("ericsson", ""),
            new NameComponent("nms", ""), new NameComponent("tss", "") };

    private static final Logger log = LoggerFactory
            .getLogger(TestCorbaClientStandalone.class);

    @BeforeClass
    public static void setUp() {
        System.setProperty(TSSConfiguration.OSSRC_IPADDRESS, "10.45.206.74");
    }

    @Test
    public void testPasswordService() throws Exception {
        log.info("start");
        nsa = NameServiceAgent.getInstance();
        final String service = "Password";
        final String nssHost = TSSConfiguration.resolveNameServiceHost();
        final Object tssObject = nsa.resolve(nssHost, TSS_SERVICE_REF, service);
        Password password = PasswordHelper
                .narrow((org.omg.CORBA.Object) tssObject);

        /**
         * TYPES CAN BE NORMAL/SECURE
         */

        String miContext = "LTE01ERBS120GZIP";
        TypedString system = new TypedString(miContext, "NORMAL");
        final TypedString[] adminPrivileges = new TypedString[1];
        adminPrivileges[0] = new TypedString("SYSADMIN", "Role");
        final TypedString administrator = new TypedString("nmsadm", User.value);

        PasswordEntry[] ee = password.getAccounts(system);

        String pass = password.getPassword(system, ee[0].account,
                administrator, adminPrivileges);
        String username = ee[0].account;
        String type = ee[0].system.type;
        log.debug("TYPE: " + type);
        log.debug("Username: " + username);
        log.debug("Password: " + pass);
        Assert.assertEquals("secret", pass);
        Assert.assertEquals("LTE01ERBS120GZIP", username);
    }
}
