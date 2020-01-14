package com.example.currenciesapp.currencies_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.currenciesapp.R;

import java.util.List;

/**
 * Adapter that populates currency list.
 */
public class CurrenciesListAdapter extends RecyclerView.Adapter<CurrenciesListAdapter.CurrenciesViewHolder> {

    private List<ExchangeRatesViewModel> list;
    private ExchangeRatesViewModel focusedItem;

    interface ItemClickListener {
        void onItemClick();
    }
    private ItemClickListener itemClickListener;

    public CurrenciesListAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setList(List<ExchangeRatesViewModel> list) {
        this.list = list;
        if (focusedItem != null) {
            int index = list.indexOf(focusedItem);
            ExchangeRatesViewModel first = list.get(0);
            this.list.set(0, focusedItem);
            this.list.set(index, first);
        }
        notifyDataSetChanged();
        // TODO: 1/8/2020 use diffutil
    }

    @NonNull
    @Override
    public CurrenciesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrenciesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrenciesViewHolder holder, int position) {
        ExchangeRatesViewModel exchangeRateViewModel = list.get(position);
        holder.itemView.setOnClickListener(v -> {
            ExchangeRatesViewModel selected = list.get(position);
            ExchangeRatesViewModel first = list.get(0);
            focusedItem = selected;
            list.set(position, first);
            list.set(0, selected);
            itemClickListener.onItemClick();
            notifyItemMoved(position, 0);
        });
        holder.currency.setText(exchangeRateViewModel.currency);
        holder.currencyCode.setText(exchangeRateViewModel.currencyCode);
        holder.amount.setText(String.valueOf(exchangeRateViewModel.rate));
        Glide.with(holder.itemView)
                .load("https://www.countryflags.io/" + exchangeRateViewModel.currencyCode.substring(0, 2) + "/flat/64.png")
                .into(holder.flagIcon);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class CurrenciesViewHolder extends RecyclerView.ViewHolder {
        private TextView currencyCode;
        private TextView currency;
        private EditText amount;
        private ImageView flagIcon;

        public CurrenciesViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyCode = itemView.findViewById(R.id.currency_code);
            currency = itemView.findViewById(R.id.currency_name);
            amount = itemView.findViewById(R.id.amount);
            flagIcon = itemView.findViewById(R.id.flag_icon);
        }
    }

}
