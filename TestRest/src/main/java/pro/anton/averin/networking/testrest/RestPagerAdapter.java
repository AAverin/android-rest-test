package pro.anton.averin.networking.testrest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by AAverin on 09.11.13.
 */
public class RestPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;

    private Class[] pagerViews = new Class[] {
        RequestFragment.class,
        ResponseFragment.class
    };
    private String[] pageTitles;

    public RestPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.fragmentManager = fm;
        if (titles.length != pagerViews.length) {
            throw new RuntimeException("illegal number of titles");
        }
        this.pageTitles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        try {
            return (Fragment) pagerViews[i].newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.pageTitles[position];
    }

    @Override
    public int getCount() {
        return pagerViews.length;
    }
}
