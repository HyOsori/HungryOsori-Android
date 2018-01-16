package android.webcrawler.osori.hungryosori.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.Common.Pref;
import android.webcrawler.osori.hungryosori.CrawlerActivity;
import android.webcrawler.osori.hungryosori.CrawlerInfo.CrawlerInfos;
import android.webcrawler.osori.hungryosori.KeywordActivity;
import android.webcrawler.osori.hungryosori.Method.DeleteMethod;
import android.webcrawler.osori.hungryosori.Method.PostMethod;
import android.webcrawler.osori.hungryosori.Model.CrawlerInfo;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.R;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Owner on 2018-01-15.
 */

public class CrawlerMyListAdapter extends ArrayAdapter<CrawlerInfo> implements View.OnClickListener{

    private Typeface fontArial;
    private DisplayImageOptions options;

    public CrawlerMyListAdapter(Context context, int resource, int textViewResourceId,
                              List<CrawlerInfo> data, DisplayImageOptions options){
        super(context, resource, textViewResourceId, data);
        this.options        = options;
        fontArial           = Typeface.createFromAsset(context.getAssets(), "fonts/arial.ttf");
    }

    class ViewHolder
    {
        TextView textView_title;
        TextView textView_description;
        ImageView imageView;
        //ToggleButton toggleButton_subscription;
        Button button_keyword;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View itemLayout = super.getView(position, convertView, parent);
        CrawlerMyListAdapter.ViewHolder viewHolder = (CrawlerMyListAdapter.ViewHolder)itemLayout.getTag();

        // 뷰홀더 생성
        if(viewHolder == null)
        {
            viewHolder = new CrawlerMyListAdapter.ViewHolder();

            /** 객체 설정 */
            viewHolder.textView_title           = (TextView)itemLayout.findViewById(R.id.list_crawler_textView_title);
            viewHolder.textView_description     = (TextView)itemLayout.findViewById(R.id.list_crawler_textView_description);
            viewHolder.imageView                = (ImageView)itemLayout.findViewById(R.id.list_crawler_imageView);
            //viewHolder.toggleButton_subscription   = (ToggleButton)itemLayout.findViewById(R.id.list_crawler_button_subscription);
            viewHolder.button_keyword = (Button)itemLayout.findViewById(R.id.list_crawler_button_keyword);
            /** 폰트 설정 */
            viewHolder.textView_title.setTypeface(fontArial);
            viewHolder.textView_description.setTypeface(fontArial);
            //viewHolder.toggleButton_subscription.setTypeface(fontArial);
            viewHolder.button_keyword.setTypeface(fontArial);

            /** 클릭 리스너 설정 */
            viewHolder.button_keyword.setOnClickListener(this);
            //viewHolder.toggleButton_subscription.setOnClickListener(this);
            itemLayout.setTag(viewHolder);
        }
        viewHolder.button_keyword.setTag(position);
        //viewHolder.toggleButton_subscription.setTag(position);
        viewHolder.textView_title.setText(getItem(position).getTitle());
        viewHolder.textView_description.setText(getItem(position).getDescription());
        //viewHolder.toggleButton_subscription.setChecked(getItem(position).getSubscription());
        ImageLoader.getInstance().displayImage(getItem(position).getUrl(), viewHolder.imageView, options);

        return itemLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list_crawler_button_subscription: {
                int position = (Integer)v.getTag();

                if(((ToggleButton)v).isChecked() == true) {
                    /** 구독 */
                    subscribeCrawler(position, v);
                }else {
                    /** 구독 해제 */
                    unSubscribeCrawler(position, v);
                }
                break;
            }
            case R.id.list_crawler_button_keyword: {
                int position = (Integer)v.getTag();
                //intent
                Intent intent = new Intent(v.getContext(), KeywordActivity.class);
                intent.putExtra("CrawlerTitle", getItem(position).getTitle());
                intent.putExtra("CrawlerDescription", getItem(position).getDescription());
                intent.putExtra("CrawlerUrl", getItem(position).getUrl());
                v.getContext().startActivity(intent);
            }

        }
    }

    private void subscribeCrawler(int position, View view)
    {
        String crawlerID = getItem(position).getId();
        String crawlerID_en = "";
        try{
            crawlerID_en = URLEncoder.encode(crawlerID, "utf-8");
        }catch (Exception e){

        }
        String url = Constant.SERVER_URL + "/subscription/";

        ParamModel params = new ParamModel();

        params.setUrl(url);
        params.addParameter("crawler_id", crawlerID_en);
        new CrawlerMyListAdapter.subscribeCrawlerTask(crawlerID, view).execute(params);
    }

    private class subscribeCrawlerTask extends AsyncTask<ParamModel, Void, Boolean> {

        private String id;
        private View view;

        public subscribeCrawlerTask(String id, View view){
            this.id       = id;
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
            String token = Pref.getUserKey();
            //String token = Pref.getPushToken();
            String result = PostMethod.getInstance().send(params[0], token);

            Log.e("sub_doInBack_token", token);
            try {
                JSONObject jsonObject = new JSONObject(result);
                int error = jsonObject.getInt("ErrorCode");
                Log.e("sub_doInBackground", "errorCode: "+error);
                if (error == 0) {
                    return true;
                }
            } catch (Exception e) {
                Log.e("sub_doInBackground", e.toString());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if(success) {
                // 성공
                Log.e("subscribeCrawlerTask", "onPostExecute: success");
                CrawlerInfos.getInstance().subscriptionCrawler(id);
            }else{
                ((ToggleButton)view).setChecked(false);
            }
            view.setClickable(true);
        }
    }

    private void unSubscribeCrawler(int position, View view)
    {
        String crawlerID = getItem(position).getId();

        String url = Constant.SERVER_URL + "/subscription/";

        ParamModel params = new ParamModel();
        params.setUrl(url);


        //id, key대신 token
        //params.addParameter("push_token", Constant.pushToken);

        params.addParameter("crawler_id", crawlerID);

        new CrawlerMyListAdapter.unSubscribeCrawlerTask(crawlerID, view).execute(params);
    }

    private class unSubscribeCrawlerTask extends AsyncTask<ParamModel, Void, Boolean> {
        private String id;
        private View view;

        public unSubscribeCrawlerTask(String id, View view){
            this.id = id;
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
            String token = Pref.getUserKey();
            String result = DeleteMethod.getInstance().send(params[0], token);

            try {
                JSONObject jsonObject = new JSONObject(result);

                int error = jsonObject.getInt("ErrorCode");
                if (error == 0) {
                    return true;
                }
            } catch (Exception e) {

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            // TODO Auto-generated method stub
            if(success) {
                // 성공

                CrawlerInfos.getInstance().unSubscriptionCrawler(id);
            }else{
                ((ToggleButton)view).setChecked(true);
            }
            view.setClickable(true);
        }
    }
}
