package in.aishxrai.documed;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class BottomSheetFragment extends BottomSheetDialogFragment
{
    TextView fd, cam;
    Bitmap bmp;
    Uri imageURI = null;
    byte[] byteArray;

    Button pdf;

    ArrayList<Uri> imageList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheetdialog, container, false);

        fd = view.findViewById(R.id.fd);
        cam = view.findViewById(R.id.cam);
        pdf = view.findViewById(R.id.pdf);

        Bundle b= getArguments();
        String documentType = b.getString("DocumentType");
        String hospitalName = b.getString("HospitalName");
        String date = b.getString("Date");

        fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Files & Docs", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Camera", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imagePath = createImage();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
                startActivityForResult(intent, 102);
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GeneratePdfActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("images", (Serializable) imageList);
                args.putString("DocumentType", documentType);
                args.putString("HospitalName", hospitalName);
                args.putString("Date", date);
                intent.putExtra("bundle", args);
                startActivity(intent);
            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case 101:
                    imageURI = data.getData();
                    break;
                case 102:
                    break;
            }
            // Uri to Bitmap
            try{bmp = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageURI);}
            catch (IOException e)   {e.printStackTrace();}

            // Bitmap to byte[]
            if (bmp != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
            }

            /* NOW WE HAVE SELECTED / CAPTURED IMAGE IN 3 FORMS:
                1) Uri    : imageURI
                2) Bitmap : bmp
                3) byte[] : byteArray

                Use whichever is required. */

            imageList.add(imageURI);
        }
    }

    private Uri createImage() {
        // Storing the image captured by Camera at "Pictures/DocuMed/Images/" so that we can get its URI
        ContentResolver resolver = getActivity().getApplicationContext().getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            imageURI = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        else
            imageURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_" + System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "DocuMed/" + "Images/");

        imageURI = resolver.insert(imageURI, contentValues);
        return imageURI;
    }

}
