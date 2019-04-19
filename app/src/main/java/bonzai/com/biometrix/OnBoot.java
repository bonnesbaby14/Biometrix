package bonzai.com.biometrix;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;

import java.util.Calendar;

public class OnBoot extends BroadcastReceiver {
    Helper helper;
    Context context;
int hora1, hora2, minuto1, minuto2;
    @Override
    public void onReceive(Context context, Intent intent) {
this.context=context;
        String action=intent.getAction();
        if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
            helper=new Helper(context,"DB",null,1);
            SQLiteDatabase DB=helper.getReadableDatabase();
            Cursor cursor=DB.rawQuery("SELECT * FROM ALARMAS;", null);
            cursor.moveToFirst();
hora1= Integer.parseInt(cursor.getString(1));
minuto1=Integer.parseInt(cursor.getString(2));
cursor.moveToNext();
hora2= Integer.parseInt(cursor.getString(1));
minuto2=Integer.parseInt(cursor.getString(2));
startAlamr1();
startAlamr2();






        }


    }


    public void startAlamr1(){
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hora1);
        c.set(Calendar.MINUTE,minuto1);
        c.set(Calendar.SECOND,0);

        Intent intent =new Intent(context, AlertRecive.class);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(context,1,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }

    public  void startAlamr2(){
        AlarmManager alarmManager2=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hora2);
        c.set(Calendar.MINUTE,minuto2);
        c.set(Calendar.SECOND,0);

        Intent intent =new Intent(context, AlertRecive.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,2,intent,0);

        alarmManager2.setInexactRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }
}
