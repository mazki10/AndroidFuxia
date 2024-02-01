package com.example.almohadascomodasademsbonitas.pedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.almohadascomodasademsbonitas.R;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private List<Pedido> listaDePedidos;
    private Context context;

    public PedidoAdapter(List<Pedido> listaDePedidos) {
        this.listaDePedidos = listaDePedidos;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = listaDePedidos.get(position);

        holder.textViewIdPedido.setText("ID Pedido: " + pedido.getIdPedido());
        holder.textViewIdPartner.setText("ID Partner: " + pedido.getId_partner());
        holder.textViewIdComercial.setText("ID Comercial: " + pedido.getId_comercial());
        holder.textViewFecha.setText("Fecha: " + pedido.getFecha().toString());
        holder.textViewPrecioTotal.setText("Precio Total: " + pedido.getPrecio_total());
    }

    @Override
    public int getItemCount() {
        return listaDePedidos.size();
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewIdPedido;
        TextView textViewIdPartner;
        TextView textViewIdComercial;
        TextView textViewFecha;
        TextView textViewPrecioTotal;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIdPedido = itemView.findViewById(R.id.text_view_id_pedido);
            textViewIdPartner = itemView.findViewById(R.id.text_view_id_partner);
            textViewIdComercial = itemView.findViewById(R.id.text_view_id_comercial);
            textViewFecha = itemView.findViewById(R.id.text_view_fecha);
            textViewPrecioTotal = itemView.findViewById(R.id.text_view_precio_total);
        }
    }
}

