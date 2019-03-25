package e.kiosque.appca.ModelData;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import e.kiosque.appca.ModelData.Etare.Etare;
import e.kiosque.appca.R;

public class EtareRecyclerViewAdapter extends
        RecyclerView.Adapter<EtareRecyclerViewAdapter.ViewHolder> {
    private List<Etare> listEtares;
    private Context context;
    private static ClickListener clickListener;

    private int upperCardSectionColor = 0;

    private int locationNameColor = 0;
    private int locationAddressColor = 0;
    private int locationPhoneHeaderColor = 0;


    public EtareRecyclerViewAdapter(List<Etare> styles, Context context, ClickListener cardClickListener) {
        this.context = context;
        this.listEtares = styles;
        this.clickListener = cardClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int singleRvCardToUse = R.layout.card;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(singleRvCardToUse, parent, false);
        return new ViewHolder(itemView);
    }

    public interface ClickListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return listEtares.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder card, int position) {

        Etare etareCard = listEtares.get(position);

        card.nameTextView.setText(etareCard.getNom());
        card.addressTextView.setText(etareCard.getAdress_ad());
        card.communeTextView.setText(etareCard.getCommune());

        card.constraintUpperColorSection.setBackgroundColor(upperCardSectionColor);
        card.nameTextView.setTextColor(locationNameColor);
        card.addressTextView.setTextColor(locationAddressColor);
        card.communeTextView.setTextColor(locationPhoneHeaderColor);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView addressTextView;
        TextView communeTextView;
        ConstraintLayout constraintUpperColorSection;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.location_name_tv);
            addressTextView = itemView.findViewById(R.id.location_description_tv);
            communeTextView = itemView.findViewById(R.id.location_phone_num_tv);

            constraintUpperColorSection = itemView.findViewById(R.id.constraint_upper_color);

            cardView = itemView.findViewById(R.id.map_view_location_card);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getLayoutPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
        }
    }
}
