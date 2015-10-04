package Objects;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by HenryChiang on 15-10-03.
 */
public class Sandwich {

    public boolean isHasKind() {
        return hasKind;
    }


    public void setHasKind(boolean hasKind) {
        this.hasKind = hasKind;
    }


    public boolean isHasToasted() {
        return hasToasted;
    }


    public void setHasToasted(boolean hasToasted) {
        this.hasToasted = hasToasted;
    }

    private boolean hasKind =false;
    private boolean hasBread =false;
    private boolean hasCheese =false;
    private boolean hasToasted =false;
    private boolean hasVegetable =false;
    private boolean hasSauce =false;
    private Food kind=null;
    private Food toasted =null;
    private Food bread=null;
    private Food cheese=null;

    private ArrayList<Food> vegetable=new ArrayList<Food>();

    private ArrayList<Food> sauce=new ArrayList<Food>();

    public Sandwich (){

    }


    private void updateSubStates(){

        if (kind!=null){
            setHasKind(true);
        }else{
            setHasKind(false);
        }
        if (bread!=null){
            setHasBread(true);
        }else{
            setHasBread(false);
        }
        if (cheese!=null){
            setHasCheese(true);
        }else{
            setHasCheese(false);
        }
        if (toasted!=null){
            setHasToasted(true);
        }else{
            setHasToasted(false);
        }
        if (!vegetable.isEmpty()){
            setHasVegetable(true);
        }else{
            setHasVegetable(false);
        }
        if (!sauce.isEmpty()){
            setHasSauce(true);
        }else{
            setHasSauce(false);
        }


    }

    public boolean isComplete(){


        return hasBread&&hasCheese&&hasVegetable&&hasSauce;
    }

    public boolean isPreComplete(String category){

        if(category.equals("kind")){
            return true;
        }else if(!hasKind){
            return false;
        }else if(category.equals("bread")){
            return true;
        }else if(!hasBread){
            return false;
        }else if(category.equals("cheese")){
            return true;
        }else if(!hasCheese){
            return false;
        }else if(category.equals("toasted")){
            return true;
        }else if(!hasToasted){
            return false;
        }else if(category.equals("vegetable")){
            return true;
        }else if(!hasVegetable){
            return false;
        }else if(category.equals("sauce")){
            return true;
        }else if(!hasSauce){
            return false;
        }else{
          //  System.out.println("Can't determ whether the previous steps are completed or not.");
            return false;
        }



    }


    public String getFirstIncomplete(){
        if(!hasKind){
            return "kind";
        }else if(!hasBread){
            return "bread";
        }else if(!hasCheese){
            System.out.print(hasCheese);
            return "cheese";
        }else if(!hasToasted){
            return "toasted";
        }else if(!hasVegetable){
            return "vegetable";
        }else if(!hasSauce){
            return "sauce";
        }else{
          //  System.out.println("Can't determ whether the previous steps are completed or not.");
            return "null";
        }
    }



    public void addFood(ArrayList<Food> food){
        Food tempFood=null;
        int numberOfFood=food.size();
        Iterator<Food> iter=food.iterator();
        for(;iter.hasNext();){
            tempFood=iter.next();
            String tempCategory=tempFood.getCategory();
            if(tempCategory.equals("kind")){
                this.setKind(tempFood);
            }
            else if(tempCategory.equals("bread")){
                this.setBread(tempFood);
            }
            else if(tempCategory.equals("cheese")){
                this.setCheese(tempFood);

            }
            else if(tempCategory.equals("toasted")){
                this.setToasted(tempFood);

            }
            else if(tempCategory.equals("vegetable")){
                this.addVegetable(tempFood);

            }
            else if(tempCategory.equals("sauce")){
                this.addSauce(tempFood);

            }

        }

        this.updateSubStates();


    }



    public void addFood(Food tempFood){


        String tempCategory=tempFood.getCategory();
        if(tempCategory.equals("kind")){
            this.setKind(tempFood);
        }
        else if(tempCategory.equals("bread")){
            this.setBread(tempFood);
        }
        else if(tempCategory.equals("cheese")){
            this.setCheese(tempFood);

        }
        else if(tempCategory.equals("toasted")){
            this.setToasted(tempFood);

        }
        else if(tempCategory.equals("vegetable")){
            this.addVegetable(tempFood);

        }
        else if(tempCategory.equals("sauce")){
            this.addSauce(tempFood);

        }



        this.updateSubStates();


    }

    public Food getKind() {
        return kind;
    }


    public void setKind(Food kind) {
        this.kind = kind;
    }





    public Food getToasted() {
        return toasted;
    }


    public void setToasted(Food toasted) {
        this.toasted = toasted;
    }


    public void setHasVegetable(boolean hasVegetable) {
        this.hasVegetable = hasVegetable;
    }


    public void addVegetable(Food vegetable){
        this.vegetable.add(vegetable);
    }

    public void addSauce(Food sauce){
        this.sauce.add(sauce);
    }




    public boolean isHasCheese() {
        return hasCheese;
    }

    public void setHasCheese(boolean hasCheese) {
        this.hasCheese = hasCheese;
    }

    public boolean isHasVegetable() {
        return hasVegetable;
    }

    public void setHasVegs(boolean hasVegetable) {
        this.hasVegetable = hasVegetable;
    }

    public boolean isHasSauce() {
        return hasSauce;
    }

    public void setHasSauce(boolean hasSauce) {
        this.hasSauce = hasSauce;
    }

    public Food getBread() {
        return bread;
    }

    public void setBread(Food bread) {
        this.bread = bread;
    }

    public Food getCheese() {
        return cheese;
    }

    public void setCheese(Food cheese) {
        this.cheese = cheese;
    }

    public ArrayList<Food> getVegetable() {
        return vegetable;
    }

    public void setVegetable(ArrayList<Food> vegetable) {
        this.vegetable = vegetable;
    }

    public ArrayList<Food> getSauce() {
        return sauce;
    }

    public void setSauce(ArrayList<Food> sauce) {
        this.sauce = sauce;
    }





    //Bread
    public boolean isHasBread() {
        return hasBread;
    }

    public void setHasBread(boolean hasBread) {
        this.hasBread = hasBread;
    }

    public int categoryToNumber(String category){
        int result=0;
        switch (category){
            case "kind":
                result =1;
                break;
            case "bread":
                result =2;
                break;
            case "cheese":
                result =3;
                break;
            case "toasted":
                result =4;
                break;
            case "vegetable":
                result =5;
                break;
            case "sauce":
                result =6;
                break;

        }

        if(result==0){
           // System.out.println("NO SUCH A CATEGORY FOUND");
        }
        return result;
    }


    @Override
    public String toString() {
        return super.toString();
    }


}