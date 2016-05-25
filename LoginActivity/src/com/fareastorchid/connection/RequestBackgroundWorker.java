package com.fareastorchid.connection;

import android.util.Log;

import com.fareastorchid.data.GlobalData;


public class RequestBackgroundWorker extends Thread {
    public static final String TAG = "RequestBackgroundWorker";

    private static PriorityRequestQueue queueRequest = null;
    private volatile static RequestBackgroundWorker worker = null;

    private volatile boolean running = true;

    private RequestBackgroundWorker() {
        super("RequestBackgroundWorker");
        queueRequest = new PriorityRequestQueue();
        start();
    }

    public static void startWaitingForRequest() {
        if (worker == null) {
            synchronized (RequestBackgroundWorker.class) {
                if (worker == null) {
                    worker = new RequestBackgroundWorker();
                }
            }
        }
    }

    public static void queueRequest(Request request) {

        if (worker != null) {
            synchronized (worker) {
                queueRequest.addRequest(request);
                worker.notify();
            }
        }

    }

    public static void queueRequestFirst(Request request) {

        if (worker != null) {
            synchronized (worker) {
                queueRequest.addRequestFirst(request);
                worker.notify();
            }
        }

    }

    public static void stopThread() {
        if (worker != null) {
            if (GlobalData.LOG_REQUEST) Log.w(TAG, "Try to stop RequestBackgroundWorker");
            synchronized (worker) {
                queueRequest.clear();
                worker.running = false;
                worker.notify();
            }

            if (worker != null) {
                worker.interrupt();
            }
        }
    }

    public static void clearOldRequests() {
        if (worker != null) {
            if (GlobalData.LOG_REQUEST) Log.w(TAG, "Clear old requests in RequestBackgroundWorker");
            synchronized (worker) {
                queueRequest.clear();
                worker.notify();
            }
        }
    }

    public void run() {
        if (GlobalData.LOG_REQUEST) Log.w(TAG, "Start RequestBackgroundWorker");

        while (running) {
            try {
                synchronized (this) {
                    if (queueRequest.isEmpty()) {
                        if (GlobalData.LOG_REQUEST) Log.w(TAG, "Waiting for new requests...");
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                }

                if (!running) {
                    break;
                }

                Request request = queueRequest.getFirst();
                if (request != null) {
                    if (GlobalData.LOG_REQUEST) Log.w(TAG, "Process request " + request.url);

                    Thread.sleep(request.getRetryCount() * 2000);
                    request.send();
                }

                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (GlobalData.LOG_REQUEST) Log.w(TAG, "Stop RequestBackgroundWorker");
        worker = null;
    }
}
