package pro.anton.averin.networking.testrest.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import pro.anton.averin.networking.testrest.ui.RequestFragment;
import pro.anton.averin.networking.testrest.ui.ResponseFragment;

/**
 * Created by AAverin on 09.11.13.
 */
public class RestPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

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
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment newFragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, newFragment);
        return newFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
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
