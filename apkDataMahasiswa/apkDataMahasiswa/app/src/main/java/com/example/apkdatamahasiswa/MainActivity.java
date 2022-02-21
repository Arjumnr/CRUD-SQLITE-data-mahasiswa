package com.example.apkdatamahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editNis, editNama, editTlp;
    Button btnEntry, btnModify, btnViewAll, btnHapus, btnView, btnAbout;
    SQLiteDatabase conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNis = findViewById(R.id.nis);
        editNama = findViewById(R.id.nama);
        editTlp = findViewById(R.id.tlp);
        btnEntry = findViewById(R.id.entry);
        btnModify = findViewById(R.id.modify);
        btnViewAll = findViewById(R.id.viewAll);
        btnHapus = findViewById(R.id.hapus);
        btnView = findViewById(R.id.view);
        btnAbout = findViewById(R.id.about);
        conn = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        conn.execSQL("CREATE TABLE IF NOT EXISTS student(nis VARCHAR,nama VARCHAR,tlp VARCHAR);");
    }
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText() {
        editNis.setText("");
        editNama.setText("");
        editTlp.setText("");
        editNis.requestFocus();
    }

    public void add(View view) {
        if (editNis.getText().toString().trim().equals("") || editNama.getText().toString().equals("") ||
                editTlp.getText().toString().equals("")) {
            showMessage("Error", "Form Tidak Boleh Kosong");
            return;
        }

        conn.execSQL("INSERT INTO student VALUES('" + editNis.getText() + "','" + editNama.getText() + "','" + editTlp.getText() + "');");
        showMessage("Success", "Data Tersimpan");
        clearText();
    }

    public void delete(View view) {
        if (editNis.getText().toString().trim().equals("")) {
            showMessage("Error", "Nis Tidak Boleh Kosong");
            return;
        }

        @SuppressLint("Recycle") Cursor c = conn.rawQuery("SELECT * FROM student WHERE nis='" + editNis.getText() + "'", null);
        if (c.moveToFirst()) {
            conn.execSQL("DELETE FROM student WHERE nis='" + editNis.getText() + "'");
            showMessage("Success", "Record Deleted");
        } else {
            showMessage("Error", "Invalid NIS");
        }
        clearText();
    }

    public void update(View view) {
        if (editNis.getText().toString().trim().equals("")) {
            showMessage("Error", "Nis Tidak Boleh Kosong");
            return;
        }

        @SuppressLint("Recycle") Cursor c = conn.rawQuery("SELECT * FROM student WHERE nis='" + editNis.getText() + "'", null);
        if (c.moveToFirst()) {
            conn.execSQL("UPDATE student SET nama='" + editNama.getText() + "',tlp='" + editTlp.getText() +
                    "' WHERE nis='" + editNis.getText() + "'");
            showMessage("Success", "Data Terganti");
        } else {
            showMessage("Error", "Invalid NIS");
        }
        clearText();
    }

    public void view(View view){
        if (editNis.getText().toString().trim().equals("")) {
            showMessage("Error", "Nis Tidak Boleh Kosong");
            return;
        }

        @SuppressLint("Recycle") Cursor c=conn.rawQuery("SELECT * FROM student WHERE nis='"+editNis.getText()+"'", null);
        if(c.moveToFirst())
        {
            editNama.setText(c.getString(1));
            editTlp.setText(c.getString(2));
        }
        else
        {
            showMessage("Error", "Invalid NIS");
            clearText();
        }
    }

    public void viewAll(View view){
        @SuppressLint("Recycle") Cursor c=conn.rawQuery("SELECT * FROM student", null);
        if(c.getCount()==0)
        {
            showMessage("Error", "No records found");
            return;
        }
        StringBuilder buffer=new StringBuilder();
        while(c.moveToNext())
        {
            buffer.append("NIS    : ").append(c.getString(0)).append("\n");
            buffer.append("Nama   : ").append(c.getString(1)).append("\n");
            buffer.append("Telepon: ").append(c.getString(2)).append("\n\n");
        }
        showMessage("Student Details", buffer.toString());
    }

    public void showInfo(View view){
        showMessage("UNIVERSITAS DIPA MAKASSAR", "Developed By Rafli Rivan Fani");
    }
}