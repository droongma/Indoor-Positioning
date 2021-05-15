package edu.skku.capstone.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        if (!Python.isStarted()) Python.start(new AndroidPlatform(this));

        Python py = Python.getInstance();
        PyObject Pyobj = py.getModule("using_model_example");  //python file name


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PyObject obj = Pyobj.callAttr("main");

                textView.setText(obj.toString());
            }
        });
    }
}