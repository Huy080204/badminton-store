package com.mgr.api.dto;

public class ErrorCode {
    /**
     * General error code
     */
    public static final String GENERAL_ERROR_INVALID_USERNAME_OR_PASSWORD = "ERROR-GENERAL-0000";

    /**
     * Starting error code Account
     */
    public static final String ACCOUNT_ERROR_NOT_FOUND = "ERROR-ACCOUNT-0000";
    public static final String ACCOUNT_ERROR_USERNAME_EXISTED = "ERROR-ACCOUNT-0001";
    public static final String ACCOUNT_ERROR_WRONG_PASSWORD = "ERROR-ACCOUNT-0002";
    public static final String ACCOUNT_ERROR_UNABLE_CREATE = "ERROR-ACCOUNT-0003";
    public static final String ACCOUNT_ERROR_UNABLE_UPDATE = "ERROR-ACCOUNT-0004";
    public static final String ACCOUNT_ERROR_UNABLE_DELETE = "ERROR-ACCOUNT-0005";
    public static final String ACCOUNT_ERROR_EMAIL_EXISTED = "ERROR-ACCOUNT-0006";
    public static final String ACCOUNT_ERROR_PHONE_EXISTED = "ERROR-ACCOUNT-0007";

    /**
     * Starting error code DATABASE_ERROR
     */
    public static final String ERROR_DB_QUERY = "ERROR-DB-QUERY-0000";

    /**
     * Permission error code
     */
    public static final String PERMISSION_ERROR_NOT_FOUND = "ERROR-PERMISSION-0000";
    public static final String PERMISSION_ERROR_NAME_EXISTED = "ERROR-PERMISSION-0001";
    public static final String PERMISSION_ERROR_CODE_EXISTED = "ERROR-PERMISSION-0002";

    /**
     * Group error code
     */
    public static final String GROUP_ERROR_NOT_FOUND = "ERROR-GROUP-0000";
    public static final String GROUP_ERROR_NAME_EXISTED = "ERROR-GROUP-0001";
    public static final String GROUP_ERROR_INVALID_KIND = "ERROR-GROUP-0002";
    /**
     * User error code
     */
    public static final String USER_ERROR_NOT_FOUND = "ERROR-USER-0000";
    public static final String USER_ERROR_USERNAME_EXISTED = "ERROR-USER-0001";
    public static final String USER_ERROR_EMAIL_EXISTED = "ERROR-USER-0002";
    public static final String USER_ERROR_PHONE_EXISTED = "ERROR-USER-0003";
    public static final String USER_ERROR_PERMISSION_CREATE = "ERROR-USER-0004";
    public static final String USER_ERROR_PERMISSION_UPDATE = "ERROR-USER-0005";
    public static final String USER_ERROR_PERMISSION_GET = "ERROR-USER-0006";
    /**
     * Seller error code
     */
    public static final String SELLER_ERROR_EXISTED = "ERROR-SELLER-0000";

    public static final String CATEGORY_ERROR_NOT_FOUND = "ERROR-CATEGORY-0001";
    public static final String ERROR_INVALID_FORM = "ERROR-FORM-0001";

    /**
     * Nation error code
     */
    public static final String NATION_ERROR_NOT_FOUND = "ERROR-NATION-0000";
    public static final String NATION_ERROR_NAME_EXISTED = "ERROR-NATION-0001";
    public static final String NATION_ERROR_KIND_INVALID = "ERROR-NATION-0002";
    public static final String NATION_ERROR_PARENT_NOT_FOUND = "ERROR-NATION-0003";
    public static final String NATION_ERROR_PROVINCE_PARENT_INVALID = "ERROR-NATION-0004";

    /**
     * Product  error code
     */
    public static final String PRODUCT_ERROR_NOT_FOUND = "ERROR-PRODUCT-0000";
}
