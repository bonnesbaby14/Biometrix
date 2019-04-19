package bonzai.com.biometrix;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.UUID;


public class Mediciones extends Fragment {
    private ConnectedThread MyConexionBT;
    Helper helper;
    Handler bluetoothIn;
    View view;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = null;
    ProgressBar progressBar;
    TextView FREQ,PRES2,PRES,S02,TEMP;
    String ss02,spres1,spres2,sfreq,stemp;
    Button REGISTRAR;
    String getAddress;


    public Mediciones() {
        // Required empty public constructor
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //crea un conexion de salida segura para el dispositivo
        //usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }
    @Override
    public void onResume() {
        super.onResume();
        boolean falla=false;
        Intent back;

        Bundle bundle=this.getArguments();
        getAddress=bundle.getString("mac");

        BluetoothDevice device = btAdapter.getRemoteDevice(getAddress);

        try
        {
            btSocket =createBluetoothSocket(device);
        } catch (IOException e) {
            falla=true;
            Snackbar.make(view, "La conexion falló, intenta de nuevo", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Fragment fragment=new Bluetooth();

            getFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
            return;
        }
        // Establece la conexión con el socket Bluetooth.
        try
        {
            btSocket.connect();
            falla=false;
        } catch (IOException e) {
            Snackbar.make(view, "La conexion falló, intenta de nuevo", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Fragment fragment=new Bluetooth();

            getFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
            //salir del fragment


            try {
                btSocket.close();
            } catch (IOException e2) {
                Snackbar.make(view, "La conexion falló, intenta de nuevo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Fragment fragment2=new Bluetooth();

                getFragmentManager().beginTransaction().replace(R.id.content_main,fragment2).commit();
                return;
            }
        }
        if(!falla) {
            MyConexionBT = new ConnectedThread(btSocket);
            MyConexionBT.start();
        }


    }

    public static Mediciones newInstance(String param1, String param2) {
        Mediciones fragment = new Mediciones();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_mediciones, container, false);


        Bluetooth.progressDialog.dismiss();

        FREQ=view.findViewById(R.id.freq);
        PRES2=view.findViewById(R.id.press2);
        PRES=view.findViewById(R.id.press);
        S02=view.findViewById(R.id.s02);
        TEMP=view.findViewById(R.id.temp);
        REGISTRAR=view.findViewById(R.id.btnRegistrar);
        REGISTRAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper=new Helper(getContext(),"DB",null,1);
                SQLiteDatabase DB=helper.getWritableDatabase();
                Calendar calendar=Calendar.getInstance();
                String fecha=calendar.getTime().toString();
                try {

                    DB.execSQL("INSERT INTO REGISTROS VALUES(NULL,'" + fecha + "','" + ss02 + "','" + sfreq + "','" + spres1 + "','" + spres2 + "','" + stemp + "');");
                    //DB.execSQL("INSERT INTO REGISTROS VALUES(NULL,HOY,123,1234,12345,123,1222)");
                    Snackbar.make(view, "Se registro correctamente", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }catch (Exception e){
                    Toast.makeText(getContext(),"no se pudo registrar",Toast.LENGTH_LONG).show();

                }




            }
        });

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    DataStringIN.append(readMessage);

                    int endOfLineIndex = DataStringIN.indexOf("#");

                    if (endOfLineIndex > 0) {
                        String dataInPrint = DataStringIN.substring(0, endOfLineIndex);
                        int x=0;
                        for( ; x<dataInPrint.length();x++){
                            if(dataInPrint.charAt(x)=='$'){
                                ss02=dataInPrint.substring(0,x);
                                S02.setText(ss02);
                                break;
                            }
                        }
                        int y=x+1;
                        for( ; y<dataInPrint.length();y++){
                            if(dataInPrint.charAt(y)=='$'){
                                sfreq=dataInPrint.substring(x+1,y);
                                FREQ.setText(sfreq);
                                break;

                            }
                        }
                        int z=y+1;
                        for( ; z<dataInPrint.length();z++){
                            if(dataInPrint.charAt(z)=='$'){
                                spres1=dataInPrint.substring(y+1,z);
                                PRES.setText(spres1);
                                break;

                            }
                        }
                        int w=z+1;
                        for( ; w<dataInPrint.length();w++){
                            if(dataInPrint.charAt(w)=='$'){
                                spres2=dataInPrint.substring(z+1,w);
                                PRES2.setText(spres2);
                                break;

                            }
                        }
                        int q=w+1;
                        for( ; q<dataInPrint.length();q++){
                            if(dataInPrint.charAt(q)=='$'){
                                stemp=dataInPrint.substring(w+1,q);
                                TEMP.setText(stemp);
                                break;

                            }
                        }

                        DataStringIN.delete(0, DataStringIN.length());
                    }
                }
            }
        };

        btAdapter=BluetoothAdapter.getDefaultAdapter();








        return view;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //iuncio de l aclase secundaria
    private class ConnectedThread extends Thread
    {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            byte[] buffer = new byte[256];
            int bytes;

            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    // Envia los datos obtenidos hacia el evento via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //Envio de trama
        public void write(String input)
        {
            try {
                mmOutStream.write(input.getBytes());
            }
            catch (IOException e)
            {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();


            }
        }
    }//fin de la clase secundaria


}//finde la clase principal

