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
package com.ericsson.oss.mediation.tss.service.cacheupdate.rest;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.tss.service.ejb.AccessControlServiceUpdateBean;

@ApplicationPath("/updateServiceTss")
@Path("/")
@ApplicationScoped
public class CacheUpdateServiceREST extends Application {

    private static final Logger log = LoggerFactory
            .getLogger(CacheUpdateServiceREST.class);

    @EJB
    private AccessControlServiceUpdateBean cacheUpdateBean;

    /**
     * This method will trigger deleting cache entry for node name provided as
     * parameter of the REST call.
     * http://hostIP:port/tss/updateServiceTss/deleteCacheEntry?nodeName=
     * nodeName
     * 
     * @param nodeName
     *            Name of the node for which cache entry should be deleted.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/deleteCacheEntry")
    public void processCacheEntryUpdateRequest(
            @QueryParam("nodeName") final String nodeName) {

        log.debug("Received request for cache entry update for node {}",
                nodeName);
        cacheUpdateBean.updateCacheEntry(nodeName);
    }

}
