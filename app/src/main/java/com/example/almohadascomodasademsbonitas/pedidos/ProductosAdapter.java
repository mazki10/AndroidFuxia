package com.example.almohadascomodasademsbonitas.pedidos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.almohadascomodasademsbonitas.R;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {

    private List<Producto> productos;

    // Constructor para inicializar la lista de productos
    public ProductosAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    // Clase ViewHolder para contener las vistas de cada elemento del RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDescripcion;
        TextView textViewCantidad;
        TextView textViewPrecioUn;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewDescripcion = itemView.findViewById(R.id.text_view_descripcion);
            textViewCantidad = itemView.findViewById(R.id.text_view_cantidad);
            textViewPrecioUn = itemView.findViewById(R.id.text_view_precio_un);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.textViewDescripcion.setText("Descripción: " + producto.getDescripcion());
        holder.textViewCantidad.setText("Cantidad: " + producto.getCantidad());
        holder.textViewPrecioUn.setText("Precio Unitario: " + producto.getPrecio_un());
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }
}

