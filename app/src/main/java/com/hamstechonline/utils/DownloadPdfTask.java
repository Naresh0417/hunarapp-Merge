package com.hamstechonline.utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.hamstechonline.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class DownloadPdfTask {
    private static final String TAG = "Download Task";
    private Context context;

    private String downloadUrl = "", downloadFileName = "",CourseName = "";
    private ProgressDialog progressDialog;
    String mPdfPath;

    public DownloadPdfTask(Context context, String downloadUrl, String CourseName) {
        this.context = context;

        this.downloadUrl = downloadUrl;
        this.CourseName = CourseName;


        downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL
        //downloadFileName = "Sample3.pdf";//Create file name by picking download file name from URL
        downloadFileName = CourseName +new Date().getTime()+".jpg";

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
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage(context.getResources().getString(R.string.certificate_download_liner));
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    alertDialogBuilder.setNegativeButton("Open report",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Uri pdfURI = FileProvider.getUriForFile(context, "com.hamstechonline.fileprovider",
                                    new File(ApiConstants.BASE_PATH+"/"+downloadFileName));

                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(pdfURI);
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                context.startActivity(intent);

                            } catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alertDialogBuilder.show();
//                    Toast.makeText(context, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);
                    progressDialog.dismiss();

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);

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

                }


                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(ApiConstants.BASE_PATH);
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                //outputFile = null;
            }

            return null;
        }
    }
}
