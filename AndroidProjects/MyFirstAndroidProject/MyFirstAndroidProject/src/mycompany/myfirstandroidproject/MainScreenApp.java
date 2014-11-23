package mycompany.myfirstandroidproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;


public class MainScreenApp extends Activity {
	
	
	Button AddButton;
    Button viewTodoList;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_first);
 
        // Buttons
        AddButton = (Button) findViewById(R.id.AddButton);
        viewTodoList = (Button) findViewById(R.id.viewTodoList);
 
        // view products click event
        viewTodoList.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AllTodoActivity.class);
                startActivity(i);
 
            }
        });
 
        // view products click event
        AddButton.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), NewTodoActivity.class);
                startActivity(i);
 
            }
        });
    }
}

   