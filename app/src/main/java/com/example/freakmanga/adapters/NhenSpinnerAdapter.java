package com.example.freakmanga.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.freakmanga.R;
import com.example.freakmanga.models.NhenSortModel;

import java.util.List;

public class NhenSpinnerAdapter extends ArrayAdapter<NhenSortModel> {
    private final Context context;
    private final List<NhenSortModel> sortByList;

    public NhenSpinnerAdapter(Context context, int textViewResourceId, List<NhenSortModel> sortByList) {
        super(context, textViewResourceId, sortByList);
        this.context = context;
        this.sortByList = sortByList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View rowView = LayoutInflater.from(context).inflate(R.layout.spinner_text_item, parent, false);
        TextView textProvinceName = rowView.findViewById(R.id.spinner_text_view);
        textProvinceName.setText(sortByList.get(position).getSortName());
        return rowView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View rowView = LayoutInflater.from(context).inflate(R.layout.spinner_text_item, parent, false);
        TextView textProvinceName = rowView.findViewById(R.id.spinner_text_view);
        textProvinceName.setText(sortByList.get(position).getSortName());
        return rowView;
    }
}