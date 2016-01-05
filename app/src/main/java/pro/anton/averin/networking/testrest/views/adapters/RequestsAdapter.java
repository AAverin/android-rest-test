package pro.anton.averin.networking.testrest.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.data.models.Request;

/**
 * Created by AAverin on 17.12.13.
 */
public class RequestsAdapter extends ArrayAdapter<Request> {

    private Context context;
    private View.OnClickListener expandClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LinearLayout layout = (LinearLayout) view.getTag();
            layout.setVisibility(View.VISIBLE);
            view.setOnClickListener(collapseClickListener);
        }
    };
    private View.OnClickListener collapseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LinearLayout layout = (LinearLayout) view.getTag();
            layout.setVisibility(View.GONE);
            view.setOnClickListener(expandClickListener);
        }
    };
    private View.OnClickListener layoutCollapseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.setVisibility(View.GONE);
        }
    };

    public RequestsAdapter(Context context) {
        super(context, R.layout.detailed_request_list_item);
        this.context = context;
    }

    public void setData(List<Request> requests) {
        clear();
        if (requests != null) {
            for (int i = 0; i < requests.size(); i++) {
                add(requests.get(i));
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.detailed_request_list_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.requestName = (TextView) view.findViewById(R.id.request_name);
            viewHolder.detailsExpandButton = (ImageButton) view.findViewById(R.id.details_expand_button);
            viewHolder.detailsLayout = (LinearLayout) view.findViewById(R.id.details_layout);
            viewHolder.details = (TextView) view.findViewById(R.id.details);
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Request request = getItem(position);

        viewHolder.requestName.setText(request.name);

        viewHolder.detailsLayout.setOnClickListener(layoutCollapseClickListener);

        viewHolder.detailsExpandButton.setTag(viewHolder.detailsLayout);
        viewHolder.detailsExpandButton.setOnClickListener(expandClickListener);

        viewHolder.details.setText(request.protocol + request.baseUrl + request.queryString);

        view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.entrieslist_selector));

        return view;
    }

    class ViewHolder {
        TextView requestName;
        ImageButton detailsExpandButton;
        LinearLayout detailsLayout;
        TextView details;
    }

}
