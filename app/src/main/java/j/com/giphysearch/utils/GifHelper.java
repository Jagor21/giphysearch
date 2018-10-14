package j.com.giphysearch.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import j.com.giphysearch.entity.Gif;

public class GifHelper {

    private File file;
    private Gif mGif;
    private List<Gif> mGifs;
    private Context context;
    private String gifAbsPAth;
    private Uri uri;

    public GifHelper() {
    }

    public GifHelper(Context context, Gif gif) {
        this.context = context;
        this.mGif = gif;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private byte[] getBytesFromFile(File file) throws IOException {
        long fileLength = file.length();

        //Checking file size
        if (fileLength > Integer.MAX_VALUE) {
            throw new IOException("Fille is too big");
        }
        byte[] bytes = new byte[(int) fileLength];
        int offset = 0;
        int numRead = 0;
        InputStream is = new FileInputStream(file);
        try {
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }
        if (offset < bytes.length) {
            throw new IOException("Couldn't completely read the file" + file.getName());
        }
        return bytes;
    }

    private File saveGif(Context context, byte[] bytes, String imgName) {
        FileOutputStream os = null;
        String absPath = null;
        try {
            File externalStorage = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File giphySearchDirectory = new File(externalStorage, "Giphy Search");
            if (!giphySearchDirectory.exists()) {
                boolean mkdir = giphySearchDirectory.mkdirs();
                if (mkdir) {

                } else {
                    Log.i("MY_TAG", "saveGif: cannot create folder!");
                }
            }
            if (giphySearchDirectory.exists()) {
                File file = new File(giphySearchDirectory, imgName + ".gif");
                os = new FileOutputStream(file);
                os.write(bytes);
                os.flush();
                os.close();
                if (file != null) {
                    absPath = file.getAbsolutePath();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.Images.Media.TITLE, file.getName());
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
                    contentValues.put(MediaStore.Images.Media.DESCRIPTION, "");
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/gif");
                    contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                    contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                    contentValues.put(MediaStore.Images.Media.DATE_TAKEN, file.getAbsolutePath());
                    context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    Log.i("MY_TAG", "saveGif: " + absPath);
                    return file;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return file;
    }

    //Glide.downloadOnly() needs to be called async
    public void startSavingGif(boolean isShare) {
        new SaveGifAsyncTask(isShare).execute();
    }

    class SaveGifAsyncTask extends AsyncTask<Void, Void, File> {

        boolean isShare;
        File mFile;

        private SaveGifAsyncTask(boolean isShare) {
            this.isShare = isShare;
        }

        @Override
        protected File doInBackground(Void... voids) {
            RequestManager requestManager = Glide.with(context);
            try {
                file = requestManager.downloadOnly().load(mGif.getImages().getFixed_height().getUrl()).submit().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                uri = Uri.fromFile(file);
                try {
                    gifAbsPAth = file.getAbsolutePath();
                    mFile = saveGif(context, getBytesFromFile(file), mGif.getTitle());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (isShare) {
                    Uri gifUri = FileProvider.getUriForFile(context, "j.com.giphysearch.provider", mFile);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(gifUri);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, gifUri);
                    intent.setDataAndType(gifUri, context.getContentResolver().getType(gifUri));
                    context.startActivity(Intent.createChooser(intent, "Share using"));
                }
            }
        }
    }
}
