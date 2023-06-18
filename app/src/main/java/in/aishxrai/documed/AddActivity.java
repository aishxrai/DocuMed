package in.aishxrai.documed;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aishxrai.documed.adapter.DocumentAdapter;
import in.aishxrai.documed.model.Document;

public class AddActivity extends AppCompatActivity {

    Button btn_add;

    private StorageReference storageReference;

    private Map<String, Object> docuMedsMap = new HashMap<>();
    private List<Document> documentsList=new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private List<HashMap> objectList;

    RecyclerView recyclerView;

    DocumentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btn_add = findViewById(R.id.btn_add);
        recyclerView = findViewById(R.id.rv);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        adapter = new DocumentAdapter(this, documentsList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        DocumentReference docRef = firestore.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        docuMedsMap = document.getData();
                        objectList = (List<HashMap>) docuMedsMap.get("documents");


                        if (objectList != null) {
                            for (int i = 0; i < objectList.size(); i++) {

                                Document document1 = new Document(objectList.get(i).get("documentType").toString(),objectList.get(i).get("hospitalName").toString()
                                        ,objectList.get(i).get("date").toString(),objectList.get(i).get("pdfUrl").toString());
                                documentsList.add(document1);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        Log.i(TAG, "onComplete: "+objectList);

                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(AddActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.w(TAG, "get failed with ", task.getException());
                    Toast.makeText(AddActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AddActivity.this, UpActivity.class));

            }
        });




    }
}