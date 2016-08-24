package android.webcrawler.osori.hungryosori.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webcrawler.osori.hungryosori.Adapter.CrawlerListAdapter;
import android.webcrawler.osori.hungryosori.CrawlerActivity;
import android.webcrawler.osori.hungryosori.R;
import android.widget.ListView;

/**
 * Created by kunju on 2016-08-25.
 */
public class CrawlerFragment extends Fragment{

    private int page;
    private ListView listView;
    private CrawlerListAdapter listAdapter;

    public static CrawlerFragment newInstance(int pagePosition){
        CrawlerFragment fragment = new CrawlerFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("PAGE", pagePosition);
        fragment.setArguments(bundle);

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null){
            page      = args.getInt("PAGE");
        }

        if(page == 0) {
            listAdapter = new CrawlerListAdapter(getActivity(), R.layout.list_crawler, R.id.list_crawler_textView_title, CrawlerActivity.crawlerInfosMy);
        }else if(page == 1){
            listAdapter = new CrawlerListAdapter(getActivity(), R.layout.list_crawler, R.id.list_crawler_textView_title, CrawlerActivity.crawlerInfosAll);
        }
    }

    // 프레그먼트의 뷰를 생성한다
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crawler, container, false);

        listView = (ListView)view.findViewById(R.id.fragment_crawler_listView);
        listView.setAdapter(listAdapter);                       // 어댑터 설정

        return view;
    }

}
