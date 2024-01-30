package com.example.almohadascomodasademsbonitas.pedidos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.almohadascomodasademsbonitas.R;

import java.util.ArrayList;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private ArrayList<Pedido> pedidos;

    public PedidoAdapter(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido_item, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);

        // Configurar los elementos de la vista según los datos del pedido
        holder.textViewIdPedido.setText(String.valueOf(pedido.getIdPedido()));
        holder.textViewIdPartner.setText(String.valueOf(pedido.getId_partner()));
        holder.textViewIdComercial.setText(String.valueOf(pedido.getId_comercial()));
        // ... Ajustar según tus campos de Pedido

        // Puedes agregar más elementos de la vista aquí
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    // Clase interna para representar la vista de cada elemento del RecyclerView
    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewIdPedido;
        TextView textViewIdPartner;
        TextView textViewIdComercial;
        // ... Otros elementos de la vista según tus campos de Pedido

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewIdPedido = itemView.findViewById(R.id.textIdPedido);
            textViewIdPartner = itemView.findViewById(R.id.textIdPartner);
            textViewIdComercial = itemView.findViewById(R.id.textIdComercial);
            // ... Otros elementos de la vista según tus campos de Pedido
        }
    }
}
