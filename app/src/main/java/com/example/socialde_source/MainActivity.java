package com.example.socialde_source;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.net.*;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RecieveMessageTask((TextView)findViewById(R.id.tvChat)) .execute();

    }
    public void onSend(android.view.View view) {
        EditText editText = findViewById(R.id.Message); //Groß=Klasse, klein=Datentyp
        String message = editText.getText().toString();
        editText.setText("");
        Log.d("log",message);

        EditText editText1 = findViewById(R.id.Input);
        String localip = editText1.getText().toString();
        Log.d( "log",localip);
        new SendMessageTask(localip, message).start();
    }
}

class SendMessageTask extends Thread {

    String localip;
    String message;

    public SendMessageTask(String localip, String message) {

        this.localip = localip;
        this.message = message;
    }

    public void run() {
        try {
            InetAddress address = InetAddress.getByName(localip);
            byte[] b = message.getBytes();
            DatagramPacket packet = new DatagramPacket(
                    b, b.length, address, 7004
            );
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);
            Log.d("log","Nachricht verschickt!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class RecieveMessageTask extends AsyncTask<String, String, Long> {

    TextView chat;

    public RecieveMessageTask(TextView chat) {
        this.chat = chat;
    }

    protected Long doInBackground(String... urls) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(7004);
            byte[] receiveData = new byte[1024];

            while(true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                Log.d( "log","Waiting vor Message");
                serverSocket.receive(receivePacket);
                byte[] slice = Arrays.copyOfRange(receivePacket.getData(), 0, receivePacket.getLength());
                String sentence = receivePacket.getAddress().toString().substring(1) + ": " + new String(slice) + System.getProperty("line.separator");
                publishProgress(sentence);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    protected void onProgressUpdate(String... message) {
        Log.d("log", message[0]);
        chat.append(message[0]);
    }

    protected void onPostExecute(Long result) {
        // showDialog("Downloaded " + result + " bytes");
    }
}