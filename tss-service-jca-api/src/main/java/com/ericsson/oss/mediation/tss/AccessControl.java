package com.ericsson.oss.mediation.tss;

import java.io.Serializable;

import javax.resource.Referenceable;
import javax.resource.ResourceException;

/**
 * AccessControlService interface is exposing all available tss service,
 * currently only tss Password service is exposed, as it is the only one being
 * used.
 * 
 * @author edejket
 * 
 */
public interface AccessControl extends Serializable, Referenceable {

    /**
     * Getter method for AccessControlContext
     * 
     * @return AccessControlContext exposing available services
     * @throws ResourceException
     *             if unable to create AccessControlContext
     */
    AccessControlContext getAccessControlContext() throws ResourceException;
}
