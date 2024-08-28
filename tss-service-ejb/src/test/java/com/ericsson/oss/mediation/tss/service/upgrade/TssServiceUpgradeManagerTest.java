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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.itpf.sdk.upgrade.UpgradeEvent;
import com.ericsson.oss.itpf.sdk.upgrade.UpgradePhase;

@RunWith(MockitoJUnitRunner.class)
public class TssServiceUpgradeManagerTest {

    @Mock
    private UpgradeEvent upgradeEvent;

    private TssServiceUpgradeManager upgradeManager;

    @Before
    public void setup() throws IllegalAccessException {

        upgradeManager = new TssServiceUpgradeManager();

    }

    private void verifyUpgradePhaseAccepted(final UpgradePhase upgradePhase) {
        when(upgradeEvent.getPhase()).thenReturn(upgradePhase);
        upgradeManager.onUpgradeEvent(upgradeEvent);
        verify(upgradeEvent).accept("OK");
    }

    @Test
    public void testReceptionOfUpgradeEventPrepareForClusterUpgradePrepare() {
        verifyUpgradePhaseAccepted(UpgradePhase.SERVICE_CLUSTER_UPGRADE_PREPARE);
    }

    @Test
    public void testReceptionOfUpgradeEventClusterUpgradeSuccess() {
        verifyUpgradePhaseAccepted(UpgradePhase.SERVICE_CLUSTER_UPGRADE_FINISHED_SUCCESSFULLY);
    }

    @Test
    public void testReceptionOfUpgradeEventClusterUpgradeFailed() {
        verifyUpgradePhaseAccepted(UpgradePhase.SERVICE_CLUSTER_UPGRADE_FAILED);
    }

    @Test
    public void testReceptionOfUpgradeEventInstanceUpgradePrepare() {
        verifyUpgradePhaseAccepted(UpgradePhase.SERVICE_INSTANCE_UPGRADE_PREPARE);
    }

    @Test
    public void testReceptionOfUpgradeEventServiceInstanceUpgradeSuccess() {
        verifyUpgradePhaseAccepted(UpgradePhase.SERVICE_INSTANCE_UPGRADE_FINISHED_SUCCESSFULLY);
    }

    @Test
    public void testReceptionOfUpgradeEventInstanceUpgradePrepareFailed() {
        verifyUpgradePhaseAccepted(UpgradePhase.SERVICE_CLUSTER_UPGRADE_FAILED);
    }

}
