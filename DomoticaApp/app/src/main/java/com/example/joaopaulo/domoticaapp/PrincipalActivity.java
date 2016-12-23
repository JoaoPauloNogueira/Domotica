package com.example.joaopaulo.domoticaapp;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.joaopaulo.domoticaapp.bluetooth.BluetoothService;
import com.example.joaopaulo.domoticaapp.bluetooth.Constants;
import com.example.joaopaulo.domoticaapp.data.Comodo;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.joaopaulo.domoticaapp.R.drawable.lamp_off_icon2;
import static com.example.joaopaulo.domoticaapp.R.drawable.lamp_on_icon2;

public class PrincipalActivity extends AppCompatActivity {

    private static final int REQUEST_CONNECT = 123;
    private static final int REQUEST_ENABLE_BT = 2345;

    @BindView(R.id.tb_navigation)
    Toolbar tbNavigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout dLayout;
    @BindView(R.id.nvv_principal)
    NavigationView nvPrincipal;

    @BindView(R.id.tb_vermelho)
    ToggleButton tbLedVermelho;
    @BindView(R.id.tb_amarelo)
    ToggleButton tbLedAmarelo;
    @BindView(R.id.tb_verde)
    ToggleButton tbLedVerde;

    @BindView(R.id.txt_temperatura)
    TextView tvTemperatura;

    private BluetoothService mService = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private String mConnectedDeviceName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ButterKnife.bind(this);

        inicializaViews();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void inicializaViews() {
        setSupportActionBar(tbNavigation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nvPrincipal.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.mit_listar:
                        break;
                    case R.id.mit_adicionar:
                        break;
                }

                dLayout.closeDrawers();

                return true;
            }
        });
        dLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(View drawerView) {

                getSupportActionBar().setHomeButtonEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                getSupportActionBar().setHomeButtonEnabled(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        tbLedVermelho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tbLedVermelho.setBackground(getResources().getDrawable(lamp_on_icon2));
                    sendMessage("VO");
                } else {
                    tbLedVermelho.setBackground(getResources().getDrawable(lamp_off_icon2));
                    sendMessage("VF");
                }
            }
        });
        tbLedAmarelo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tbLedAmarelo.setBackground(getResources().getDrawable(lamp_on_icon2));
                    sendMessage("AO");
                } else {
                    tbLedAmarelo.setBackground(getResources().getDrawable(lamp_off_icon2));
                    sendMessage("AF");
                }
            }
        });
        tbLedVerde.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tbLedVerde.setBackground(getResources().getDrawable(lamp_on_icon2));
                    sendMessage("DO");
                } else {
                    tbLedVerde.setBackground(getResources().getDrawable(lamp_off_icon2));
                    sendMessage("DF");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                dLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.bluetooth_search:
                Intent serverIntent = new Intent(this, ListaDispositivosActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        inicializaBluetooth();
    }

    private void inicializaBluetooth() {
        if (mBluetoothAdapter != null) {

            if (!mBluetoothAdapter.isEnabled()) {

                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

            } else if (mService == null) {

                setupBluetoothService();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mService != null) {

            mService.stop();
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        if (mService != null) {

            if (mService.getState() == BluetoothService.STATE_NONE) {

                mService.start();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CONNECT:
                //-- When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {

                    connectDevice(data);
                }
                break;
        }
    }

    private void connectDevice(Intent data) {

        String address = data.getExtras().getString(ListaDispositivosActivity.ENDERECO_NOVO_DISPOSITIVO);

        if (address.compareTo("") != 0) {

            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

            mService.connect(device);

        } else {

            Toast.makeText(this, "Não foi possível se conectar a nenhum dispositivo.", Toast.LENGTH_LONG).show();
        }
    }
    private void setupBluetoothService() {

        mService = new BluetoothService(mHandler);
    }

    private void sendMessage(String message) {

        if (mService == null) {

            inicializaBluetooth();

        } else {

            // Check that we're actually connected before trying anything
            if (mService.getState() != BluetoothService.STATE_CONNECTED) {
                Toast.makeText(this, "Não conectado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check that there's actually something to send
            if (message.length() > 0) {
                // Get the message bytes and tell the BluetoothService to write
                byte[] send = message.getBytes();
                mService.write(send);
            }
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus("Conectado a " + mConnectedDeviceName);

                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus("Conectando");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            setStatus("Não conectado");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);

                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Double temperatura = Double.valueOf(readMessage) /100;
                    tvTemperatura.setText(temperatura.toString());

                    break;
                case Constants.MESSAGE_DEVICE_NAME:

                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);

                        Toast.makeText(PrincipalActivity.this, "Conectado a "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                    break;
                case Constants.MESSAGE_TOAST:

                        Toast.makeText(PrincipalActivity.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();

                    break;
            }
        }

    };
    private void setStatus(CharSequence subTitle) {

        final ActionBar actionBar = getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    public void detalhaComodo(Comodo comodo) {
/*
        filmesDB.adicionarAcessoAoFilme(comodo, System.currentTimeMillis());

        Intent intFilme = new Intent(PrincipalActivity.this, FilmeActivity.class);
        intFilme.putExtra("filme", filme);
        startActivity(intFilme);*/
    }
}
