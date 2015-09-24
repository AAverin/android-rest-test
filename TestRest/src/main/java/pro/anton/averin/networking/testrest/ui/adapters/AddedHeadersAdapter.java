package pro.anton.averin.networking.testrest.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.models.RequestHeader;

/**
 * Created by AAverin on 27.11.13.
 */
public class AddedHeadersAdapter extends ArrayAdapter<RequestHeader> {

    private OnHeaderChangeListener listener = null;
    private Context context;
    public AddedHeadersAdapter(Context context, List<RequestHeader> headersList) {
        super(context, 0, 0, headersList);
        this.context = context;

        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setOnHeaderChangeListener(OnHeaderChangeListener l) {
        listener = l;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.view_header, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.headerName = (TextView) view.findViewById(R.id.name);
            viewHolder.headerValue = (TextView) view.findViewById(R.id.value);
            viewHolder.deleteButton = (ImageButton) view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        RequestHeader element = getItem(position);
        holder.headerName.setText(element.name);
        holder.headerValue.setText(element.value);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(getItem(position));
                if (listener != null) {
                    listener.onDelete(position);
                }
            }
        });

        return view;
    }

    public interface OnHeaderChangeListener {
        public void onDelete(int position);
    }

    private class ViewHolder {
        public TextView headerName;
        public TextView headerValue;
        public ImageButton deleteButton;
    }
}
