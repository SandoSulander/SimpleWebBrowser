package store.catsocket.simplewebbrowser;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class WebAddressIterator {

    ArrayList<WebAddress> urlList = new ArrayList<>();

    ArrayList<WebAddress> forwardUrls = new ArrayList<>();

    int currentPage;

    private static WebAddressIterator wai = new WebAddressIterator();

    boolean forward;

    public boolean getForward(){

        return forward;
    }

    public void setForward(boolean b){

        forward = b;
    }

    public WebAddressIterator() {

        currentPage = -1;
        forward = true;
    }

    public static WebAddressIterator getInstance() {

        return wai;
    }

    public void setCurrentPage(int i){

        currentPage += i;
    }


    public void addWebAddress(WebAddress wa) {

        urlList.add(wa);

    }

    public String getRefreshAddressFromList() {

            String url = urlList.get(currentPage).getUrl();
            System.out.println("##############################INDEX##################" + currentPage);
            System.out.println("##############################REFRESH_URL##################" +urlList.get(currentPage).getUrl());
            return url;

    }

    public WebAddress getPreviousUrl(){

        System.out.println("##########################################INDEX#############################"+wai.currentPage);
        System.out.println("##########################################LIST_SIZE#############################"+wai.urlList.size());
        System.out.println("##########################################URL#############################"+wai.urlList.get(wai.currentPage).getUrl());
        if (urlList.size()> 0){
            WebAddress wa = urlList.get(currentPage-1);
            forwardUrls.add(urlList.get(currentPage));
            urlList.remove(currentPage);
            setCurrentPage(-1);
            return wa;
        } else{
            forward = false;
            WebAddress wa = new WebAddress();
            return wa;
        }

    }

    public String getForwardUrl(){

        System.out.println("##########################################INDEX#############################"+wai.currentPage);
        System.out.println("##########################################LIST_SIZE#############################"+wai.urlList.size());
        System.out.println("##########################################URL#############################"+wai.urlList.get(wai.currentPage).getUrl());
        int i = forwardUrls.size()-1;
        if (forwardUrls.size() >= 1){
            String forwardUrl = forwardUrls.get(i).getUrl();
            urlList.add(forwardUrls.get(i));
            forwardUrls.remove(forwardUrls.get(i));
            setCurrentPage(1);
            return forwardUrl;
        }else{
            forward = false;
            String forwardUrl = urlList.get(currentPage).getUrl();
            return forwardUrl;
        }

    }
}