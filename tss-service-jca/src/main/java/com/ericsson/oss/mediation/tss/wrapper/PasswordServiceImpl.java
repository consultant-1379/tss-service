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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TSS.Password;
import TSSDef.*;

import com.ericsson.oss.mediation.tss.NodeAuthInfo;
import com.ericsson.oss.mediation.tss.PasswordService;
import com.ericsson.oss.mediation.tss.exception.AccessControlEntryException;
import com.ericsson.oss.mediation.tss.exception.AccessControlUnavailableException;

/**
 * Wrapper class arround actual tss password service. It should contain
 * implementation to all tss methods being used.
 * 
 * @author edejket
 * 
 */
public class PasswordServiceImpl implements PasswordService {

    private static final Logger log = LoggerFactory
            .getLogger(PasswordServiceImpl.class);
    private final Password tssPasswordService;

    public PasswordServiceImpl(final Password tssPasswordService) {
        this.tssPasswordService = tssPasswordService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ericsson.oss.mediation.tss.PasswordService#getNodeAuthInfo(java.lang
     * .String, java.lang.String)
     */
    @Override
    public NodeAuthInfo getNodeAuthInfo(final String nodeName,
            final String nodeType) throws AccessControlUnavailableException,
            AccessControlEntryException {
        final TypedString system = new TypedString(nodeName, nodeType);
        final TypedString[] adminPrivileges = new TypedString[1];
        adminPrivileges[0] = new TypedString("SYSADMIN", "Role");
        final TypedString administrator = new TypedString("nmsadm", User.value);
        try {
            final PasswordEntry[] ee = this.tssPasswordService
                    .getAccounts(system);
            final String password = this.tssPasswordService.getPassword(system,
                    ee[0].account, administrator, adminPrivileges);
            final NodeAuthInfo nodeAuthInfo = new NodeAuthInfo(ee[0].account,
                    password);
            return nodeAuthInfo;
        } catch (StoreNotAccessible sna) {
            log.error(
                    "Error caught while accessing tsa password service, stack trace: {}",
                    sna);
            throw new AccessControlUnavailableException(sna);

        } catch (EntryNotDefined end) {
            log.error(
                    "Error caught while looking for tsa entry, stack trace: {}",
                    end);
            throw new AccessControlEntryException(end);
        }

    }

}
