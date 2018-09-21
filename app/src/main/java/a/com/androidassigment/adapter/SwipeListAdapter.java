package a.com.androidassigment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import a.com.androidassigment.R;
import a.com.androidassigment.model.RowsItems;

public class SwipeListAdapter extends BaseAdapter {

    private List<RowsItems> rowList = new ArrayList<>();

    Context context;
    public SwipeListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return rowList.size();
    }

    @Override
    public RowsItems getItem(int position) {
        if (position < 0 || position >= rowList.size()) {
            return null;
        } else {
            return rowList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view = (convertView != null ? convertView : createView(parent));
        final RowViewHolder viewHolder = (RowViewHolder) view.getTag();
        viewHolder.setRowViewHolder(getItem(position));
        return view;
    }


    public void setRows(List<RowsItems> rows) {
        if (rows == null) {
            return;
        }
        rowList.clear();
        rowList.addAll(rows);
        notifyDataSetChanged();
    }

    private View createView(ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.list_row, parent, false);
        final RowViewHolder viewHolder = new RowViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    private static class RowViewHolder {
        private TextView textTitle;
        private TextView textDescription;
        private ImageView image;

        public RowViewHolder(View view) {
            textTitle = view.findViewById(R.id.title);
            textDescription = view.findViewById(R.id.description);
            image = view.findViewById(R.id.list_image);
        }


        public void setRowViewHolder(RowsItems row) {
            textTitle.setText(row.title);
            textDescription.setText(row.description);

            if(row.imageHref.length()>0) {
                Picasso.get().load(row.imageHref).placeholder(R.drawable.loading).error(R.drawable.ic_error_outline_black_24dp).into(image);
            }else{

                image.setImageResource(R.drawable.ic_error_outline_black_24dp);
            }
        }
    }
}

