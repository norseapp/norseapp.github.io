package mycompany.myfirstandroidproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
 
public class EditTodoActivity extends Activity {
 
    EditText inputName;
    EditText inputLocation;
    EditText timePicker;
    EditText datePicker;
    EditText txtCreatedAt;
    Button btnSave;
    Button btnDelete;
 
    String tid;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single product url
    private static final String url_todo_details = "http://10.24.10.203/android_connect/get_Todo_details.php";
 
    // url to update product
    private static final String url_update_todo = "http://10.24.10.203/android_connect/update_todos.php";
 
    // url to delete product
    private static final String url_delete_todo = "http://10.24.10.203/android_connect/delete_todo.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TODO = "todo";
    private static final String TAG_TID = "tid";
    private static final String TAG_NAME = "name";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_TIME = "time";
    private static final String TAG_DATE = "date";
    
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_todo);
 
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
 
        // getting product details from intent
        Intent i = getIntent();
 
        // getting product id (pid) from intent
        tid = i.getStringExtra(TAG_TID);
 
        // Getting complete product details in background thread
        new GetTodoDetails().execute();
 
        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new SaveTodoDetails().execute();
            }
        });
 
        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                new DeleteTodo().execute();
            }
        });
 
    }
 
    /**
     * Background Async Task to Get complete product details
     * */
    class GetTodoDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditTodoActivity.this);
            pDialog.setMessage("Loading todo details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {
 
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("tid", tid));
 
                        // getting todo details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_todo_details, "GET", params);
 
                        // check your log for json response
                        Log.d("Single Product Details", json.toString());
 
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray todoObj = json
                                    .getJSONArray(TAG_TODO); // JSON Array
 
                            // get first product object from JSON Array
                            JSONObject todo = todoObj.getJSONObject(0);
 
                            // product with this tid found
                            // Edit Text
                            inputName = (EditText) findViewById(R.id.inputName);
                            inputLocation = (EditText) findViewById(R.id.inputLocation);
                            timePicker = (EditText) findViewById(R.id.timePicker);
                            datePicker = (EditText) findViewById(R.id.datePicker);
                            
                            // display product data in EditText
                            inputName.setText(todo.getString(TAG_NAME));
                            inputLocation.setText(todo.getString(TAG_LOCATION));
                            timePicker.setText(todo.getString(TAG_TIME));
                            datePicker.setText(todo.getString(TAG_DATE));
                            
 
                        }else{
                            // todo with tid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }
 
    /**
     * Background Async Task to  Save product Details
     * */
    class SaveTodoDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditTodoActivity.this);
            pDialog.setMessage("Saving todo ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
 
            // getting updated data from EditTexts
        	String name = inputName.getText().toString();
        	String location = inputLocation.getText().toString();
        	String due_time = timePicker.getText().toString();
        	String due_date = datePicker.getText().toString();
   
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_TID, tid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_LOCATION, location));
            params.add(new BasicNameValuePair(TAG_TIME, due_time));
            params.add(new BasicNameValuePair(TAG_DATE, due_date));
 
            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_todo,
                    "POST", params);
 
            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
 
    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteTodo extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditTodoActivity.this);
            pDialog.setMessage("Deleting Todo...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Deleting todo
         * */
        protected String doInBackground(String... args) {
 
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tid", tid));
 
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_todo, "POST", params);
 
                // check your log for json response
                Log.d("Delete Todo", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
 
        }
 
    }
}