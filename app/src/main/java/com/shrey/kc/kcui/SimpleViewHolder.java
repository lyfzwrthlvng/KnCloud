package com.shrey.kc.kcui;

import android.view.View;
import android.widget.TextView;

import de.blox.graphview.ViewHolder;

public class SimpleViewHolder extends ViewHolder {
    TextView textView;

    SimpleViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.graph_node_card_text);
    }
}
