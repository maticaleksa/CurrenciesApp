package com.example.currenciesapp.currencies_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currenciesapp.R;

import java.util.List;

/**
 * Adapter that populates currency list.
 */
public class CurrenciesListAdapter extends RecyclerView.Adapter<CurrenciesListAdapter.CurrenciesViewHolder> {

    private List<CurrencyViewModel> list;

    public void setList(List<CurrencyViewModel> list) {
        this.list = list;
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
        holder.currency.setText(list.get(position).currency);
        holder.currencyCode.setText(list.get(position).currencyCode);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class CurrenciesViewHolder extends RecyclerView.ViewHolder {
        private TextView currencyCode;
        private TextView currency;
        private EditText amount;
        public CurrenciesViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyCode = itemView.findViewById(R.id.currency_name_short);
            currency = itemView.findViewById(R.id.currency_name);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
