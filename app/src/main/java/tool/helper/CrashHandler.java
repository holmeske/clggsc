package tool.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * @author：lvke
 * @date：2017/12/11 13:16
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;
    private PendingIntent intent; //APP退出后,PendingIntent还存在,设置2秒之后,重新启动你的主Activity
    private Context mContext;

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context ctx, PendingIntent intent) {
        this.intent = intent;
        this.mContext = ctx;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e) && instance != null) {
            instance.uncaughtException(t, e);
        } else {
            try {

                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, intent);
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext.getApplicationContext(), "很抱歉,程序出现异常,一秒钟后重启", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        return true;
    }
}
