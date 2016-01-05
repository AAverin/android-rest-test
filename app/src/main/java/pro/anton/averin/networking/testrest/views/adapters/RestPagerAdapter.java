package pro.anton.averin.networking.testrest.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import pro.anton.averin.networking.testrest.views.fragments.RequestFragment;
import pro.anton.averin.networking.testrest.views.fragments.ResponseFragment;

/**
 * Created by AAverin on 09.11.13.
 */
public class RestPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Map.Entry<Class, String>> pagerViews = new ArrayList<Map.Entry<Class, String>>() {
        {
            add(new AbstractMap.SimpleEntry<Class, String>(RequestFragment.class, "RequestFragment"));
            add(new AbstractMap.SimpleEntry<Class, String>(ResponseFragment.class, "ResponseFragment"));
        }
    };

    private FragmentManager fragmentManager;
    private String[] pageTitles;

    public RestPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.fragmentManager = fm;
        if (titles.length != pagerViews.size()) {
            throw new RuntimeException("illegal number of titles");
        }
        this.pageTitles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        try {
            return (Fragment) pagerViews.get(i).getKey().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment newFragment = (Fragment) super.instantiateItem(container, position);
        return newFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.pageTitles[position];
    }

    @Override
    public int getCount() {
        return pagerViews.size();
    }
}
