package apit.net.sa.searchcontactapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import apit.net.sa.searchcontactapp.Models.UserModel;
import apit.net.sa.searchcontactapp.R;

public class ContactsAdapterFilter extends RecyclerView.Adapter<ContactsAdapterFilter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<UserModel> userModels;
    private List<UserModel> filteredUserModel;
    private ContactsAdapterListener listner;

    public ContactsAdapterFilter(Context context, List<UserModel> userModels, ContactsAdapterListener listner) {
        this.context = context;
        this.userModels = userModels;
        this.filteredUserModel = userModels;
        this.listner = listner;
    }

    public interface ContactsAdapterListener{
         void onContactSelected(UserModel userModel);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_row_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        UserModel userModel = filteredUserModel.get(i);
        viewHolder.name.setText(userModel.getName());
        viewHolder.phone.setText(userModel.getPhone());
        Glide.with(context)
                .load(userModel.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return filteredUserModel.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String result = charSequence.toString();
                List<UserModel> filterdList = new ArrayList<>();
                if(result.equals("")||result.isEmpty()){
                    filteredUserModel = userModels;
                }
                else {

                    for(UserModel userModel:userModels){
                        if(userModel.getName().toLowerCase().contains(result.toLowerCase())||
                           userModel.getPhone().contains(result)){
                            filterdList.add(userModel);
                        }
                    }
                    filteredUserModel = filterdList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredUserModel;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredUserModel = (List<UserModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
         TextView name, phone;
         ImageView thumbnail;
         MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onContactSelected(filteredUserModel.get(getAdapterPosition()));
                }
            });
        }
    }
}
