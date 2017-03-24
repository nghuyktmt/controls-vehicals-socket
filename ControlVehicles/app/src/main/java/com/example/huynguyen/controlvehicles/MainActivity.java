package com.example.huynguyen.controlvehicles;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements ClientSocket.ServerListener {

    FrameLayout frame;
    EditText edtIp;
    EditText edtPort;
    TextView txtReceive;

    private SeekBar volumeControl = null;

    private boolean connected = false;
    private ClientSocket clientSocket;
    private Settings settings;

    final String[] messenge = new String[1];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set cho màn hình nằm ngang
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        edtIp = (EditText) findViewById(R.id.edtIp);
        edtPort = (EditText) findViewById(R.id.edtPort);

        //Khai báo frame layout và ẩn frame này khi khởi chạy ứng dụng
        frame = (FrameLayout) findViewById(R.id.frame);
        frame.setVisibility(View.INVISIBLE);

        settings = new Settings(this);
        //gọi hàm xử lý các sự kiện
        addEvents();

    }

    private void addEvents() {
        //khởi tạo seekbar
        volumeControl = (SeekBar) findViewById(R.id.volume_bar);
        //xử lý sự kiện trên seekbar
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressChanged = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                clientSocket.sendMessenge(String.valueOf(progressChanged));
                Toast.makeText(getApplicationContext(), "Seekbar progess:" + progressChanged, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //sự kiện các hàm onclick của imagebutton
    public void UP(View view) {
        messenge[0]="U";
        clientSocket.sendMessenge(messenge[0]);
    }

    public void DOWN(View view) {
        messenge[0]="D";
        clientSocket.sendMessenge(messenge[0]);
    }

    public void LEFT(View view) {
        messenge[0]="L";
        clientSocket.sendMessenge(messenge[0]);
    }

    public void RIGHT(View view) {
        messenge[0]="R";
        clientSocket.sendMessenge(messenge[0]);
    }

    //hàm onclick cho button OpenSocket
    public void Start(View view) {

        if(connected){
            connectStatusChange(true);
        }

        if (!connected) {
            String ip = edtIp.getText().toString();
            int port = Integer.valueOf(edtPort.getText().toString());
            clientSocket = new ClientSocket(ip, port);
            clientSocket.setServerListener(MainActivity.this);
            clientSocket.connect();

            Toast.makeText(this,"Connected",Toast.LENGTH_SHORT).show();
            frame.setVisibility(View.VISIBLE);
        } else {
            clientSocket.disconnect();
            Toast.makeText(this,"Disconnected",Toast.LENGTH_SHORT).show();
        }
    }

    //hàm xử lý nút nhấn Close Socket
    public void onCloseSocket(View view)
    {
        clientSocket.disconnect();
        frame.setVisibility(View.INVISIBLE);
    }

    public void connectStatusChange(boolean status) {
        this.connected = status;
    }

    //Hàm xử lý khi nhận 1 messenge từ server
    public void newMessengeFromServer(String messenge) {
        Toast.makeText(this, "receive: " + messenge, Toast.LENGTH_SHORT).show();
        txtReceive.setText(messenge);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(this.connected){
            clientSocket.disconnect();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!connected){
            clientSocket.connect();
        }
    }
}