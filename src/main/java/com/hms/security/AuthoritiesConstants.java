package com.hms.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String DOCTOR = "ROLE_DOCTOR";

    public static final String PATIENT = "ROLE_PATIENT";

    public static final String INSURER = "ROLE_INSURER";

    public static final String ER = "ROLE_ER";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static String getConstantByType(String type) {
        switch (type){
            case "doctor":
                return DOCTOR;
            case "patient":
                return PATIENT;
            case "insurer":
                return INSURER;
            case "er":
                return ER;
        }
        return "";
    }

    private AuthoritiesConstants() {}
}
