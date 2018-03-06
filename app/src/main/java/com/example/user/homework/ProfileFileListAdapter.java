package com.example.user.homework;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by olpyh on 05.03.2018.
 */

public class ProfileFileListAdapter extends BaseAdapter {

    class mThread extends Thread{

        URL url;

        mThread(String urlStr) throws MalformedURLException {
            url = new URL(urlStr);
        }

        @Override
        public void run() {
            FirebaseStorage.getInstance();
        }
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<FileModel> fileModelsArray;

    ProfileFileListAdapter (Context cnt, ArrayList<FileModel> fileModels){
        this.fileModelsArray = fileModels;
        context = cnt;
        inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return fileModelsArray.size();
    }

    @Override
    public Object getItem(int i) {
        return fileModelsArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return fileModelsArray.get(i).getRef().hashCode();
    }



    @Override
    public View getView(int i, View view, ViewGroup parent) {
        View nView = view;
        if (nView == null) {
            nView = inflater.inflate(R.layout.file_list_item, parent, false);
        }

        final FileModel p = getFileModel(i);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) nView.findViewById(R.id.file_item_name_of_file)).setText(p.name);
        ((ImageView) nView.findViewById(R.id.file_item_image)).setImageResource(R.drawable.ic_home_white_24dp);
        nView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlStr = p.getRef();
                try {
                    downloadImage(urlStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return nView;
    }

    public void downloadImage(String urlStr) throws IOException {
        FirebaseStorage.getInstance().getReference().child(urlStr).getFile(new File("images\\" + new Random().nextDouble() + ".jpg"));

    }

    public FileModel getFileModel(int i){
        return fileModelsArray.get(i);
    }
}
