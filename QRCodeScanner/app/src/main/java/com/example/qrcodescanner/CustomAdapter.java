package com.example.qrcodescanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrcodescanner.databinding.ListItemBinding;
import com.example.qrcodescanner.roomdatabase.Data;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<Data> data;
    private OnItemClickListener listener;

    public CustomAdapter(List<Data> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.bind(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ListItemBinding binding;
        private int position;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ListItemBinding.bind(itemView);

            binding.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(position);
                }
            });

            binding.icDelete.setOnClickListener(view -> {
                listener.delete(position);
            });

        }

        public void bind(Data qr, int position) {
            this.position = position;
            binding.url.setText(showData(qr.QRType, qr.QRContent));
            binding.data.setText(qr.QRType);
            binding.date.setText(qr.QRDateAndTime);
        }

        public String showData(String type, String data) {
            String[] parts = data.split("\n");
            switch (type) {
                case "Wifi":
                case "Event":
                case "Contact":
                case "Business":
                    return parts[0].split(":")[1];
                default:
                    return data;
            }
        }

    }
}
