package com.example.currenciesapp.currencies_screen;

import android.text.Editable;
import android.util.Log;
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
import com.example.currenciesapp.domain.Money;
import com.example.currenciesapp.general.BasicTextWatcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Adapter that populates currency list.
 */
public class CurrenciesListAdapter extends RecyclerView.Adapter<CurrenciesListAdapter.CurrenciesViewHolder> {

    private List<ExchangeRatesViewModel> list = new ArrayList<>();
    private ExchangeRatesViewModel focusedItem;
    private CurrenciesViewHolder focusedHolder;

    interface ItemClickListener {
        void onItemClick();

        void onAmountChanged(Money money);
    }

    private ItemClickListener itemClickListener;

    public CurrenciesListAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getStableId();
    }

    public void setList(List<ExchangeRatesViewModel> list) {
        List<ExchangeRatesViewModel> old = this.list;
        if (focusedItem != null) {
            Collections.sort(list, (left, right) -> Integer.compare(old.indexOf(left), old.indexOf(right)));
            int index = list.indexOf(focusedItem);
            list.set(index, focusedItem);
        }
        this.list = list;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public CurrenciesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrenciesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrenciesViewHolder holder, int position) {
        ExchangeRatesViewModel exchangeRateViewModel = list.get(holder.getAdapterPosition());
        if (focusedHolder == null && holder.getAdapterPosition() == 0) {
            focusedHolder = holder;
            focusedItem = list.get(0);
            holder.amount.setFocusable(true);
            holder.amount.setFocusableInTouchMode(true);
        }
        holder.itemView.setOnClickListener(v -> {
            if (focusedHolder != null) {
                focusedHolder.amount.setFocusable(false);
                focusedHolder.amount.setFocusableInTouchMode(false);
            }
            focusedHolder = holder;
            holder.amount.setFocusable(true);
            holder.amount.setFocusableInTouchMode(true);
            focusedItem = list.get(holder.getAdapterPosition());
            ExchangeRatesViewModel first = list.get(0);
            list.set(holder.getAdapterPosition(), first);
            list.set(0, focusedItem);
            itemClickListener.onItemClick();
            notifyItemMoved(holder.getAdapterPosition(), 0);
        });
        holder.amount.addTextChangedListener(new BasicTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!holder.amount.isFocused() || input == null || input.equals(""))
                    return;
                if (input.equals(".")) input = "0";
                ExchangeRatesViewModel newModel = new ExchangeRatesViewModel(focusedItem.currencyCode, focusedItem.currency, Double.valueOf(input));
                list.set(list.indexOf(focusedItem), newModel);
                focusedItem = newModel;
                itemClickListener.onAmountChanged(
                        new Money(BigDecimal.valueOf(Float.valueOf(input)), Currency.getInstance(focusedItem.currencyCode))
                );
            }
        });
        holder.currency.setText(exchangeRateViewModel.currency);
        holder.currencyCode.setText(exchangeRateViewModel.currencyCode);

        if (!holder.amount.isFocused()) {
            holder.amount.setText(String.format(Locale.ENGLISH, "%.2f", exchangeRateViewModel.amount));
        }

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
