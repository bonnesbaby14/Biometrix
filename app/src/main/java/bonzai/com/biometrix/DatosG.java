package bonzai.com.biometrix;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class DatosG extends Fragment {
    RecyclerView recyclerView;
    ArrayList arrayList;
    Helper helper;
    Dato dato;



    public DatosG() {
        // Required empty public constructor
    }

    public static DatosG newInstance(String param1, String param2) {
        DatosG fragment = new DatosG();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
View vista=inflater.inflate(R.layout.fragment_datos_g, container, false);


        recyclerView=vista.findViewById(R.id.rvDatos);
        arrayList=new ArrayList<>();
        helper=new Helper(getContext(),"DB",null,1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        conexion();
        MyAdapter myAdapter=new MyAdapter(arrayList);
        recyclerView.setAdapter(myAdapter);

        return vista;
    }


    void conexion() {
        SQLiteDatabase DB = helper.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM REGISTROS;", null);
        int count = cursor.getCount();

        while (cursor.moveToNext()) {
            dato = new Dato(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            arrayList.add(dato);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
