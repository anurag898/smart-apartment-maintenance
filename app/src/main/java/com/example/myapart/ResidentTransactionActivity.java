package com.example.myapart;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResidentTransactionActivity extends AppCompatActivity {
    private static final String TAG = "ResidentTransactionActivity";
    FirebaseAuth mFirebaseAuth;
    TableLayout tableLayout;
    private TextView textViewData;
    public String[] id;
    public int i=0;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Transaction");
        setContentView(R.layout.activity_resident_transaction);
        textViewData = findViewById(R.id.textViewData);
        mFirebaseAuth = FirebaseAuth.getInstance();
        if(!CheckNetwork.isInternetAvailable(ResidentTransactionActivity.this))  //if connection available
        {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    protected void onStart() {
        super.onStart();

        CommonData cd=CommonData.getInstance();
        email=cd.getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference notebookRef = db.collection(email).document("transaction").collection("transaction");
        notebookRef.orderBy("Date", Query.Direction.DESCENDING).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                String data = "";
                TableLayout stk = (TableLayout) findViewById(R.id.table);
                TableRow tbrow0 = new TableRow(ResidentTransactionActivity.this);
                TextView tv0 = new TextView(ResidentTransactionActivity.this);
                tv0.setText(" Flat No ");
                tv0.setTextSize(20);
                tv0.setGravity(Gravity.CENTER);
                tv0.setTextColor(Color.BLACK);
                tbrow0.addView(tv0);
                TextView tv1 = new TextView(ResidentTransactionActivity.this);
                tv1.setText(" Date ");
                tv1.setTextSize(20);
                tv1.setGravity(Gravity.CENTER);
                tv1.setTextColor(Color.BLACK);
                tbrow0.addView(tv1);
                TextView tv2 = new TextView(ResidentTransactionActivity.this);
                tv2.setText(" Description ");
                tv2.setTextSize(20);
                tv2.setGravity(Gravity.CENTER);
                tv2.setTextColor(Color.BLACK);
                tbrow0.addView(tv2);
                TextView tv3 = new TextView(ResidentTransactionActivity.this);
                tv3.setText(" Amount ");
                tv3.setTextSize(20);
                tv3.setGravity(Gravity.CENTER);
                tv3.setTextColor(Color.BLACK);
                tbrow0.addView(tv3);
                stk.addView(tbrow0);

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject(Note.class);
                    // note.setDocumentId(documentSnapshot.getId());
                    // id[i]=notebookRef.document().getId();
                    String fno = note.getFlat_NO();
                    Timestamp date = note.getDate();
                    Date date1=date.toDate();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String strDate= formatter.format(date1);
                    String desc = note.getDescription();
                    String amount = note.getAmount();
                    String debitcredit=note.getDebit_Credit();

                    if(fno==null)
                    {
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(ResidentTransactionActivity.this);
                        builder.setTitle("No Internet !!!!");
                        builder
                                .setNegativeButton(
                                        "OK",
                                        new DialogInterface
                                                .OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which)
                                            {

                                                // If user click no
                                                // then dialog box is canceled.
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }


                    // data +="\t\t"+fno + "\t\t\t\t" + date + "\t\t\t\t" + desc + "\t\t\t\t\t" +amount+"\n\n";


                    TableRow tbrow = new TableRow(ResidentTransactionActivity.this);
                    TableLayout.LayoutParams tableRowParams=
                            new TableLayout.LayoutParams
                                    (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

                    int leftMargin=5;
                    int topMargin=15;
                    int rightMargin=10;
                    int bottomMargin=20;

                    tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

                    tbrow.setLayoutParams(tableRowParams);
                    TextView t1v = new TextView(ResidentTransactionActivity.this);
                    t1v.setText(fno);
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.CENTER);
                    tbrow.addView(t1v);
                    TextView t2v = new TextView(ResidentTransactionActivity.this);
                    t2v.setText(strDate);
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.CENTER);
                    tbrow.addView(t2v);
                    TextView t3v = new TextView(ResidentTransactionActivity.this);
                    t3v.setText(desc);
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.CENTER);
                    tbrow.addView(t3v);
                    TextView t4v = new TextView(ResidentTransactionActivity.this);
                    if(debitcredit.equals("Debit"))
                    {
                        amount="-"+amount;
                        t4v.setTextColor(Color.parseColor("#d32f2f"));
                    }
                    else
                    {
                        amount="+"+amount;
                        t4v.setTextColor(Color.parseColor("#7cb342"));
                    }
                    t4v.setText(amount);
                    t4v.setGravity(Gravity.CENTER);
                    tbrow.addView(t4v);
                    stk.addView(tbrow);
                    i++;
                }

            }
        });
    }

}