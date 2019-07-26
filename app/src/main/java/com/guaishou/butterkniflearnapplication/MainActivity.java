package com.guaishou.butterkniflearnapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.guaishou.ioclibrary.InjectManager;
import com.guaishou.ioclibrary.annotation.InjectView;
import com.guaishou.ioclibrary.annotation.OnClick;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.hello_tv)
    TextView helloTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectManager.inject(this);
        helloTv.setText("InjectHello");
    }

    @OnClick({R.id.button_00,R.id.button_11})
    public void toast(View view){
        Toast.makeText(this,"注解点击事件！"+(view.getId()==R.id.button_00?"bt00":"bt11"),Toast.LENGTH_SHORT).show();
    }
}
