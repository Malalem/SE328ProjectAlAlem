package com.example.se328projectalalem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class FireList extends AppCompatActivity {
    ArrayList<Student> studentsList = new ArrayList<>();
    ArrayList<Student> listKeys = new ArrayList<>();
    ListView firelist;
    DatabaseReference Sref;
    int listIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_list);

        Sref = FirebaseDatabase.getInstance().getReference("Students");

        firelist = (ListView) findViewById(R.id.fireBaseList);
        EditText fireSearch = (EditText) findViewById(R.id.searchFireID);

        TextWatcher watcher =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    listIndex=-1;
                }else{
                    listIndex=Integer.valueOf(s.toString());
                }
                updateFireList();
            }
        };
        fireSearch.addTextChangedListener(watcher);
        firelist.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return studentsList.size();
            }

            @Override
            public Object getItem(int index) {
                return studentsList.get(index);
            }

            @Override
            public long getItemId(int index) {
                return 0;
            }

            @Override
            public View getView(int index, View view, ViewGroup viewGroup) {
                TableLayout table = (TableLayout) LayoutInflater.from(FireList.this).inflate(R.layout.list_items,viewGroup,false);
                TextView listFName = table.findViewById(R.id.listFName);
                TextView listLName = table.findViewById(R.id.listLName);
                TextView listEmail = table.findViewById(R.id.listEmail);
                TextView listDate = table.findViewById(R.id.listDate);
                TextView listNatID = table.findViewById(R.id.listNatID);
                TextView listID = table.findViewById(R.id.listID);
                TextView listGender = table.findViewById(R.id.listGender);

                Student listStudent = studentsList.get(index);

                listFName.setText(listStudent.fName);
                listLName.setText(listStudent.lName);
                listEmail.setText(listStudent.email);
                listDate.setText(listStudent.date);
                listNatID.setText(listStudent.sNatId);
                listID.setText(""+listStudent.sId);
                listGender.setText(listStudent.gender);
                return table;
            }
        });
        Sref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listKeys = snapshot.getValue(new GenericTypeIndicator<ArrayList<Student>>() {});
                ArrayList<Student> newStudentsList = new ArrayList<>();
                for (Student s:listKeys){
                    if (s != null){
                        newStudentsList.add(s);
                        Log.d("Mohammed",snapshot.toString());
                    }
                }
                listKeys = newStudentsList;
                updateFireList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Mohammed List ",error.getMessage());
                //Toast.makeText(FireList.this,"Error: " + error.getMessage(),Toast.LENGTH_LONG).show();
                Toasty.error(FireList.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG, true).show();
            }
        });
    }
    public void updateFireList(){
        if (listIndex != -1){
            for (Student s:listKeys){
                if (listIndex == s.getsId()){
                    studentsList.clear();
                    studentsList.add(s);
                }
            }
        }
        else{
            studentsList.clear();
            studentsList.addAll(listKeys);
        }
        ((BaseAdapter)firelist.getAdapter()).notifyDataSetChanged();
    }

}