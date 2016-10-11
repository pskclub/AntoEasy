package com.mhalong.antoeasy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mhalong.antoeasy.R;
import com.mhalong.antoeasy.models.AntoChannelItem;
import com.mhalong.antoeasy.models.AntoChannelList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by passa on 10/10/2559.
 */

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {
    private AddSwitchChangeListenner listener;

    public interface AddSwitchChangeListenner {
        void onChange(boolean swChecked, int position);
    }

    public ChannelListAdapter() {
        // set null or default listener or accept as argument to constructor
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setSwitchChangeListenner(AddSwitchChangeListenner listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView description;
        private SwitchCompat swChannel;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.tvName);
            this.description = (TextView) itemView.findViewById(R.id.tvDescription);
            this.swChannel = (SwitchCompat) itemView.findViewById(R.id.swControl);
        }

    }

    @Override
    public ChannelListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View channelView = inflater.inflate(R.layout.list_item_channel, parent, false);

        // Return a new holder instance
        return new ViewHolder(channelView);
    }

    @Override
    public void onBindViewHolder(ChannelListAdapter.ViewHolder holder, int position) {
        holder.name.setText(AntoChannelList.getInstance().getChannelList().get(position).getName());
        holder.description.setText(
                "Think : " +
                        AntoChannelList.getInstance().getChannelList().get(position).getThink() +
                        " | Channel : " +
                        AntoChannelList.getInstance().getChannelList().get(position).getChannel()
        );
        if (AntoChannelList.getInstance().getChannelList().get(position).getValue() == 1) {
            holder.swChannel.setChecked(true);
        } else {
            holder.swChannel.setChecked(false);
        }
        final int positionN = position;
        final boolean swChecked = holder.swChannel.isChecked();
        holder.swChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChange(!swChecked, positionN);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (AntoChannelList.getInstance().getChannelList() == null) {
            return 0;
        }
        return AntoChannelList.getInstance().getChannelList().size();
    }


}
