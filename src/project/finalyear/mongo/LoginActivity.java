package project.finalyear.mongo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import project.finalyear.model.SQLiteHelper;
import project.finalyear.mongo.UserRegistrationActivity;


public class LoginActivity extends Activity {
//	private Button retailer;
//	private Button user;
	SQLiteHelper sq;
	EditText edittext_user_username;
	EditText edittext_user_password;

	EditText edittext_retailer_username;

	EditText edittext_retailer_password;

	Button button_user_login;
	Button button_user_newuser;
	Button button_retailer_login;
	Button button_retailer_newuser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		/*
		 *----------------------------------------------- 
		retailer = (Button) findViewById(R.id.retailer);
		user = (Button)findViewById(R.id.user);
		
		user.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent launchactivity= new Intent(UserActivity.this,MainActivity.class);                               
				 startActivity(launchactivity); 
			}
		});
		---------------------------------------------------  
		*/
		 sq=new SQLiteHelper(this);
			edittext_user_username=(EditText)findViewById(R.id.editText_registration_username);
			edittext_user_password=(EditText)findViewById(R.id.editText_login_user_password);
			edittext_retailer_username=(EditText)findViewById(R.id.editText_registration_password);
			edittext_retailer_password=(EditText)findViewById(R.id.editText_login_retailer_password);
			button_user_login=(Button)findViewById(R.id.button_login_user);
			button_user_newuser=(Button)findViewById(R.id.button_newuser_user);
			button_retailer_login=(Button)findViewById(R.id.button_login_retailer);
			button_retailer_newuser=(Button)findViewById(R.id.button_newuser_retailer);
			button_user_login.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					
					String m=sq.userloginData(edittext_user_username.getText().toString(), edittext_user_password.getText().toString());
					if(m.equals("ok"))
						
					{
						Intent i1=new Intent(LoginActivity.this,TodoMainActivity.class);
					
				         
						startActivity(i1);
						
						
					}
					else
					{
						Toast.makeText(LoginActivity.this, "Login Failed.Try again", 10).show();
					}
					
				}
			});
			
			button_user_newuser.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					 Intent i = new Intent(LoginActivity.this,UserRegistrationActivity.class);
			         	
				        
			         
			  startActivity(i);
				}
			});
			button_retailer_login.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					String m=sq.retailerloginData(edittext_retailer_username.getText().toString(), edittext_retailer_password.getText().toString());
					if(m.equals("ok"))
						
					{
						Intent i1=new Intent(LoginActivity.this,TodoMainActivity.class);
					
				         
						startActivity(i1);
						
						
					}
					else
					{
						Toast.makeText(LoginActivity.this, "Login Failed.Try again", 10).show();
					}
				}
			});
			button_retailer_newuser.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Intent i = new Intent(LoginActivity.this,RetailerRegistrationActivity.class);
			         	
				        
			         
			  startActivity(i);
				}
			});
		

	}
}
