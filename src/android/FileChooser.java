package com.megster.cordova;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.Arrays;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

public class FileChooser extends CordovaPlugin {

    private static final String TAG = "FileChooser";
    private static final String ACTION_OPEN = "open";
    private static final int PICK_FILE_REQUEST = 1;
    CallbackContext callback;

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        //if (action.equals(ACTION_OPEN)) {
            //chooseFile(callbackContext);
            //return true;
        //}

        //return false;
        //
		
		FileChooser newFile = new FileChooser();
		JSONArray mJSONArray = newFile.executeFile(args.getString(0));
		
 //       String mStringArray[] = { args.getString(0), args.getString(1) };
 //       JSONArray mJSONArray = new JSONArray(Arrays.asList(mStringArray));

        callback = callbackContext;
        callback.success(mJSONArray);
        return true;
    }
	
	  
	private JSONArray executeFile(String fileName) {
            String[] temp = parseFile(fileName);
            List<Map.Entry<String, Integer>> sortedList = mapFile(temp).mapSort(new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue() - o1.getValue();
                }
            });
			return new JSONArray(sortedList);
    }

    private String[] parseFile(String fileData) throws IOException {
        fileData = fileData.replace(System.getProperty("line.separator"), " ").toLowerCase();
        fileData = fileData.replaceAll("[\\p{Graph}—«»…–?:\uFEFF ]","").replaceAll("  +"," ").trim();
//        System.out.println(fileData);
        return fileData.split(" ");
    }

    private HashMapToSortedList<String, Integer> mapFile(String[] parsedFileData){
        System.out.println("Total number of words processed: " + parsedFileData.length);
        HashMapToSortedList<String, Integer> mapFile = new HashMapToSortedList<> ();
        for(String word : parsedFileData){
            if(!mapFile.containsKey(word))
                mapFile.put(word.trim(), 1);
            else{
                int count = mapFile.get(word);
                mapFile.put(word.trim(), count + 1);
            }
        }
        System.out.println("Number of unique words: " + mapFile.size());
//        System.out.println(mapFile.get(""));
        return mapFile;
    }
	
	    class HashMapToSortedList<K,V> extends HashMap<K,V> {

        List<Map.Entry<K,V>> mapSort(Comparator<Entry<K, V>> customComparator){
            ArrayList<Entry<K,V>> arrayList = new ArrayList<>(entrySet());
            arrayList.sort(customComparator);
            return arrayList;
        }

    }

}
