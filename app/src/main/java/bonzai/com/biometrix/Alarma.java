package bonzai.com.biometrix;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class Alarma extends Fragment implements TimePickerDialog.OnTimeSetListener {

    Button btnconectar1,btnconectar2;
public Boolean Alarma;
TextView Alarma1, Alarma2;
    Helper helper;
    View view;
    BluetoothAdapter bluetoothAdapter;
     public int hora1, minuto1,hora2,minuto2;



    public Alarma() {
        // Required empty public constructor
    }


    public static Alarma newInstance(String param1, String param2) {
        Alarma fragment = new Alarma();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_alarma, container, false);
btnconectar1=view.findViewById(R.id.btnAlarma1);
btnconectar2=view.findViewById(R.id.btnAlarma2);
Alarma1=view.findViewById(R.id.tvAlarma1);
Alarma2=view.findViewById(R.id.tvAlarma2);

        helper=new Helper(getContext(),"DB",null,1);
        SQLiteDatabase DB=helper.getReadableDatabase();
        Cursor cursor=DB.rawQuery("SELECT * FROM ALARMAS;", null);
        cursor.moveToFirst();


        Alarma1.setText("Alarma 1: "+String.valueOf(cursor.getInt(1))+" : "+String.valueOf(cursor.getInt(2)));
        cursor.moveToNext();
        Alarma2.setText("Alarma 2: "+String.valueOf(cursor.getInt(1))+" : "+String.valueOf(cursor.getInt(2)));



btnconectar1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Alarma=true;
        DialogFragment dialogFragment=new TimmerPicket();
            dialogFragment.show(getChildFragmentManager(), "timmer picket");


    }
});
btnconectar2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Alarma=false;
        DialogFragment dialogFragment=new TimmerPicket();
        dialogFragment.show(getChildFragmentManager().beginTransaction(),"timepicket");

    }
});



        return view;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    public void startAlamr1(Calendar c, int hora, int minuto){
        AlarmManager alarmManager=(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent =new Intent(getContext(), AlertRecive.class);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(getContext(),1,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        helper=new Helper(getContext(),"DB",null,1);
        SQLiteDatabase DB=helper.getWritableDatabase();


        DB.execSQL("UPDATE ALARMAS SET HORA="+ hora+",  MINUTO="+ minuto+" where ID=1 ");

        Snackbar.make(view, "Alarma 1 configurada", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
    public  void startAlamr2(Calendar c, int hora,int minuto){
        AlarmManager alarmManager2=(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent =new Intent(getContext(), AlertRecive.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(),2,intent,0);

        alarmManager2.setInexactRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        helper=new Helper(getContext(),"DB",null,1);
        SQLiteDatabase DB=helper.getWritableDatabase();
        DB.execSQL("UPDATE ALARMAS SET HORA="+ hora+",  MINUTO="+ minuto+" where ID=2 ");

        Snackbar.make(view, "Alarma 2 configurada", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);





        if(Alarma){
            hora1=hourOfDay;
            minuto1=minute;
            Alarma1.setText("Alarma 1: "+hourOfDay+" : "+minute);
            startAlamr1(c,hora1,minuto1);
        }else{
            hora2=hourOfDay;
            minuto2=minute;
            Alarma2.setText("Alarma 2: "+hourOfDay+" : "+minute);
            startAlamr2(c,hora2,minuto2);



        }
    }


}//fin de la clase
