package me.bwis.wardrobe;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.button.MaterialButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.bwis.wardrobe.utils.Constant;
import me.bwis.wardrobe.utils.Res;
import me.bwis.wardrobe.utils.SharedPreferenceUtils;

public class NewPostActivity extends AppCompatActivity
{

    private View.OnClickListener mAddPictureButtonOnClickListener;
    private ImageView mClothesImageView;
    private boolean hasLoadedPicture;
    private EditText mEditText;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        mEditText = findViewById(R.id.new_post_edit);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        hasLoadedPicture = false;

        mClothesImageView = findViewById(R.id.new_post_add_picture);

        if (initOnClickListeners())
        {
            mClothesImageView.setOnClickListener(mAddPictureButtonOnClickListener);
        }

        mProgressBar = (ProgressBar) findViewById(R.id.new_post_upload_progress);
        mProgressBar.setVisibility(View.INVISIBLE);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                try
                {
                    onNewPostDone();
                }
                catch (IOException e)
                {
                    Log.e("OnNewPostDone", e.getMessage());
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onNewPostDone() throws FileNotFoundException
    {
        // post to server
        if (hasLoadedPicture)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = null;

            try
            {
                image = File.createTempFile(
                        "WRDB_POST_TEMP",  /* prefix */
                        ".png",         /* suffix */
                        storageDir      /* directory */

                );
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(image));
                bitmap.compress(Bitmap.CompressFormat.PNG, 30, bos);
                bos.flush();
                bos.close();

            } catch (IOException e)
            {
                e.printStackTrace();
            }
            Log.i("OnNewPostDone", "Picture Compress finished");
            final AVFile file = AVFile.withAbsoluteLocalPath(
                    AVUser.getCurrentUser().getUsername()+"_Post_"+Long.toString(System.currentTimeMillis())+".png"
                    , image.getAbsolutePath());
            final String[] picURL = new String[1];
            mProgressBar.setVisibility(View.VISIBLE);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null)
                    {
                        picURL[0] = file.getUrl();
                        Log.d("OnNewPostDone", file.getUrl());//返回一个唯一的 Url 地址
                        if (!picURL[0].isEmpty())
                        {

                            AVStatus status = AVStatus.createStatus(picURL[0], mEditText.getText().toString());
                            status.setInboxType(AVStatus.INBOX_TYPE.TIMELINE.toString());
                            AVStatus.sendStatusToFollowersInBackgroud(status, new SaveCallback() {
                                @Override
                                public void done(AVException avException) {
                                    Log.i("OnNewPostDone", "Send status finished.");
                                }
                            });
                        }
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {
                    mProgressBar.setProgress(integer);
                }
            });


        }

        this.finish();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discard?");
        builder.setMessage("You will lose all progress!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // delete temp file
                try
                {
                    File toBeDeleted = new File(mCurrentPhotoPath);
                    toBeDeleted.delete();
                }
                catch (Exception ex)
                {

                }
                NewPostActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    protected boolean initOnClickListeners()
    {

        this.mAddPictureButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder getImageFrom = new AlertDialog.Builder(NewPostActivity.this);

                getImageFrom.setTitle("Select:");
                final CharSequence[] opsChars = {getResources().getString(R.string.add_choose_camera),
                        getResources().getString(R.string.add_choose_gallery)};
                getImageFrom.setItems(opsChars, new android.content.DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            dispatchTakePictureIntent();
                        }else
                        if(which == 1){
                            dispatchGetFromGalleryIntent();
                        }
                        dialog.dismiss();
                    }
                });
                getImageFrom.show();

            }
        };

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case Constant.REQUEST_TAKE_PHOTO:
                if(resultCode == RESULT_OK){

                    setPictureOnImageView();
                }

                break;
            case Constant.REQUEST_GET_PHOTO_FROM_GALLERY:
                if(resultCode == RESULT_OK){

                    try
                    {
                        InputStream inputStream = getContentResolver()
                                .openInputStream(imageReturnedIntent.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(
                                mCurrentPhotoPath);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                        return;
                    }
                    setPictureOnImageView();

                }
                break;
        }
    }


    /////////////////////////////////// TAKE PHOTO ////////////////////////////////////////

    protected String mCurrentPhotoPath;

    protected File createImageFile() throws IOException {
        // Create an image file name
        File image;
        if (!hasLoadedPicture)
        {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "WRDB_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            hasLoadedPicture = true;

        }
        else
        {
            image = new File(mCurrentPhotoPath);
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    protected void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Constant.REQUEST_TAKE_PHOTO);
            }
        }
    }

    protected void setPictureOnImageView() {
        // Get the dimensions of the View
        int targetW = mClothesImageView.getWidth();
        int targetH = mClothesImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = (int)(Math.min(photoW/targetW, photoH/targetH)*1.5);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mClothesImageView.setImageBitmap(bitmap);

    }


    ///////////////////////////////////////////// FROM GALLERY ////////////////////////////////////////////////

    protected void dispatchGetFromGalleryIntent() {
        Intent fromGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            intent.setType("image/*");
//                            intent.setAction(Intent.ACTION_GET_CONTENT);

        if (fromGalleryIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                startActivityForResult(Intent.createChooser(fromGalleryIntent,
                        "Choose From Gallery"), Constant.REQUEST_GET_PHOTO_FROM_GALLERY);
            }

        }
    }


    protected static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }



}
