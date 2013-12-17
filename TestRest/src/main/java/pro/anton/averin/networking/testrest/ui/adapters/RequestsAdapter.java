package pro.anton.averin.networking.testrest.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.models.Request;

/**
 * Created by AAverin on 17.12.13.
 */
public class RequestsAdapter extends ArrayAdapter<Request> {

    private Context context;

    public RequestsAdapter(Context context) {
        super(context, R.layout.detailed_request_list_item);
        this.context = context;
    }

    class ViewHolder {
        TextView requestName;
        TextView detailsExpandButton;
        LinearLayout detailsLayout;
        TextView detailsBaseurl;
        TextView detailsQuery;
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
            viewHolder.detailsExpandButton = (TextView) view.findViewById(R.id.details_expand_button);
            viewHolder.detailsLayout = (LinearLayout) view.findViewById(R.id.details_layout);
            viewHolder.detailsBaseurl = (TextView) view.findViewById(R.id.details_baseurl);
            viewHolder.detailsQuery = (TextView) view.findViewById(R.id.details_query);
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Request request = getItem(position);

        viewHolder.requestName.setText(request.name);

        viewHolder.detailsLayout.setOnClickListener(collapseClickListener);

        viewHolder.detailsExpandButton.setTag(viewHolder.detailsLayout);
        viewHolder.detailsExpandButton.setOnClickListener(expandClickListener);

        viewHolder.detailsBaseurl.setText(request.protocol + request.baseUrl);
        viewHolder.detailsQuery.setText(request.queryString);

        return view;
    }

    private View.OnClickListener expandClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LinearLayout layout = (LinearLayout) view.getTag();
            layout.setVisibility(View.VISIBLE);
        }
    };

    private View.OnClickListener collapseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.setVisibility(View.GONE);
        }
    };

}
