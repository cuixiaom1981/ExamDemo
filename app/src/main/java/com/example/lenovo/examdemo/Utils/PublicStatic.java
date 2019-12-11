
package com.example.lenovo.examdemo.Utils;

/**
 * ClassName:PublicStatic Function: 存放地址和其它公共信息的类 Reason: TODO ADD REASON
 *
 * @author sunhao_alien
 * @Date 2016-3-14 下午2:28:41
 * @see
 * @since Ver 1.1
 */
public class PublicStatic {
    // #服务访问地址
    // service.host=http://125.211.221.232:60080
   // public static final String SERVICE_HOST = "http://192.168.1.182:8089";// 钓鱼新
    public static final String SERVICE_HOST = "http://172.81.253.56:8090";
    //public static final String SERVICE_HOST = "http://222.171.242.146:60082";// 电信外网
    //public static final String SERVICE_HOST = "http://125.211.221.232:60099";// 联通外网
    //public static final String SERVICE_HOST = "http://47.92.5.144:8088";
    //public static final String SERVICE_HOST = "http://www.tusousou.cn";// 生产环境
    // #计算签名的地址，当服务对外使用端口映射时使用，此地址是真实地址
    // sign.host=http://192.168.137.15:8080
    public static final String SIGN_HOST = "";

    // #应用的名称
    //public static final String APP_NAME = "/tusousou";
    public static final String APP_NAME = "";

    // #接口API地址
    public static final String API_URL = "/hmu/";

    // 本地共享数据库
    public static final String NAOCHUXUE = "NAOCHUXUE";

}
