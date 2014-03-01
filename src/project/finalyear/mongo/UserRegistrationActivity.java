package project.finalyear.mongo;

import project.finalyear.model.SQLiteHelper;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;


public class UserRegistrationActivity extends Activity {
SQLiteHelper sq;
EditText edittext_username;
EditText edittext_password;


Button button_registration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		  sq=new SQLiteHelper(this);
		edittext_username=(EditText)findViewById(R.id.editText_registration_username);
		edittext_password=(EditText)findViewById(R.id.editText_registration_password);
		
		button_registration=(Button)findViewById(R.id.button_login_user);
	
		button_registration.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				sq.insertUserLoginData(edittext_username.getText().toString(), edittext_password.getText().toString());
				Toast.makeText(UserRegistrationActivity.this, "Registered", 10).show();
				 Intent launchactivity = new Intent(UserRegistrationActivity.this,LoginActivity.class);
		         	
			        
		         
		         
		         	startActivity(launchactivity);
			}
		});
		
	}
}
