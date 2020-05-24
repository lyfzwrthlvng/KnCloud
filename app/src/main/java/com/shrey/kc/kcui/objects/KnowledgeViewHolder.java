package com.shrey.kc.kcui.objects;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shrey.kc.kcui.KnowledgeDetails;
import com.shrey.kc.kcui.R;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.KnowledgeOrTag;
import com.shrey.kc.kcui.workerActivities.AsyncCall;

import java.io.IOException;
import java.util.ArrayList;

public class KnowledgeViewHolder extends RecyclerView.ViewHolder {

    private TextView text;
    private Context context;
    private CardView cardView;

    public KnowledgeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.text = itemView.findViewById(R.id.text_view_in_card);
        this.cardView = (CardView) itemView;
        this.context = itemView.getContext();
    }

    public void bind(KnowledgeOrTag kot) throws IOException {
        this.text.setText(kot.isPrintTag() ? kot.getTag() : kot.getKnowledge());
        if(this.text.getText().length() > 20) {
            //this.text.setCompoundDrawables(null, null, new ClipDrawable((android.R.drawable.status_bar_item_app_background),0,0),null);
        }
        if(!kot.isPrintTag()) {
            setKnowledgeClick(kot);
        } else {
            setTagClick(kot);
        }
    }

    private void setKnowledgeClick(KnowledgeOrTag kot) {
        this.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsOfKnowledge = new Intent(context, KnowledgeDetails.class);
                detailsOfKnowledge.putExtra("knowledge", kot.getKnowledge());
                // the tag that referrred to this knowledge
                detailsOfKnowledge.putExtra("tag", kot.getTag());
                context.startActivity(detailsOfKnowledge);
            }
        });
    }

    private void setTagClick(KnowledgeOrTag kot) {
        this.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> al = new ArrayList<String>();
                al.add(kot.getTag());
                KCReadRequest readRequest = KCReadRequest.constructRequest(al);
                AsyncCall.startActionRead(context, readRequest);
            }
        });
    }
}
