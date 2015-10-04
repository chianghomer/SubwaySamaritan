package Objects;

/**
 * Created by HenryChiang on 15-10-03.
 */
public class Food {

    private String name;
    private String[] foodName;
    private double similarity=0;
    private int foodNameLength;
    private String category;

    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public Food(String foodName,String catagory){
        this.category=catagory;
        this.name=foodName;

        String[] fn= foodName.split(" ");
        this.foodNameLength=fn.length;
        this.foodName=new String[this.foodNameLength];
        for(int i=0;i<this.foodNameLength;i++){
            this.foodName[i]=fn[i];

        }


    }


    public String getName(){
        return name;
    }

    public double getSimilarity(){
        return similarity;
    }

    public void resetSimilarity(){
        this.similarity=0;
    }

    public double calcSimilarity(String[] nameCompare){
        resetSimilarity();
        int nameCompareLength=nameCompare.length;
        for(int i=0;i<nameCompareLength;i++){
            for(int j=0;j<this.foodNameLength;j++){
                if(nameCompare[i].equals(this.foodName[j])){
                    this.similarity += 0.5;
                }
            }
        }

        if(similarity==0.5*this.foodNameLength){
            similarity+=5;
        }

        return this.similarity;
    }
}