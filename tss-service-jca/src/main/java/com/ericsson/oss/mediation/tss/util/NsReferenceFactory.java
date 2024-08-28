/*
(C) Copyright Ericsson Radio Systems AB, 2001.

The copyright to the computer program(s) herein is the property of Ericsson
Radio Systems AB, Sweden. The program(s) may be used and/or copied only with
the written permission from Ericsson Radio System AB or in accordance with
the terms and conditions stipulated in the agreement/contract under which
the program(s) have been supplied.

File name: /vobs/fw/cif2_nsa/nms_cif_nsa/source/com/ericsson/nms/cif/nsa/NsReferenceFactory.java

Rev: Date:    Prepared: TR/Req/CR:         Description:
     20050809 edavnee                      Removed PMD QRank4 Errors
     20050809 edavnee                      Removed PMD QRank5 Errors     
     
 */
package com.ericsson.oss.mediation.tss.util;

import java.util.Hashtable;

import org.omg.CosNaming.NameComponent;

public class NsReferenceFactory {
    public static final RefItem MIBINFO_REF = new RefItem("mibInfo");
    public static final RefItem SERVICE_REF = new RefItem("service");
    public static final RefItem MC_REF = new RefItem("mc");
    public static final RefItem INTERNAL_REF = new RefItem("internal");

    public static final NameComponent PATH_COM = new NameComponent("com", "");
    public static final NameComponent PATH_ERICSSON = new NameComponent(
            "ericsson", "");
    public static final NameComponent PATH_NMS = new NameComponent("nms", "");
    public static final NameComponent PATH_CIF = new NameComponent("cif", "");
    public static final NameComponent PATH_MC = new NameComponent("mc", "");
    public static final NameComponent PATH_MIBINFO = new NameComponent(
            "mibInfo", "");
    public static final NameComponent PATH_SERVICE = new NameComponent(
            "service", "");
    public static final NameComponent PATH_INTERNAL = new NameComponent(
            "internal", "");

    private static NsReferenceFactory factory = new NsReferenceFactory();
    final private Hashtable refs = new Hashtable();

    /**
     * Construct the Ldap references
     */
    private NsReferenceFactory() {
        refs.put(MC_REF, new NameComponent[] { PATH_COM, PATH_ERICSSON,
                PATH_NMS, PATH_CIF, PATH_MC });
        refs.put(MIBINFO_REF, new NameComponent[] { PATH_COM, PATH_ERICSSON,
                PATH_NMS, PATH_CIF, PATH_MIBINFO });
        refs.put(SERVICE_REF, new NameComponent[] { PATH_COM, PATH_ERICSSON,
                PATH_NMS, PATH_CIF, PATH_SERVICE });
        refs.put(INTERNAL_REF, new NameComponent[] { PATH_COM, PATH_ERICSSON,
                PATH_NMS, PATH_CIF, PATH_INTERNAL });
    }

    /**
     * Returns the specified Naming Service reference.
     * 
     * @param refItem
     *            The reference item name
     * @return The Naming Service reference associated with the item
     */
    public static NameComponent[] getRef(final RefItem refItem) {
        return (NameComponent[]) factory.refs.get(refItem);
    }

    private static class RefItem {
        final private String refItemName;

        private RefItem(final String name) {
            refItemName = name;
        }

        @Override
        public String toString() {
            return refItemName;
        }
    }
}
