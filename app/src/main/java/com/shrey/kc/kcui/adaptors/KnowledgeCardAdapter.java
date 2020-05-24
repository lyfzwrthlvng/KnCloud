package com.shrey.kc.kcui.adaptors;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shrey.kc.kcui.R;
import com.shrey.kc.kcui.entities.KnowledgeOrTag;
import com.shrey.kc.kcui.objects.KnowledgeViewHolder;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.IOException;
import java.util.List;

public class KnowledgeCardAdapter extends RecyclerView.Adapter<KnowledgeViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private List<KnowledgeOrTag> stuff;

    public KnowledgeCardAdapter(List<KnowledgeOrTag> stuffRef) {
        this.stuff = stuffRef;
    }

    @NonNull
    @Override
    public KnowledgeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView inflatedView = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.knowledge_card, viewGroup, false);
        return new KnowledgeViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull KnowledgeViewHolder viewHolder, int i) {
        try {
            viewHolder.bind(stuff.get(i));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(KnowledgeCardAdapter.class.getName(), "error while binding " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return stuff.size();
    }

    @Override
    public String getSectionName(int position) {
        return stuff.get(position).isPrintTag() ? stuff.get(position).getTag().substring(0,1) : stuff.get(position).getKnowledge().substring(0,1);
    }
}
