package android.webcrawler.osori.hungryosori.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.webcrawler.osori.hungryosori.Common.Constant;
import android.webcrawler.osori.hungryosori.CrawlerInfo.CrawlerInfos;
import android.webcrawler.osori.hungryosori.Method.DeleteMethod;
import android.webcrawler.osori.hungryosori.Method.PostMethod;
import android.webcrawler.osori.hungryosori.Model.CrawlerInfo;
import android.webcrawler.osori.hungryosori.Model.ParamModel;
import android.webcrawler.osori.hungryosori.R;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONObject;
import java.util.List;

/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerListAdapter extends ArrayAdapter<CrawlerInfo> implements View.OnClickListener{

    private Typeface fontArial;
    private DisplayImageOptions options;

    public CrawlerListAdapter(Context context, int resource, int textViewResourceId,
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

                if(((ToggleButton)v).isChecked() == true) {
                    /** 구독 */
                    subscribeCrawler(position, v);
                }else {
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

        String url = Constant.SERVER_URL + "/subscriptions/";

        ParamModel params = new ParamModel();

        params.setUrl(url);

        params.addParameter("user_id", Constant.userID);
        params.addParameter("user_key", Constant.userKey);
        //id, key대신 token
        //params.addParameter("push_token", Constant.pushToken);

        params.addParameter("crawler_id", crawlerID);

        new subscribeCrawlerTask(crawlerID, view).execute(params);
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
            String result = PostMethod.getInstance().send(params[0]);

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


        params.addParameter("user_id", Constant.userID);
        params.addParameter("user_key", Constant.userKey);
        //id, key대신 token
        //params.addParameter("push_token", Constant.pushToken);

        params.addParameter("crawler_id", crawlerID);

        new unSubscribeCrawlerTask(crawlerID, view).execute(params);
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
            String result = DeleteMethod.getInstance().send(params[0]);

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
