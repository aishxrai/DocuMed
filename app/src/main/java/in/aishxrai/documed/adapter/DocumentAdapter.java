package in.aishxrai.documed.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import in.aishxrai.documed.R;
import in.aishxrai.documed.model.Document;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private Context context;
    private List<Document> documents;

    public DocumentAdapter(Context context, List<Document> documents) {
        this.context = context;
        this.documents = documents;

    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_card, parent, false);
        DocumentViewHolder chatViewHolder = new DocumentViewHolder(view);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        holder.tv1.setText(documents.get(holder.getAdapterPosition()).getDocumentType());
        holder.tv2.setText(documents.get(holder.getAdapterPosition()).getHospitalName());
        holder.tv3.setText(documents.get(holder.getAdapterPosition()).getDate());


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(documents.get(holder.getAdapterPosition()).getPdfUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    class DocumentViewHolder extends RecyclerView.ViewHolder {

        TextView tv1, tv2, tv3;

        MaterialCardView card;



        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
           tv1 = itemView.findViewById(R.id.textView1);
           tv2 = itemView.findViewById(R.id.textView2);
           tv3 = itemView.findViewById(R.id.textView3);
           card = itemView.findViewById(R.id.card1);
        }
    }
}
