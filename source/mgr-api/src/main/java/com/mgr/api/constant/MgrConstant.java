package com.mgr.api.constant;

public class MgrConstant {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_USER = 2;
    public static final Integer  USER_KIND_SELLER = 3;

    public static final Long GROUP_ID_SELLER = 3L;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    public static final Integer NATION_KIND_PROVINCE = 1;
    public static final Integer NATION_KIND_DISTRICT = 2;
    public static final Integer NATION_KIND_COMMUNE = 3;

    // Order status
    public static final Integer ORDER_STATUS_PENDING = 1;       // Chờ xác nhận
    public static final Integer ORDER_STATUS_CONFIRMED = 2;     // Đã xác nhận
    public static final Integer ORDER_STATUS_SHIPPING = 3;      // Đang giao
    public static final Integer ORDER_STATUS_COMPLETED = 4;     // Hoàn thành
    public static final Integer ORDER_STATUS_CANCELLED = 0;     // Đã hủy

    // Tenant types
    public static final String TENANT_HEADER = "X-tenant";

    private MgrConstant() {
        throw new IllegalStateException("Utility class");
    }
}
