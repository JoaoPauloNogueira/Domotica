package com.example.joaopaulo.domoticaapp;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListaDispositivosActivity extends AppCompatActivity {

    public static final String ENDERECO_NOVO_DISPOSITIVO = "enderecoNovoDispositivo";
    private static final int HABILITA_BLUETOOTH = 234;
    public static final int REQUEST_CODE = 1;

    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private ArrayAdapter<String> pairedDevicesArrayAdapter;

    @BindView(R.id.btn_scan)
    Button btScan;
    @BindView(R.id.txt_dispositivos_pareados)
    TextView txtDispositivosPareados;
    @BindView(R.id.lv_dispositivos_pareados)
    ListView lvDispositivosPareados;
    @BindView(R.id.txt_novos_dispositivos)
    TextView txtNovosDispositivos;
    @BindView(R.id.lv_novos_dispositivos)
    ListView lvNovosDispositivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);

        setContentView(R.layout.activity_lista_dispositivos);

        setResult(Activity.RESULT_CANCELED);
        ButterKnife.bind(this);

        inicializaViews();

        inicializaRegisterReceivers();

        verificaPermissoes();
    }

    private void verificaPermissoes() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE);
            }
        } else {
            configBluetooth();
        }
    }

    private void inicializaRegisterReceivers() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        this.registerReceiver(mReceiver, filter);
    }

    private void inicializaViews() {
        pairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.nome_dispositivo);
        mNewDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.nome_dispositivo);

        lvDispositivosPareados.setAdapter(pairedDevicesArrayAdapter);
        lvDispositivosPareados.setOnItemClickListener(mDeviceClickListener);

        lvNovosDispositivos.setAdapter(mNewDevicesArrayAdapter);
        lvNovosDispositivos.setOnItemClickListener(mDeviceClickListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_CODE:

                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED){

                    configBluetooth();
                }

                break;
        }
    }

    private void configBluetooth() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBtAdapter == null) {

            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();

        } else if (!mBtAdapter.isEnabled()) {

            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, HABILITA_BLUETOOTH);
        }

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            txtDispositivosPareados.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.titulo_nao_encontrado).toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }

    @OnClick(R.id.btn_scan)
    public void btnScanOnClick(View view) {

        mNewDevicesArrayAdapter.clear();
        doDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBtAdapter != null) {

            mBtAdapter.cancelDiscovery();
        }

        this.unregisterReceiver(mReceiver);
    }

    private void doDiscovery() {

        setTitle(R.string.titulo_escaneando);

        txtNovosDispositivos.setVisibility(View.VISIBLE);

        if (mBtAdapter.isDiscovering()) {

            mBtAdapter.cancelDiscovery();
        }

        mBtAdapter.startDiscovery();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();

            String address = "";

            if (info.compareTo(getResources().getText(R.string.titulo_nao_encontrado).toString()) != 0) {
                address = info.substring(info.length() - 17);
            }

            Intent intent = new Intent();
            intent.putExtra(ENDERECO_NOVO_DISPOSITIVO, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.compareTo(action) == 0) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Log.i("APP", "Encontrado [" + device.getName() + ", " + device.getAddress() + "]");

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.compareTo(action) == 0) {

                setTitle(R.string.titulo_selecionado);

                Log.i("APP", "Processo de discivery terminado");

                if (mNewDevicesArrayAdapter.getCount() == 0) {

                    String noDevices = getResources().getText(R.string.titulo_nao_encontrado).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.compareTo(action) == 0) {

                Log.i("App", "Iniciado o processo!");
            }
        }
    };
}
