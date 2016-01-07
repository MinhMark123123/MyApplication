package empire.stark.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import java.io.File;
import java.io.IOException;

/**
 * Created by YENMINH on 1/7/2016.
 * Code and Life
 */
public class RecorderCallService extends Service {
    private MediaRecorder mMediaRecorder;
    private boolean mIsRecording = false;
    private final String FOLDER_NAME = "RecorderPhoneCall";
    private String mFileType = ".3gp";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener phoneStateListener = new PhoneStateListener();
        telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        //
        return START_STICKY;
    }
    private void startRecording(String phoneNumber){
        if(!mIsRecording){

            //
            String outPutFile = getOutputFile(phoneNumber);
            if(outPutFile != null){

                setupMediaRecorder(MediaRecorder.AudioSource.VOICE_CALL, MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.AudioEncoder.AAC);
                //setupBitRate();
                setOutPutFile(outPutFile);
                try{
                    mMediaRecorder.release();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    mMediaRecorder.start();
                }catch (Exception e){
                    e.printStackTrace();
                }

                mIsRecording = true;
            }
        }
    }

    /**
     *
     */
    private void setupMediaRecorder(int audioSource, int outputFormat, int audioEncoder){
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(audioSource);
        mMediaRecorder.setOutputFormat(outputFormat);
        mMediaRecorder.setAudioEncoder(audioEncoder);
    }
    private void setupBitRate(int encodingBitrate, int samplingBitrate){
        mMediaRecorder.setAudioEncodingBitRate(encodingBitrate);
        mMediaRecorder.setAudioSamplingRate(samplingBitrate);
    }
    private void setOutPutFile(String path){
        mMediaRecorder.setOutputFile(path);
    }
    private String getOutputFile(String phoneNumber){
        long time = System.currentTimeMillis();
        String nameFile = phoneNumber+"_"+time;
        File file = new File(Environment.getExternalStorageDirectory(), "/"+ FOLDER_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        File recorderFile = null;
        try {
            recorderFile = File.createTempFile(nameFile, mFileType, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(recorderFile != null){
            return recorderFile.getAbsolutePath();
        }
        return null;
    }
    private void stopRecording(){
        //
        if(mIsRecording){
            if(mMediaRecorder != null){
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mIsRecording = false;
            }
        }
        //stop service
        stopSelf();
    }

    //
    private class PhoneStateListener extends android.telephony.PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    stopRecording();

                    break;
                case TelephonyManager.CALL_STATE_RINGING:

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    startRecording(incomingNumber);
                    break;
            }
        }
    }

}
