package com.example.polinelapeduli.activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.User;
import java.util.List;
public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> users;
    public UserAdapter(Context context, List<User> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }
        // Get the current user
        User user = users.get(position);
        // Bind the data to the views
        TextView fullNameTextView = convertView.findViewById(R.id.fullNameTextView);
        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        fullNameTextView.setText(user.getFullName());
        emailTextView.setText(user.getEmail());
        return convertView;
    }
}