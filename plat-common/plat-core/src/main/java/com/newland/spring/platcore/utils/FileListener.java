package com.newland.spring.platcore.utils;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class FileListener {
    /**
     * A facility for threads to schedule tasks for future execution in a background thread.
     * Tasks may be scheduled for one-time execution, or for repeated execution at regular intervals.
     */
    private Timer timer;

    private FileChangeDo fileChangeDo;

    /**
     * current time
     */
    private long currentTime = -1;

    /**
     * last Modified Time
     */
    private long lastModifiedTime = -1;

    /**
     * 秒钟
     */
    private long times = 5;

    /**
     * time
     */
    private long pollingInterval = 1000 * times;

    /**
     * file path
     */
    private String filePath;

    private URI fileUri;

    public FileListener(URI fileUri) {
        this.fileUri = fileUri;
        this.filePath = fileUri.getPath();
        this.timer = new Timer(true);
        File file = new File(filePath);
        lastModifiedTime = file.lastModified();
        currentTime = lastModifiedTime;
    }

    public FileListener setLogChangeDo(FileChangeDo fileChangeDo) {
        this.fileChangeDo = fileChangeDo;
        return this;
    }

    public void start() {
        timer.schedule(new FileMonitor(), 0, pollingInterval);
    }

    private class FileMonitor extends TimerTask {
        public void run() {
            File file = new File(filePath);
            lastModifiedTime = file.exists() ? file.lastModified() : -1;
            if (currentTime != lastModifiedTime) {
                System.out.println("********** File [ " + fileUri + " ] changed At: "
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(lastModifiedTime));
                fileChangeDo.changeDo(fileUri);
                currentTime = lastModifiedTime;
            }
        }
    }
}

