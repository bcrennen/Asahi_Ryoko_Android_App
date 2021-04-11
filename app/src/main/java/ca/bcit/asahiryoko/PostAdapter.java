package ca.bcit.asahiryoko;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
/***
 * This adapter uses a Post object and the database to display
 * information to the list view.
 * Author: Kevin Lee
 * StudentNo: A01185710
 * Data April 09, 2021
 * Version: 1.0
 */
public class PostAdapter extends ArrayAdapter<Post> {
    private ArrayList<Post> postList;

    public PostAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Post> objects) {
        super(context, resource);
        this.postList = (ArrayList<Post>) objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_template, parent, false);
        }

        //TODO: Get View elements from convertView

        //TODO: Set values from postList to ConvertView Elements


        return convertView;
    }
}
