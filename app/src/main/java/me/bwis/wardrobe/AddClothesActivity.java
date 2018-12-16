package me.bwis.wardrobe;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
import me.bwis.wardrobe.utils.SharedPreferenceUtils;

import static me.bwis.wardrobe.ClothesItemContract.ClothesSeasonEntry;
import static me.bwis.wardrobe.ClothesItemContract.ClothesItemEntry;

public class AddClothesActivity extends AppCompatActivity {

    protected View.OnClickListener mAddPictureButtonOnClickListener;
    protected View.OnClickListener mSelectColorButtonOnClickListener;
    protected ImageView mClothesImageView;
    protected View mColorSelectedView;
    protected MaterialButton mSelectColorButton;
    protected ColorPicker mColorPicker;
    protected boolean hasLoadedPicture;
    protected Spinner mTypeSpinner;
    protected int mSelectedColor;
    protected long mCurrentClothesID;



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
        mColorPicker = new ColorPicker(this, 62,39,35);
        mClothesImageView = findViewById(R.id.input_add_picture);
        mColorSelectedView = findViewById(R.id.input_color_selected);
        mSelectColorButton = findViewById(R.id.button_choose_color);
        mCurrentClothesID = System.currentTimeMillis();
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


    protected void onAddClothesDone()
    {
        EditText inName;  //输入名称
        Spinner inType;  //选择type
        CheckBox checkSpring;
        CheckBox checkSummer;
        CheckBox checkAutumn;
        CheckBox checkWinter;
        Button inColor;
        EditText inStore;
        EditText inBrand;
        EditText inPrice;
        ClothesItem addClothes;


        inName = (EditText) findViewById(R.id.input_add_name);
        inType = (Spinner) findViewById(R.id.input_add_type_spinner);
        checkSpring = (CheckBox) findViewById(R.id.add_checkbox_spring);
        checkSummer = (CheckBox) findViewById(R.id.add_checkbox_summer);
        checkAutumn = (CheckBox) findViewById(R.id.add_checkbox_autumn);
        checkWinter = (CheckBox) findViewById(R.id.add_checkbox_winter);

        inStore = (EditText) findViewById(R.id.input_add_store_location);
        inBrand = (EditText) findViewById(R.id.input_add_brand);
        inPrice = (EditText) findViewById(R.id.input_add_price);

        if (!hasLoadedPicture)
        {
            Toast.makeText(this, "Please upload a picture", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(checkSpring.isChecked() || checkSummer.isChecked() || checkAutumn.isChecked()
                    ||checkWinter.isChecked()))
        {
            Toast.makeText(this, "Please choose season", Toast.LENGTH_SHORT).show();
            return;
        }




        ContentValues values = new ContentValues();
        ClothesItemDBHelper dbHelper = new ClothesItemDBHelper(getApplication());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        values.put(ClothesItemEntry._ID, mCurrentClothesID);
        values.put(ClothesItemEntry.COLUMN_NAME_CLOTHES_NAME,inName.getText().toString());
        values.put(ClothesItemEntry.COLUMN_NAME_TYPE,inType.getSelectedItem().toString());
        values.put(ClothesItemEntry.COLUMN_NAME_PHOTO_PATH, this.mCurrentPhotoPath);

        values.put(ClothesItemEntry.COLUMN_NAME_COLOR,mSelectedColor);
        values.put(ClothesItemEntry.COLUMN_NAME_COLOR_TYPE, Constant.getNearestColorName(mSelectedColor));
        values.put(ClothesItemEntry.COLUMN_NAME_STORE_LOCATION,inStore.getText().toString());
        values.put(ClothesItemEntry.COLUMN_NAME_BRAND,inBrand.getText().toString());
        values.put(ClothesItemEntry.COLUMN_NAME_PRICE,Double.parseDouble(inPrice.getText().toString()));

        long ret = db.insert(ClothesItemEntry.TABLE_NAME,null,values);
        Log.d("AddClothesActivity", "add:"+Long.toString(ret));

        //添加除季节以外的其他部分



        if(checkSpring.isChecked())
        {
            values = new ContentValues();
            values.put(ClothesSeasonEntry._ID,mCurrentClothesID);
            values.put(ClothesSeasonEntry.COLUMN_NAME_SEASON, "Spring");
            db.insert(ClothesSeasonEntry.TABLE_NAME,null,values);
        }

        if (checkSpring.isChecked())
        {
            values = new ContentValues();
            values.put(ClothesSeasonEntry._ID, mCurrentClothesID);
            values.put(ClothesSeasonEntry.COLUMN_NAME_SEASON, "Spring");
            db.insert(ClothesSeasonEntry.TABLE_NAME,null,values);
        }
        if (checkSummer.isChecked())
        {
            values = new ContentValues();
            values.put(ClothesSeasonEntry._ID, mCurrentClothesID);
            values.put(ClothesSeasonEntry.COLUMN_NAME_SEASON, "Summer");
            db.insert(ClothesSeasonEntry.TABLE_NAME,null,values);
        }
        if (checkAutumn.isChecked())
        {
            values = new ContentValues();
            values.put(ClothesSeasonEntry._ID, mCurrentClothesID);
            values.put(ClothesSeasonEntry.COLUMN_NAME_SEASON, "Autumn");
            db.insert(ClothesSeasonEntry.TABLE_NAME,null,values);
        }
        if (checkWinter.isChecked())
        {
            values = new ContentValues();
            values.put(ClothesSeasonEntry._ID, mCurrentClothesID);
            values.put(ClothesSeasonEntry.COLUMN_NAME_SEASON, "Winter");
            db.insert(ClothesSeasonEntry.TABLE_NAME,null,values);
        }


        this.finish();

    }

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
        //Picasso.get().load(mCurrentPhotoPath).fit().centerInside().into(mClothesImageView);


        // use palette to get vibrant color
        Palette palette = Palette.from(bitmap).generate();
        Palette.Swatch dominantSwatch = palette.getDominantSwatch();
        mColorSelectedView.setBackgroundColor(dominantSwatch.getRgb());
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
        mTypeSpinner = findViewById(R.id.input_add_type_spinner);
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(SharedPreferenceUtils.getStringSet(Constant.PREF_TYPE_SET, Res.TYPE_SET));
        java.util.Collections.sort(list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);
    }


}
