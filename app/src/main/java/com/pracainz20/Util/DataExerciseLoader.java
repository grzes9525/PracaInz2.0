package com.pracainz20.Util;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.pracainz20.Model.Exercise;
import com.pracainz20.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grzechu on 16.04.2018.
 */

public class DataExerciseLoader extends AppCompatActivity{
    private Context mContext=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = null;
        FileReader fileReader = null;
        BufferedReader reader = null;
        StringBuilder str =null;
        List<Exercise> listExercise = null;
        String a = null;
        String b = null;
        int k=0;
        try {

            //file = new File("C:\\Users\\Grzechu\\Desktop\\INZ\\tabela.txt");
            //Log.d("path",file.getAbsolutePath());
            //fileReader = new FileReader(file);
            reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("tabela2.txt"), "UTF8"));
            String nextLine = null;

            listExercise = new ArrayList<>();
            while ((nextLine = reader.readLine()) != null) {
                if(nextLine!=null){
                    k++;
                    Exercise exercise = new Exercise();
                    if(nextLine.contains("*")){
                        a=nextLine.substring(1,nextLine.length());
                        System.out.println(a);
                    }
                    if(nextLine.contains("&")){
                        System.out.println(b);

                        b=nextLine.substring(1,nextLine.length());
                    }
                    if(!nextLine.contains("*") && !nextLine.contains("&") && !nextLine.isEmpty()){
                        exercise.setKindOfExercise(a);
                        if(b!=null){
                            exercise.setKindOfExerciseInDetail(b);

                        }
                        exercise.setName(nextLine);
                    }

                    listExercise.add(exercise);
                }

            }

        }catch (FileNotFoundException e){

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(k>=159){
            for(Exercise e : listExercise){
                FirebaseDatabase.getInstance().getReference().child("Exercises").push().setValue(e);
            }
        }


    }


}
