package ca.bcit.asahiryoko;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.InvalidParameterException;
/***
 * This Class is used to display a dialog when the user wants to
 * edit their profile. Provides user input to the program.
 * Author: Kevin Lee
 * StudentNo: A01185710
 * Data April 09, 2021
 * Version: 1.0
 */
public class EditFieldDialog extends AppCompatDialogFragment {
    private EditText editTextDialog;
    private TextView currentField;
    private String databaseField;
    private final String TAG = "EditNameDialog";


    /**
     * Get an instance of the Cloud Firestore.
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Get a reference to firebase auth.
     */
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    /** Create a Cloud Storage reference from the app */
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();


    public EditFieldDialog(TextView currentField, String databaseField) {
        this.currentField = currentField;
        if (databaseField.trim().length() == 0) {
            throw new InvalidParameterException("databaseField can't be empty");
        }
        this.databaseField = databaseField.trim().toLowerCase();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view).setTitle("edit " + databaseField)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentField.setText(editTextDialog.getText().toString());

                        // Get current user's data
                        DocumentReference docRef = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());

                        docRef.update(databaseField, editTextDialog.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Database successfully updated.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                    }
                });

        editTextDialog = view.findViewById(R.id.editTextDialog);

        // Return the dialog.
        return builder.create();
    }
}
