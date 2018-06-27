package com.example.bruce.myapp.Common;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.bruce.myapp.AddMissingLocation;
import com.example.bruce.myapp.Utility;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class  ShowDiaLog extends AppCompatActivity {
    private String userChoosenTask;
    private int REQUEST_CAMERA=777;
    private int REQUEST_GALLERY=888;
    public void CameraAndGallery(Context context){
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(context);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),REQUEST_GALLERY);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    public void onSelectFromGalleryResult(Intent data,ArrayList<String> imagesPost)
    {
        Uri filePath;
        if(data.getClipData()==null)
        {
            imagesPost.add(data.getData().toString());
        }
        else{
            ArrayList<Uri> mArrayUri;
            ClipData mClipData=data.getClipData();
            mArrayUri = new ArrayList<Uri>();
            for(int i = 0 ;i < mClipData.getItemCount(); i++){
                ClipData.Item item = mClipData.getItemAt(i);
                filePath = item.getUri();
                imagesPost.add(filePath.toString());
            }
        }
        Set<String> hs = new HashSet<>();
        hs.addAll(imagesPost);
        imagesPost.clear();
        imagesPost.addAll(hs);
    }
    public void onCaptureImageResult(Intent data, ArrayList<String> imagesPost) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(getApplication().getContentResolver(), thumbnail, "Title", null);
        imagesPost.add(Uri.parse(path).toString());
    }
}
