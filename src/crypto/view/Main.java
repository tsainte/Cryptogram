package crypto.view;
import java.io.File;
import java.io.InputStream;

import crypto.gram.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;

public class Main extends Activity {

	private static final String TAG = "crypt";
	private static int TAKE_PICTURE = 1;
	private Uri outputFileUri;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    private static final int SELECT_PICTURE = 2;
    private String selectedImagePath;
    
    public void selectPhoto(View v){
    	 Intent intent = new Intent();
         intent.setType("image/*");
         intent.setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(Intent.createChooser(intent,
                 "Select Picture"), SELECT_PICTURE);	
    }
    public void takePhoto(View v) {

    	Intent intent =	 new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
 
		outputFileUri = Uri.fromFile(file);
		alert("x : " + outputFileUri.toString());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri.toString());
		startActivityForResult(intent, TAKE_PICTURE);
 
	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	 try{
    		 String path="";
    		 if (requestCode == TAKE_PICTURE){
	
				path = getPathImage();
				
				showImage(path);
				
    		 } else  if (requestCode == SELECT_PICTURE) {
	            Uri selectedImageUri = data.getData();
	            selectedImagePath = getPath(selectedImageUri);
	            showImage(selectedImagePath);
    	     }
    	 } catch (Exception e){
    		 e.printStackTrace();
    	 }
 
	}
    public void showImage(String path){
    	Bitmap imgBitmap = BitmapFactory.decodeFile(path);
        ImageView img = (ImageView)findViewById(R.id.imageView1);
        img.setImageBitmap(imgBitmap);
    }
    //metodo que pega o path da ultima foto tirada 
    private String getPathImage(){
        final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        final String imageOrderBy = MediaStore.Images.Media._ID+" DESC";
        Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
        if(imageCursor.moveToFirst()){
            int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));

            imageCursor.close();
            return fullPath;
        }else{
            return "";
        }
    }
    //pega o caminho da galeria de imagens
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public void codify(View v){
    	
    }
    public void decodify(View v){
    	
    }
   public void alert(String msg){
	   Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	  }
}
