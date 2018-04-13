package com.pracainz20.Util;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pracainz20.Model.Product;
import com.pracainz20.Model.User;
import com.pracainz20.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grzechu on 29.03.2018.
 */

public class DataLoader extends AppCompatActivity {

    private Context mContext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("dataLoader","eceute");
        File file = null;
        FileReader fileReader = null;
        BufferedReader reader = null;
        StringBuilder str =null;
        List<Product> listProduct = null;


        int k=0;
        try
        {

            //file = new File("C:\\Users\\Grzechu\\Desktop\\INZ\\tabela.txt");
            //Log.d("path",file.getAbsolutePath());
            //fileReader = new FileReader(file);
            reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("tabela1.txt"), "UTF8"));
            String nextLine = null;

            listProduct = new ArrayList<>();
            while ((nextLine = reader.readLine()) != null) {
                Product product = new Product();


                if(nextLine.contains("%")){
                    product.setId(k);
                    try {
                        product.setName(nextLine.substring(0,nextLine.indexOf("%")+1));
                        Log.d("name", product.getName());

                    }catch (StringIndexOutOfBoundsException e){System.out.println(k+" "+nextLine.substring(0,nextLine.indexOf("%")));}
                    catch (NumberFormatException e){ System.out.println(k+" "+nextLine.substring(0,nextLine.indexOf("%")));}
                    String a = nextLine.substring(nextLine.indexOf("%")+1,nextLine.length()).replaceAll("\\s","").replaceAll(",",".");


                    try {
                        product.setKcal(Float.valueOf(a.substring(0,a.indexOf("kcal"))));
                        //System.out.println("HHHH"+a.substring(0,a.indexOf("kcal")));

                    }catch (StringIndexOutOfBoundsException e){System.out.println(k+" aaa "+a);}
                    catch (NumberFormatException e){ System.out.println(k+" bbbb "+a);}
                    //System.out.println("ZZZZ"+a.substring(a.indexOf("kcal")+4,a.indexOf('g')));
                    try {
                        product.setProtein(Float.valueOf(a.substring(a.indexOf("kcal")+4,a.indexOf('g'))));


                    }catch (StringIndexOutOfBoundsException e){System.out.println(k+" aaa "+a);}
                    catch (NumberFormatException e){ System.out.println(k+" bbb "+a);}
                    String b = a.substring(a.indexOf('g')+1,a.length());
                    //System.out.println("ZZZZ"+b);
                    try {
                        product.setLipid(Float.valueOf(b.substring(0,b.indexOf('g'))));

                    }catch (StringIndexOutOfBoundsException e){System.out.println(k+" aaa "+b);}
                    catch (NumberFormatException e){ System.out.println(k+" "+b);}
                    String c = b.substring(b.indexOf('g')+1,b.length());
                    try {
                        product.setCarb(Float.valueOf(c.substring(0,c.indexOf('g'))));

                    }catch (StringIndexOutOfBoundsException e){System.out.println(k+" aaa "+c);}
                    catch (NumberFormatException e){ System.out.println(k+" "+c);}

                    k++;
                }



                for(int i=0; i<nextLine.toCharArray().length;i++){
                    char a=nextLine.toCharArray()[i];
                    String b= String.valueOf(a);
                    if(b.matches(".*\\d+.*") && !nextLine.contains("%")){
                        product.setId(k);

                        product.setName(nextLine.substring(0,i));
                        Log.d("name", product.getName());

                        String c= nextLine.substring(i,nextLine.toCharArray().length).replaceAll("\\s","");
                        //System.out.println(c);
                        try{
                            product.setKcal(Float.valueOf(c.substring(0,c.indexOf("kcal"))));

                        }
                        catch (StringIndexOutOfBoundsException e){System.out.println(k+" "+c);}
                        catch (NumberFormatException e){ System.out.println(k+" "+c);}
                        String d = c.substring(c.indexOf("kcal")+4,c.indexOf('g')).replaceAll(",",".");
                        //System.out.println(d);
                        try{
                            product.setProtein(Float.valueOf(d));

                        }
                        catch (StringIndexOutOfBoundsException e){System.out.println(k+" "+d);}
                        catch (NumberFormatException e){ System.out.println(k+" "+d);}
                        String f = c.substring(c.indexOf('g')+1,c.length());
                        try{
                            product.setLipid(Float.valueOf(f.substring(0,f.indexOf('g')).replaceAll(",",".")));

                        }
                        catch (StringIndexOutOfBoundsException e){System.out.println(k+" "+f);}
                        catch (NumberFormatException e){ System.out.println(k+" "+f);}
                        String h = f.substring(f.indexOf('g')+1,f.length());

                        //System.out.println(h);
                        try{
                            product.setCarb(Float.valueOf(h.substring(0,h.indexOf('g')).replaceAll(",",".")));

                        }
                        catch (StringIndexOutOfBoundsException e){System.out.println("asda"+k+" "+h);}
                        catch (NumberFormatException e){ System.out.println(k+" "+h);}
                        i=nextLine.toCharArray().length;
                        k++;
                    }
                }

                if( product.getCarb()!=null){
                    listProduct.add(product);

                }






            }
        }
        catch (Exception exep)
        {
            exep.printStackTrace();
        }


        Log.d("kkkk", String.valueOf(k));

        if(k==2){
            Log.d("kkkk", String.valueOf(k));
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            for(Product p : listProduct){
                reference.child("Products").push().setValue(p);
            }

        }



    }
}
