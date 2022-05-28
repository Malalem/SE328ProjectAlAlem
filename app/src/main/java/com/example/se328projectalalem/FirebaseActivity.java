package com.example.se328projectalalem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.github.muddz.styleabletoast.StyleableToast;

public class FirebaseActivity extends AppCompatActivity {
    //FirebaseDatabase database;
    DatabaseReference Sref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Edit Texts
        setContentView(R.layout.activity_firebase);
        EditText fname = findViewById(R.id.inpFirefname);
        EditText lname = findViewById(R.id.inpFirelname);
        EditText email = findViewById(R.id.inpFireemail);
        EditText date = findViewById(R.id.inpFiredate);
        EditText id = findViewById(R.id.inpFireid);
        EditText Nid = findViewById(R.id.inpFireNid);
        EditText gender = findViewById(R.id.inpFiregender);
        //Buttons
        Button insertFire = findViewById(R.id.insertFire);
        Button deleteFire = findViewById(R.id.deleteFire);
        Button updateFire = findViewById(R.id.updateFire);
        Button searchFire = findViewById(R.id.searchFire);

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        ImageView weatherimgFire = findViewById(R.id.weatherFirebase);
        String img = pref.getString("url",null);
        Log.d("Mohammed", img.toString());
        Glide.with(FirebaseActivity.this).load(img).into(weatherimgFire);

        Sref = FirebaseDatabase.getInstance().getReference("Students");

        insertFire.setOnClickListener(new View.OnClickListener() {
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
                    //Toast.makeText(FirebaseActivity.this,"You Must Fill All Fields",Toast.LENGTH_LONG).show();
                    Toasty.warning(FirebaseActivity.this, "You Must Fill All Fields", Toast.LENGTH_LONG, true).show();
                    return;
                }
                Student student = new Student(FirstName,LastName,Email,Date,NationalId,Gender,Integer.parseInt(String.valueOf(Id)));
                insertFireStudent(student);
            }
        });

        searchFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirebaseActivity.this,FireList.class));
            }
        });

        updateFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FirstName = fname.getText().toString();
                String LastName = lname.getText().toString();
                String Email = email.getText().toString();
                String Date = date.getText().toString();
                String Id = id.getText().toString();
                String NationalId = Nid.getText().toString();
                String Gender = gender.getText().toString();
                HashMap<String,Object> studentMap = new HashMap<>();

                if (Id.isEmpty()){
                    //Toast.makeText(FirebaseActivity.this,"You must insert the student ID of the student record you wish to modify.",Toast.LENGTH_SHORT).show();
                    Toasty.warning(FirebaseActivity.this, "You must insert the student ID of the student record you wish to modify.", Toast.LENGTH_LONG, true).show();

                    return;
                }
                if (!FirstName.isEmpty()){
                    studentMap.put("fName",FirstName);
                }
                if (!LastName.isEmpty()){
                    studentMap.put("lName",LastName);
                }
                if (!Email.isEmpty()){
                    studentMap.put("email",Email);
                }
                if (!Date.isEmpty()){
                    studentMap.put("date",Date);
                }
                if (!Id.isEmpty()){
                    studentMap.put("sId",Integer.valueOf(Id));
                }
                if (!Gender.isEmpty()) {
                    studentMap.put("gender", Gender);
                }
                updateFireStudent(Integer.valueOf(Id),studentMap);
            }
        });

        deleteFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Id = id.getText().toString();
                if (Id.isEmpty()){
                    //Toast.makeText(FirebaseActivity.this,"You must enter the ID of the student you wish to delete",Toast.LENGTH_LONG).show();
                    Toasty.warning(FirebaseActivity.this, "You must enter the ID of the student you wish to delete", Toast.LENGTH_LONG, true).show();
                    return;
                }
                deleteFireStudent(Integer.valueOf(Id));
            }
        });
    }

    private void insertFireStudent(Student student){
        Sref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("sId").getValue(Integer.class)== student.sId){
                        //Toast.makeText(FirebaseActivity.this,"A student with this ID already exists",Toast.LENGTH_LONG).show();
                        Toasty.warning(FirebaseActivity.this, "A student with this ID already exists.", Toast.LENGTH_LONG, true).show();

                        return;
                    }
                }
                int position = (int)snapshot.getChildrenCount();
                while (snapshot.hasChild(position+"")){
                    position++;
                }
                DatabaseReference insertReference = Sref.child(position+"");

                insertReference.child("sId").setValue(student.sId);
                insertReference.child("fName").setValue(student.fName);
                insertReference.child("lName").setValue(student.lName);
                insertReference.child("email").setValue(student.email);
                insertReference.child("date").setValue(student.date);
                insertReference.child("sNatId").setValue(student.sNatId);
                insertReference.child("gender").setValue(student.gender);
                //inserting data
                //Toast.makeText(FirebaseActivity.this,"Done Successfully",Toast.LENGTH_LONG).show();
                Toasty.success(FirebaseActivity.this, "Done Successfully, StudentID: "+student.sId+", "+student.fName+" "+student.lName+" was inserted to Firebase", Toast.LENGTH_LONG, true).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteFireStudent(int sId){
        Sref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()){
                    if (child.child("sId").getValue(Integer.class)== sId){
                        Sref.child(child.getKey()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //Toast.makeText(FirebaseActivity.this,"Deleted Student Successfully",Toast.LENGTH_LONG).show();
                                        Toasty.success(FirebaseActivity.this, "Deleted Student Successfully", Toast.LENGTH_LONG, true).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Toast.makeText(FirebaseActivity.this,"Error in Deleting Student",Toast.LENGTH_LONG).show();
                                        Toasty.error(FirebaseActivity.this, "Error in Deleting Student", Toast.LENGTH_LONG, true).show();
                                    }
                                });
                    }
                }
               // Toast.makeText(FirebaseActivity.this,"No Student with this Student ID was found",Toast.LENGTH_LONG).show();
                Toasty.warning(FirebaseActivity.this, "No Student with this Student ID was found", Toast.LENGTH_LONG, true).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateFireStudent(int sId, Map<String,Object> key){
        Sref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()){
                    if (child.child("sId").getValue(Integer.class)==sId){
                        Sref.child(child.getKey()).updateChildren(key).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(FirebaseActivity.this,"Student data updated.",Toast.LENGTH_SHORT).show();
                                Toasty.success(FirebaseActivity.this, "Student data updated.", Toast.LENGTH_LONG, true).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(FirebaseActivity.this,"Student data failed to update.",Toast.LENGTH_SHORT).show();
                                Toasty.error(FirebaseActivity.this, "Student data failed to update.", Toast.LENGTH_LONG, true).show();

                            }
                        });
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

class Student{
    int sId;
     String  fName, lName, email, date, gender, sNatId;

    public Student(String fName, String lName, String email, String date, String natId, String gender, int id){
    this.fName = fName;
    this.lName = lName;
    this.email = email;
    this.date = date;
    this.sNatId = natId;
    this.gender = gender;
    this.sId = id;
    }
    public Student(){
    }

    public int getsId() { return sId; }

    public void setsId(int sId) { this.sId = sId; }

    public String getsNatId() { return sNatId; }

    public void setsNatId(String sNatId) { this.sNatId = sNatId; }

    public String getfName() { return fName; }

    public void setfName(String fName) { this.fName = fName; }

    public String getlName() { return lName; }

    public void setlName(String lName) { this.lName = lName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }
}