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
package com.ericsson.integration.tss.mock;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.itpf.sdk.eventbus.annotation.Consumes;
import com.ericsson.oss.itpf.sdk.upgrade.core.jms.UpgradeChannelConstants;
import com.ericsson.oss.itpf.sdk.upgrade.core.jms.UpgradeResponseMessage;
import com.ericsson.oss.mediation.tss.exception.AccessControlEntryException;
import com.ericsson.oss.mediation.tss.exception.AccessControlUnavailableException;
import com.ericsson.oss.mediation.tss.service.api.AccessControlService;
import com.ericsson.oss.mediation.tss.service.api.enums.NodeSecurityType;

@ApplicationScoped
public class TargetResolverMock {

    @EServiceRef
    AccessControlService accessControlService;
    
    Logger log = LoggerFactory.getLogger(TargetResolverMock.class);

    private List<String> status = new LinkedList<String>();

    public String getUsername(final String nodeName,
            final NodeSecurityType nodeType)
            throws AccessControlEntryException,
            AccessControlUnavailableException {
        return accessControlService.getAuthenticationInfo(nodeName, nodeType)
                .getUsername();
    }

    public String getPassword(final String nodeName,
            final NodeSecurityType nodeType)
            throws AccessControlEntryException,
            AccessControlUnavailableException {
        return accessControlService.getAuthenticationInfo(nodeName, nodeType)
                .getPassword();
    }
    
    public void receiveMessage(
            @Observes @Consumes(endpoint = UpgradeChannelConstants.CHANNEL_NAME,
                    filter = UpgradeChannelConstants.UPGRADE_RESPONSE_FILTER) final UpgradeResponseMessage message) {
        log.debug("Upgrade response message={} was received", message);
        status.add(message.getMessage());
        status.add(message.getId());
    }

    public List<String> getUpgradeStatus() {
        return status;
    }
}
