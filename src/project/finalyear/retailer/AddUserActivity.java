package project.finalyear.retailer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import project.finalyear.database.UserDataSource;
import project.finalyear.database.Users;
import project.finalyear.mongo.R;

public class AddUserActivity extends Activity {

    private UserDataSource userdatasource;
    private long userId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_add_user);
        userdatasource = new UserDataSource(this);
        userId = getIntent().getLongExtra("project.finalyear.retailer.userid",
                0);
        
        if (userId == 0) {
            this.setTitle("Add New User");
        } else {
            this.setTitle("Change Password");
            final EditText user_name_EditText = (EditText) findViewById(R.id.retailer_add_user_name);
            Users user = userdatasource.getUserById(userId);
            user_name_EditText.setText(user.getName());
            user_name_EditText.setEnabled(false);
            final Button addButton = (Button) findViewById(R.id.retailer_add_user_button);
            addButton.setText(R.string.changepwd);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_retailer_add_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddUserClick(View view) {
        final EditText user_name_EditText = (EditText) findViewById(R.id.retailer_add_user_name);
        final EditText pwd_EditText = (EditText) findViewById(R.id.retailer_add_password);
        final EditText confirm_pwd_EditText = (EditText) findViewById(R.id.retailer_add_confirm_password);
        if (user_name_EditText.getText() != null
                && user_name_EditText.getText().length() > 0) {
            if (pwd_EditText.getText() != null
                    && confirm_pwd_EditText.getText() != null
                    && pwd_EditText.getText().length() > 0
                    && confirm_pwd_EditText.getText().length() > 0) {
                String pwdStr = pwd_EditText.getText().toString().trim();
                String confirm_pwdStr = confirm_pwd_EditText.getText()
                        .toString().trim();
                String userName = user_name_EditText.getText().toString()
                        .trim();
                if (pwdStr.equals(confirm_pwdStr)) {
                    if (userId == 0) {
                        if (userdatasource.selectUser(userName) == null) {
                            if (userdatasource.createUsers(userName,
                                    PwdEncrypt.encrypt(pwdStr)) != null) {
                                Toast toast = Toast.makeText(this,
                                        "User created.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            AlertDialog alertDialog;
                            alertDialog = new AlertDialog.Builder(this)
                                    .create();
                            alertDialog.setTitle("Create User Account Error");
                            alertDialog.setMessage("User name exists.");
                            alertDialog.setButton(
                                    DialogInterface.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    } else {
                        Users updateUser = new Users();
                        updateUser.setId(userId);
                        updateUser.setName(userName);
                        updateUser.setPwd(PwdEncrypt.encrypt(pwdStr));
                            Toast toast = Toast.makeText(this,
                                    "Password changed.", Toast.LENGTH_LONG);
                            toast.show();
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Create User Account Error");
                    alertDialog.setMessage("Passwords don't match.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {

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
                alertDialog.setTitle("Create User Account Error");
                alertDialog.setMessage("Password cannot be empty.");
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
            alertDialog.setTitle("Create User Account Error");
            alertDialog.setMessage("User name cannot be empty.");
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
}
