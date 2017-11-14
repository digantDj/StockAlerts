package com.digantjagtap.stockalerts.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.digantjagtap.stockalerts.Model.Alert;
import com.digantjagtap.stockalerts.R;

import java.util.ArrayList;

/**
 * Created by digantjagtap on 11/4/16.
 */

public class ListViewAdapter extends BaseAdapter implements Filterable{

    private ArrayList<Alert> allAlerts;
    public ArrayList<Alert> orig;
    private Context context;
    private LayoutInflater inflater;

    public ListViewAdapter(Context context, ArrayList<Alert> allAlerts) {
        super();
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.allAlerts = allAlerts;
    }

    @Override
    public int getCount() {
        return allAlerts.size();
    }

    @Override
    public Object getItem(int position) {
        return allAlerts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AlertHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_alert, parent, false);
            holder = new AlertHolder();
            assert view != null;
            holder.alertSymbol = (TextView) view.findViewById(R.id.alertSymbol);
            holder.alertCurrentPrice = (TextView) view.findViewById(R.id.alertCurrentPrice);
            view.setTag(holder);
        } else {
            holder = (AlertHolder) view.getTag();
        }

        holder.alertSymbol.setText(allAlerts.get(position).getSymbol());
        holder.alertCurrentPrice.setText(allAlerts.get(position).getCurrentPrice());

        return view;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Alert> results = new ArrayList<Alert>();
                if (orig == null)
                    orig = allAlerts;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Alert g : orig) {
                           // if (g.getCategory().toLowerCase()
                             //       .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                allAlerts = (ArrayList<Alert>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

class AlertHolder {
    TextView alertSymbol;
    TextView alertCurrentPrice;
}