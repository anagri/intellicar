package com.arundavid.odbread;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.enums.ObdProtocols;


public class MainActivity extends ActionBarActivity {
    BluetoothSocket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList deviceStrs = new ArrayList();
        final ArrayList devices = new ArrayList();

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0)
        {
            for (Object device : pairedDevices)
            {
                BluetoothDevice bdevice= (BluetoothDevice)device;
                deviceStrs.add(bdevice.getName() + "\n" + bdevice.getAddress());
                devices.add(bdevice.getAddress());
            }
        }

        // show list
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_singlechoice,
                deviceStrs.toArray(new String[deviceStrs.size()]));

        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                try {
                    Log.d("OBDHACK", "init-------------: ");
                    dialog.dismiss();
                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                String deviceAddress = devices.get(position).toString();
                // TODO save deviceAddress
                //--------------------------------
                Log.d("OBDHACK", "22222222222222222-------------: ");
                Log.d("OBDHACK", "1-------------: ");
                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                Log.d("OBDHACK", "2-------------: ");
                BluetoothDevice device = btAdapter.getRemoteDevice(deviceAddress);
                Log.d("OBDHACK", "3-------------: ");
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//AA:BB:CC:11:22:33");

                Log.d("OBDHACK", "4-------------: ");

                    socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                    Log.d("OBDHACK", "5-------------: ");
                    socket.connect();
                    Log.d("OBDHACK", "6-------------: ");
                //-------------------------------------
                    try{
                        Log.d("OBDHACK", "3333333333333333333-------------: ");
                new EchoOffCommand().run(socket.getInputStream(), socket.getOutputStream());
                    Log.d("OBDHACK", "7-------------: ");
                new LineFeedOffCommand().run(socket.getInputStream(), socket.getOutputStream());
                    Log.d("OBDHACK", "8-------------: ");
                new TimeoutCommand(10).run(socket.getInputStream(), socket.getOutputStream());
                    Log.d("OBDHACK", "9-------------: ");
                new SelectProtocolCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("OBDHACK", "E: " + e);
                    }
                //-------------------------------------------------
                Log.d("OBDHACK", "4444444444444444444-------------: ");
                //EngineRPMCommand engineRpmCommand = new EngineRPMObdCommand();
                SpeedCommand speedCommand = new SpeedCommand();
                while (!Thread.currentThread().isInterrupted())
                {
                    //engineRpmCommand.run(sock.getInputStream(), sock.getOutputStream());
                    try{
                    speedCommand.run(socket.getInputStream(), socket.getOutputStream());
                    // TODO handle commands result
                    //Log.d(TAG, "RPM: " + engineRpmCommand.getFormattedResult());
                    Log.d("OBDHACK", "Speed: " + speedCommand.getFormattedResult());
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("OBDHACK", "E: " + e);
                    }
                }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("OBDHACK", "E: " + e);
                }
                //---------------------------------------------
            }
        });

        alertDialog.setTitle("Choose Bluetooth device");
        alertDialog.show();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
