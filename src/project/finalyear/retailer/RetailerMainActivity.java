package project.finalyear.retailer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import project.finalyear.database.RetailerItemDataSource;
import project.finalyear.database.UserDataSource;
import project.finalyear.database.Users;
import project.finalyear.mongo.R;


public class RetailerMainActivity extends Activity {

    private UserDataSource userdatasource;
    private ArrayList<Users> listofUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_login);
        userdatasource = new UserDataSource(this);
        
        listofUsers = new ArrayList<Users>();
        
        listofUsers.addAll(userdatasource.getAllUsers());

        final Spinner user_spinner = (Spinner) findViewById(R.id.retailer_loginform_users_spinner);
        List<Users> allusers = userdatasource.getAllUsers();
        ArrayAdapter<Users> adapter = new ArrayAdapter<Users>(this,
                android.R.layout.simple_spinner_item, allusers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_spinner.setAdapter(adapter);
    }

    public void onAddClick(View view) {
        Intent myIntent = new Intent(view.getContext(), AddUserActivity.class);
        myIntent.putExtra("project.finalyear.retailer.userid", 0);
        startActivityForResult(myIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final EditText pwd_EditText = (EditText) findViewById(R.id.retailer_loginform_login_password);
        pwd_EditText.setText("");
        if (resultCode == RESULT_OK) {
            final Spinner user_spinner = (Spinner) findViewById(R.id.retailer_loginform_users_spinner);
            List<Users> allusers = userdatasource.getAllUsers();
            ArrayAdapter<Users> adapter = new ArrayAdapter<Users>(this,
                    android.R.layout.simple_spinner_item, allusers);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            user_spinner.setAdapter(adapter);
        }
    }

    public void onLoginClick(View view) {
        final Spinner user_spinner = (Spinner) findViewById(R.id.retailer_loginform_users_spinner);
        final EditText pwd_EditText = (EditText) findViewById(R.id.retailer_loginform_login_password);
        if (user_spinner.getAdapter().getCount() == 0) {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Login Error");
            alertDialog.setMessage("Please create user account.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }
        Users selectedUser = (Users) user_spinner.getSelectedItem();
        // if
        // (selectedUser.getName()!=null&&user_name_EditText.getText().length()>0)
        // {
        if (pwd_EditText.getText() != null
                && pwd_EditText.getText().length() > 0) {
            String pwdStr = pwd_EditText.getText().toString().trim();
            if (PwdEncrypt.encrypt(pwdStr).equals(selectedUser.getPwd())) {
                // Login view;
                Intent myIntent = new Intent(view.getContext(),
                        RetailerListActivity.class);
                myIntent.putExtra("project.finalyear.retailer.userid",
                        selectedUser.getId());
                myIntent.putExtra("project.finalyear.retailer.username",
                        selectedUser.getName());
                // Should do automatic sync here before intent is started
                startActivityForResult(myIntent, 0);
            } else {
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Login Error");
                alertDialog.setMessage("Password do not match.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        } else {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Login Error");
            alertDialog.setMessage("Password field cannot be empty.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_retailer_login, menu);
        return true;
    }
}
