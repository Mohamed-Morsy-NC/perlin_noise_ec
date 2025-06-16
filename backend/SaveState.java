package backend;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SaveState {
    private int ssNum;

    public SaveState() {}

    public SaveState(int ssNum) {
        this.ssNum = ssNum;
    }
    
    public int getSsNum() {
        return ssNum;
    }

    public void setSsNum(int ssNum) {
        this.ssNum = ssNum;
    }

    @Override
    public String toString() {
        return "SaveState{" +
                "ssNum='" + ssNum + '\'' +
                 "}";
    }
}
