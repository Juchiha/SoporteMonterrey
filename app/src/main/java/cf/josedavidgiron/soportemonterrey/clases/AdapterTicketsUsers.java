package cf.josedavidgiron.soportemonterrey.clases;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cf.josedavidgiron.soportemonterrey.FinalActivity;
import cf.josedavidgiron.soportemonterrey.Interfaces.ItemClickListener;
import cf.josedavidgiron.soportemonterrey.R;

public class AdapterTicketsUsers extends RecyclerView.Adapter<AdapterTicketsUsers.ViewHolder> {
    private ArrayList<FirebaseTickets> ticketsSoporte;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView textFechaTicket, textEstadoTicket, textMotivo, textDescripcion, textDireccion;
        public ImageView imageTicket;

        public ViewHolder(View itemView) {
            super(itemView);
            textFechaTicket = itemView.findViewById(R.id.textFechaTicket);
            textEstadoTicket = itemView.findViewById(R.id.textEstadoTicket);
            textMotivo = itemView.findViewById(R.id.textMotivoTicket);
            imageTicket = itemView.findViewById(R.id.imagenTicket);
            textDescripcion = itemView.findViewById(R.id.txtDescripcion);
            textDireccion = itemView.findViewById(R.id.textDireccion);

        }
    }


    public AdapterTicketsUsers(ArrayList<FirebaseTickets> ticketsSoporte, Context context){
        this.ticketsSoporte = ticketsSoporte;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return ticketsSoporte.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tickets_cardview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterTicketsUsers.ViewHolder viewHolder, int i) {
        viewHolder.textFechaTicket.setText(ticketsSoporte.get(i).getStrFechaC());
        viewHolder.textEstadoTicket.setText(ticketsSoporte.get(i).getStrEstado());
        viewHolder.textMotivo.setText(ticketsSoporte.get(i).getStrMotivo());
        if(ticketsSoporte.get(i).getStrDesc__().length() > 60){
            String sSubCadena = ticketsSoporte.get(i).getStrDesc__().substring(0,60);
            viewHolder.textDescripcion.setText(sSubCadena);
        }else{
            viewHolder.textDescripcion.setText(ticketsSoporte.get(i).getStrDesc__());
        }
        viewHolder.textDireccion.setText(ticketsSoporte.get(i).getStrDireccion());
        Glide.with(this.context)
                .load(ticketsSoporte.get(i).getStrFoto__())
                .fitCenter()
                .centerCrop()
                .into(viewHolder.imageTicket);
        /*viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View viewVista, int intPosition) {
                FirebaseTickets firebaseTickets = ticketsSoporte.get(intPosition);
                //Log.e("Esto::> ", "Numero ::> "+);
                Intent intent = new Intent(context, FinalActivity.class);
                intent.putExtra("FireTickets", firebaseTickets.getStrId____());
                context.startActivity(intent);
            }
        });*/
    }
}
