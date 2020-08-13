package team.tsinghua.ipsc.watch_data_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.button.MaterialButton;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static int REQUEST_PERMISSION_CODE = 1;

    private String serverAddr;
    private int PORT;
    private String src_dir;
    private String tar_dir;
    private String src_file_tar_dir;
    private String tar_file_tar_dir;
    private String mode;
    private Button btnCheck, btnCheck3, btnLogin;
    private String device_sc;
    private String lower_rate, upper_rate;
    private String[] user = new String[6];
    private String user_id, group;
    private String[] fnames = new String[8];
    private EditText user_input;

    @SuppressLint(value = "ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
        }

        String settingFilePath = "/storage/emulated/0/watch_data/settings.txt";
        String[] settings = readSettings(settingFilePath);
        serverAddr = settings[0];
        PORT = Integer.parseInt(settings[1]);
        src_dir = settings[2];
        tar_dir = settings[3];
        lower_rate = settings[4];
        upper_rate = settings[5];
        src_file_tar_dir = tar_dir + "origin/";
        tar_file_tar_dir = tar_dir + "decoded/";
        device_sc = new File("/storage/emulated/0/dev_info/").list()[0];

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }


        user_input = findViewById(R.id.user);
        btnLogin = findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id = user_input.getText().toString().split("_")[0];
                group = user_input.getText().toString().split("_")[0];
                btnLogin.setText("用户" + user_input.getText().toString() + "已登录");
//                Toast toast = Toast.makeText(MainActivity.this, "用户" + user_input.getText().toString() + "已登录", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.BOTTOM, 0, 0);
//                toast.show();
            }
        });

        final Button btnDecode = findViewById(R.id.decode_save);
        btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (String dir:new File(src_dir).list()){
                    for (String files:new File(src_dir +dir+"/").list()){
                        if (files.endsWith(".txt") && !files.endsWith("desc.txt")){

                            String src_file = src_dir + dir + "/" + files;
                            String[] src_file_arr = {src_file};
                            File f_src_file_tar_dir = new File(src_file_tar_dir);
                            File f_tar_file_tar_dir = new File(tar_file_tar_dir);
                            if (!f_src_file_tar_dir.exists()) f_src_file_tar_dir.mkdir();
                            if (!f_tar_file_tar_dir.exists()) f_tar_file_tar_dir.mkdir();

                            user[0] = files.split("_")[5];
                            user[1] = user_id;
                            user[2] = group;
                            user[3] = device_sc;
                            user[4] = lower_rate + "-" + upper_rate;
                            user[5] = "00";

                            fnames[0] = user[0] + "_" + user[1] + "_" + user[2] + "_" + user[3] + "_" + user[4] + "_PPG1" + "_" + user[5] + ".txt";
                            fnames[1] = user[0] + "_" + user[1] + "_" + user[2] + "_" + user[3] + "_" + user[4] + "_PPG2" + "_" + user[5] + ".txt";
                            fnames[2] = user[0] + "_" + user[1] + "_" + user[2] + "_" + user[3] + "_" + user[4] + "_PPG3" + "_" + user[5] + ".txt";
                            fnames[3] = user[0] + "_" + user[1] + "_" + user[2] + "_" + user[3] + "_" + user[4] + "_PPG4" + "_" + user[5] + ".txt";
                            fnames[4] = user[0] + "_" + user[1] + "_" + user[2] + "_" + user[3] + "_" + user[4] + "_accx" + "_" + user[5] + ".txt";
                            fnames[5] = user[0] + "_" + user[1] + "_" + user[2] + "_" + user[3] + "_" + user[4] + "_accy" + "_" + user[5] + ".txt";
                            fnames[6] = user[0] + "_" + user[1] + "_" + user[2] + "_" + user[3] + "_" + user[4] + "_accz" + "_" + user[5] + ".txt";
                            fnames[7] = user[0] + "_" + user[1] + "_" + user[2] + "_" + user[3] + "_" + user[4] + "_" + user[5] + ".txt";
                            PPGParser.decode(src_file_arr, tar_file_tar_dir, user, fnames);
                            try {
                                copyFile(new File(src_file), new File(src_file_tar_dir + fnames[7]));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Toast toast = Toast.makeText(MainActivity.this, "保存完成", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 0);
                            toast.show();
                            try {
                                if (new File(src_dir).list() != null){
                                    for (String dirs: new File(src_dir).list()){
                                        FileUtils.deleteDirectory(new File(src_dir+dirs));
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

//                for (String decodedFiles:new File(tar_file_tar_dir).list()){
//                    try{
//                        copyFile(new File(tar_file_tar_dir + decodedFiles), new File(temp_tar_dir + decodedFiles));
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
            }
        });



        final Button btnSend = findViewById(R.id.upload);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File f_src_file_tar_dir = new File(src_file_tar_dir);
                        File f_tar_file_tar_dir = new File(tar_file_tar_dir);
                        if (!f_src_file_tar_dir.exists()) f_src_file_tar_dir.mkdir();
                        if (!f_tar_file_tar_dir.exists()) f_tar_file_tar_dir.mkdir();

                        btnSend.setText("开始上传");
                        if(new File(src_file_tar_dir).list() != null && new File(tar_file_tar_dir).list() != null) {
                            for (String fname : new File(src_file_tar_dir).list()) {
                                if (fname.split("_")[1].equals(user_id) && fname.split("_")[2].equals(group)) {
                                    mode = "origin/";
                                    sendToRemote(fname, mode);
                                }
                            }
                            btnSend.setText("上传中");
                            for (String fname : new File(tar_file_tar_dir).list()) {
                                if (fname.split("_")[1].equals(user_id) && fname.split("_")[2].equals(group)){
                                    mode = "decoded/";
                                    sendToRemote(fname, mode);
                                }

                            }
                        } else{
                            btnSend.setText("文件夹为空，请检查");
                        }

//                        try {
//                            FileUtils.deleteDirectory(new File(temp_dir));
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        btnSend.setText("上传完成");
                    }
                }).start();

            }
        });

        btnCheck = findViewById(R.id.check);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCheck.setText("分析中。。。");
                String[] func = {"script", "examine"};
                String fname = "";
                for (String f:new File(tar_file_tar_dir).list()){
                    if (f.split("_")[1].equals(user_id) && f.split("_")[2].equals(group) && f.split("_")[5].equals("PPG2")){
                        fname = f;
                    }
                }
                String[] args = {
                        tar_file_tar_dir + fname,
                        lower_rate,
                        upper_rate
                };

                PythonTask examine_file = new PythonTask(func, args);
                examine_file.execute();

            }
        });

        btnCheck3 = findViewById(R.id.check3);
        btnCheck3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCheck3.setText("分析中。。。");
                String[] func = {"script", "examine"};
                String fname = "";
                for (String f:new File(tar_file_tar_dir).list()){
                    if (f.split("_")[1].equals(user_id) && f.split("_")[2].equals(group) && f.split("_")[5].equals("PPG3")){
                        fname = f;
                    }
                }
                String[] args = {
                        tar_file_tar_dir + fname,
                        lower_rate,
                        upper_rate
                };

                PythonTask3 examine_file = new PythonTask3(func, args);
                examine_file.execute();

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
//                Toast toast = Toast.makeText(MainActivity.this, "数据无效，请重新采集", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.BOTTOM, 0, 0);
//                toast.show();
            } else{
                btnCheck.setText("本次采集到了" + a  + "个波形");
//                Toast toast = Toast.makeText(MainActivity.this, "本次采集到了 \" + a  + \"个波形", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.BOTTOM, 0, 0);
//                toast.show();
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
//                Toast toast = Toast.makeText(MainActivity.this, "数据无效，请重新采集", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.BOTTOM, 0, 0);
//                toast.show();
            } else{
                btnCheck3.setText("本次采集到了 " + a  + "个波形");
//                Toast toast = Toast.makeText(MainActivity.this, "本次采集到了 \" + a  + \"个波形", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.BOTTOM, 0, 0);
//                toast.show();
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
        int i;
        try{
            BufferedReader in=new BufferedReader(new FileReader(settingFileName));
            line=in.readLine();
            for (i=0;i<args.length;i++){
                if (line != null){
                    args[i] = line;
                    line = in.readLine();
                }
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return args;
    }


    public void sendToRemote(String fname, String mode){
        Socket s = null;
        DataOutputStream dos = null;
        FileInputStream fis = null;
        File f;
        try{
            try{
                if (mode.equals("origin/")){
                    f = new File(src_file_tar_dir + fname);
                } else {
                    f = new File(tar_file_tar_dir + fname);
                }

                s = new Socket(serverAddr, PORT);
                dos = new DataOutputStream(s.getOutputStream());
                fis = new FileInputStream(f);
                long fl = f.length();

                dos.writeUTF(mode + fname);
                dos.flush();

                dos.writeLong(fl);
                dos.flush();

                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;

                while ((length = fis.read(bytes, 0, bytes.length))!= -1){
                    dos.write(bytes, 0, length);
                    dos.flush();
                    progress += length;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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


}

