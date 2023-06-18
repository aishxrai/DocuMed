package in.aishxrai.documed;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aishxrai.documed.model.Document;

public class GeneratePdfActivity extends AppCompatActivity {

    File file;
    ArrayList<Uri> list = new ArrayList<>();

    String pdfURL;

//    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    private Map<String, Object> docuMedsMap = new HashMap<>();
    private List<Document> documentsList=new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private List<HashMap> objectList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_pdf);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        // Getting chat from Firestore
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
                        }

                        Log.i(TAG, "onComplete: "+objectList);

                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(GeneratePdfActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.w(TAG, "get failed with ", task.getException());
                    Toast.makeText(GeneratePdfActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        // getting images from previous activity
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        String documentType = args.getString("DocumentType");
        String hospitalName = args.getString("HospitalName");
        String date = args.getString("Date");
        list = (ArrayList<Uri>) args.getSerializable("images");

//////        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        // generating PDF
        createPDF();

        StorageReference ref = storageReference.child("DocuMed/" + System.currentTimeMillis() + ".pdf ");
        ref.putFile(Uri.fromFile(file)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        pdfURL = uri.toString();
                        Document document = new Document(documentType, hospitalName, date, pdfURL);
                        documentsList.add(document);

                        // sending messageList to Firebase
                        docuMedsMap.put("documents", documentsList);
                        firestore.collection("users")
                                .document(firebaseUser.getUid()).set(docuMedsMap, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "DocumentSnapshot added");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                        Toast.makeText(GeneratePdfActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        Log.i(TAG, "onSuccess: PDF uploaded to Firebase Cloud Storage");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "onFailure: ", e);
            }
        });

        // opening PDF
        open_file(file);
    }
    public void createPDF() {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(410,600,1).create();


        // creating new page for every image
        for (Uri uri : list) {
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            // Uri to Bitmap
            try {
                // Get the dimensions of the PDF page
                int pdfPageWidth = pageInfo.getPageWidth();
                int pdfPageHeight = pageInfo.getPageHeight();

                Bitmap bmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);

                // Calculate the scale factor to maintain the original ratio
                float scaleFactor = Math.min((float) pdfPageWidth / bmp.getWidth(), (float) pdfPageHeight / bmp.getHeight());

                // Calculate the dimensions of the scaled image
                int scaledImageWidth = (int) (bmp.getWidth() * scaleFactor);
                int scaledImageHeight = (int) (bmp.getHeight() * scaleFactor);

                // Calculate the position of the image at the center of the page
                int offsetX = (pdfPageWidth - scaledImageWidth) / 2;
                int offsetY = (pdfPageHeight - scaledImageHeight) / 2;

                // Create a new RectF with the calculated dimensions and position
                RectF rectangle = new RectF(offsetX, offsetY, offsetX + scaledImageWidth, offsetY + scaledImageHeight);

                canvas.drawBitmap(bmp, null, rectangle, null);
            } catch (IOException e)   {e.printStackTrace();}

            pdfDocument.finishPage(page);
        }



        // Giving filename
        file = new File(this.getExternalFilesDir(null), + System.currentTimeMillis() + ".pdf");

        // Saving PDF
        try {pdfDocument.writeTo(new FileOutputStream(file));}
        catch (IOException e)   {e.printStackTrace();}

        pdfDocument.close();
    }

    public void open_file(File file) {
        // Get URI & MIME type of file
        Uri uri = FileProvider.getUriForFile(this, "in.aishxrai.documed", file);
        String mime = getContentResolver().getType(uri);

        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Going back to Home Activity on Restarting this Activity
        startActivity(new Intent(GeneratePdfActivity.this, MainActivity.class));
    }
}