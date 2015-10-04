package samaritan.wearhacks.ca.subwaysamaritan;

/**
 * Created by HenryChiang on 15-10-03.
 */
public class Message {

    public String text;
    public int viewType;

    public Message(String text, int viewType){
        this.text=text;
        this.viewType=viewType;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
