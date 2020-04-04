package com.prajwal331.qfs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prajwal331.qfs.models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetUserDetails extends AppCompatActivity {
    Button mStartDate, mSubmitBtn;
    TextView mStartDateValue, mEndDateValue, mStatusValue, mName, mAddress;
    User user;
    private boolean quarantined=false;
    FirebaseDatabase mDatabase;
    DatabaseReference mUserDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_details);
        if (user==null){
            user=new User();
        }

        mDatabase=FirebaseDatabase.getInstance();
        mUserDatabaseReference=mDatabase.getReference().child("User");

        mStartDate =findViewById(R.id.startDate);
        mStartDateValue =findViewById(R.id.startDateValue);
        mEndDateValue =findViewById(R.id.endDateValue);
        mStatusValue =findViewById(R.id.statusValue);
        mName =findViewById(R.id.name);
        mAddress =findViewById(R.id.address);
        mSubmitBtn =findViewById(R.id.submitBtn);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount!=null){
            mName.setText(signInAccount.getDisplayName());
        }

        datePickerSetup();
        submitSetup();

    }

    private void datePickerSetup(){

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(GetUserDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                mStartDateValue.setText(day + "/" + (month+1 ) + "/" + year);
                                Calendar c=Calendar.getInstance();
                                c.set(year,month,day);
                                c.add(Calendar.DAY_OF_MONTH,14);
                                mEndDateValue.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + ( c.get(Calendar.MONTH)+1 ) + "/" + c.get(Calendar.YEAR));
                                if (calendar.before(c)){
                                    quarantined=true;
                                    mStatusValue.setText("Quarantined");
                                }else {
                                    quarantined=false;
                                    mStatusValue.setText("Not Quarantined");

                                }

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    private  boolean emptyValidation(CharSequence value){
        if (value==null){
            return false;
        }else return value.length() != 0;
    }

    private void submitSetup(){
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            final String name =mName.getText().toString().trim();
            final String address =mAddress.getText().toString().trim();
            final String startDateString =mStartDateValue.getText().toString().trim();
            final String endDateString =mEndDateValue.getText().toString().trim();
            final String statusString =mStatusValue.getText().toString().trim();
            @Override
            public void onClick(View v) {
                if (emptyValidation(name) && emptyValidation(address) && emptyValidation(startDateString) && emptyValidation(endDateString) && emptyValidation(statusString) ){
                    user=new User(name, address, startDateString, endDateString, quarantined);
                    mUserDatabaseReference.child("user").setValue(user);

                }
            }
        });
    }


}
