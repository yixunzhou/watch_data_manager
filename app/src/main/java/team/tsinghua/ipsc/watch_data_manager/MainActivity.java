package team.tsinghua.ipsc.watch_data_manager;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import team.tsinghua.ipsc.watch_data_manager.utils;


public class MainActivity  extends AppCompatActivity {
        private String origin_file_path;
        private String target_file_path;
        private String remote_mode;
        private String remote_addr;
        private String device_sc;
        private String setting_file_path;
        private Button load;
        private String[] args;
        private String s1, s2, s3, s4, s5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        args = utils.read_settings(setting_file_path);
        s1 = args[0];
        s2 = args[1];
        s3 = args[2];
        s4 = args[3];
        s5 = args[4];
        remote_mode = args[5];
        remote_addr = args[6];
    }
}
