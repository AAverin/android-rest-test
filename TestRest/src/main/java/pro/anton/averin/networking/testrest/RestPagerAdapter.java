package pro.anton.averin.networking.testrest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by AAverin on 09.11.13.
 */
public class RestPagerAdapter extends FragmentPagerAdapter {

    private Class[] pagerViews = new Class[] {
        RequestFragment.class,
        ResponseFragment.class
    };

    public RestPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        try {
            return (Fragment)pagerViews[i].newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
