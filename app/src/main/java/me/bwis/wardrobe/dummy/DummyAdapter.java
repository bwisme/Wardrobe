package me.bwis.wardrobe.dummy;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.bwis.wardrobe.R;

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.DummyViewHolder> {

    private List<DummyItem> dummyItems;
    int selectedPos = RecyclerView.NO_POSITION;

    public DummyAdapter(List<DummyItem> dummyItems) {
        this.dummyItems = dummyItems;
    }


    @NonNull
    @Override
    public DummyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DummyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dummy_item_layout,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DummyViewHolder viewHolder, int i) {
        int someInt = dummyItems.get(i).someInt;
        String someString = dummyItems.get(i).someString;
        viewHolder.intView.setText(Integer.toString(someInt));
        viewHolder.stringView.setText(someString);
        viewHolder.itemView.setSelected(selectedPos == i);
    }

    @Override
    public int getItemCount() {
        return dummyItems.size();
    }

    public class DummyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView intView;
        TextView stringView;

        public DummyViewHolder(@NonNull View itemView) {
            super(itemView);
            intView = (TextView) itemView.findViewById(R.id.dummy_int);
            stringView = itemView.findViewById(R.id.dummy_string);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
        }
    }
}
