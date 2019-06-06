package com.example.androidclient;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 申请权限的集合，同时要在AndroidManifest.xml中申请，Android 6以上需要动态申请权限
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    //显示图片窗体
    private ImageView showImg;
    //图片路径
    private String path;
    //
    File f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showImg = findViewById(R.id.img);
        //6.0获取多个权限
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        //未授予的权限为空，表示都授予了
        if (mPermissionList.isEmpty()) {
            //读取根目录下的一张图片
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/1.png";
            boolean fileExist = fileIsExists(path);
            if(fileExist){
                Toast.makeText(MainActivity.this,"开始上传"+f.getAbsolutePath(),Toast.LENGTH_LONG).show();
                try {
                    //上传图片
                    ImageUpload.run(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            readImg(showImg);
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }
    //界面显示图片
    public void readImg(View view) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        showImg.setImageBitmap(bitmap);
    }

    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(MainActivity.this,"权限未申请",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

