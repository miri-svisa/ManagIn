package com.example.managein;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
//This department manages the camera, saving it in a file, performing an OCR on the image and searching the data in the text file (obtained from OCR)
public class Camera extends AppCompatActivity {

    private static final String TAG = "Camera";
    private ProgressDialog mProgressDialog;
    private TesseractOCR mTessOCR;
    private Context context;
    protected String mCurrentPhotoPath;
    private Uri photoURI1;
    private Uri oldPhotoURI;
    Mat imageMat;
    private static final String errorFileCreate = "Error file create!";
    private static final String errorConvert = "Error convert!";
    private static final int REQUEST_IMAGE1_CAPTURE = 1;
    ImageView firstImage;
    TextView ocrText;


    int PERMISSION_ALL = 1;
    boolean flagPermissions = false;
    //------------------------------------------------------------------------------------------------
    String[] PERMISSIONS = {
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    android.Manifest.permission.CAMERA
    };
    //------------------------------------------------------------------------------------------------
    private Imgcodecs Highgui;
    //------------------------------------------------------------------------------------------------
    //    Initializing the mLoaderCallback to the status
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    imageMat = new Mat();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    //------------------------------------------------------------------------------------------------
    //    This function updates photography-related variables and builds a TesseractOCR object
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        context = Camera.this;
        ButterKnife.bind(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        TakeAPic();
        String language = "eng";
        mTessOCR = new TesseractOCR(this, language);
    }
    //------------------------------------------------------------------------------------------------
    //This function is called after the fun startActivityForResult and its function in case the image was able to open the image file and send to OCR
//Otherwise, update the photoURI1 variable to the old one
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_IMAGE1_CAPTURE:{
                if (resultCode == RESULT_OK){
                    Bitmap bmp = null;
                    try {
                        InputStream ImageFile = getAssets().open("11232.jpg");
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        bmp = BitmapFactory.decodeStream(ImageFile, null, options);

                    } catch (Exception ex) {
                        Toast.makeText(context, errorConvert, Toast.LENGTH_SHORT).show();
                    }
                    if(bmp!=null)
                        doOCR(bmp);
                    else
                        Toast.makeText(context, "there is no invoice scan :(", Toast.LENGTH_SHORT).show();
                }else
                    {
                        photoURI1 = oldPhotoURI;
                        TakeAPic();
                    }

            }
        }
    }
    //------------------------------------------------------------------------------------------------
    //    Function responsible for taking a photo: opens a new file and path to the photo and opens the photo camera
    @SuppressLint({"NonConstantResourceId", "QueryPermissionsNeeded"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void TakeAPic() {
        //prepare intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(context, errorFileCreate, Toast.LENGTH_SHORT).show();
                Log.i("File error", ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                oldPhotoURI = photoURI1;
                photoURI1 = FileProvider.getUriForFile(this,
                        "com.example.android.filepr",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI1);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE1_CAPTURE);
            }
    }
    //------------------------------------------------------------------------------------------------
    //    A function that opens a new and unique image file and updates the mCurrentPhotoPath variable to the file path
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    //------------------------------------------------------------------------------------------------
    //    This function checks the Permissions and accordingly updates the variable flagPermissions
    @RequiresApi(api = Build.VERSION_CODES.M)
    void checkPermissions(){
        if (!hasPermissions(context, PERMISSIONS)) {
            requestPermissions(PERMISSIONS,
                    PERMISSION_ALL);
            flagPermissions = false;
        }
        flagPermissions = true;
    }
    //------------------------------------------------------------------------------------------------
    //    This function checks the Permissions
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    //------------------------------------------------------------------------------------------------
    //This function performs the OCR, uses Threads to speed up the work
    private void doOCR(final Bitmap bitmap) {

        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "Processing",
                    "read invoice data...", true);
        } else {
            mProgressDialog.show();}
        new Thread(new Runnable() {
            public void run() {

                final String srcText = mTessOCR.getOCRResult(bitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (srcText != null && !srcText.equals("")){
                            searchInvoiceData(srcText);
                        }
                        mProgressDialog.dismiss();
                    }
                });
            }
        }).start();
    }
    //------------------------------------------------------------------------------------------------
    //    This function calls the function written in the python that returns the data from the processed image file and this function inserts the data into the intent and transfers to
//    Activity InvoiceDetails
    private void searchInvoiceData(String srcText) {
        if (! Python.isStarted())
        {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("readInvoice");
        PyObject obj = pyObject.callAttr("main",srcText);
        String[] data3 = obj.toJava(String[].class);
        HelperUtil.isInvoiceDetalesBackPressed=false;
        Intent intent = new Intent(Camera.this, InvoiceDetails.class);
        intent.putExtra("ImageUri",mCurrentPhotoPath);
        intent.putExtra("StoreName", data3[0]);
        intent.putExtra("Date", data3[1]);
        intent.putExtra("Price", data3[2]);
        HelperUtil.isChangeDetails=false;
        startActivity(intent);
    }
    //---------------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        HelperUtil.isCameraActivityBackPressed = true;
    }
    //------------------------------------------------------------------------------------------------
    //    This function pre-processes the image to improve OCR performance
    private void opencv(InputStream imageFile) {
        imageMat = Imgcodecs.imread("11.jpeg", 0);
// apply Otsu threshold
        Mat bw = new Mat(imageMat.size(), CvType.CV_8U);
        Imgproc.threshold(imageMat, bw, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
// take the distance transform
        Mat dist = new Mat(imageMat.size(), CvType.CV_32F);
        Imgproc.distanceTransform(bw, dist, Imgproc.CV_DIST_L2, Imgproc.CV_DIST_MASK_PRECISE);
// threshold the distance transform
        Mat dibw32f = new Mat(imageMat.size(), CvType.CV_32F);
        final double SWTHRESH = 8.0;    // stroke width threshold
        Imgproc.threshold(dist, dibw32f, SWTHRESH/2.0, 255, Imgproc.THRESH_BINARY);
        Mat dibw8u = new Mat(imageMat.size(), CvType.CV_8U);
        dibw32f.convertTo(dibw8u, CvType.CV_8U);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
// open to remove connections to stray elements
        Mat cont = new Mat(imageMat.size(), CvType.CV_8U);
        Imgproc.morphologyEx(dibw8u, cont, Imgproc.MORPH_OPEN, kernel);
        // find contours and filter based on bounding-box height
        final double HTHRESH = imageMat.rows() * 0.5; // bounding-box height threshold
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        List<Point> digits = new ArrayList<Point>();    // contours of the possible digits
        Imgproc.findContours(cont, contours, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        for (int i = 0; i < contours.size(); i++)
        {
            if (Imgproc.boundingRect(contours.get(i)).height > HTHRESH)
            {
                // this contour passed the bounding-box height threshold. add it to digits
                digits.addAll(contours.get(i).toList());
            }
        }
// find the convexhull of the digit contours
        MatOfInt digitsHullIdx = new MatOfInt();
        MatOfPoint hullPoints = new MatOfPoint();
        hullPoints.fromList(digits);
        Imgproc.convexHull(hullPoints, digitsHullIdx);
// convert hull index to hull points
        List<Point> digitsHullPointsList = new ArrayList<Point>();
        List<Point> points = hullPoints.toList();
        for (Integer i: digitsHullIdx.toList())
        {
            digitsHullPointsList.add(points.get(i));
        }
        MatOfPoint digitsHullPoints = new MatOfPoint();
        digitsHullPoints.fromList(digitsHullPointsList);
// create the mask for digits
        List<MatOfPoint> digitRegions = new ArrayList<MatOfPoint>();
        digitRegions.add(digitsHullPoints);
        Mat digitsMask = Mat.zeros(imageMat.size(), CvType.CV_8U);
        Imgproc.drawContours(digitsMask, digitRegions, 0, new Scalar(255, 255, 255), -1);
// dilate the mask to capture any info we lost in earlier opening
        Imgproc.morphologyEx(digitsMask, digitsMask, Imgproc.MORPH_DILATE, kernel);
// cleaned image ready for OCR
        Mat cleaned = Mat.zeros(imageMat.size(), CvType.CV_8U);
        dibw8u.copyTo(cleaned, digitsMask);
// feed cleaned to Tesseract
    }
}

