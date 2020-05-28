package com.shrey.kc.kcui.adaptors;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shrey.kc.kcui.R;
import com.shrey.kc.kcui.entities.KnowledgeOrTag;
import com.shrey.kc.kcui.objects.KnowledgeViewHolder;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.IOException;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class KnowledgeCardAdapter extends RecyclerView.Adapter<KnowledgeViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private List<KnowledgeOrTag> stuff;
    private int height;
    private boolean hash;

    public KnowledgeCardAdapter(List<KnowledgeOrTag> stuffRef, int height, boolean hash) {
        this.stuff = stuffRef;
        this.height = height;
        this.hash = hash;
    }

    @NonNull
    @Override
    public KnowledgeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView inflatedView = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.knowledge_card, viewGroup, false);
        setCardDims(inflatedView);
        return new KnowledgeViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull KnowledgeViewHolder viewHolder, int i) {
        try {
            viewHolder.bind(stuff.get(i), hash);
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
        return stuff.get(position).isPrintTag() ? stuff.get(position).getTag().substring(0,1)
                : stuff.get(position).getKnowledge().substring(0,1);
    }

    private void setCardDims(CardView cardView) {
        RecyclerView.LayoutParams layoutParams= (RecyclerView.LayoutParams) cardView.getLayoutParams();
        layoutParams.height= height;
        layoutParams.width=MATCH_PARENT;
    }
}
