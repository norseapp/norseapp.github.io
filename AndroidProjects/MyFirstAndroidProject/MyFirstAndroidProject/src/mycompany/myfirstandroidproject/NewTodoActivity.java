package mycompany.myfirstandroidproject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import android.widget.TimePicker;
import android.widget.DatePicker;

public class NewTodoActivity extends Activity {

// Progress Dialog
private ProgressDialog pDialog;

JSONParser jsonParser = new JSONParser();
EditText inputName;
EditText inputLocation;
EditText timePicker;
EditText datePicker;

// url to create new product
private static String url_create_todo = "http://10.24.10.203/android_connect/create_Todo.php";

// JSON Node names
private static final String TAG_SUCCESS = "success";

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.add_todo);

// Edit Text
inputName = (EditText) findViewById(R.id.inputName);
inputLocation = (EditText) findViewById(R.id.inputLocation);
timePicker = (EditText) findViewById(R.id.timePicker);
datePicker = (EditText) findViewById(R.id.datePicker);

// Create button
Button btnCreateTodo = (Button) findViewById(R.id.btnCreateTodo);

// button click event
btnCreateTodo.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View view) {
        // creating new product in background thread
        new CreateNewTodo().execute();
    }
});
}

/**
* Background Async Task to Create new product
* */
class CreateNewTodo extends AsyncTask<String, String, String> {

/**
 * Before starting background thread Show Progress Dialog
 * */
@Override
	protected void onPreExecute() {
    	super.onPreExecute();
    	pDialog = new ProgressDialog(NewTodoActivity.this);
    	pDialog.setMessage("Creating Todo..");
    	pDialog.setIndeterminate(false);
    	pDialog.setCancelable(true);
    	pDialog.show();
}


		protected String doInBackground(String... args) {
		    String name = inputName.getText().toString();
		    String location = inputLocation.getText().toString();
		    String due_time = timePicker.getText().toString();
		    String due_date = datePicker.getText().toString();
		
		
		    // Building Parameters
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("name", name));
		    params.add(new BasicNameValuePair("location", location));
		    params.add(new BasicNameValuePair("due_time", due_time));
		    params.add(new BasicNameValuePair("due_date", due_date));

		
		    // getting JSON Object
		    // Note that create to do url accepts POST method
		    JSONObject json = jsonParser.makeHttpRequest(url_create_todo,
		            "POST", params);
		
		    // check log cat fro response
		    Log.d("Create Response", json.toString());
		
		    // check for success tag
		    try {
		        int success = json.getInt(TAG_SUCCESS);
		
		        if (success == 1) {
		            // successfully created product
		            Intent i = new Intent(getApplicationContext(), AllTodoActivity.class);
		            startActivity(i);
		
		            // closing this screen
		            finish();
		        } else {
		            // failed to create product
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
			    // dismiss the dialog once done
			    pDialog.dismiss();
			}
			
			}
			}
