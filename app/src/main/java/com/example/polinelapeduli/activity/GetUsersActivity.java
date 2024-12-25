package com.example.polinelapeduli.activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.User;
import com.example.polinelapeduli.repository.UserRepository;

import java.util.List;
public class GetUsersActivity extends AppCompatActivity {
    private ListView listViewUsers;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        listViewUsers = findViewById(R.id.listView);
        userRepository = new UserRepository(this);
        initViews();
    }

    private void initViews() {
        // Fetch users from the repository
        List<User> listUsers = userRepository.getAllUsers();
        // Check if the list is empty or null
        if (listUsers == null || listUsers.isEmpty()) {
            Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a custom adapter and set it to the ListView
        UserAdapter adapter = new UserAdapter(this, listUsers);
        listViewUsers.setAdapter(adapter);
    }
}