package com.dci.android.gallery;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dci.android.gallery.model.PhotoModel;

import android.database.Cursor;
import android.test.AndroidTestCase;

/**
 * Created by chanlevel on 14/10/26.
 */
public class MyTest  extends AndroidTestCase{

    public void scan(){


        DButil db=new DButil(getContext());
       
        Map<String, List<PhotoModel>> map= db.getImages();
      
        Iterator<Entry<String, List<PhotoModel>>> c=map.entrySet().iterator();
        while(c.hasNext()){
        	Entry<String, List<PhotoModel>> entry=c.next();
        	System.out.println("===========");
        	System.out.println(entry.getKey());
        	System.out.println("===========");
        	List<PhotoModel> list=entry.getValue();
        	for(PhotoModel model:list){
        		System.out.println(model.toString());
        	}
        	
        	
        }

    }
    public void thum(){
    	   DButil db=new DButil(getContext());
    	   db.getThumbMap();
    	     
    }
 


}
