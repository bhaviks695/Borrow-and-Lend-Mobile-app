package com.example.bl;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bl.Models.Post;
import com.example.bl.Models.User;
import com.example.bl.util.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;




import static android.support.constraint.Constraints.TAG;


public class Lend_Fragment extends Fragment implements SelectPhotoDialog.OnPhotoSelectedListener {


    TextView itemname, itemdesc, itemprice, itemlocation ;
    private ImageView itemimageview;
    Button mPostbutton;

    private ProgressBar mProgressBar;

    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;
    private byte[] mUploadBytes;
    private double mProgress = 0;

    private FirebaseAuth mAuth;

    private StorageReference mStorageReference;
    private static final int REQUEST_CODE = 5678;

    String userphoneno;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lend, container, false);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        itemname = view.findViewById(R.id.itemname);
        itemdesc = view.findViewById(R.id.itemdesc);
        itemprice = view.findViewById(R.id.itemrentprice);
        itemimageview = view.findViewById(R.id.itemimage);
        itemlocation = view.findViewById(R.id.itemlocation);
        mPostbutton = view.findViewById(R.id.btnsaveitem);
        mProgressBar = view.findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        mStorageReference = FirebaseStorage.getInstance().getReference();


        getActivity().setTitle("Lend");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        init();

        getuserphoneno();

        return view;

    }

    public void getuserphoneno(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                userphoneno = user.getPhoneno();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void init() {
        itemimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPhotoDialog dialog = new SelectPhotoDialog();
                dialog.show(getFragmentManager(), getString(R.string.dialog_select_photo));
                dialog.setTargetFragment(Lend_Fragment.this, 1);
            }
        });

        mPostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(itemname.getText().toString())
                        && !isEmpty(itemprice.getText().toString())
                        && !isEmpty(itemdesc.getText().toString())
                        && !isEmpty(itemlocation.getText().toString())){

                    //We have a bitmap and no URI
                    if (mSelectedBitmap != null && mSelectedUri == null) {
                        uploadNewPhoto(mSelectedBitmap);
                    }
                    //we have no bitmap and uri
                    else if (mSelectedBitmap == null && mSelectedUri != null) {
                        uploadNewPhoto(mSelectedUri);
                    } else {
                        Toast.makeText(getActivity(), "All fields must be filled out", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void uploadNewPhoto(Bitmap bitmap) {
        BackgroundImageResize resize = new BackgroundImageResize(bitmap);
        Uri uri = null;
        resize.execute(uri);
    }

    private void uploadNewPhoto(Uri imagePath) {
        BackgroundImageResize resize = new BackgroundImageResize(null);
        resize.execute(imagePath);
    }

    public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {
        Bitmap mBitmap;

        public BackgroundImageResize(Bitmap bitmap) {
            this.mBitmap = bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Compressing image", Toast.LENGTH_SHORT).show();
            showProgressBar();
        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            if (mBitmap == null) {
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), params[0]);
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: IOEception" + e.getMessage());
                }
            }

            byte[] bytes = null;
            bytes = getByteFromBitmap(mBitmap, 100);
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            mUploadBytes = bytes;
            hideProgressBar();
            //execute upload task
            executeUploadTask();
        }
    }

    private void executeUploadTask() {
        Toast.makeText(getActivity(), "Uploading image", Toast.LENGTH_SHORT).show();

        final String postId = FirebaseDatabase.getInstance().getReference().push().getKey();

        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final String user_phoneno = userphoneno;



       // final String user_phone =

        final StorageReference storageReference =mStorageReference
                .child("posts/users/" + user_id + "/" + postId + "/post_image");

        final UploadTask uploadTask = storageReference.putBytes(mUploadBytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Post success", Toast.LENGTH_SHORT).show();

               mStorageReference.child("posts/users/" + user_id + "/" + postId + "/post_image").getDownloadUrl()
                       .addOnCompleteListener(new OnCompleteListener<Uri>() {
                           @Override
                           public void onComplete(@NonNull Task<Uri> task) {
                               Uri image = task.getResult();
                               DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                               Post post = new Post();
                               post.setImage(String.valueOf(image));
                               post.setPost_id(postId);
                               post.setItemname(itemname.getText().toString());
                               post.setItemprice(itemprice.getText().toString());
                               post.setItemdesc(itemdesc.getText().toString());
                               post.setItemlocation(itemlocation.getText().toString());
                               post.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                               post.setPhoneno(user_phoneno);

                               reference.child(getString(R.string.node_posts))
                                       .child(postId)
                                       .setValue(post);

                               clear();

                           }
                       });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Could not upload photo", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double currentProgress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                if (currentProgress > (mProgress + 15)) {
                    mProgress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    Toast.makeText(getActivity(), mProgress + "%", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static byte[] getByteFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    @Override
    public void getImagePath(Uri imagePath) {
        UniversalImageLoader.setImage(imagePath.toString(), itemimageview);
        //assign to global variable
        mSelectedBitmap = null;
        mSelectedUri = imagePath;
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        itemimageview.setImageBitmap(bitmap);
        //assign to global variable
        mSelectedUri = null;
        mSelectedBitmap = bitmap;
    }

    public void clear() {
       itemimageview.setImageResource(R.drawable.ic_android);
        itemname.setText(null);
        itemname.requestFocus();
        itemprice.setText(null);
        itemdesc.setText(null);
        itemlocation.setText(null);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

}


