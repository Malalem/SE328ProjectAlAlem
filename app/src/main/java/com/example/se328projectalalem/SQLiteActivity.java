package com.example.se328projectalalem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class SQLiteActivity extends AppCompatActivity {

    private DatabaseReference Sref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        EditText fname = findViewById(R.id.inpSQLfname);
        EditText lname = findViewById(R.id.inpSQLlname);
        EditText email = findViewById(R.id.inpSQLemail);
        EditText date = findViewById(R.id.inpSQLdate);
        EditText id = findViewById(R.id.inpSQLid);
        EditText Nid = findViewById(R.id.inpSQLNid);
        EditText gender = findViewById(R.id.inpSQLgender);
        //Buttons
        Button insertSQL = findViewById(R.id.insertSQL);
        Button deleteSQL = findViewById(R.id.deleteSQL);
        Button updateSQL = findViewById(R.id.updateSQL);
        Button searchSQL = findViewById(R.id.searchSQL);
        Button insertFire = findViewById(R.id.insertFromFirebase);

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        ImageView weatherimgSQL = findViewById(R.id.weatherSQL);
        String img = pref.getString("url",null);
        Log.d("Mohammed", img.toString());
        Glide.with(SQLiteActivity.this).load(img).into(weatherimgSQL);

        Sref = FirebaseDatabase.getInstance().getReference("Students");

        DBHelper MyDB = new DBHelper(this);
        insertSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FirstName = fname.getText().toString();
                String LastName = lname.getText().toString();
                String Email = email.getText().toString();
                String Date = date.getText().toString();
                String Id = id.getText().toString();
                String NationalId = Nid.getText().toString();
                String Gender = gender.getText().toString();

                if (FirstName.isEmpty() || LastName.isEmpty() || Email.isEmpty() || Date.isEmpty() || Id.isEmpty() || NationalId.isEmpty() || Gender.isEmpty()){
                    //Toast.makeText(SQLiteActivity.this,"You Must Fill All Fields",Toast.LENGTH_LONG).show();
                    Toasty.warning(SQLiteActivity.this, "You Must Fill All Fields", Toast.LENGTH_LONG, true).show();
                    return;
                }
                Student student = new Student(FirstName,LastName,Email,Date,NationalId,Gender,Integer.parseInt(String.valueOf(Id)));
                MyDB.insert(student);
                //Toast.makeText(SQLiteActivity.this, " Successful, Student: "+ student.fName+" was added to the SQLite database", Toast.LENGTH_LONG).show();
                Toasty.success(SQLiteActivity.this, " Successful, Student: "+ student.fName+" was added to the SQLite database", Toast.LENGTH_LONG, true).show();
            }
        });
        insertFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Id = id.getText().toString();
                if (Id.isEmpty()){
                    //Toast.makeText(SQLiteActivity.this, "You have to insert the ID of the student from the Firebase database that you wish to insert to SQL database", Toast.LENGTH_LONG).show();
                    Toasty.warning(SQLiteActivity.this, "You have to insert the ID of the student from the Firebase database that you wish to insert to SQL database", Toast.LENGTH_LONG, true).show();

                    return;
                }
                Sref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()){
                            if (child.child("sId").getValue(Integer.class) == Integer.valueOf(Id)){
                                //Student student = snapshot.child(child.getKey()).getValue(Student.class); Not working
                                String FirstName = child.child("fName").getValue(String.class);
                                String LastName = child.child("lName").getValue(String.class);
                                String Email = child.child("email").getValue(String.class);
                                String Date = child.child("date").getValue(String.class);
                                //String ID = child.child("studentID").getValue(String.class);//CRASH
                                String Id = id.getText().toString();
                                String NatID = child.child("sNatId").getValue(String.class);
                                String Gender = child.child("gender").getValue(String.class);
                                Student student = new Student(FirstName,LastName,Email,Date,NatID,Gender,Integer.parseInt(String.valueOf(Id)));
                                int newStudent = MyDB.insert(student);
                                if (newStudent != -1) { //testing
                                    //Toast.makeText(SQLiteActivity.this, "Student inserted from Firebase successfully.", Toast.LENGTH_SHORT).show();
                                    Toasty.success(SQLiteActivity.this, "Student inserted from Firebase successfully.", Toast.LENGTH_LONG, true).show();
                                } else {
                                    Toast.makeText(SQLiteActivity.this, "Error inserting from Firebase.", Toast.LENGTH_SHORT).show();
                                    Log.d("Mohammed: ",FirstName + " " +LastName + " " +Email+" "+Id);
                                }
                                return;
                            }
                        }
                        //Toast.makeText(SQLiteActivity.this, "No student with such ID was found, check students in the firebase database", Toast.LENGTH_SHORT).show();
                        Toasty.warning(SQLiteActivity.this, "No student with such ID was found, check students in the firebase database", Toast.LENGTH_LONG, true).show();
                        return;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(SQLiteActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toasty.error(SQLiteActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG, true).show();

                    }
                });
            }
        });

        deleteSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Id = id.getText().toString();
                MyDB.delete(Integer.valueOf(Id));
                //Toast.makeText(SQLiteActivity.this, "Successful Delete", Toast.LENGTH_LONG).show();
                Toasty.success(SQLiteActivity.this, "Successful Delete", Toast.LENGTH_LONG, true).show();

            }
        });
        updateSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FirstName = fname.getText().toString();
                String LastName = lname.getText().toString();
                String Email = email.getText().toString();
                String Date = date.getText().toString();
                String Id = id.getText().toString();
                String NationalId = Nid.getText().toString();
                String Gender = gender.getText().toString();

                if (FirstName.isEmpty() || LastName.isEmpty() || Email.isEmpty() || Date.isEmpty() || Id.isEmpty() || NationalId.isEmpty() || Gender.isEmpty()) {
                    //Toast.makeText(SQLiteActivity.this, "You Must Fill All Fields", Toast.LENGTH_LONG).show();
                    Toasty.warning(SQLiteActivity.this, "You Must Fill All Fields", Toast.LENGTH_LONG, true).show();

                    return;
                }
                int update = MyDB.updateSQLStudent( FirstName,  LastName,  Email,  Date,  NationalId,  Gender,  Integer.valueOf(Id));
                if (update > 0) {
                    //Toast.makeText(SQLiteActivity.this, "Record updated successfully.", Toast.LENGTH_SHORT).show();
                    Toasty.success(SQLiteActivity.this, "Record updated successfully.", Toast.LENGTH_LONG, true).show();

                } else {
                    //Toast.makeText(SQLiteActivity.this, "Error updating", Toast.LENGTH_SHORT).show();
                    Toasty.error(SQLiteActivity.this, "Error updating", Toast.LENGTH_LONG, true).show();
                }
            }
        });
        searchSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SQLiteActivity.this,SQLlist.class));
            }
        });
    }
}