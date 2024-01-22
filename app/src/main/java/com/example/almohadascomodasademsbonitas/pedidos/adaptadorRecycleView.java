package com.example.almohadascomodasademsbonitas.pedidos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.almohadascomodasademsbonitas.R;

import java.util.ArrayList;
public class adaptadorRecycleView extends RecyclerView.Adapter<adaptadorRecycleView.ViewHolder> {
    private OnItemClickListener mListener;
    private ArrayList<String> mImagesUrls;
    private ArrayList<String> mNames = new ArrayList<>();

    private Context mcontext;
TextView name;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // Interfaz para el listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public adaptadorRecycleView(ArrayList<String> mImagesUrls, Context mcontext,ArrayList<String> mNames) {
        this.mImagesUrls = mImagesUrls;
        this.mcontext = mcontext;
        this.mNames = mNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageneslistview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageName = mImagesUrls.get(position);

        // Utiliza Glide para cargar la imagen directamente desde el nombre (sin necesidad de identificador numérico)
        Glide.with(mcontext)
                .asBitmap()
                .load(getImageResourceId(imageName))
                .into(holder.image);
        /*Glide.with(mcontext): Inicia la construcción de una solicitud de carga de Glide con el contexto proporcionado.
        .asBitmap(): Indica que la carga debe tratarse como un bitmap (formato de imagen).
        .load(getImageResourceId(imageName)): Carga la imagen utilizando el
        método getImageResourceId (que probablemente convierte el nombre de la imagen en un
        identificador de recurso, como un recurso drawable).
        .into(holder.image): Indica en qué ImageView (en este caso, holder.image) se debe cargar la imagen.*/

        // Logea el nombre de la imagen en lugar del identificador numérico
        Log.d("MainActivity", "Nuevo pedido: Imagen: " + imageName + ", Cantidad: 1");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(adapterPosition);
                        Toast toast = Toast.makeText(mcontext, mNames.get(adapterPosition), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
        // Puedes utilizar imageName como identificador único si es necesario para futuras operaciones.
    }


    @Override
    public int getItemCount() {
        return mImagesUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image1);
            name = itemView.findViewById(R.id.name);

            // Configura el listener para el clic en la imagen
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    // Método para obtener el identificador de recurso de imagen por nombre
    private int getImageResourceId(String imageName) {
        return mcontext.getResources().getIdentifier(imageName, "drawable", mcontext.getPackageName());
    }
}
