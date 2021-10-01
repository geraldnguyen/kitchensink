package example.codereview;

/**
 * This model class contains 2 properties `name` and `description`
 * It also contains method name `maxLength` which returns the longest
 * prefix whose reversed order is a suffix (e.g. `abXXXXba`) without overlapping
 *
 * Imagine a colleague create this class and submit it for code review,
 * please show us how you would review this code
 */
public class PrefixSuffixProblem {
    public String name;
    public String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String maxLength(String str){
        String temp = "";
        for(int i=0;i<str.length()/2;i++   ){
        if (str.charAt(i) == str.charAt(str.length() - 1 - i)) {
        temp = temp + str.charAt(i);
    }
    else
        break;
        }
        return temp;
    }
}
