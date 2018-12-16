package me.bwis.wardrobe;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.bwis.wardrobe.utils.Constant;

public class ModifyClothesActivity extends AddClothesActivity
{

    private long mCurrentClothesID;
    private String mPreviousPhotoPath;
    private ClothesItemDatabase database;
    private ClothesItem mCurrentItem;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.mCurrentClothesID = intent.getLongExtra("currentID", 0);
        database = new ClothesItemDatabase(this.getApplication());
        List list = database.getClothesById(mCurrentClothesID);
        this.mCurrentItem = (ClothesItem) list.get(0);
        this.mPreviousPhotoPath = mCurrentItem.photoPath;
        this.mCurrentPhotoPath = mPreviousPhotoPath;
        this.mColorSelectedView = findViewById(R.id.input_color_selected);
        this.hasLoadedPicture = true;
        this.mClothesImageView = findViewById(R.id.input_add_picture);
        this.mSelectedColor = mCurrentItem.color;
        this.mColorSelectedView.setBackgroundColor(mSelectedColor);
        this.mColorPicker.setColor(mSelectedColor);
//        initCurrentClothesInfo();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initCurrentClothesInfo();
    }

    private void initCurrentClothesInfo()
    {
        EditText inName;  //输入名称
        Spinner inType;  //选择type
        CheckBox checkSpring;
        CheckBox checkSummer;
        CheckBox checkAutumn;
        CheckBox checkWinter;

        EditText inStore;
        EditText inBrand;
        EditText inPrice;

        inName = (EditText) findViewById(R.id.input_add_name);
        inType = (Spinner) findViewById(R.id.input_add_type_spinner);
        checkSpring = (CheckBox) findViewById(R.id.add_checkbox_spring);
        checkSummer = (CheckBox) findViewById(R.id.add_checkbox_summer);
        checkAutumn = (CheckBox) findViewById(R.id.add_checkbox_autumn);
        checkWinter = (CheckBox) findViewById(R.id.add_checkbox_winter);

        inStore = (EditText) findViewById(R.id.input_add_store_location);
        inBrand = (EditText) findViewById(R.id.input_add_brand);
        inPrice = (EditText) findViewById(R.id.input_add_price);

        inName.setText(mCurrentItem.name);
        inType.setSelection(((ArrayAdapter)mTypeSpinner.getAdapter()).getPosition(mCurrentItem.type));
        checkSpring.setChecked(mCurrentItem.seasons.contains("Spring"));
        checkSummer.setChecked(mCurrentItem.seasons.contains("Summer"));
        checkAutumn.setChecked(mCurrentItem.seasons.contains("Autumn"));
        checkWinter.setChecked(mCurrentItem.seasons.contains("Winter"));
        inStore.setText(mCurrentItem.storeLocation);
        inBrand.setText(mCurrentItem.brand);
        inPrice.setText(Double.toString(mCurrentItem.price));

        Picasso.get().load(new File(this.mCurrentPhotoPath)).resize(300,300)
                .centerInside()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(this.mClothesImageView);
//        setPictureOnImageView();
        this.mSelectedColor = mCurrentItem.color;
        this.mColorSelectedView.setBackgroundColor(mSelectedColor);
        this.mColorPicker = new ColorPicker(this, ((mSelectedColor&(0xFF0000)) >> 16),
                (mSelectedColor&(0x00FF00)) >> 8,(mSelectedColor&(0x0000FF)));



    }

    @Override
    protected void onAddClothesDone()
    {
        database.deleteClothes(mCurrentClothesID);
        super.onAddClothesDone();
    }

    @Override
    public void onBackPressed()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discard?");
        builder.setMessage("You will lose all progress!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // delete temp file
                try
                {
                    if (!mPreviousPhotoPath.equals(mCurrentPhotoPath))
                    {
                        File toBeDeleted = new File(mCurrentPhotoPath);
                        toBeDeleted.delete();
                    }

                }
                catch (Exception ex)
                {

                }
                ModifyClothesActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    protected void setPictureOnImageView()
    {

        Picasso.get().load(new File(mCurrentPhotoPath))
                .resize(300,300)
                .centerInside().memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(this.mClothesImageView);
        //Picasso.get().load(mCurrentPhotoPath).fit().centerInside().into(mClothesImageView);


        // use palette to get vibrant color


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

    @Override
    protected File createImageFile() throws IOException
    {
        // Create an image file name
        File image;
        if (mCurrentPhotoPath.equals(mPreviousPhotoPath))
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
            Log.d("createImageFile", "new photo path");

        }
        else
        {
            image = new File(mCurrentPhotoPath);
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
