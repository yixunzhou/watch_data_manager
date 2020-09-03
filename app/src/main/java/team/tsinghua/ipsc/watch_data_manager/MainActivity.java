package team.tsinghua.ipsc.watch_data_manager;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;


public class MainActivity extends AppCompatActivity {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static int REQUEST_PERMISSION_CODE = 1;

    private String serverAddr;
    private int PORT;
    private final String root_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    private String src_dir;
    private String tar_dir;
    private String src_file_tar_dir;
    private String tar_file_tar_dir;
    private String src_file_tar_dir_user;
    private String tar_file_tar_dir_user;
    private String mode;
    private String remote_path;
    private Button btnCheck, btnCheck3, btnLogin, btnSend, btnDecode, btnExit;
    private TextView hint;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private String device_sc;
    private String watch_sc;
    private String lower_rate, upper_rate;
    private String[] user = new String[50];
    private String[] watch_scs  = new String[]{"-------------------请选择腕表编号-------------------", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};;
    private String user_id, group;
    private String[] fnames = new String[50];
    private EditText user_input;
    private final String remoteUserName = "ipsc";
    private final String remotePassword = "ipsc";
    private final String tag = "Debug";
    private final String underline = "_";
    private final String colon = ":";


    @SuppressLint(value = "ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
        }
        final String settingFilePath = root_dir + "watch_data/settings.txt";
        final String[] settings = readSettings(settingFilePath);
        serverAddr = settings[0].split(colon)[1];
        PORT = Integer.parseInt(settings[1].split(colon)[1]);
        device_sc = settings[2].split(colon)[1];
        remote_path = settings[3].split(colon)[1];
        src_dir = root_dir + settings[4].split(colon)[1];
        tar_dir = root_dir + settings[5].split(colon)[1];
        lower_rate = settings[6].split(colon)[1];
        upper_rate = settings[7].split(colon)[1];
        watch_sc = settings[8].split(colon)[1];
        src_file_tar_dir = tar_dir + "origin/";
        tar_file_tar_dir = tar_dir + "decoded/";

//        if (watch_sc.equals("1")){
//            watch_scs = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("2")){
//            watch_scs = new String[]{"2", "1", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("3")){
//            watch_scs = new String[]{"3", "1", "2", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("4")){
//            watch_scs = new String[]{"4", "1", "2", "3", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("5")){
//            watch_scs = new String[]{"5", "1", "2", "3", "4", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("6")){
//            watch_scs = new String[]{"6", "1", "2", "3", "4", "5", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("7")){
//            watch_scs = new String[]{"7", "1", "2", "3", "4", "5", "6", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("8")){
//            watch_scs = new String[]{"8", "1", "2", "3", "4", "5", "6", "7", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("9")){
//            watch_scs = new String[]{"9", "1", "2", "3", "4", "5", "6", "7", "8", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("10")){
//            watch_scs = new String[]{"10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("11")){
//            watch_scs = new String[]{"11", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("12")){
//            watch_scs = new String[]{"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "13", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("13")){
//            watch_scs = new String[]{"13", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "14", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("14")){
//            watch_scs = new String[]{"14", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "15", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("15")){
//            watch_scs = new String[]{"15", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "16", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("16")){
//            watch_scs = new String[]{"16", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "17", "18", "19", "20"};
//        } else if (watch_sc.equals("17")){
//            watch_scs = new String[]{"17", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "18", "19", "20"};
//        } else if (watch_scs.equals("18")){
//            watch_scs = new String[]{"18", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "19", "20"};
//        } else if (watch_sc.equals("19")){
//            watch_scs = new String[]{"19", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "20"};
//        } else if (watch_sc.equals("20")){
//            watch_scs = new String[]{"20", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};
//        }


        Log.d(tag, "server addr is " + serverAddr);
        Log.d(tag, "server port is " + PORT);
        Log.d(tag, "device number is " + device_sc);
        Log.d(tag, "remote path is " + remote_path);
        Log.d(tag, "source dir is " + src_dir);
        Log.d(tag, "target dir is " + tar_dir);
        Log.d(tag, "lower heart rate is " + lower_rate);
        Log.d(tag, "upper heart rate is " + upper_rate);
        Log.d(tag, "target dir of source files is " + src_file_tar_dir);
        Log.d(tag, "target dir of decoded files is " + tar_file_tar_dir);
        Log.d(tag, "watch number is " + watch_sc);


        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        user_input = findViewById(R.id.user);
        btnLogin = findViewById(R.id.login);
        btnDecode = findViewById(R.id.decode_save);
        btnCheck = findViewById(R.id.check);
        btnCheck3 = findViewById(R.id.check3);
        btnSend = findViewById(R.id.upload);
        hint = findViewById(R.id.hint);
        btnExit = findViewById(R.id.exit);

        hint.setText("请在下方输入用户id和组号，用下划线分隔(你的默认腕表编号为 " + watch_sc + " ）");
        hint.setTextColor(Color.BLACK);

        btnDecode.setVisibility(View.INVISIBLE);
        btnCheck.setVisibility(View.INVISIBLE);
        btnCheck3.setVisibility(View.INVISIBLE);
        btnSend.setVisibility(View.INVISIBLE);
        btnExit.setVisibility(View.INVISIBLE);

        spinner = findViewById(R.id.watch_sc_spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, watch_scs);
        spinnerAdapter.setDropDownViewResource(R.layout.list_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setPopupBackgroundResource(R.drawable.view_radius);
        spinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0){
                    watch_sc = String.valueOf(i);
                }

                Log.d(tag, "new watch number is " + watch_sc);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(tag, "new watch number is still " + watch_sc);
            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id = user_input.getText().toString().split(underline)[0];
                group = user_input.getText().toString().split(underline)[1];
                Log.d(tag, "user id is " + user_id);
                Log.d(tag, "group is " + group);
                btnLogin.setText("用户 " + watch_sc + underline + user_input.getText().toString() + " 已登录");
                Toast t1 = Toast.makeText(MainActivity.this, "user " + watch_sc + underline + user_input.getText().toString() + " login successfully.", Toast.LENGTH_SHORT);
                t1.show();
                Log.d(tag, "user " + watch_sc + underline + user_input.getText().toString() + " login successfully.");
                String[] settingsTemp = new String[]{settings[0], settings[1], settings[2], settings[3], settings[4], settings[5], settings[6], settings[7], settings[8].split(colon)[0] + colon + watch_sc};


                writeToSettings(settingFilePath, settingsTemp);
                hint.setVisibility(View.INVISIBLE);
                user_input.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                btnDecode.setVisibility(View.VISIBLE);
                btnCheck.setVisibility(View.VISIBLE);
                btnCheck3.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                btnExit.setVisibility(View.VISIBLE);
            }
        });


        btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                boolean flag2 = true;
                long temp = 0;
                String dir;
                try {
                    dir = new File(src_dir).list()[0];
                    for (String dirs:new File(src_dir).list()){
                        if (!dirs.equals("upload")){
                            long t = Long.parseLong(dirs.split(underline)[2]);
                            if (t > temp){
                                temp = t;
                                dir = dirs;
                            }
                        }
                    }
                    Log.d(tag, "valid source dir is " + src_dir + dir + "/");
                    for (String files:new File(src_dir + dir + "/").list()){

                        if (files.endsWith("_normal.txt")){
                            flag2 = false;
                            Log.d(tag, "file path is " + src_dir + dir + "/" + files);
                            String src_file = src_dir + dir + "/" + files;
                            String[] src_file_arr = {src_file};
                            File f_src_file_tar_dir = new File(src_file_tar_dir);
                            File f_tar_file_tar_dir = new File(tar_file_tar_dir);
                            if (!f_src_file_tar_dir.exists()) {
                                f_src_file_tar_dir.mkdir();
                                Log.d(tag, "dir " + src_file_tar_dir + " created.");
                            }
                            if (!f_tar_file_tar_dir.exists()) {
                                f_tar_file_tar_dir.mkdir();
                                Log.d(tag, "dir " + tar_file_tar_dir + " created.");
                            }

                            user[0] = files.split(underline)[5];
                            user[1] = watch_sc;
                            user[2] = user_id;
                            user[3] = group;
                            user[4] = device_sc;
                            user[5] = lower_rate + "-" + upper_rate;
                            user[6] = "00";

                            fnames[0] = user[0] + underline + user[1] + underline + user[2] + "_" + user[3] + "_" + user[4] + underline + user[5] + "_PPG1_" + user[6] + ".txt";
                            fnames[1] = user[0] + underline + user[1] + underline + user[2] + "_" + user[3] + "_" + user[4] + underline + user[5] + "_PPG2_" + user[6] + ".txt";
                            fnames[2] = user[0] + underline + user[1] + underline + user[2] + "_" + user[3] + "_" + user[4] + underline + user[5] + "_PPG3_" + user[6] + ".txt";
                            fnames[3] = user[0] + underline + user[1] + underline + user[2] + "_" + user[3] + "_" + user[4] + underline + user[5] + "_PPG4_" + user[6] + ".txt";
                            fnames[4] = user[0] + underline + user[1] + underline + user[2] + "_" + user[3] + "_" + user[4] + underline + user[5] + "_accx_" + user[6] + ".txt";
                            fnames[5] = user[0] + underline + user[1] + underline + user[2] + "_" + user[3] + "_" + user[4] + underline + user[5] + "_accy_" + user[6] + ".txt";
                            fnames[6] = user[0] + underline + user[1] + underline + user[2] + "_" + user[3] + "_" + user[4] + underline + user[5] + "_accz_" + user[6] + ".txt";
                            fnames[7] = user[0] + underline + user[1] + underline + user[2] + "_" + user[3] + "_" + user[4] + underline + user[5] + underline + user[6] + ".txt";

                            src_file_tar_dir_user = src_file_tar_dir + user[0] + underline + user[1] + underline + user[2] + underline + user[3] + "/";
                            tar_file_tar_dir_user = tar_file_tar_dir + user[0] + underline+ user[1] + underline + user[2] + underline + user[3] + "/";
                            File f_src_file_tar_dir_user = new File(src_file_tar_dir_user);
                            File f_tar_file_tar_dir_user = new File(tar_file_tar_dir_user);
                            if (!f_src_file_tar_dir_user.exists()) {
                                f_src_file_tar_dir_user.mkdir();
                                Log.d(tag, "dir " + src_file_tar_dir_user + " created.");
                            }
                            if (!f_tar_file_tar_dir_user.exists()) {
                                f_tar_file_tar_dir_user.mkdir();
                                Log.d(tag, "dir " + tar_file_tar_dir_user + " created.");
                            }

                            try{
                                PPGParser.decode(src_file_arr, tar_file_tar_dir_user, user, fnames);
                                Toast t2 = Toast.makeText(MainActivity.this, "file " + files + " has been decoded and saved to " + tar_file_tar_dir_user, Toast.LENGTH_SHORT);
                                t2.show();
                                Log.d(tag, "file " + files + " has been decoded and saved to " + tar_file_tar_dir_user);
                                try {
                                    copyFile(new File(src_file), new File(src_file_tar_dir_user + fnames[7]));
                                    Toast t3 = Toast.makeText(MainActivity.this, "source file " + files + " has been copied to " + src_file_tar_dir_user + fnames[7], Toast.LENGTH_SHORT);
                                    t3.show();
                                    Log.d(tag, "source file " + files + " has been copied to " + src_file_tar_dir_user + fnames[7]);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                btnDecode.setText("文件已保存");
                                Toast t4 = Toast.makeText(MainActivity.this, "All files processed on localhost.", Toast.LENGTH_SHORT);
                                t4.show();
                                Log.d(tag, "All files processed on localhost.");
                            } catch (Exception e){
                                e.printStackTrace();
                                btnDecode.setText("无有效数据文件，请重新采集");
                                flag = false;
                                Toast t5 = Toast.makeText(MainActivity.this, "data is invalid.", Toast.LENGTH_SHORT);
                                t5.show();
                                Log.d(tag, "data is invalid.");
                            }

                        } else {
                            Log.d(tag, "file " + src_dir + dir + "/" + files + " is unuseful.");
                        }

                    }

                    Log.d(tag, "flag " + flag + ", flag2 " + flag2);
                    if (!flag || flag2){
                        btnDecode.setText("无有效数据文件，请关闭应用程序重新采集");
                        Log.d(tag, "no valid data file.");
                        hint.setVisibility(View.INVISIBLE);
                        user_input.setVisibility(View.INVISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                        btnCheck.setVisibility(View.INVISIBLE);
                        btnCheck3.setVisibility(View.INVISIBLE);
                        btnSend.setVisibility(View.INVISIBLE);
                        btnExit.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    btnDecode.setText("文件夹为空，请检查。");

                    Log.d(tag, "dir is empty.");
                }


            }
        });




        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (connectivityManager.getActiveNetwork() == null){
                            btnSend.setText("无法连接到服务器，请检查你的网络连接");
                            Log.d(tag, "no available network.");
                        } else {
                            File f_src_file_tar_dir = new File(src_file_tar_dir);
                            File f_tar_file_tar_dir = new File(tar_file_tar_dir);
                            File f_src_file_tar_dir_user = new File(src_file_tar_dir_user);
                            File f_tar_file_tar_dir_user = new File(tar_file_tar_dir_user);
                            if (!f_src_file_tar_dir.exists()) {
                                f_src_file_tar_dir.mkdir();
                                Log.d(tag, "dir " + src_file_tar_dir + " created.");
                            }
                            if (!f_tar_file_tar_dir.exists()) {
                                f_tar_file_tar_dir.mkdir();
                                Log.d(tag, "dir " + tar_file_tar_dir + " created.");
                            }
                            if (!f_src_file_tar_dir_user.exists()) {
                                f_src_file_tar_dir_user.mkdir();
                                Log.d(tag, "dir " + src_file_tar_dir_user + " created.");
                            }
                            if (!f_tar_file_tar_dir_user.exists()) {
                                f_tar_file_tar_dir_user.mkdir();
                                Log.d(tag, "dir " + tar_file_tar_dir_user + " created.");
                            }

                            Connection connection = new Connection(serverAddr, PORT);
                            Session ssh = null;
                            try{
                                connection.connect();
                                boolean isConnected = connection.authenticateWithPassword(remoteUserName,remotePassword);
                                if (isConnected){
                                    Log.d(tag, "remote server " + serverAddr + colon + PORT + " connected.");
                                    btnSend.setText("远程服务器已连接");
                                }
                                SCPClient scpClient = connection.createSCPClient();
                                if (new File(src_file_tar_dir_user).list() != null){
                                    btnSend.setText("文件上传中");
                                    for (String fname:new File(src_file_tar_dir_user).list()){
                                        if (fname.split(underline)[1].equals(user_id) && fname.split(underline)[2].equals(group)){
                                            try {
                                                scpClient.put(src_file_tar_dir_user + fname, remote_path + "origin/" + fname.split(underline)[0] + underline +  watch_sc + underline + user_id + underline + group + "/");
                                            }catch (IOException e){
                                                ssh = connection.openSession();
                                                ssh.execCommand("mkdir " + remote_path + "origin/" + fname.split(underline)[0] + underline + watch_sc + underline + user_id + underline + group + "/");
                                                Log.d(tag, "dir " + remote_path + "origin/" + fname.split(underline)[0] + underline + watch_sc + underline + user_id + underline + group + "/" + " created.");
                                                scpClient.put(src_file_tar_dir_user + fname, remote_path + "origin/" + fname.split(underline)[0] + underline +  watch_sc + underline + user_id + underline + group + "/");
                                            }finally {
                                                if (ssh!=null){
                                                    ssh.close();
                                                    Log.d(tag, "source file " + src_file_tar_dir_user + fname + " uploaded to " + remote_path + "origin/" + fname.split(underline)[0] + underline +  watch_sc + underline + user_id + underline + group + "/" + fname);
                                                }
                                            }
                                        }
                                    }
                                }
                                if (new File(tar_file_tar_dir_user).list() != null){
                                    for (String fname:new File(tar_file_tar_dir_user).list()){
                                        if (fname.split(underline)[1].equals(watch_sc) && fname.split(underline)[2].equals(user_id) && fname.split(underline)[3].equals(group)){
                                            try {
                                                scpClient.put(tar_file_tar_dir_user + fname, remote_path + "decoded/" + fname.split(underline)[0] + underline + watch_sc + underline + user_id + underline + group + "/");
                                            }catch (IOException e){
                                                ssh = connection.openSession();
                                                ssh.execCommand("mkdir " + remote_path + "decoded/" + fname.split(underline)[0] + underline + watch_sc + underline + user_id + underline + group + "/");
                                                Log.d(tag, "dir " + remote_path + "decoded/" + fname.split(underline)[0] + underline + watch_sc + underline + user_id + underline + group + "/" + " created.");
                                                scpClient.put(tar_file_tar_dir_user + fname, remote_path + "decoded/" + fname.split(underline)[0] + underline + watch_sc + underline + user_id + underline + group + "/");
                                            }finally {
                                                if (ssh!=null){
                                                    ssh.close();
                                                    Log.d(tag, "decoded file " + tar_file_tar_dir_user + fname + " uploaded to " + remote_path + "decoded/" + fname.split(underline)[0] + underline + watch_sc + underline + user_id + underline + group + "/" + fname);
                                                }
                                            }
                                        }
                                    }
                                }
                                Log.d(tag, "All files uploaded.");
                                btnSend.setText("文件已上传");
                            }catch (IOException e){
                                e.printStackTrace();
                            }finally {
                                if (connection!=null){
                                    connection.close();
                                    try {
                                        if (new File(src_dir).list() != null){
                                            for (String dirs: new File(src_dir).list()){
                                                FileUtils.deleteDirectory(new File(src_dir + dirs));
                                                Log.d(tag, "dir " + src_dir + " is clear. All source files and directories are removed.");
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }


                    }
                }).start();

            }
        });


        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCheck.setText("分析中。。。");
                String[] func = {"script", "examine"};
                String fname = "";
                for (String f:new File(tar_file_tar_dir_user).list()){
                    if (f.split(underline)[1].equals(watch_sc) && f.split(underline)[2].equals(user_id) && f.split(underline)[3].equals(group) && f.split(underline)[6].equals("PPG2")){
                        fname = f;

                    }
                }
                String[] args = {
                        tar_file_tar_dir_user + fname,
                        lower_rate,
                        upper_rate
                };

                PythonTask examine_file = new PythonTask(func, args);
                Log.d(tag, "starting to analyze file " + tar_file_tar_dir_user + fname);
                examine_file.execute();

            }
        });


        btnCheck3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCheck3.setText("分析中。。。");
                String[] func = {"script", "examine"};
                String fname = "";
                for (String f:new File(tar_file_tar_dir_user).list()){
                    if (f.split(underline)[1].equals(watch_sc) &&f.split(underline)[2].equals(user_id) && f.split(underline)[3].equals(group) && f.split(underline)[6].equals("PPG3")){
                        fname = f;

                    }
                }
                String[] args = {
                        tar_file_tar_dir_user + fname,
                        lower_rate,
                        upper_rate
                };

                PythonTask3 examine_file = new PythonTask3(func, args);
                Log.d(tag, "starting to analyze file " + tar_file_tar_dir_user + fname);
                examine_file.execute();

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
                Log.d(tag, "Application exits.");
                System.exit(0);
            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    private class PythonTask extends AsyncTask<Void, Void, Integer> {

        private String[] func;
        private String[] args;


        PythonTask(String[] _func, String[] _args) {
            func = _func;
            args = _args;
        }


        @Override
        protected Integer doInBackground(Void... dummy) {
            Python python = Python.getInstance();
            PyObject res = python.getModule(func[0]).callAttr(func[1], (Object[]) args);
            return res.toInt();
        }


        @Override
        protected void onPreExecute() {
        }


        protected void onPostExecute(Integer a) {
            if (a == 0){
                btnCheck.setText("数据无效，请重新采集");
                Log.d(tag, "no valid signal.");
            } else{
                btnCheck.setText("本次采集到了" + a  + "个波形");
                Log.d(tag, a + " valid signals are collected.");
            }

        }
    }


    @SuppressLint("StaticFieldLeak")
    private class PythonTask3 extends AsyncTask<Void, Void, Integer> {

        private String[] func;
        private String[] args;


        PythonTask3(String[] _func, String[] _args) {
            func = _func;
            args = _args;
        }


        @Override
        protected Integer doInBackground(Void... dummy) {
            Python python = Python.getInstance();
            PyObject res = python.getModule(func[0]).callAttr(func[1], (Object[]) args);
            return res.toInt();
        }


        @Override
        protected void onPreExecute() {
        }


        protected void onPostExecute(Integer a) {

            if (a == 0){
                btnCheck3.setText("数据无效，请重新采集");
                Log.d(tag, "no valid signal.");
            } else{
                btnCheck3.setText("本次采集到了 " + a  + "个波形");
                Log.d(tag, a + " valid signals are collected.");
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("Permission", "permission: " + permissions[i] + ", result: " + grantResults[i]);
            }
        }
    }


    public static String[] readSettings(String settingFileName){
        String line;
        String[] args = new String[20];
        BufferedReader in = null;
        int i;
        try{
            in=new BufferedReader(new FileReader(settingFileName));
            line=in.readLine();
            for (i=0;i<args.length;i++){
                if (line != null){
                    args[i] = line;
                    line = in.readLine();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (in!=null){
                try{
                    in.close();
                }catch (IOException e){
                    e.printStackTrace();

                }
            }
        }

        return args;
    }


    public static void copyFile(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }


    public static void writeToSettings(String filePath, String[] settings){
        BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath), false), StandardCharsets.UTF_8));
            for (int i=0;i<settings.length;i++){
                bufferedWriter.write(settings[i] + "\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }



    }


}

