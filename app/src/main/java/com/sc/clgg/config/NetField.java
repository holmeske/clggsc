package com.sc.clgg.config;

public class NetField {
    public static final String CHARSET = "UTF-8";
    public static final String SIGN_TYPE = "RSA";
    public static final String D_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMiAec6fsssguUoRN3oEVEnQaqBLZjeafXAxCbKH3MTJaXPmnXOtqFFqFtcB8J9KqyFI1+o6YBDNIdFWMKqOwDDWPKqtdo90oGav3QMikjGYjIpe/gYYCQ/In/oVMVj326GmKrSpp0P+5LNCx59ajRpO8//rnOLd6h/tNxnfahanAgMBAAECgYEAusouMFfJGsIWvLEDbPIhkE7RNxpnVP/hQqb8sM0v2EkHrAk5wG4VNBvQwWe2QsAuY6jYNgdCPgTNL5fLaOnqkyy8IobrddtT/t3vDX96NNjHP4xfhnMbpGjkKZuljWKduK2FAh83eegrSH48TuWS87LjeZNHhr5x4C0KHeBTYekCQQD5cyrFuKua6GNG0dTj5gA67R9jcmtcDWgSsuIXS0lzUeGxZC4y/y/76l6S7jBYuGkz/x2mJaZ/b3MxxcGQ01YNAkEAzcRGLTXgTMg33UOR13oqXiV9cQbraHR/aPmS8kZxkJNYows3K3umNVjLhFGusstmLIY2pIpPNUOho1YYatPGgwJBANq8vnj64p/Hv6ZOQZxGB1WksK2Hm9TwfJ5I9jDu982Ds6DV9B0L4IvKjHvTGdnye234+4rB4SpGFIFEo+PXLdECQBiOPMW2cT8YgboxDx2E4bt8g9zSM5Oym2Xeqs+o4nKbcu96LipNRkeFgjwXN1708QuNNMYsD0nO+WIxqxZMkZsCQHtS+Jj/LCnQZgLKxXZAllxqSTlBln2YnBgk6HqHLp8Eknx2rUXhoxE1vD9tNmom6PiaZlQyukrQkp5GOMWDMkU=";
    public static final String VERSION = "1";
    public static final String MERCHANTID = "3";

    public static String WX_PAY_SITE = "http://back.clgg.com:9838/api/";

//        public static String SITE = "http://back.clgg.com:9838/api/";
//    public static String SITE = "http://10.1.200.74:8080/api/";
    public static String SITE = "http://10.1.12.51:8080/api/";
//    public static String SITE = "http://10.1.12.20:9838/api/";
//    public static String SITE = "http://192.168.109.158:8080/api/";
    public static String CLGG_SITE = "http://api.clgg.com:9000/api/";
    public static String SSO_SITE = "http://sso.clgg.com/";

    /**
     * 陕汽服务站 - 业务介绍
     */
    public static String SERVICE_STATION = SITE + "serviceStation/introduce";
    /**
     * 营运证服务商 - 业务介绍
     */
    public static String OPERATION_CERTIFICATE = SITE + "serviceStation/certIntroduce";
    /**
     * 配件经销商 - 业务介绍
     */
    public static String PARTS_DISTRIBUTOR = SITE + "serviceStation/accessories";
}
