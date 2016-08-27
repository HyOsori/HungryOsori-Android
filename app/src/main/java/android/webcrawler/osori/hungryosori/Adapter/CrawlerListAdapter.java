package android.webcrawler.osori.hungryosori.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Http;
import android.webcrawler.osori.hungryosori.CrawlerActivity;
import android.webcrawler.osori.hungryosori.Model.CrawlerInfo;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.R;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerListAdapter extends ArrayAdapter<CrawlerInfo> implements View.OnClickListener{

    private Typeface fontArial;
    private DisplayImageOptions options;        // 이미지 로더 옵션

    public CrawlerListAdapter(Context context, int resource, int textViewResourceId, List<CrawlerInfo> data, DisplayImageOptions options){
        super(context, resource, textViewResourceId, data);

        fontArial           = Typeface.createFromAsset(context.getAssets(), "fonts/arial.ttf");
        this.options        = options;
    }

    class ViewHolder
    {
        TextView textView_title;
        TextView textView_description;
        ImageView imageView;
        ToggleButton toggleButton_subscription;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View itemLayout = super.getView(position, convertView, parent);
        ViewHolder viewHolder = (ViewHolder)itemLayout.getTag();

        // 뷰홀더 생성
        if(viewHolder == null)
        {
            viewHolder = new ViewHolder();

            /** 객체 설정 */
            viewHolder.textView_title           = (TextView)itemLayout.findViewById(R.id.list_crawler_textView_title);
            viewHolder.textView_description     = (TextView)itemLayout.findViewById(R.id.list_crawler_textView_description);
            viewHolder.imageView                = (ImageView)itemLayout.findViewById(R.id.list_crawler_imageView);
            viewHolder.toggleButton_subscription   = (ToggleButton)itemLayout.findViewById(R.id.list_crawler_button_subscription);

            /** 폰트 설정 */
            viewHolder.textView_title.setTypeface(fontArial);
            viewHolder.textView_description.setTypeface(fontArial);
            viewHolder.toggleButton_subscription.setTypeface(fontArial);

            /** 클릭 리스너 설정 */
            viewHolder.toggleButton_subscription.setOnClickListener(this);
            itemLayout.setTag(viewHolder);
        }
        viewHolder.toggleButton_subscription.setTag(position);

        viewHolder.textView_title.setText(getItem(position).getTitle());
        viewHolder.textView_description.setText(getItem(position).getDescription());
        viewHolder.toggleButton_subscription.setChecked(getItem(position).getSubscription());
        ImageLoader.getInstance().displayImage(getItem(position).getUrl(), viewHolder.imageView, options);
        return itemLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list_crawler_button_subscription: {
                int position = (Integer)v.getTag();

                if(((ToggleButton)v).isChecked() == true)
                {
                    /** 구독 */
                    subscribeCrawler(position, v);
                }else
                {
                    /** 구독 해제 */
                    unSubscribeCrawler(position, v);
                }
                break;
            }
        }
    }

    private void subscribeCrawler(int position, View view)
    {
        String crawlerID = getItem(position).getId();

        String url = Constant.SERVER_URL + "/req_subscribe_crawler";

        ParamModel params = new ParamModel();

        params.setUrl(url);
        params.setParamStr("user_id", Constant.userID);
        params.setParamStr("user_key", Constant.userKey);
        params.setParamStr("crawler_id", crawlerID);

        new subscribeCrawlerTask(getContext(), position, view).execute(params);
    }

    private class subscribeCrawlerTask extends AsyncTask<ParamModel, Void, Boolean> {

        private Context mContext;
        private int position;
        private View view;

        public subscribeCrawlerTask(Context context, int position, View view){
            this.mContext = context;
            this.position = position;
            this.view     = view;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            view.setClickable(false);
        }

        @Override
        protected Boolean doInBackground(ParamModel... params) {
            // TODO Auto-generated method stub
            Http http = new Http(mContext);

            String result = http.send(params[0], false);

            if(result == null){
                return false;
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String message = jsonObject.getString(Constant.MESSAGE);
                    if(message.equals(Constant.MESSAGE_SUCCESS)){
                        return true;
                    }
                }catch(Exception e){

                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if(success) {
                // 성공
                CrawlerInfo crawlerInfo = new CrawlerInfo(getItem(position).getId(), getItem(position).getTitle(),
                        getItem(position).getDescription(), getItem(position).getUrl(), true);
                // 구독
                CrawlerActivity.myCrawlerInfoList.add(crawlerInfo);
                CrawlerActivity.allCrawlerInfoList.get(position).setSubscription(true);

                CrawlerViewPagerAdapter.notifyMyCrawlerInfoListChanged();
                CrawlerViewPagerAdapter.notifyAllCrawlerInfoListChanged();
            }
            view.setClickable(true);
        }
    }

    private void unSubscribeCrawler(int position, View view)
    {
        String crawlerID = getItem(position).getId();

        String url = Constant.SERVER_URL + "/req_unsubscribe_crawler";

        ParamModel params = new ParamModel();

        params.setUrl(url);
        params.setParamStr("user_id", Constant.userID);
        params.setParamStr("user_key", Constant.userKey);
        params.setParamStr("crawler_id", crawlerID);

        new unSubscribeCrawlerTask(getContext(), position, view).execute(params);
    }

    private class unSubscribeCrawlerTask extends AsyncTask<ParamModel, Void, Boolean> {

        private Context mContext;
        private int position;
        private View view;

        public unSubscribeCrawlerTask(Context context, int position, View view){
            this.mContext = context;
            this.position = position;
            this.view     = view;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            view.setClickable(false);
        }

        @Override
        protected Boolean doInBackground(ParamModel... params) {
            // TODO Auto-generated method stub
            Http http = new Http(mContext);

            String result = http.send(params[0], false);

            if(result == null){
                return false;
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String message = jsonObject.getString(Constant.MESSAGE);
                    if(message.equals(Constant.MESSAGE_SUCCESS)){
                        return true;
                    }
                }catch(Exception e){

                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if(success) {
                // 성공
                String id = getItem(position).getId();

                // 구독 해제
                for(int i=0; i< CrawlerActivity.myCrawlerInfoList.size(); ++i){
                    if(CrawlerActivity.myCrawlerInfoList.get(i).getId().equals(id)){
                        CrawlerActivity.myCrawlerInfoList.remove(i);
                    }
                }

                for(CrawlerInfo crawlerInfo : CrawlerActivity.allCrawlerInfoList){
                    if(crawlerInfo.getId().equals(id)){
                        crawlerInfo.setSubscription(false);
                    }
                }

                CrawlerViewPagerAdapter.notifyMyCrawlerInfoListChanged();
                CrawlerViewPagerAdapter.notifyAllCrawlerInfoListChanged();
            }
            view.setClickable(true);
        }
    }

}
