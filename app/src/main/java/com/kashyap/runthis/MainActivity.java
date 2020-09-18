package com.kashyap.runthis;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv1;

    //  ArrayList<String> al1;





    private String  items[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RuntimePermissions();

    }

    private ArrayList<File> findSongs(File file) {
        ArrayList<File> al1=new ArrayList<File>();
        File files[]=file.listFiles();

        for(File singlefile:files){
            if(singlefile.isDirectory() ){
                al1.addAll(findSongs(singlefile));
            }
            else{
                if(singlefile.getName().endsWith(".mp3") ||singlefile.getName().endsWith(".wav")){
                    al1.add(singlefile);
                }
            }
        }
        return al1;

    }
    public void RuntimePermissions(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        display();
                        //       doStuff();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
   public void display(){
       lv1=findViewById(R.id.lv1);
       final ArrayList<File> mysongs=findSongs(Environment.getExternalStorageDirectory());
       items=new String[mysongs.size()];
       for(int i=0;i<mysongs.size();i++){
           items[i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
       }
       ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items
       );
       lv1.setAdapter(arrayAdapter);
       lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String songname=lv1.getItemAtPosition(i).toString();
               Intent intent=new Intent(MainActivity.this,Music_Layou.class);
               intent.putExtra("position",i);
               intent.putExtra("arraylist",mysongs);
               intent.putExtra("songname",songname);
               startActivity(intent);
           }
       });
   }
}