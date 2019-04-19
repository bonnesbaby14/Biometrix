package bonzai.com.biometrix;

import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Alarma.OnFragmentInteractionListener,Home.OnFragmentInteractionListener {
    BluetoothAdapter bluetoothAdapter;
    OnBoot onBoot;
    IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
onBoot=new OnBoot();
intentFilter=new IntentFilter();
intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment fragment=new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment=null;

        int id = item.getItemId();

        if (id == R.id.nav_datos) {
            fragment=new DatosG();

            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
        } else if (id == R.id.nav_alarmas) {
            fragment=new Alarma();

            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();

        } else if (id == R.id.nav_mediciones) {
            fragment=new Bluetooth();

            if(bluetoothAdapter==null){


            }else {
                Intent enableBluetooth =new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth,1);
            }
            if(bluetoothAdapter.isEnabled()) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();


            }else {
                Toast.makeText(getApplicationContext(),"Activa el Bluetooth",Toast.LENGTH_LONG).show();

            }



       }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(onBoot,intentFilter);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
