package com.example.se328projectalalem;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SQLlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBHelper MyDb;
        ArrayList<Student> Students;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqllist);

        MyDb = new DBHelper(this);
        EditText SQLID = (EditText) findViewById(R.id.searchSQLID);
        Button searchSQL = (Button) findViewById(R.id.SQLSearchList);
        ListView listSQL = (ListView) findViewById(R.id.SQLList);
        Students = new ArrayList<>();

        searchSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = SQLID.getText().toString();
                Cursor cursor;
                if (id.isEmpty()){
                    cursor = MyDb.viewAll();
                }
                else {
                    cursor = MyDb.viewByID(Integer.valueOf(id));
                }
                if(cursor==null){
                    //Toast.makeText(SQLlist.this,"No Student was found",Toast.LENGTH_LONG).show();
                    Toasty.error(SQLlist.this, "No Student was found", Toast.LENGTH_LONG, true).show();
                }
                Students.clear();
                do {
                    Student student = new Student();
                    student.fName = cursor.getString(0);
                    student.lName = cursor.getString(1);
                    student.email = cursor.getString(2);
                    student.date = cursor.getString(3);
                    student.sNatId = cursor.getString(4);
                    student.gender = cursor.getString(5);
                    student.sId = cursor.getInt(6);
                    Students.add(student);
                }while (cursor.moveToNext());
                ((BaseAdapter)listSQL.getAdapter()).notifyDataSetChanged();
            }
        });
        listSQL.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return Students.size();
            }

            @Override
            public Object getItem(int index) {
                return Students.get(index);
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int index, View view, ViewGroup viewGroup) {
                Student listStudent = Students.get(index);
                TableLayout table = (TableLayout) LayoutInflater.from(SQLlist.this).inflate(R.layout.list_items,viewGroup,false);
                TextView listFName = table.findViewById(R.id.listFName);
                TextView listLName = table.findViewById(R.id.listLName);
                TextView listEmail = table.findViewById(R.id.listEmail);
                TextView listDate = table.findViewById(R.id.listDate);
                TextView listNatID = table.findViewById(R.id.listNatID);
                TextView listID = table.findViewById(R.id.listID);
                TextView listGender = table.findViewById(R.id.listGender);

                listID.setText(""+listStudent.sId);
                listFName.setText(listStudent.fName);
                listLName.setText(listStudent.lName);
                listEmail.setText(listStudent.email);
                listDate.setText(listStudent.date);
                listNatID.setText(listStudent.sNatId);
                listGender.setText(listStudent.gender);
                return table;
            }
        });
        listSQL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                Student student = Students.get(index);
                //Toast.makeText(SQLlist.this,"Student ID: "+student.sId+", Student Name: "+student.fName+" "+ student.lName,Toast.LENGTH_SHORT).show();
                Toasty.warning(SQLlist.this, "Student ID: "+student.sId+", Student Name: "+student.fName+" "+ student.lName,Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}