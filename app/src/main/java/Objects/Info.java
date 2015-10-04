package Objects;

/**
 * Created by HenryChiang on 15-10-03.
 */
public class Info {

    private String concept,conceptValue;

    public Info(String concept, String conceptValue) {
        this.concept = concept;
        this.conceptValue = conceptValue;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getConceptValue() {
        return conceptValue;
    }

    public void setConceptValue(String conceptValue) {
        this.conceptValue = conceptValue;
    }
}
