package com.example.agendacontactos_gnsm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ViewHolder> {

    private List<Contacto> contactosList;

    public ContactosAdapter(List<Contacto> contactosList) {
        this.contactosList = contactosList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contacto contacto = contactosList.get(position);
        holder.textViewNombre.setText(contacto.getNombres());
    }

    @Override
    public int getItemCount() {
        return contactosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombre;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombreContacto);
        }
    }
}
