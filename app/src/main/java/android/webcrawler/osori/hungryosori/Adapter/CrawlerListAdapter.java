package android.webcrawler.osori.hungryosori.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.webcrawler.osori.hungryosori.CrawlerActivity;
import android.webcrawler.osori.hungryosori.Model.CrawlerInfo;
import android.webcrawler.osori.hungryosori.R;
import android.webcrawler.osori.hungryosori.common.Constant;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import java.util.List;

/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerListAdapter extends ArrayAdapter<CrawlerInfo> implements View.OnClickListener{

    private Typeface fontArial;

    public CrawlerListAdapter(Context context, int resource, int textViewResourceId, List<CrawlerInfo> data){
        super(context, resource, textViewResourceId, data);

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

        return itemLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list_crawler_button_subscription: {
                int position = (Integer)v.getTag();
                String id    = getItem(position).getId();

                if(((ToggleButton)v).isChecked() == true)
                {
                    CrawlerInfo crawlerInfo = new CrawlerInfo(id, getItem(position).getTitle(),
                            getItem(position).getDescription(), getItem(position).getUrl(), true);
                    // 구독
                    CrawlerActivity.crawlerInfosMy.add(crawlerInfo);
                    CrawlerActivity.crawlerInfosAll.get(position).setSubscription(true);
                }else
                {
                    // 구독 해제
                    for(int i=0; i< CrawlerActivity.crawlerInfosMy.size(); ++i){
                        if(CrawlerActivity.crawlerInfosMy.get(i).getId().equals(id)){
                            CrawlerActivity.crawlerInfosMy.remove(i);
                        }
                    }

                    for(CrawlerInfo crawlerInfo : CrawlerActivity.crawlerInfosAll){
                        if(crawlerInfo.getId().equals(id)){
                            crawlerInfo.setSubscription(false);
                        }
                    }
                }
                CrawlerViewPagerAdapter.fragmentALL.listAdapter.notifyDataSetChanged();
                CrawlerViewPagerAdapter.fragmentMy.listAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
