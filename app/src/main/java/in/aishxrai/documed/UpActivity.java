package in.aishxrai.documed;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.List;


public class UpActivity extends AppCompatActivity {

    Button btn_add;
   // BottomSheetDialog sheetDialog;

    EditText ed2, ed1;

    ChipGroup cg;

    TextView fd, cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up);
        btn_add = findViewById(R.id.btn_add);
        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        cg = findViewById(R.id.cg);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String documentType="";

                List<Integer> ids = cg.getCheckedChipIds();
                for (Integer id:ids){
                    Chip chip = cg.findViewById(id);
                    documentType = chip.getText().toString();
                }

                String hospitalName=ed1.getText().toString();
                String date=ed2.getText().toString();

                Bundle b = new Bundle();
                b.putString("DocumentType",documentType);
                b.putString("HospitalName",hospitalName);
                b.putString("Date",date);



                BottomSheetFragment bottomSheet = new BottomSheetFragment();
                bottomSheet.setArguments(b);
                bottomSheet.show(getSupportFragmentManager(), "bottomSheet");


//                sheetDialog = new BottomSheetDialog(UpActivity.this,R.style.BottomSheetStyle);

                View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheetdialog,
                        (ViewGroup) findViewById(R.id.sheet));











//                view1.findViewById(R.id.cam).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(UpActivity.this,"Cam is On", Toast.LENGTH_SHORT);
//                        sheetDialog.dismiss();
//                    }
//                });
//                view1.findViewById(R.id.fd).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(UpActivity.this,"Cam is On", Toast.LENGTH_SHORT);
//                        sheetDialog.dismiss();
//                    }
//                });

//                sheetDialog.setContentView(view1);

//                sheetDialog.show();
            }
        });

//        cam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                if (ActivityCompat.checkSelfPermission(UpActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                    ActivityCompat.requestPermissions(UpActivity.this, new String[] {android.Manifest.permission.CAMERA},1);
//            }
//        });

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder();
        constraints.setOpenAt(today);   // Setting today's date when it will open first time

        // Material Date Picker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Document Date");
        builder.setSelection(today);  // For default Selection means the current date ke liye hai
        builder.setCalendarConstraints(constraints.build());    // Setting constraints so that it cannot go beyond or below the start and end dates
        final MaterialDatePicker materialDatePicker = builder.build();

        ed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                ed2.setText(materialDatePicker.getHeaderText());
            }
        });
    }
}