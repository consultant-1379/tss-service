/*
(C) Copyright Ericsson Radio Systems AB, 2001.

The copyright to the computer program(s) herein is the property of Ericsson
Radio Systems AB, Sweden. The program(s) may be used and/or copied only with
the written permission from Ericsson Radio System AB or in accordance with
the terms and conditions stipulated in the agreement/contract under which
the program(s) have been supplied.

File name: /vobs/fw/cif2_nsa/nms_cif_nsa/source/com/ericsson/nms/cif/nsa/NameServiceException.java

Rev: Date:    Prepared: TR/Req/CR:         Description:
     20030520 EHFRJO    CIFTR3887          Added info about what and why...
     20050809 edavnee                      Removed PMD QRank4 Errors     

 */
package com.ericsson.oss.mediation.tss.util;

import java.util.StringTokenizer;

public class NameServiceException extends java.lang.Exception {
    /**
     * 
     */
    private static final long serialVersionUID = -3581969139295655941L;
    public final int NSport; // Name Service port
    public final String contextName; // The context name
    public final String corbaObjectName; // The name of the corba object
    public final String corbaObjectPath; // Path to the corba object
    public final String description; // Reason for the exception
    public final String exception; // Exception that was thrown
    public final String ior; // IOR to the naming context
    public final String method; // The method that was called
    public final String namingContext; // The naming context specified by the IOR
    public final String NShost; // Name Service host
    public final String orbInitRef; // 
    public final String url; // 

    private String protocol = null;

    public NameServiceException(final String _exception) {
        super(_exception);
        method = null;
        orbInitRef = null;
        NShost = null;
        NSport = -1;
        corbaObjectName = null;
        corbaObjectPath = null;
        url = null;
        namingContext = null;
        contextName = null;
        ior = null;
        description = null;
        exception = _exception;
    }

    public NameServiceException(final String _method, final String _orbInitRef, // NOPMD by edejket on 12/6/12 8:18 PM
            final String _NShost, final int _NSport,
            final String _corbaObjectName, final String _corbaObjectPath,
            final String _url, final String _namingContext,
            final String _contextName, final String _ior,
            final String _description, final String _exception) {
        super(_exception);
        method = _method;
        orbInitRef = _orbInitRef;
        NShost = _NShost;
        NSport = _NSport;
        corbaObjectName = _corbaObjectName;
        corbaObjectPath = _corbaObjectPath;
        url = _url;
        namingContext = _namingContext;
        contextName = _contextName;
        ior = _ior;
        description = _description;
        exception = _exception;
    }

    public String getContextName() {
        return contextName;
    }

    public String getCorbaObjectName() {
        return corbaObjectName;
    }

    public String getCorbaObjectPath() {
        return corbaObjectPath;
    }

    public String getDescription() {
        return description;
    }

    public String getException() {
        return exception;
    }

    public String getIOR() {
        return ior;
    }

    public String getMethod() {
        return method;
    }

    public String getNamingContext() {
        return namingContext;
    }

    public String getNSHost() {
        return NShost;
    }

    public int getNSPort() {
        return NSport;
    }

    public String getOrbInitRef() {
        return orbInitRef;
    }

    // Get the protocol from: NameService=iioploc://masterservice:12468/NameService
    public String getProtocol() {
        if (orbInitRef != null) {
            // NameService=iioploc, //masterservice, 12468/NameService
            final StringTokenizer tokens = new StringTokenizer(orbInitRef, ":");
            if (tokens.hasMoreTokens()) {
                //We are only interested in the first token, NameService=iioploc
                final StringTokenizer protocolToken = new StringTokenizer(
                        tokens.nextToken(), "=");
                while (protocolToken.hasMoreTokens()) {
                    protocol = protocolToken.nextToken(); // We want the last token, iioploc
                }
            }
        }

        return protocol;
    }

    public String getURL() {
        return url;
    }
}
