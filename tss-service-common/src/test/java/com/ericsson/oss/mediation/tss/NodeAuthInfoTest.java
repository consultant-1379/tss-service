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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class NodeAuthInfoTest {

    private NodeAuthInfo nodeAuthInfo;
    private static final String NODE_USERNAME = "ERBS00001";
    private static final String NODE_PASSWORD = "secret";
    private static final int HASH_CODE = -440841148;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        nodeAuthInfo = new NodeAuthInfo(NODE_USERNAME, NODE_PASSWORD);
    }

    @Test
    public void testToString() {
        Assert.assertEquals(
                "NodeAuthInfoImpl [username=ERBS00001, password=secret]",
                nodeAuthInfo.toString());
    }

    @Test
    public void testNotEqualsUsernameDifferent() {
        final NodeAuthInfo anotherNodeAuthInfo = new NodeAuthInfo(NODE_USERNAME
                + "aa", NODE_PASSWORD);
        Assert.assertFalse(nodeAuthInfo.equals(anotherNodeAuthInfo));
    }

    @Test
    public void testNotEqualsPasswordDifferent() {
        final NodeAuthInfo anotherNodeAuthInfo = new NodeAuthInfo(
                NODE_USERNAME, NODE_PASSWORD + "aa");
        Assert.assertFalse(nodeAuthInfo.equals(anotherNodeAuthInfo));
    }

    @Test
    public void testNotEqualsNull() {
        Assert.assertFalse(nodeAuthInfo.equals(null));
    }

    @Test
    public void testEquals() {
        final NodeAuthInfo anotherNodeAuthInfo = new NodeAuthInfo(
                NODE_USERNAME, NODE_PASSWORD);
        Assert.assertTrue(nodeAuthInfo.equals(anotherNodeAuthInfo));
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(HASH_CODE, nodeAuthInfo.hashCode());
    }
}
