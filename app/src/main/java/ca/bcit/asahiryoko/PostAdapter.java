package ca.bcit.asahiryoko;

import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
/***
 * This adapter uses a Post object and the database to display
 * information to the list view.
 * Author: Kevin Lee
 * StudentNo: A01185710
 * Data April 09, 2021
 * Version: 1.0
 */
public class PostAdapter extends ArrayAdapter<PostData> {
    private ArrayList<PostData> postList;
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("Uploads");
    private Context context;


    public PostAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PostData> objects) {
        super(context, resource, objects);
        this.postList = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_template, parent, false);
        }


        TextView postName = convertView.findViewById(R.id.post_name);
        TextView postDesc = convertView.findViewById(R.id.post_desc);
        ImageView postPic = convertView.findViewById(R.id.post_pic);

        postName.setText(postList.get(position).getPlaceName());
        postDesc.setText(postList.get(position).getDescription());

        // Get the image from the firebase storage.

        StorageReference storageImageLoc = mStorageReference.child( postList.get(position).getImageUrl() );
        storageImageLoc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into(postPic);
            }
        });


        return convertView;
    }
}
