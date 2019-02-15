package com.example.user.homework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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
        //((ImageView) nView.findViewById(R.id.file_item_image)).setImageResource(R.drawable.ic_home_white_24dp);
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
