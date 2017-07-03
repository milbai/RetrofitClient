package com.jady.sample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jady.retrofitclient.download.DownloadInfo;
import com.jady.retrofitclient.download.DownloadManager;
import com.jady.retrofitclient.listener.DownloadFileListener;
import com.jady.sample.R;
import com.jady.sample.ui.widget.MultipleProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipingfa on 2017/6/12.
 */
public class DownloadFileAdapter extends RecyclerView.Adapter<DownloadFileAdapter.ViewHolder> {

    private Context mContext;
    private List<DownloadInfo> downloadInfoList = new ArrayList<>();

    public DownloadFileAdapter(Context context) {
        this.mContext = context;
    }

    public void setDownloadInfoList(List<DownloadInfo> downloadInfoList) {
        this.downloadInfoList.clear();
        this.downloadInfoList.addAll(downloadInfoList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.download_file_adapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DownloadInfo downloadInfo = downloadInfoList.get(position);
        downloadInfo.setListener(new DownloadFileListener() {
            @Override
            public void onNext(Object o) {
            }

            @Override
            public void onComplete() {
            }

            @Override
            public void updateProgress(long contentRead, long contentLength, boolean completed) {
                holder.pbDownloadItem.setProgress((int) (contentRead / contentLength));
            }

            @Override
            public void onError(Throwable e) {
            }
        });
        holder.btnDownloadItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = DownloadManager.getInstance();
                switch (downloadInfo.getState()) {
                    case DownloadInfo.DOWNLOAD:
                        holder.btnDownloadItem.setText("继续");
                        downloadManager.pause(downloadInfo);
                        break;
                    case DownloadInfo.STOP:
                    case DownloadInfo.ERROR:
                    case DownloadInfo.START:
                    case DownloadInfo.FINISH:
                        holder.btnDownloadItem.setText("暂停");
                        downloadManager.restartDownload(downloadInfo);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadInfoList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        protected MultipleProgressBar pbDownloadItem;
        protected Button btnDownloadItem;

        public ViewHolder(View itemView) {
            super(itemView);
            pbDownloadItem = (MultipleProgressBar) itemView.findViewById(R.id.pb_download_item);
            btnDownloadItem = (Button) itemView.findViewById(R.id.btn_download_item);
        }
    }

}
