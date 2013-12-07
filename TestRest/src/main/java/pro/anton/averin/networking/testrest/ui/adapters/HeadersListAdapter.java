package pro.anton.averin.networking.testrest.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pro.anton.averin.networking.testrest.models.Headers;

/**
 * Created by AAverin on 19.11.13.
 */
public class HeadersListAdapter extends ArrayAdapter<Headers.Header> {

    private Context context;

    public HeadersListAdapter(Context context, List<Headers.Header> headersList) {
        super(context, android.R.layout.simple_spinner_item, 0, headersList);
        this.context = context;

        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (convertView == null) {
            itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, null);
        }
        ((TextView)itemView).setText(getItem(position).name);
        return itemView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (convertView == null) {
            itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, null);
        }
        ((TextView)itemView).setText(getItem(position).name);
        return itemView;
    }
}