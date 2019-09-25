package com.tirsportif.backend.model;

import com.tirsportif.backend.configuration.CustomMethodSecurityExpressionRoot;

/**
 * Authority values, representing a role (or permission) on the application.
 *
 * IMPORTANT: Order of enum entries has signification.
 * See {@link CustomMethodSecurityExpressionRoot} on how it's used.
 */
public enum AuthorityType {

    ADMIN,
    MANAGER,
    VIEW

}
