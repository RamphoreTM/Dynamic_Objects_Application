package com.example.dynamic_instantiation_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.ActionBar;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Method;

//import static com.example.dynamic_instantiation_test.R.id.btnAdd;

public class MainActivity extends AppCompatActivity {
    //private Button btnAdd;
    private EditText edtCarName;
    private LinearLayout llListView;
    private LinearLayout llButton;

    //custom function to disable animation
    private void setSystemAnimationScale(float animationScale) {
        try {
            Class windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
            Class serviceManagerClazz = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
            Class windowManagerClazz = Class.forName("android.view.IWindowManager");
            Method setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales", float[].class);
            Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

            IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
            Object windowManagerObj = asInterface.invoke(null, windowManagerBinder);
            float[] currentScales = (float[]) getAnimationScales.invoke(windowManagerObj);
            for (int i = 0; i < currentScales.length; i++) {
                currentScales[i] = animationScale;
            }
            setAnimationScales.invoke(windowManagerObj, new Object[]{currentScales});
            Log.d("CustomTestRunner", "Changed permissions of animations");
        } catch (Exception e) {
            Log.e("CustomTestRunner", "Could not change animation scale to " + animationScale + " :'(");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context ctx = MainActivity.this.getApplicationContext();
        //emulator unlock
        try {
            KeyguardManager mKeyGuardManager = (KeyguardManager)      ctx.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock mLock = mKeyGuardManager.newKeyguardLock(MainActivity.class.getSimpleName());
            mLock.disableKeyguard();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //keeping screen awake
        try{
            PowerManager power = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
            power.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, MainActivity.class.getSimpleName()).acquire();
        }catch (Exception e){
            e.printStackTrace();
        }

        //disabling animations
        try{
            int permStatus = ctx.checkCallingOrSelfPermission(Manifest.permission.SET_ANIMATION_SCALE);
            if (permStatus == PackageManager.PERMISSION_GRANTED){
                setSystemAnimationScale(1.0f);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        edtCarName = (EditText)findViewById(R.id.edtCarName);
        llButton = (LinearLayout) findViewById(R.id.llButtonPlace);
        llListView = (LinearLayout)  findViewById(R.id.llListView);

        LinearLayout.LayoutParams llButParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        Button btnAdd = new Button(MainActivity.this);
        btnAdd.setText("Add");
        llButParams.gravity = Gravity.CENTER_VERTICAL;
        btnAdd.setLayoutParams(llButParams);
        llButton.addView(btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                final TextView txtShow = new TextView(MainActivity.this);
                txtShow.setLayoutParams(lparams);
                txtShow.setText(edtCarName.getText());
                llListView.addView(txtShow);

                //text view onclick listener for deletion
                txtShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this,txtShow.getText() + " has been deleted",Toast.LENGTH_LONG).show();
                        llListView.removeViewAt(llListView.indexOfChild(txtShow));
                    }
                });

                edtCarName.setText("");
            }
        });
    }

    public static int Add(int a,int b){
        return a+b;
    }

}


