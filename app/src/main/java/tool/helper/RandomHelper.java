package tool.helper;

/**
 * Author：lvke
 * CreateDate：2017/10/11 11:28
 */

public class RandomHelper {

    /*随机6位数*/
    public static String six() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /*随机5位数*/
    public static String five() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 10000));
    }

    /*随机4位数*/
    public static String four() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
    }

}
