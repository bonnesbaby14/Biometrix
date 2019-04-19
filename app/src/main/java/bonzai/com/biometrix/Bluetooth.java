package bonzai.com.biometrix;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;


public class Bluetooth extends Fragment {
    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> paired;
    ListView listView;
    static public ProgressDialog progressDialog;

    public Bluetooth() {
        // Required empty public constructor
    }


    public static Bluetooth newInstance(String param1, String param2) {
        Bluetooth fragment = new Bluetooth();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        paired = bluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();
        for (BluetoothDevice bt : paired) {
            list.add(bt.getName() + "\n" + bt.getAddress());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);


        AdapterView.OnItemClickListener mDeviceSelected = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);
                Fragment fragment=new Mediciones();

                progressDialog=new ProgressDialog(getContext());
                progressDialog.setMessage("Conectando...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Bundle bundle=new Bundle();
                bundle.putString("mac",address);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();




            }
        };

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(mDeviceSelected);
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view=inflater.inflate(R.layout.fragment_bluetooth, container, false);
        listView = view.findViewById(R.id.listaB);

        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
