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
package com.ericsson.oss.mediation.tss.service.upgrade;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.upgrade.UpgradeEvent;

/**
 * This class responds to <code>UpgradeEvent</code> coming in from Service
 * Framework.
 */
@Singleton
@Startup
public class TssServiceUpgradeManager {

    private static final Logger logger = LoggerFactory
            .getLogger(TssServiceUpgradeManager.class);

    private static final String applicationId = "tss-service";

    /**
     * Receives <code>UpgradeEvent</code> from the Service Framework and
     * processes these events by accepting the upgrade request.
     * 
     * @param upgradeEvent
     *            a CDI event indicating that the <code>UpgradePhase</code> has
     *            changed in the upgrade procedure.
     */
    public void onUpgradeEvent(@Observes final UpgradeEvent upgradeEvent) {

        logger.debug(
                "UpgradeEvent received in application '{}' with Upgrade Phase {}",
                applicationId, upgradeEvent.getPhase());
        logger.debug("Accepting upgrade prepare request by returning OK");
        upgradeEvent.accept("OK");
    }
}
