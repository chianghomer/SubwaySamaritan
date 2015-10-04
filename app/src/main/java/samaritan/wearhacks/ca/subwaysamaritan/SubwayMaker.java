package samaritan.wearhacks.ca.subwaysamaritan;

/**
 * Created by HenryChiang on 15-10-04.
 */
        import java.util.Iterator;
        import java.util.ArrayList;
        import java.util.Random;

        import Objects.Command;
        import Objects.Food;
        import Objects.Info;
        import Objects.Sandwich;

public class SubwayMaker {
    public ArrayList<Food> foodBase= new ArrayList<Food>();

    public  Sandwich sub= new Sandwich();


    public  String expectedAnswer="null";


    public  int toastedIndex;
    public  int notToastedIndex;
    public  int noCheeseIndex;
    public  int noVegetableIndex;
    public  int allDressIndex;
    public  int noSauceIndex;

    public  SubwayMaker(ArrayList<Food> foodBase){
        this.foodBase=foodBase;
        for(int i =0;i<foodBase.size();i++){
            switch (foodBase.get(i).getName()){
                case "toasted":
                    toastedIndex = i;
                    break;
                case "not toasted":
                    notToastedIndex = i;
                    break;
                case "no cheese":
                    noCheeseIndex = i;
                    break;
                case "no vegetable":
                    noVegetableIndex = i;
                    break;
                case "all dress":
                    allDressIndex = i;
                    break;
                case "no sauce":
                    noSauceIndex = i;
                    break;
            }
        }

    }

    public  ArrayList<Food> searchFood(String name){
        ArrayList<Food> result=new ArrayList<Food>();
        String[] tempName=name.split(" ");
        int namelength=tempName.length;
        int numberOfMatch=0;
        double best=0;
        double current=0;
        Iterator<Food> iter=  foodBase.iterator();
        for(;iter.hasNext();){
            Food tempFood=iter.next();
            current = tempFood.calcSimilarity(tempName);
            if (current>best){
                result.clear();
                result.add(tempFood);
                best=current;
            }
            else if(current==best&&current>0.0){
                result.add(tempFood);
            }
        }
        numberOfMatch=result.size();
        Food[] resultFood=new Food[numberOfMatch];
        for(int i=0;i<numberOfMatch;i++){
            resultFood[i]=result.get(i);
        }

        return result;


    }


    public String responser(Command cm){


        String intent=cm.getIntent();
        ArrayList<Info> infoList= cm.getInfoList();

        //order intent
        if(intent.equals("ORDER_INTENT")){
            ArrayList<Food> tempFood=new ArrayList<Food>();
            int attitude=1;
            Iterator<Info> iter=infoList.iterator();
            for(;iter.hasNext();){
                Info tempInfo=iter.next();
                String tempConcept=tempInfo.getConcept();
                String tempContent=tempInfo.getConceptValue();

                if(tempConcept.equals("NEGATIVE_ATT")){
                    attitude=attitude*(-1);

                }
                else if(tempConcept.equals("OBJECT")){

                    ArrayList<Food> searchFoodResult=searchFood(tempContent);

                    Iterator<Food> iter2= searchFoodResult.iterator();
                    for(;iter2.hasNext();){
                        Food tempFood2=iter2.next();

                        tempFood.add(tempFood2);
                    }


                }


            }

            int foodFound=tempFood.size();
            if(foodFound==0){
                return "Sorry, we don't have it";

            }
            else if(foodFound>1){

                String tempString="";

                Iterator<Food> iter2= tempFood.iterator();

                for(;iter2.hasNext();){
                    Food tempFood2=iter2.next();
                    if(iter2.hasNext()){

                        tempString= tempString+ tempFood2.getName() +", ";

                    }

                    else{
                        tempString= tempString+"or "+tempFood2.getName()+"?";
                    }
                }

                return ("Sorry, did you mean "+tempString);
            }


            else if(foodFound==1){

                Food addingFood1=tempFood.get(0);
                sub.addFood(tempFood);
                String text=textMaker(addingFood1,"adding one food.")+textFollower(addingFood1.getCategory());


                return text;

            }
            else{
                //System.out.println("ERROR1");
            }


        }

        //confirm intent
        else if (intent.equals("CONFIRMING_INTENT")){
            String result="";
            switch(expectedAnswer){
                case "kind":
                    result="sorry what? what kind of meat?";
                    expectedAnswer="kind";
                    break;

                case "bread":
                    result="pardon me? what bread you'd like to have?";
                    expectedAnswer="bread";
                    break;

                case "cheese":
                    expectedAnswer="cheese";
                    result="want cheese? yea, but what cheese? Cheddar or Swiss?";
                    break;
                case "toasted":
                    expectedAnswer="null";
                    sub.addFood(foodBase.get(toastedIndex));
                    result="OK, I'll make it toasted."+textFollower("toasted");


                    break;
                case "vegetable":
                    expectedAnswer="null";
                    sub.addFood(foodBase.get(allDressIndex));
                    result="OK, then I'll make it all dress."+textFollower("vegetable");

                    break;
                case "sauce":
                    expectedAnswer="sauce";
                    result="Yes? what sauce you want? I may didn't hear you clear.";
                    break;
                case "null":

                    result="cool."+textFollower(sub.getFirstIncomplete());
                    break;


            }

            return result;




        }
        //deny intent
        else if(intent.equals("DENY_INTENT")){



            String result="";
            switch(expectedAnswer){
                case "kind":
                    result="Sorry but you can't have no meant in you sub. So please, choose a kind.";
                    expectedAnswer="kind";
                    break;

                case "bread":
                    expectedAnswer="bread";
                    result="Oh no, you may HAVE to choose a bread";
                    break;
                case "cheese":
                    expectedAnswer="null";
                    sub.addFood(foodBase.get(noCheeseIndex));
                    result="No cheese? ok. " + textFollower("cheese");

                    break;
                case "toasted":
                    expectedAnswer="null";
                    sub.addFood(foodBase.get(notToastedIndex));
                    result="Sure, not toasted."+textFollower("toasted");


                    break;
                case "vegetable":
                    expectedAnswer="null";
                    sub.addFood(foodBase.get(noVegetableIndex));
                    result="No veggies? OK"+ textFollower("vegetable");

                    break;
                case "sauce":
                    expectedAnswer="null";
                    sub.addFood(foodBase.get(noSauceIndex));
                    result="Well, no sauce then."+textFollower("sauce");

                    break;
                case "null":
                    result="sorry I don't get it. you mean no what?";
                    break;

                case "completion":

                    double random=Math.random();
                    if(random<0.33){
                        result= "Here is your sandwich. "+ sandwichDescriber();
                    }else if(random<0.66){
                        result= "Your sub is ready! "+ sandwichDescriber();
                    }else{
                        result= "Alright, I've got your sandwich ready! "+ sandwichDescriber();
                    }

                    break;


            }


            return result;


        }

        //Greeting intent
        else if(intent.equals("GREETING_INTENT")){
            String result="";
            double random=Math.random();
            if(random<0.33){
                result= "Bon jour hi. What kind of sub would you like to have for lunch?";
            }else if(random<0.66){
                result= "Good day, sir. How can I help you today?";
            }else{
                result= "Good afternoon! May I help you?";
            }
            return result;
        }
        //Thanks intent
        else if(intent.equals("THANKS_INTENT")){
            String result="";
            double random=Math.random();
            if(random<0.33){
                result= "You're welcome."+textFollower(sub.getFirstIncomplete());
            }else if(random<0.66){
                result= "My pleasure! "+textFollower(sub.getFirstIncomplete());
            }else{
                result= "No problem. "+textFollower(sub.getFirstIncomplete());
            }
            return result;
        }
        //TERMINATING_INTENT
        else if(intent.equals("TERMINATING_INTENT")){
            String result="";
            double random=Math.random();
            if(random<0.33){
                result= "Bye!";
            }else if(random<0.66){
                result= "See ya!";
            }else{
                result= "Have a good day!";
            }
            return result;
        }
        //Query intent
        else if(intent.equals("QUERY_INTENT")){
            String result="";
            double random=Math.random();
            if(random<0.33){
                result= "well, it's all on the menu sir.";
            }else if(random<0.66){
                result= "All of them are listed in the menu.";
            }else{
                result= "You may choose whatever you want from the menu.";
            }
            return result;
        }

        else if(intent.equals("NO_MATCH")){

            String result="";
            double random=Math.random();
            if(random<0.33){
                result= "Sorry, I don't quite understand what do you mean. Could you repeat please?";
            }else if(random<0.66){
                result= "I'm not absolutely sure about your intent, may I ask you to rephase it in another way?";
            }else{
                result= "Excuse me?";
            }
            return result;
        }
        else if(intent.equals("COMPLETION_INTENT")){
            if (expectedAnswer.equals("completion")){
                return ("Here is your sandwich! "+ sandwichDescriber());
            }else{
                return "that's it?";
            }
        }

        return null;
    }


    public  String textMaker(Food food,String instruction){
        String result="";
        if(instruction.equals("adding one food.")){
            //System.out.print(food.getCategory());
            //System.out.print(food.getName());

            switch(food.getCategory()){
                case "kind":
                    result= "Sure, I'll make a " +food.getName()+ " for you sir.";
                    break;
                case "bread":
                    result= "no problem. *getting " +food.getName()+ " from the oven*.";
                    break;
                case "toasted":
                    result= "Cool.";
                    break;
                case "sauce":
                    result= food.getName()+"? alright. *squeezing "+food.getName()+" out of the bottle*.";
                    break;
                case "vegetable":
                    double random=Math.random();
                    if(random<0.33){
                        result= "Of course. *adding "+food.getName()+" to the toppings*.";
                    }else if(random<0.66){
                        result= "Sure. *grabbing some "+food.getName()+" to the sub*";
                    }else{
                        result= "As your wish, sir. *getting some "+food.getName()+" for your sandwich*.";
                    }
                    break;

                case"cheese":

                    result="ok. *using clips to add 4 pieces of "+food.getName()+".";


                    break;




            }

        }

        return result;
    }

    public  String textFollower(String category){


        String result="";

        if(sub.isComplete()){
            expectedAnswer="completion";
            double random=Math.random();
            if(random<0.33){
                result= "Is there anything else you'd like to add sir?";
            }else if(random<0.66){
                result= "Anything else you like to have in your sandwich sir?";
            }else{
                result= "What else do you want me to add to your sub?";
            }
        }
        else if(!sub.isPreComplete(category)){

            switch (sub.getFirstIncomplete()){
                case "kind":
                    expectedAnswer="kind";
                    result=" But sir, first I need to know what kind of sub, I mean what meat do you like to have.";
                    break;

                case "bread":
                    expectedAnswer="bread";
                    result=" But you did not choose what kind of bread you want.";
                    break;

                case "cheese":
                    expectedAnswer="cheese";
                    result=" But sir, you haven't told me yet you'd like to have Cheddars, Swiss, or no cheese?";
                    break;
                case "toasted":
                    expectedAnswer="toasted";
                    result=" So...Toasted? Or not?";
                    break;
                case "vegetable":
                    expectedAnswer="vegetable";
                    result=" And, you still have no topping in your sandwich yet.";
                    break;
                case "sauce":
                    expectedAnswer="sauce";
                    result=" No sauce at all?";
                    break;


            }



        }
        else{
            switch (sub.getFirstIncomplete()){
                case "kind":
                    expectedAnswer="kind";
                    double random=Math.random();
                    if(random<0.33){
                        result= " What kind of sub do you want sir?";
                    }else if(random<0.66){
                        result= "what kind meat do you want for you sandwich?";
                    }else{
                        result= "What sandwich would you to have today?";
                    }


                    break;

                case "bread":
                    expectedAnswer="bread";
                    random=Math.random();
                    if(random<0.33){
                        result=" What kind of bread? whole wheat, Honey oat, Italian or...?";
                    }else if(random<0.66){
                        result= "So you may choose a bread now. What bread you'd love to have for your sandwich?";
                    }else{
                        result= "Which bread you want?";
                    }

                    break;

                case "cheese":
                    expectedAnswer="cheese";
                    result=" Cheddars or Swiss?";
                    break;
                case "toasted":
                    expectedAnswer="toasted";
                    random=Math.random();
                    if(random<0.33){
                        result=" Toasted?";
                    }else if(random<0.66){
                        result= "Would you like it toasted?";
                    }else{
                        result= "You want it toasted sir?";
                    }

                    break;
                case "vegetable":
                    expectedAnswer="vegetable";
                    random=Math.random();
                    if(random<0.33){
                        result=" and...veggies?";
                    }else if(random<0.66){
                        result= "Any veggies?";
                    }else{
                        result= "Vegetables?";
                    }

                    break;
                case "sauce":
                    expectedAnswer="sauce";

                    random=Math.random();
                    if(random<0.33){
                        result=" What sauce?";
                    }else if(random<0.66){
                        result= "Any sauce? Or more toppings?";
                    }else{
                        result= "Any other veggies? Or pick a sauce?";
                    }

                    break;
            }
        }


        return result;



    }

    //to quick build up an command with 1 intent and 1 info only by passing intent, concept, and content as arguments.
    public static Command cmmk(String intent,String concept,String content){
        Info tempInfo=new Info(concept,content);

        Command tempCommand=new Command(intent,tempInfo);

        return tempCommand;


    }

    public String sandwichDescriber(){

        ArrayList<Food> vegetableArray=sub.getVegetable();
        ArrayList<Food> sauceArray=sub.getSauce();

        String vegetables="";
        String sauces="";
        if(vegetableArray.size()>1){
            Iterator<Food> iter1=vegetableArray.iterator();

            for(;iter1.hasNext();){
                Food tempFood2=iter1.next();
                if(iter1.hasNext()){

                    vegetables= vegetables+ tempFood2.getName() +", ";

                }

                else{
                    vegetables= vegetables+"and "+tempFood2.getName();
                }
            }

        }
        else {
            vegetables=vegetableArray.get(0).getName();
        }

        if(sauceArray.size()>1){
            Iterator<Food> iter2=sauceArray.iterator();

            for(;iter2.hasNext();){
                Food tempFood2=iter2.next();
                if(iter2.hasNext()){

                    sauces= sauces+ tempFood2.getName() +", ";

                }

                else{
                    sauces= sauces+"and "+tempFood2.getName();
                }
            }

        }
        else {
            sauces=sauceArray.get(0).getName();
        }

        String result = "It is an "+ sub.getKind().getName() + " with "+sub.getBread().getName()+", and it is "+sub.getToasted().getName()+". You added "+vegetables+" to your sandwich and had "+sauces+" on it.";

        return result;
    }
}
