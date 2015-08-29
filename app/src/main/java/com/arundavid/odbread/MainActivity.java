package com.arundavid.odbread;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.ObdMultiCommand;
import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.control.DistanceTraveledSinceCodesClearedCommand;
import com.github.pires.obd.commands.control.DistanceTraveledWithMILOnCommand;
import com.github.pires.obd.commands.engine.AbsoluteLoadCommand;
import com.github.pires.obd.commands.engine.LoadCommand;
import com.github.pires.obd.commands.engine.OilTempCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.engine.RuntimeCommand;
import com.github.pires.obd.commands.fuel.FuelLevelCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.enums.ObdProtocols;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


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
        if (pairedDevices.size() > 0) {
            for (Object device : pairedDevices) {
                BluetoothDevice bdevice = (BluetoothDevice) device;
                deviceStrs.add(bdevice.getName() + "\n" + bdevice.getAddress());
                devices.add(bdevice.getAddress());
            }
        }

        // show list
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_singlechoice,
                deviceStrs.toArray(new String[deviceStrs.size()]));

        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    //Log.d("OBDHACK", "init-------------: ");
                    dialog.dismiss();
                    int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    String deviceAddress = devices.get(position).toString();
                    // TODO save deviceAddress
                    //--------------------------------
                    //Log.d("OBDHACK", "22222222222222222-------------: ");
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
                    try {
                        //Log.d("OBDHACK", "3333333333333333333-------------: ");
                        new EchoOffCommand().run(socket.getInputStream(), socket.getOutputStream());
                        Log.d("OBDHACK", "7-------------: ");
                        new LineFeedOffCommand().run(socket.getInputStream(), socket.getOutputStream());
                        Log.d("OBDHACK", "8-------------: ");
                        new TimeoutCommand(10).run(socket.getInputStream(), socket.getOutputStream());
                        Log.d("OBDHACK", "9-------------: ");
                        new SelectProtocolCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("OBDHACK", "ER: " + e);
                    }
                    //-------------------------------------------------
                    //Log.d("OBDHACK", "4444444444444444444-------------: ");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("OBDHACK", "ER: " + e);
                }
                try {
                    LoadCommand loadCommand = null;
                    AbsoluteLoadCommand absLoadCommand = null;
                    OilTempCommand oilTempCommand = null;
                    ObdCommand coolantTempCommand = null;
                    RPMCommand rpmCommand = null;
                    SpeedCommand speedCommand = null;
                    DistanceTraveledWithMILOnCommand distanceTraveledOn = null;
                    RuntimeCommand runtimeCommand = null;
                    FuelLevelCommand fuelLevel = null;
                    ObdCommand egrErrorCommand = null;
                    ObdCommand commandedEgrErrorCommand = null;
                    try {
                        loadCommand = new LoadCommand();//04#
                        absLoadCommand = new AbsoluteLoadCommand();//43#

                        oilTempCommand = new OilTempCommand();
                        coolantTempCommand = new ObdCommand("01 05") {
                            @Override
                            protected void performCalculations() {
                            }

                            @Override
                            public String getFormattedResult() {
                                return null;
                            }

                            @Override
                            public String getCalculatedResult() {
                                return null;
                            }

                            @Override
                            public String getName() {
                                return null;
                            }
                        };

                        rpmCommand = new RPMCommand();

                        speedCommand = new SpeedCommand();

                        distanceTraveledOn = new DistanceTraveledWithMILOnCommand();
                        runtimeCommand = new RuntimeCommand();

                        fuelLevel = new FuelLevelCommand();

                        egrErrorCommand = new ObdCommand("01 2D") {
                            @Override
                            protected void performCalculations() {
                            }

                            @Override
                            public String getFormattedResult() {
                                return null;
                            }

                            @Override
                            public String getCalculatedResult() {
                                return null;
                            }

                            @Override
                            public String getName() {
                                return null;
                            }
                        };

                        commandedEgrErrorCommand = new ObdCommand("01 2D") {
                            @Override
                            protected void performCalculations() {
                            }

                            @Override
                            public String getFormattedResult() {
                                return null;
                            }

                            @Override
                            public String getCalculatedResult() {
                                return null;
                            }

                            @Override
                            public String getName() {
                                return null;
                            }
                        };
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("OBDHACK", "ER: " + e);
                    }
                    //ObdMultiCommand obdCommand=new ObdMultiCommand();

                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            loadCommand.run(socket.getInputStream(), socket.getOutputStream());

                            Log.d("OBDHACK_RES", "Load: " + loadCommand.getResult());


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E1: " + e);
                        }
                        try {
                            absLoadCommand.run(socket.getInputStream(), socket.getOutputStream());
                            Log.d("OBDHACK_RES", "Abs Load: " + absLoadCommand.getResult());

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E2: " + e);
                        }

                        try {
                            oilTempCommand.run(socket.getInputStream(), socket.getOutputStream());
                            Log.d("OBDHACK_RES", "Oil temp: " + oilTempCommand.getResult());

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E3: " + e);
                        }
                        try {
                            coolantTempCommand.run(socket.getInputStream(), socket.getOutputStream());

                            Log.d("OBDHACK_RES", "Coolant temp: " + coolantTempCommand.getResult());

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E4: " + e);
                        }
                        try {
                            rpmCommand.run(socket.getInputStream(), socket.getOutputStream());
                            Log.d("OBDHACK_RES", "Rpm: " + rpmCommand.getResult());

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E5: " + e);
                        }
                        try {
                            speedCommand.run(socket.getInputStream(), socket.getOutputStream());

                            Log.d("OBDHACK_RES", "Speed: " + speedCommand.getResult());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E6: " + e);
                        }

                        try {
                            distanceTraveledOn.run(socket.getInputStream(), socket.getOutputStream());
                            Log.d("OBDHACK_RES", "Distance since on: " + distanceTraveledOn.getResult());


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E7: " + e);
                        }

                        try {
                            runtimeCommand.run(socket.getInputStream(), socket.getOutputStream());

                            Log.d("OBDHACK_RES", "Runtime since start: " + runtimeCommand.getResult());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E8: " + e);
                        }

                        try {
                            fuelLevel.run(socket.getInputStream(), socket.getOutputStream());

                            Log.d("OBDHACK_RES", "Fuel level: " + fuelLevel.getResult());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E9: " + e);
                        }

                        try {
                            egrErrorCommand.run(socket.getInputStream(), socket.getOutputStream());
                            Log.d("OBDHACK_RES", "Egr err: " + egrErrorCommand.getResult());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E10: " + e);
                        }
                        try {
                            commandedEgrErrorCommand.run(socket.getInputStream(), socket.getOutputStream());

                            Log.d("OBDHACK_RES", "Commanded egr err: " + commandedEgrErrorCommand.getResult());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("OBDHACK", "E11: " + e);
                        }
                        //obdCommand.run(socket.getInputStream(), socket.getOutputStream());


                        //Log.d("OBDHACK_RES", "Obd data: " + obdCommand.getResult());

                    }
                } catch (Exception e) {
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
