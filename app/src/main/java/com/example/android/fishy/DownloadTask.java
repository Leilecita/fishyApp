package com.example.android.fishy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class DownloadTask {

    private static final String TAG = "Download Task";
    private Context context;

    private String downloadUrl = "", downloadFileName = "";
    private ProgressDialog progressDialog;

    private String phone;

    public DownloadTask(Context context, String downloadUrl,String fileName, String phone) {
        this.context = context;

        this.downloadUrl = downloadUrl;

        downloadFileName = fileName;
        Log.e(TAG, downloadFileName);

        this.phone=phone;

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Downloading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Downloaded Successfully", Toast.LENGTH_SHORT).show();

                    sharePdfToSpecificNumber(outputFile);

                } else {
                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }

                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    //para este usar sharePdf2
                   /* apkStorage = new File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/"
                                    + "FISHY");*/

                   //para este usar sharePdfToSpecificNumber
                    apkStorage = new File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());


                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        boolean result = apkStorage.mkdir();
                        Log.e(TAG, "Directory Created. " + result);
                    }

                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    //Create New File if not present
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }

                    boolean result = outputFile.createNewFile();
                    Log.e(TAG, "File Created "+result);

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                    InputStream is = c.getInputStream();//Get InputStream for connection

                    byte[] buffer = new byte[1024];//Set buffer type
                    int len1 = 0;//init length
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);//Write new file
                    }

                    fos.close();
                    is.close();

                } else {
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }

    //este metodo abre el contacto y vos lo tenes que buscar la factura y mandarla
    public void sharePdf(File outputFile){
        Uri uriP = Uri.parse("smsto:" + this.phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO,uriP);
        intent.setPackage("com.whatsapp");

        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, outputFile);

        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // validate that the device can open your File!
        PackageManager pm = context.getPackageManager();
        if (intent.resolveActivity(pm) != null) {
            context.getApplicationContext().startActivity(Intent.createChooser(intent,""));
        }
    }

    //te abre el whatsapp , elegis contacto y envia documento directamente
    public void sharePdf2(File outputFile){

        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, outputFile);

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setPackage("com.whatsapp");

        context.startActivity(share);
    }

    //te abre el whatsapp en el contacto, tenes que buscar domcumento y enviarlo, para este guardar la factura en documentos.
    public void sharePdfToSpecificNumber(File outputFile){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+549"+this.phone));

            //esto esta al pepe porque no lo archiva directamente
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, outputFile);
            intent.putExtra(Intent.EXTRA_STREAM, uri);

            context.startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}