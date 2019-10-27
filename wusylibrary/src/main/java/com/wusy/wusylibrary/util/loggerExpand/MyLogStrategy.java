package com.wusy.wusylibrary.util.loggerExpand;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.LogStrategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MyLogStrategy implements LogStrategy {
    @NonNull
    private Handler handler;

    public MyLogStrategy(@NonNull Handler handler) {
        this.handler = handler;
    }

    @Override
    public void log(int level, @Nullable String tag, @NonNull String message) {


        // do nothing on the calling thread, simply pass the tag/msg to the background thread
        handler.sendMessage(handler.obtainMessage(level, message));
    }

    static class WriteHandler extends Handler {

        private String folder;
        private  int maxFileSize;

        WriteHandler(@NonNull Looper looper, @NonNull String folder, int maxFileSize) {
            super(looper);
            this.folder = folder;
            this.maxFileSize = maxFileSize;
        }

        @SuppressWarnings("checkstyle:emptyblock")
        @Override
        public void handleMessage(@NonNull Message msg) {
            String content = (String) msg.obj;

            FileWriter fileWriter = null;
            File logFile = getLogFile(folder, LoggerSetting.fileName);

            try {
                fileWriter = new FileWriter(logFile, true);

                writeLog(fileWriter, content);

                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                if (fileWriter != null) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e1) { /* fail silently */ }
                }
            }
        }

        /**
         * This is always called on a single background thread.
         * Implementing classes must ONLY write to the fileWriter and nothing more.
         * The abstract class takes care of everything else including close the stream and catching IOException
         *
         * @param fileWriter an instance of FileWriter already initialised to the correct file
         */
        private void writeLog(@NonNull FileWriter fileWriter, @NonNull String content) throws IOException {


            fileWriter.append(content);
        }

        private File getLogFile(@NonNull String folderName, @NonNull String fileName) {


            File folder = new File(folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String[] files=folder.list();
            int filecount=files.length;
            File newFile= null;
            File existingFile = null;

            newFile = new File(folder, String.format("%s_%s.log", fileName, 0));
            while (newFile.exists()) {
                existingFile = newFile;
                newFile = new File(folder, String.format("%s_%s.log", fileName, filecount));
            }

            if (existingFile != null) {
                if (existingFile.length() >= maxFileSize) {
                    if(filecount>=5){
                        for(int i=0;i<filecount;i++){
                            File localfile= new File(folder.getPath()+ File.separator+files[i]);
                            if(localfile.exists()){
                                localfile.delete();
                            }
                        }
                    }else{
                        existingFile.renameTo(newFile);
                    }
                    newFile=new File(folder, String.format("%s_%s.log", fileName, 0));
                    return newFile;
                }
                return existingFile;
            }

            return newFile;
        }
    }
}
