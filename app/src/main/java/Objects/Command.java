package Objects;

import java.util.ArrayList;

/**
 * Created by HenryChiang on 15-10-03.
 */
public class Command {
    private String intent;
    private ArrayList<Info> infoList=new ArrayList<Info>();

    public Command(String intent, ArrayList<Info> infoList){
        this.intent=intent;
        this.infoList=infoList;
    }

    public Command(String intent, Info info){
        this.intent=intent;
        ArrayList<Info> infoList=new ArrayList<Info>();
        infoList.add(info);
        this.infoList=infoList;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public ArrayList<Info> getInfoList() {
        return infoList;
    }

    public void setInfoList(ArrayList<Info> infoList) {
        this.infoList = infoList;
    }


}

