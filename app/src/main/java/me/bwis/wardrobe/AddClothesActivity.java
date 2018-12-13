package me.bwis.wardrobe;


import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ContentValues;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.button.MaterialButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.bwis.wardrobe.utils.Constant;
import me.bwis.wardrobe.utils.Res;

public class AddClothesActivity extends AppCompatActivity {

    private View.OnClickListener mAddPictureButtonOnClickListener;
    private View.OnClickListener mSelectColorButtonOnClickListener;
    private ImageView mClothesImageView;
    private View mColorSelectedView;
    private MaterialButton mSelectColorButton;
    private ColorPicker mColorPicker;
    private boolean hasLoadedPicture;
    private int mSelectedColor;
    protected String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        hasLoadedPicture = false;
        mColorPicker = new ColorPicker(AddClothesActivity.this, 62,39,35);
        mClothesImageView = findViewById(R.id.input_add_picture);
        mColorSelectedView = findViewById(R.id.input_color_selected);
        mSelectColorButton = findViewById(R.id.button_choose_color);
        if (initOnClickListeners())
        {
            mClothesImageView.setOnClickListener(mAddPictureButtonOnClickListener);
            mSelectColorButton.setOnClickListener(mSelectColorButtonOnClickListener);
        }
        addItemsToTypeSpinner();


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
                onAddClothesDone();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                AddClothesActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    @SuppressLint("WrongViewCast")
    /****************************************************/
    protected void onAddClothesDone()
    {
        EditText inname;  //输入名称
        Spinner intype;  //选择type
        CheckBox Spring;
        CheckBox Summer;
        CheckBox Autumn;
        CheckBox Winter;
        Button incolor;
        EditText instore;
        EditText inbrand;
        EditText inprice;
        ClothesItem addClothes;
        Button confirm;

        inname = (EditText) findViewById(R.id.input_add_name);
        intype = (Spinner) findViewById(R.id.input_add_type);
        Spring = (CheckBox) findViewById(R.id.Spring);
        Summer = (CheckBox) findViewById(R.id.Summer);
        Autumn = (CheckBox) findViewById(R.id.Autumn);
        Winter = (CheckBox) findViewById(R.id.Winter);
        incolor = (Button) findViewById(R.id.button_choose_color);
        instore = (EditText) findViewById(R.id.input_add_store_location);
        inbrand = (EditText) findViewById(R.id.input_add_brand);
        inprice = (EditText) findViewById(R.id.input_add_price);
        confirm = (Button) findViewById(R.id.action_done);

        ContentValues values = new ContentValues();
        DatabaseHelper dbHelper1 = new DatabaseHelper(this,"Base_Helper1");
        SQLiteDatabase Database1 = dbHelper1.getWritableDatabase();

        values.put("id",System.currentTimeMillis());
        values.put("name",inname.getText().toString());
        values.put("type",intype.getSelectedItem().toString());
        values.put("photoPah",mCurrentPhotoPath);
        //values.put(season,newclothes.seasons);
        //季节这里再考虑一下
        values.put("color",mSelectedColor);
        //缺少colorType
        values.put("storeLocation",instore.getText().toString());
        values.put("brand",inbrand.getText().toString());
        values.put("price",inprice.getText().toString());

        Database1.insert("user",null,values);
        Database1.close();

        //添加除季节以外的其他部分

        DatabaseHelper dbHelper2 = new DatabaseHelper(this,"Base_Helper2");
        SQLiteDatabase Database2 = dbHelper2.getWritableDatabase();

        values.put("id",System.currentTimeMillis());
        if(Spring.isChecked()) {
            values.put("season", "Spring");
            Database2.insert("user",null,values);
        }

        values.put("id",System.currentTimeMillis());
        if(Spring.isChecked()) {
            values.put("season", "Summer");
            Database2.insert("user",null,values);
        }

        values.put("id",System.currentTimeMillis());
        if(Spring.isChecked()) {
            values.put("season", "Autumn");
            Database2.insert("user",null,values);
        }

        values.put("id",System.currentTimeMillis());
        if(Spring.isChecked()) {
            values.put("season", "Winter");
            Database2.insert("user",null,values);
        }

        Database2.close();

        this.finish();

    }
    //Where Lee changed
    /***********************************************************/

    protected boolean initOnClickListeners()
    {
        // TODO init onClickListeners
        this.mSelectColorButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mColorPicker.show();
                /* On Click listener for the dialog, when the user select the color */
                Button okColor = (Button) mColorPicker.findViewById(R.id.okColorButton);

                okColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /* You can get single channel (value 0-255) */
                        int selectedColor = mColorPicker.getColor();
                        AddClothesActivity.this.mSelectedColor = selectedColor;
                        AddClothesActivity.this.mColorSelectedView.setBackgroundColor(selectedColor);
                        mColorPicker.dismiss();
                    }
                });
            }
        };

        this.mAddPictureButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder getImageFrom = new AlertDialog.Builder(AddClothesActivity.this);

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
// TODO
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

        // use palette to get vibrant color
        Palette palette = Palette.from(bitmap).generate();
        Palette.Swatch dominantSwatch = palette.getDominantSwatch();
        mColorSelectedView.setBackgroundColor(dominantSwatch.getRgb());
        mSelectedColor = dominantSwatch.getRgb();
        mColorPicker.setColor(dominantSwatch.getRgb());
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

    protected void addItemsToTypeSpinner()
    {
        Spinner spinner = findViewById(R.id.input_add_type_spinner);
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(Res.TYPE_SET);
        java.util.Collections.sort(list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }



}
